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

Filename: Import.java
Version: 3.0
Description: Activity designed to import from CSV to SQL database.
THIS METHODOLOGY WAS NOT IMPLEMENTD 
Changes:
12/31/2013: created header data
*/
package com.biig.AllAround;

//import java.text.DecimalFormat;
//import java.text.NumberFormat;

import com.biig.android.AllAround.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Import extends Activity{
	
	private final Context cntx = Import.this;
	
	private String ImportName = "";
	

	private final static int DIALOG_IMPORT_GYMNAST=21;
	
	private EditText gymnastLevel;
	private EditText gymnastLastName;
	private EditText gymnastFirstName;
	private Spinner spnMeet;
	private Spinner spnGymnast;
	private Button importGymnast;
	private Button importMeet;
	private Button exitImport;
	private EditText meetLevel;
	
	private ImportGymnasts myGymnasts;
	private ImportMeets myMeets;
	
	private String strSelGymnast;
	
	

	private String[] meetScores;
	
	
	
	private DBHelper dbh = new DBHelper();
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.database_import);
        
        gymnastLevel = (EditText) findViewById(R.id.importGymnastLevelTxt);
        gymnastLastName = (EditText) findViewById(R.id.importLastNameTxt);
        gymnastFirstName = (EditText) findViewById(R.id.importFirstNameTxt);

        meetLevel = (EditText) findViewById(R.id.importMeetLevelTxt);
        
        spnMeet = (Spinner) findViewById(R.id.importMeetSpinner);
        spnGymnast = (Spinner) findViewById(R.id.importGymnastSpinner);
        
        importGymnast = (Button) findViewById(R.id.importGymnastBtn);
        importGymnast.setOnClickListener(gymnastOnClickListener);
        
        importMeet = (Button) findViewById(R.id.importMeetBtn);
        importMeet.setOnClickListener(meetOnClickListener);
        
        exitImport = (Button) findViewById(R.id.importExitBtn);
        exitImport.setOnClickListener(cancelOnClickListener);
        
        fillSpinners();
        
        
    }
    
    // A call-back for when the user presses the cancel button.
	OnClickListener cancelOnClickListener = new OnClickListener() {
	    public void onClick(View v) {finish();}
	};
	
	// A call-back for when the user presses the import gymnast button.
	OnClickListener gymnastOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	if (dbh.getGymnastDataByName(strSelGymnast + " " + gymnastLastName.getText().toString())[0]=="false"){
	    		Gymnasts g = new Gymnasts("0",
	    				gymnastFirstName.getText().toString(),
    					gymnastLastName.getText().toString(), 
    					gymnastLevel.getText().toString(), 
    					myGymnasts.getTargetString(strSelGymnast));
    			dbh.gymnastInsert(g);
    			g = null;
	    	}else{
	    		Toast.makeText(cntx, "Gymnast Already Exists", Toast.LENGTH_LONG).show();
	    	}
	    }
	};
	
		
	// A call-back for when the user presses the import meet button.
	OnClickListener meetOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    
	    	//need to get meet name from spinner
	    	//meetScores = TextUtils.split(myMeets.getScores(meetName),",");
			
	    	//ImportScores myScores = new ImportScores(meetScores[j], cntx);
			
	    	
	    	
	    	
	    }
	};
	OnItemSelectedListener spnGymnastListener = new OnItemSelectedListener(){
    	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
    		strSelGymnast = spnGymnast.getItemAtPosition(position).toString();
	    }
		public void onNothingSelected(AdapterView<?> arg0) {}
    }; 
    
    
    private void fillSpinners(){
    	myGymnasts = new ImportGymnasts(cntx);
        myMeets = new ImportMeets(cntx);
        
		ArrayAdapter<String> eAdapter = new ArrayAdapter<String>(cntx,android.R.layout.simple_spinner_item, myGymnasts.getNames()); 
        eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGymnast.setAdapter(eAdapter);
        
        ArrayAdapter<String> gAdapter = new ArrayAdapter<String>(cntx,android.R.layout.simple_spinner_item, myMeets.getNames()); 
        gAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMeet.setAdapter(gAdapter);
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	//---------------------------------------------------------------------------
	//***************************Dialog******************************************
	//---------------------------------------------------------------------------
	/*TODO:Dialog Routines */
		
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) { 
    	
        	
	 	case DIALOG_IMPORT_GYMNAST:
	 		LayoutInflater factory2 = LayoutInflater.from(this);
	 		final View textEntryView2 = factory2.inflate(R.layout.alert_dialog_text_entry, null);
	 		return new AlertDialog.Builder(cntx)
	 		.setIcon(R.drawable.gymnast_small)
	 		.setTitle("Enter Gymnast Level for: ")
	 		.setView(textEntryView2)
	 		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	 			public void onClick(DialogInterface dialog, int whichButton) {
	 				Dialog curDialog = (Dialog) dialog;
	 				EditText ns = (EditText) curDialog.findViewById(R.id.oldgymnast_edit );
	 				try {
						Double.parseDouble(ns.getText().toString());
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}finally{

				    	
		 				
					}
	 			}
	 		})
	 		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 			public void onClick(DialogInterface dialog, int whichButton) {}
	 		})
	 		.create();
			 }
        	
    	return null;
    	
    }

    //routine to prepare the dialog and set the data in the text 
  	 @Override protected void onPrepareDialog(final int id, final Dialog dialog) {
  		 switch (id) {   
  		 case DIALOG_IMPORT_GYMNAST: 
  			 EditText ns = (EditText) dialog.findViewById(R.id.oldgymnast_edit );
  			 ns.selectAll();
  			 TextView editNameTV = (TextView) dialog.findViewById(R.id.oldgymnast_name );
  			 editNameTV.setText(ImportName);
  			 break;
  		 }
  	 } 
    
}
