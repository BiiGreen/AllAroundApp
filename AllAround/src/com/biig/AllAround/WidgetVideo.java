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

Filename: WidgetViedo.java
Version: 3.0
Description:  Activity designed to let user set a video for a gymnast 
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
import com.biig.android.AllAround.R;

public class WidgetVideo extends Activity{
	private final Context cntx = WidgetVideo.this;
	private final int DIALOG_GYMNAST = 102;
	private final int DIALOG_EVENT = 103;
	private DBHelper dbh = new DBHelper();
	
	private String[] Gymnasts;
	private int editItem;
	private String selectedGymnast;
	private String meetID;
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

    	//dialog to select a single gymnast to get video for
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

    		//dialog to select a single event to get video for and to start the process
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
    		.setPositiveButton("Select Event", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				String s = meetID.replaceAll(" ", "") + "_" + 
    				selectedGymnast.replaceAll(" ", "") + "_" +
    				eventList[editItem].toLowerCase() + ".mp4";

    				Intent helpIntent = new Intent(cntx, ManageVideo.class);
    				helpIntent.putExtra("fileName", s);
    				startActivity(helpIntent);
    				finish();
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
