/*Copyright 2009-2014 Michael Kohler.

This file is part of AllAroundScore.

    AllAroundScore is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AllAroundScore is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AllAroundScore.  If not, see <http://www.gnu.org/licenses/>.

Filename: Gymnast.java
Version: 3.0
Description: Activity to control the flow of the app in the Gymnast definition
Changes:
12/31/2013: created header data
*/
package com.biig.AllAround;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import com.biig.android.AllAround.R;

public class Gymnast extends Activity{

	//context and constants
	private final Context cntx = Gymnast.this;
	private final static int INV = 4;
	private final static int VIS = 1;	
	private final static int SELECT_DIALOG = 11;
	
	//User Interface Elements
	private EditText txtGymnastFirstName;
	private EditText txtGymnastLastName;
	private EditText txtGymnastTarget;
	private EditText txtGymnastLevel;
	private TextView txtGymnastTargetLbl;
	private Button btnGymnastAdd;
	private Button btnGymnastEdit;
	private Button btnGymnastDelete;
	private Button btnGymnastCommit;
	private Button btnGymnastCancel;
	private TableLayout tbl; 
	
	//variables for the editing process
	private String editEvent = "";
	private int editItem=0;
	
	//variables to keep track of in dialogs
	private String[] Gymnasts;
	
	private DBHelper dbh = new DBHelper();
	
