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

Filename: WidgetScore.java
Version: 3.0
Description: Activity designed to let user set a score for a gymnast 
using the widget for that widgets meet.
Changes:
12/31/2013: created header data
*/

package com.biig.AllAround;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.biig.android.AllAround.R;

public class WidgetScore extends Activity{
	private final Context cntx = WidgetScore.this;
	private final int DIALOG_GYMNAST = 102;
	private final int DIALOG_EVENT = 103;
	private final int DIALOG_SCORE = 104;
	private DBHelper dbh = new DBHelper();
	
	private String[] Gymnasts;
	private int editItem;
	private String selectedGymnast;
	private String selectedEvent;
	private String meetID;
	private String mID;
	private final static String[] eventList = {"Floor","Vault","Bars","Beam"};
	
	/** Called when the activity is first created.
	 * and will show the dialog to select a gymnast */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Intent i = getIntent();
        Bundle b = i.getExtras();
        
        if (b!=null){
        	meetID = b.getString("launchmeet");
        	
        	Meets mt = dbh.getMeetDataAsObject(meetID);
    		mID = mt.getMeetID();
    		
    		Gymnasts = mt.getMeetGymnasts();
    		editItem = 0;
        	showDialog(DIALOG_GYMNAST);
        }else{
        	finish();
        }
    }
    
    
    //dialogs to select items
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {

    	//dialog to select a single gymnast for updating score
    	case DIALOG_GYMNAST:
    		
    		return new AlertDialog.Builder(cntx)
    		.setIcon(R.drawable.gymnast_small)
    		.setTitle("Select Meet")
    		.setSingleChoiceItems(Gymnasts, 0, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				editItem = whichButton;
    			}
    		})
    		.setPositiveButton("Select Gymnast", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				selectedGymnast = Gymnasts[editItem];
    				showDialog(DIALOG_EVENT);
    			}
    		})
    		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				finish();
    			}
    		})
    		.create();	

    	//dialog to select a single event for updating score
    	case DIALOG_EVENT:
    		editItem = 0;
    		return new AlertDialog.Builder(cntx)
    		.setIcon(R.drawable.gymnast_small)
    		.setTitle("Select Event")
    		.setSingleChoiceItems(eventList, 0, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				editItem = whichButton;
    			}
    		})
    		.setPositiveButton("Select Gymnast", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				selectedEvent = eventList[editItem].toLowerCase();
    				showDialog(DIALOG_SCORE);
    			}
    		})
    		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				finish();
    			}
    		})
    		.create();	
    		
    	//dialog to get and update the new score for the selected gymnast and meet
    	case DIALOG_SCORE:
    		LayoutInflater factory = LayoutInflater.from(this);
    		final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
    		return new AlertDialog.Builder(cntx)
    		.setIcon(R.drawable.gymnast_small)
    		.setTitle("Enter new score for: ")
    		.setView(textEntryView)
    		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				Dialog curDialog = (Dialog) dialog;
    				EditText ns = (EditText) curDialog.findViewById(R.id.newscore_edit);
    				try {
    					Double.parseDouble(ns.getText().toString());
    				} catch (NumberFormatException e) {
    					e.printStackTrace();
    				}finally{
    					dbh.updateScore(selectedGymnast,mID, selectedEvent, ns.getText().toString());
    					finish();
    				}
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
    

    //routine called before dialog score is shown to update gymnast and event info
    @Override protected void onPrepareDialog(final int id, final Dialog dialog) {
    	switch (id) {   
    	case DIALOG_SCORE: 
    		EditText ns = (EditText) dialog.findViewById(R.id.newscore_edit);
    		ns.selectAll();
    		TextView editNameTV = (TextView) dialog.findViewById(R.id.newscore_name);
    		TextView editEventTV = (TextView) dialog.findViewById(R.id.newscore_event);
    		editNameTV.setText(selectedGymnast);
    		editEventTV.setText(selectedEvent);

    		break;
    	}
    } 
}
