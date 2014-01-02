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

Filename: Meet.java
Version: 3.0
Description: Activity for managing Meet objects in the database
Changes:
12/31/2013: created header data
1/2/2014: re-factoring of new implementation
*/

package com.biig.AllAround;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.biig.android.AllAround.R;

public class Meet extends Activity{

	//context and constants
	private final Context cntx = Meet.this;
	private final static int INV = 4;
	private final static int VIS = 1;	
	private final static int SELECT_DIALOG = 11;
	private final static int DIALOG_SELECT_GYMNASTS = 12;
	
	//UI variables
	private EditText txtMeetName;
	private EditText txtMeetDate;
	private EditText txtMeetLevel;
	private TextView tvGymnasts;
	
	private Button btnMeetAdd;
	private Button btnMeetEdit;
	private Button btnMeetDelete;
	private Button btnMeetCommit;
	private Button btnMeetGymnasts;
	private Button btnMeetCancel;
	
	
	private TableLayout tbl;
	
	//editing variables
	private String editEvent = "";
	private int editItem=0;
	private String editIdValue = "";
	
	//database variables
	private DBHelper dbh = new DBHelper();
	
	//variables for selecting gymnasts and meets
	private String[] Gymnasts;
	private String[] Meets;
	private List<String> selectedGymnasts = new ArrayList<String>();
	
	
	//---------------------------------------------------------------------------
	//***************************On Click/Create Routines************************
	//---------------------------------------------------------------------------
	/*TODO:Click/Create Routines */
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.meet);
      
        //get UI and initialize
        btnMeetAdd = (Button) findViewById(R.id.meetAdd);
        btnMeetAdd.setOnClickListener(addOnClickListener);
        
        btnMeetEdit = (Button) findViewById(R.id.meetEdit);
        btnMeetEdit.setOnClickListener(editOnClickListener);
        
        btnMeetDelete = (Button) findViewById(R.id.meetDelete);
        btnMeetDelete.setOnClickListener(deleteOnClickListener);
       
        
        btnMeetCommit = (Button) findViewById(R.id.meetCommit);
        btnMeetCommit.setOnClickListener(commitOnClickListener);
        btnMeetCommit.setEnabled(false);
        
        btnMeetCancel = (Button) findViewById(R.id.meetCancel);
        btnMeetCancel.setOnClickListener(cancelOnClickListener);
    
        btnMeetGymnasts = (Button) findViewById(R.id.meetGymnasts);
        btnMeetGymnasts.setOnClickListener(gymnastsOnClickListener);
        btnMeetGymnasts.setVisibility(INV);

        txtMeetName = (EditText) findViewById(R.id.meetNameTxt);
        txtMeetName.setVisibility(INV);
        txtMeetName.setOnTouchListener(txtOnTouch);
        
        txtMeetDate = (EditText) findViewById(R.id.meetDateTxt);
        txtMeetDate.setVisibility(INV);
        txtMeetDate.setOnTouchListener(txtOnTouch);
        
        txtMeetLevel = (EditText) findViewById(R.id.meetLevelTxt);
        txtMeetLevel.setVisibility(INV);
        txtMeetLevel.setOnTouchListener(txtOnTouch);
        
        tvGymnasts = (TextView) findViewById(R.id.meetGymnastsTv);
        tvGymnasts.setVisibility(INV);
        
        tbl = (TableLayout) findViewById(R.id.meetTableLayout);

        checkSystem();
    }
    
    // A call-back for when the user presses the cancel button.
	OnClickListener cancelOnClickListener = new OnClickListener() {
	    public void onClick(View v) {finish();}
	};
    
    // A call-back for when the user presses the add button.
	OnClickListener addOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	    	btnMeetCommit.setVisibility(VIS);
	    	btnMeetCommit.setEnabled(false);
	    	txtMeetName.setVisibility(VIS);
	    	txtMeetDate.setVisibility(VIS);
	    	txtMeetLevel.setVisibility(VIS);
	    	tvGymnasts.setVisibility(VIS);
	    	btnMeetGymnasts.setVisibility(VIS);
	    	editEvent="add";
	    	txtMeetName.selectAll();
	    	
	    	btnMeetDelete.setEnabled(false);
	    	btnMeetEdit.setEnabled(false);
	    	btnMeetAdd.setEnabled(false);
	    	tbl.setLayoutParams(new TableLayout.LayoutParams(0, 0));
	    	
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	Calendar cal = Calendar.getInstance();
	    	txtMeetDate.setText(df.format(cal.getTime()));
			
	    }
	};
    	
    // A call-back for when the user presses the edit button.
	OnClickListener editOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	    	btnMeetCommit.setVisibility(VIS);
	    	btnMeetCommit.setEnabled(false);
	    	txtMeetName.setVisibility(VIS);
	    	txtMeetDate.setVisibility(VIS);
	    	txtMeetLevel.setVisibility(VIS);
	    	tvGymnasts.setVisibility(VIS);
	    	btnMeetGymnasts.setVisibility(VIS);
	    	editEvent="edit";
	    	txtMeetName.selectAll();
	    	
	    	btnMeetDelete.setEnabled(false);
	    	btnMeetEdit.setEnabled(false);
	    	btnMeetAdd.setEnabled(false);
	    	
	    	showDialog(SELECT_DIALOG);
	    	tbl.setLayoutParams(new TableLayout.LayoutParams(0, 0));
	    }
	};

    // A call-back for when the user presses the delete button.
	OnClickListener deleteOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	    	btnMeetCommit.setVisibility(VIS);
	    	btnMeetCommit.setEnabled(false);
	    	txtMeetName.setVisibility(VIS);
	    	txtMeetDate.setVisibility(VIS);
	    	txtMeetLevel.setVisibility(VIS);
	    	tvGymnasts.setVisibility(VIS);
	    	btnMeetGymnasts.setVisibility(VIS);
	    	editEvent="delete";
	    	txtMeetName.selectAll();
	    	
	    	btnMeetDelete.setEnabled(false);
	    	btnMeetEdit.setEnabled(false);
	    	btnMeetAdd.setEnabled(false);
	    	
	    	showDialog(SELECT_DIALOG);
	    	tbl.setLayoutParams(new TableLayout.LayoutParams(0, 0));
	    }
	};
	
	// A call-back for when the user presses the select gymnasts button.
	OnClickListener gymnastsOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	String s=txtMeetLevel.getText().toString();
	    	try{
				Integer.parseInt(s);
				//selectedGymnasts.clear();
		    	showDialog(DIALOG_SELECT_GYMNASTS);
			}catch(NumberFormatException e){
				Toast.makeText(cntx, "Enter a valid level", Toast.LENGTH_LONG).show();
			}
	    }
	};

    // A call-back for when the user presses the commit button.
	OnClickListener commitOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	    	if (validateEntries()){
		    	if (editEvent.compareTo("add")==0){
		    		Meets m = new Meets("0",
		    							txtMeetName.getText().toString(),
		    							txtMeetDate.getText().toString(),
		    							txtMeetLevel.getText().toString(),
		    							getSelectedGymnastsList());
		    		dbh.meetInsert(m);
		    		finish();
		    	}else if (editEvent.compareTo("edit")==0){
		    		Meets m = new Meets(editIdValue,
		    							txtMeetName.getText().toString(),
		    							txtMeetDate.getText().toString(),
		    							txtMeetLevel.getText().toString(),
		    							getSelectedGymnastsList());
		    		dbh.meetUpdate(m);
	    			finish();
		    	}else if (editEvent.compareTo("delete")==0){
		    		dbh.meetDelete(String.valueOf(editIdValue));
		    		finish();
		    	}
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
	
	//routine to check to see if all data entered is valid before writing to DB
	private boolean validateEntries(){
		String mn = txtMeetName.getText().toString();
		if (mn.compareTo("")==0){
			Toast.makeText(cntx, "Enter a meet name", Toast.LENGTH_LONG).show();
			return false;
		}
		
		String md = txtMeetDate.getText().toString();
		if (md.compareTo("")==0){
			Toast.makeText(cntx, "Enter a meet date", Toast.LENGTH_LONG).show();
			return false;
		}
		
		String lvl = txtMeetLevel.getText().toString();
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
		
		String g = tvGymnasts.getText().toString();
		if (g.compareTo("Select Gymnasts")==0)
		{
			Toast.makeText(cntx, "Select Gymnasts please", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	//routine to get the selected gymnasts from the global list
	//and return a formatted string for splitting or writing to database
	private String getSelectedGymnastsList(){
		 String setTxt = "";
		 for (int i=0;i<selectedGymnasts.size();i++)
		 {
			 setTxt = setTxt + selectedGymnasts.get(i) + ":";
		 }
		setTxt=setTxt.substring(0,setTxt.length()-1);
		return setTxt;
	}
	
    
	

	//---------------------------------------------------------------------------
	//***************************Database Management Routines********************
	//---------------------------------------------------------------------------
	/*TODO:Database Management Routines */
	
	//routine to check database to see if there are any records.
	//will enable/disable buttons and populate table based on records
    private void checkSystem(){

    	String p = dbh.getActivityBackground("meet");
		if (p!="nothing"){
			LinearLayout ll = (LinearLayout) findViewById(R.id.meet_piclayout);
			Drawable d = Drawable.createFromPath(p);
	        ll.setBackgroundDrawable(d);
	        ll.refreshDrawableState();
	        ll = null;
	        d = null;
		}
	
		btnMeetEdit.setEnabled(true);
		btnMeetDelete.setEnabled(true);
	
		if (! dbh.doMeetsExist()){
			btnMeetEdit.setEnabled(false);
			btnMeetDelete.setEnabled(false);
			
			TextView t = new TextView(cntx);
			t.setTextColor(Color.BLACK);
			t.setText("Press the ADD button to add a meet.");
			t.setTextSize(24);
			tbl.addView(t);
			
			return;
		}else{
			Meets = dbh.getMeetListAsArray();
			for (int i=0;i<Meets.length;i++){
				Meets  m = dbh.getMeetDataAsObject(Meets[i]);
				String[] gData = m.getMeetGymnasts();
				for (int j=0;j<gData.length;j++){
					TableRow r = new TableRow(cntx);

					TextView t1 = new TextView(cntx);
					t1.setBackgroundColor(R.drawable.translucent_background);
					t1.setTextColor(Color.WHITE);
					t1.setText("");
					t1.setTextSize(24);
					if (j==0){t1.setText(m.getMeetID() + ") ");}
					r.addView(t1);
					
					TextView t2 = new TextView(cntx);
					t2.setBackgroundColor(R.drawable.translucent_background);
					t2.setTextColor(Color.WHITE);
					t2.setText("|");
					t2.setTextSize(24);
					if (j==0){t2.setText("| " + m.getMeetName());}
					r.addView(t2);
					
					TextView t3 = new TextView(cntx);
					t3.setBackgroundColor(R.drawable.translucent_background);
					t3.setTextColor(Color.WHITE);
					t3.setText("|");
					t3.setTextSize(24);
					if (j==0){t3.setText("| " + m.getMeetDate());}
					r.addView(t3);
					
					TextView t4 = new TextView(cntx);
					t4.setBackgroundColor(R.drawable.translucent_background);
					t4.setTextColor(Color.WHITE);
					t4.setText("|");
					t4.setTextSize(24);
					if (j==0){t4.setText("| " + m.getMeetLevel());}
					r.addView(t4);
					
					TextView t5 = new TextView(cntx);
					t5.setBackgroundColor(R.drawable.translucent_background);
					t5.setTextColor(Color.WHITE);
					t5.setText("| " + gData[j]);
					t5.setTextSize(24);
					r.addView(t5);
					
					tbl.addView(r,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT ));
				}
				View v = new View(cntx);
				v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,2));
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
		 		
		 		//dialog to select a single meet from the database to edit or delete
		 		case SELECT_DIALOG:
		        	editItem = 0;
		        	
		        	String pbText = "Delete Meet";
		        	if (editEvent.compareTo("edit")==0){
		        		pbText = "Edit Meet";
		        	}
		        		
		        	return new AlertDialog.Builder(cntx)
		            .setIcon(R.drawable.gymnast_small)
		            .setTitle(pbText)
		            .setSingleChoiceItems(Meets, 0, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
		                    	editItem = whichButton;
		                }
		            })
		            .setPositiveButton(pbText, new DialogInterface.OnClickListener() {
		            	public void onClick(DialogInterface dialog, int whichButton) {
		            		Meets m = dbh.getMeetDataAsObject(Meets[editItem]);
		            		txtMeetName.setText(m.getMeetName());
		           			txtMeetDate.setText(m.getMeetDate());
		           			txtMeetLevel.setText(m.getMeetLevel());
		           			txtMeetName.setEnabled(false);
		           			txtMeetDate.setEnabled(false);
		           			txtMeetLevel.setEnabled(false);
		           			editIdValue = m.getMeetID();
			           		
		           			String[] s = m.getMeetGymnasts();
		           			String lst = "";
		           			for (int i=0;i<s.length;i++){
		           				selectedGymnasts.add(s[i]);
		           				if (i==s.length){
		           					lst = lst + s[i];
		           				}else{
		           					lst = lst + s[i] + "\n";
		           				}
		           			}
		           			tvGymnasts.setText(lst);
		           			tvGymnasts.setVisibility(VIS);
		           			btnMeetCommit.setEnabled(true);
		                }
		            })
		            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            	public void onClick(DialogInterface dialog, int whichButton) {
		            		txtMeetName.setVisibility(INV);
		            		txtMeetDate.setVisibility(INV);
		            		btnMeetCommit.setVisibility(INV);
		            		btnMeetCommit.setEnabled(false);
		            		finish();
	               }
	           })
	          .create();	
		 
		     //Dialog to select gymnasts from the database given a skill level.