	//---------------------------------------------------------------------------
	//***************************On Click/Create Routines************************
	//---------------------------------------------------------------------------
	/*TODO:Click/Create Routines */
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.gymnast);
        
        //get UI and initialize
        btnGymnastAdd = (Button) findViewById(R.id.gymnastAdd);
        btnGymnastAdd.setOnClickListener(addOnClickListener);
        
        btnGymnastEdit = (Button) findViewById(R.id.gymnastEdit);
        btnGymnastEdit.setOnClickListener(editOnClickListener);
        
        btnGymnastDelete = (Button) findViewById(R.id.gymnastDelete);
        btnGymnastDelete.setOnClickListener(deleteOnClickListener);
        
        btnGymnastCommit = (Button) findViewById(R.id.gymnastCommit);
        btnGymnastCommit.setOnClickListener(commitOnClickListener);
        btnGymnastCommit.setEnabled(false);

        btnGymnastCancel = (Button) findViewById(R.id.gymnastCancel);
        btnGymnastCancel.setOnClickListener(cancelOnClickListener);

        
        txtGymnastFirstName = (EditText) findViewById(R.id.gymnastFirstNameTxt);
        txtGymnastFirstName.setVisibility(INV);
        txtGymnastFirstName.setOnTouchListener(txtOnTouch);
        
        txtGymnastLastName = (EditText) findViewById(R.id.gymnastLastNameTxt);
        txtGymnastLastName.setVisibility(INV);
        txtGymnastLastName.setOnTouchListener(txtOnTouch);
        
        txtGymnastTarget = (EditText) findViewById(R.id.gymnastTargetTxt);
        txtGymnastTarget.setVisibility(INV);
        txtGymnastTarget.setOnTouchListener(txtOnTouch);
        
        txtGymnastTargetLbl = (TextView) findViewById(R.id.gymnastTargetLbl);
        txtGymnastTargetLbl.setVisibility(INV);
        
        txtGymnastLevel = (EditText) findViewById(R.id.gymnastLevelTxt);
        txtGymnastLevel.setVisibility(INV);
        txtGymnastLevel.setOnTouchListener(txtOnTouch);
        
        tbl = (TableLayout) findViewById(R.id.gymnastTableLayout);
        
        checkSystem();
       
    }
    

    // A call-back for when the user presses the cancel button.
	OnClickListener cancelOnClickListener = new OnClickListener() {
	    public void onClick(View v) {finish();}
	};
    
    // A call-back for when the user presses the add button.
	OnClickListener addOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	    	toggleUI(VIS);
	    	editEvent = "add";
	    	btnGymnastEdit.setEnabled(false);
	    	btnGymnastDelete.setEnabled(false);
	    	btnGymnastAdd.setEnabled(false);
	    	tbl.setLayoutParams(new TableLayout.LayoutParams(0, 0));
	    	txtGymnastFirstName.requestFocus();
	         
	    }
	};

    // A call-back for when the user presses the edit button.
	OnClickListener editOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	    	editEvent = "edit";
	    	btnGymnastAdd.setEnabled(false);
	    	btnGymnastDelete.setEnabled(false);
	    	btnGymnastEdit.setEnabled(false);
	    	tbl.setLayoutParams(new TableLayout.LayoutParams(0, 0));
	    	showDialog(SELECT_DIALOG);
	    }
	};

    // A call-back for when the user presses the delete button.
	OnClickListener deleteOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	    	editEvent = "delete";
	    	btnGymnastEdit.setEnabled(false);
	    	btnGymnastAdd.setEnabled(false);
	    	btnGymnastDelete.setEnabled(false);
	    	tbl.setLayoutParams(new TableLayout.LayoutParams(0, 0));
	    	showDialog(SELECT_DIALOG);
	    }
	};

    // A call-back for when the user presses the commit button.
	OnClickListener commitOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	if (editEvent.compareTo("add")==0){
	    		if (validateEntries()){
	    			Gymnasts g = new Gymnasts("0",
	    					txtGymnastFirstName.getText().toString(),
	    					txtGymnastLastName.getText().toString(), 
	    					txtGymnastLevel.getText().toString(), 
	    					txtGymnastTarget.getText().toString());
	    			dbh.gymnastInsert(g);
	    			g = null;
	    			dbh = null;
	    			finish();
	    		}
	    	}else if(editEvent.compareTo("edit")==0){
	    		if (validateEntries()){
	    			Gymnasts g = new Gymnasts(String.valueOf(editItem),
	    					txtGymnastFirstName.getText().toString(),
	    					txtGymnastLastName.getText().toString(), 
	    					txtGymnastLevel.getText().toString(), 
	    					txtGymnastTarget.getText().toString());
	    			dbh.gymnastUpdate(g);
	    			g=null;
	    			dbh=null;
	    			finish();
	    		}
	    	}else if (editEvent.compareTo("delete")==0){
	    		dbh.gymnastDelete(String.valueOf(editItem));
	    		
   			finish();
	    	}else{
   			return;
	    	}
	    }
	};

	// A call-back for when the user touches an edit text
	OnTouchListener txtOnTouch = new OnTouchListener() {
		public boolean onTouch(View arg0, MotionEvent arg1) {
			EditText et = (EditText) arg0;
			et.selectAll();
			return false;
		}
	};
	//---------------------------------------------------------------------------
	//***************************User Interface and Validate*********************
	//---------------------------------------------------------------------------
	/*TODO:User Interface & Validate Routines */

	//validate the entries in the UI before writing to database
	private boolean validateEntries(){
		String gln = txtGymnastLastName.getText().toString();
		if (gln.compareTo("")==0){
			Toast.makeText(cntx, "Enter a last name", Toast.LENGTH_LONG).show();
			return false;
		}
		
		String gfn = txtGymnastFirstName.getText().toString();
		if (gfn.compareTo("")==0){
			Toast.makeText(cntx, "Enter a first name", Toast.LENGTH_LONG).show();
			return false;
		}
		
		String lvl = txtGymnastLevel.getText().toString();
		if (lvl.compareTo("")!=0){
			try{
				Integer.parseInt(lvl);
			}catch(NumberFormatException e){
				Toast.makeText(cntx, "Enter a valid level", Toast.LENGTH_LONG).show();
    			return false;
			}
		}else{
			Toast.makeText(cntx, "Enter a level", Toast.LENGTH_LONG).show();
			return false;
		}
		
		String trgt = txtGymnastTarget.getText().toString();
		if (trgt.compareTo("")!=0){
			try{
				Double.parseDouble(trgt);
			}catch(NumberFormatException e){
				Toast.makeText(cntx, "Enter a valid target", Toast.LENGTH_LONG).show();
    			return false;
			}
		}else{
			Toast.makeText(cntx, "Enter a target score", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	//routine to simply toggle the user interface visibility
    private void toggleUI(int v){
    	if (v==1){
    		btnGymnastCommit.setEnabled(true);
    	}else{
    		btnGymnastCommit.setEnabled(false);
    	}
    	txtGymnastLastName.setVisibility(v);
    	txtGymnastFirstName.setVisibility(v);
    	txtGymnastTarget.setVisibility(v);
    	txtGymnastTargetLbl.setVisibility(v);
    	txtGymnastLevel.setVisibility(v);
    }
	
    
    


	//---------------------------------------------------------------------------
	//***************************Database Management Routines********************
	//---------------------------------------------------------------------------
	/*TODO:Database Management Routines */
    
    
	//routine to check database to see if there are any records.
	//will enable/disable buttons and populate table based on records
    private void checkSystem(){
		btnGymnastEdit.setEnabled(true);
		btnGymnastDelete.setEnabled(true);
		if (! dbh.doGymnastsExist()){
			btnGymnastEdit.setEnabled(false);
			btnGymnastDelete.setEnabled(false);
			
			TextView t = new TextView(cntx);
			t.setTextColor(Color.BLACK);
			t.setText("Press the ADD button to add a gymnast.");
			t.setTextSize(24);
			tbl.addView(t);
			return;
		}else{
			Gymnasts = dbh.getGymnastListArray();
			for (int i=0;i<Gymnasts.length;i++){
				
				Gymnasts g = dbh.getGymnastDataByObject(Gymnasts[i],cntx);
				
				TableRow r = new TableRow(cntx);
				TextView t1 = new TextView(cntx);
				t1.setBackgroundColor(R.drawable.translucent_background);
				t1.setTextColor(Color.WHITE);
				t1.setText(g.getAnId() + ") ");
				t1.setTextSize(24);
				r.addView(t1);
				
				TextView t2 = new TextView(cntx);
				t2.setBackgroundColor(R.drawable.translucent_background);
				t2.setTextColor(Color.WHITE);
				t2.setText("| " + g.getFirstName());
				t2.setTextSize(24);
				r.addView(t2);
				
				TextView t3 = new TextView(cntx);
				t3.setBackgroundColor(R.drawable.translucent_background);
				t3.setTextColor(Color.WHITE);
				t3.setText("| " + g.getLastName());
				t3.setTextSize(24);
				r.addView(t3);
				
				TextView t4 = new TextView(cntx);
				t4.setBackgroundColor(R.drawable.translucent_background);
				t4.setTextColor(Color.WHITE);
				t4.setText("| " + g.getLevel());
				t4.setTextSize(24);
				r.addView(t4);
				
				TextView t5 = new TextView(cntx);
				t5.setBackgroundColor(R.drawable.translucent_background);
				t5.setTextColor(Color.WHITE);
				t5.setText("| " + g.getTarget());
				t5.setTextSize(24);
				r.addView(t5);
				
				g = null;
				
				tbl.addView(r,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				
				View v = new View(cntx);
				v.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,2));
				v.setBackgroundColor(Color.BLACK);
				tbl.addView(v);
			}
		}
	}
    
    	
	//---------------------------------------------------------------------------
	//***************************Dialog and selection Routines*******************
	//---------------------------------------------------------------------------
	/*TODO:Dialog and Selection Routines */
	
	
	//dialogs to select items
	 @Override
	 protected Dialog onCreateDialog(int id) {
		 switch (id) {
		 
		 	//dialog to select a single gymnast to edit
		 	case SELECT_DIALOG:
	        	editItem = 0;
	        	String btnTxt = "Delete Gymnast";
	        	if (editEvent.compareTo("edit")==0){
	        		btnTxt = "Edit Gymnast";
	        	}
	        	return new AlertDialog.Builder(cntx)
	            .setIcon(R.drawable.gymnast_small)
	            .setTitle("Select Gymnast")
	            .setSingleChoiceItems(Gymnasts, 0, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
	                    	editItem = whichButton;
	                }
	            })
	            .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
	            	public void onClick(DialogInterface dialog, int whichButton) {
	            		toggleUI(VIS);
	        	    	Gymnasts g = dbh.getGymnastDataByObject(Gymnasts[editItem],cntx);
	        	    	editItem = Integer.valueOf(g.getAnId());
	        	    	txtGymnastFirstName.setText(g.getFirstName());
	        			txtGymnastLastName.setText(g.getLastName());
	        			txtGymnastTarget.setText(g.getTarget());
	        			txtGymnastLevel.setText(g.getLevel());
	        			txtGymnastFirstName.setEnabled(false);
	        			txtGymnastLastName.setEnabled(false);
	        			g = null;
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            	public void onClick(DialogInterface dialog, int whichButton) {
	            		finish();
                 }
             })
            .create();	
	        }
		 return null;
	 }
}