//---------------------------------------------------------------------------------------------
		 	 case DIALOG_SELECT_GYMNASTS:
		 		Gymnasts = dbh.getGymnastListArrayByLevel(txtMeetLevel.getText().toString());
		 		if (Gymnasts == null) {
		 			Toast.makeText(cntx, "Please enter a valid level", Toast.LENGTH_LONG ).show();
		 		}else{
		 		
			 		boolean[] ckd = new boolean[Gymnasts.length];
			 		Arrays.fill(ckd, false);
			 		for(int i=0;i<Gymnasts.length;i++){
			 			if (selectedGymnasts.contains(Gymnasts[i])){
			 				ckd[i] = true;
			 			}
			 		}
					 return new AlertDialog.Builder(cntx)
					 .setIcon(R.drawable.gymnast_small)
					 .setTitle("Select Gymnasts")
					 .setMultiChoiceItems(Gymnasts,ckd,
							 new DialogInterface.OnMultiChoiceClickListener() {
						 public void onClick(DialogInterface dialog, int whichButton,
								 boolean isChecked) {
							 String selGymnast = Gymnasts[whichButton];
							 if (isChecked){
								 if (!selectedGymnasts.contains(selGymnast))
								 {
									 selectedGymnasts.add(selGymnast);
								 }
							 }
							 else
							 {
								 if (selectedGymnasts.contains(selGymnast))
								 {
									 selectedGymnasts.remove(selGymnast);
								 }
							 }
						 }
					 })
					 .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
						 public void onClick(DialogInterface dialog, int whichButton) {
							 //set the gymnast list here!!!!
							 if (selectedGymnasts.size()==0)
							 {
								 Toast.makeText(cntx, "Please select at least one gymnast", Toast.LENGTH_SHORT).show();
							 }
							 else
							 {
								 String setTxt = "";
								 for (int i=0;i<selectedGymnasts.size();i++)
								 {
									 setTxt = setTxt + selectedGymnasts.get(i) + "\n";
								 }
								 tvGymnasts.setText(setTxt); 
								 btnMeetCommit.setEnabled(true);
							 }
						 }
					 })
					 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						 public void onClick(DialogInterface dialog, int whichButton) {
							 txtMeetName.setVisibility(INV);
							 txtMeetDate.setVisibility(INV);
							 btnMeetCommit.setVisibility(INV);
							 btnMeetGymnasts.setVisibility(INV);
							 finish();
						 }
					 })
					 .create();
		 		}
		 }
		 return null;
	 }
}
