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

Filename: AllAround.java
Version: 3.0
Description: main code base for controlling the program flow and initialization
Changes:
12/31/2013: created header data
1/2/2014: re-factoring of new implementation
*/

package com.biig.AllAround;


import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.database.Cursor; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.BitmapDrawable;
import com.biig.android.AllAround.R;

public class AllAround extends Activity {
	
	//context and constants
	private final Context cntx = AllAround.this;
	private final static int DIALOG_DISCLAIMER=19;
	
	//User Interface
	public Button btnMainMeet;
	private Button btnMainManage;
	private Button btnMainStats;
	
	private LinearLayout ll;
	
	
	private String selActivity="";
	private DBHelper dbh = new DBHelper();
	

	private BitmapDrawable d;
	
	
	
	
	
	//---------------------------------------------------------------------------
	//***************************OnClick & OnCreate Routines*********************
	//---------------------------------------------------------------------------
	/*TODO:OnClick, OnCreate & OnRestart Routines */

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //get UI and initialize
        Button btnMainGymnast = (Button) findViewById(R.id.mainBtnAddGymnast);
        btnMainGymnast.setOnClickListener(gymnastOnClickListener);
        
        btnMainMeet = (Button) findViewById(R.id.mainBtnAddMeet);
        btnMainMeet.setOnClickListener(meetOnClickListener);
        
        btnMainManage = (Button) findViewById(R.id.mainBtnManageMeet);
        btnMainManage.setOnClickListener(manageOnClickListener);
        
        btnMainStats = (Button) findViewById(R.id.mainBtnDoStats);
        btnMainStats.setOnClickListener(statsOnClickListener);
        
        
        Button btnHelp = (Button) findViewById(R.id.mainHelp);
        btnHelp.setOnClickListener(helpOnClickListener);


        checkSystem();
    }
    

    //routine to recycle the bitmap for the background when stopped
    @Override
    public void onStop(){
    	super.onStop();
    	if (d!=null){
 		   d.getBitmap().recycle();
 		   d = null;
 	   }
    }
    
    //routine to set the background to an image or the default
    @Override
    public void onStart(){
    	super.onStart();
    	ll = (LinearLayout) findViewById(R.id.main_piclayout);
        DBHelper dbh = new DBHelper();
		String p = dbh.getActivityBackground("main");
		if (p!="nothing"){
			d = new BitmapDrawable(cntx.getResources(),p);
			ll.setBackgroundDrawable(d);
			ll.refreshDrawableState();
		}else{
			ll.setBackgroundResource(R.drawable.main1);
		}
    }
    
    //check the system when the activity gets focus again to
    //enable or disable buttons cause the user added something
    @Override
    public void onRestart(){
    	super.onRestart();
    	checkSystem();
    }
    
    //A call-back for when the user presses the gymnasts button.
	OnClickListener gymnastOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	        Intent gymnastIntent = new Intent(cntx, Gymnast.class);
	        startActivity(gymnastIntent);
	    }
	};
	
	// A call-back for when the user presses the meets button.
	OnClickListener meetOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	        Intent meetIntent = new Intent(cntx, Meet.class);
	        startActivity(meetIntent);
	    }
	};
	
	// A call-back for when the user presses the scores button.
	OnClickListener manageOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	        Intent scoreIntent = new Intent(cntx, Score.class);
	        startActivity(scoreIntent);
	    }
	};
	
	// A call-back for when the user presses the scores button.
	OnClickListener statsOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	        Intent statsIntent = new Intent(cntx, Stats.class);
	        startActivity(statsIntent);
	    }
	};
	
	// A call-back for when the user presses the help button.
	OnClickListener helpOnClickListener = new OnClickListener() {
	    public void onClick(View v) {	
	        Intent helpIntent = new Intent(cntx, AllAroundHelp.class);
	        startActivity(helpIntent);
	    }
	};

	//---------------------------------------------------------------------------
	//***************************Database Management Routines********************
	//---------------------------------------------------------------------------
	/*TODO:Database Management Routines */
	
	//routine to check the system to see if:
	//the database exists
	//the folder exists
	//the database contains data
	//enables/disables buttons based on data content
	private void checkSystem(){
		String aadbName = Environment.getExternalStorageDirectory() + "/allaround/AllAroundScoring";
		String gymnastTable = "gymnasts";
		String meetTable = "meets";
		String scoreTable = "scores";
		String idTable = "ids";
		SQLiteDatabase AADB;
		try {
			
			btnMainMeet.setEnabled(true);
			btnMainManage.setEnabled(true);
			btnMainStats.setEnabled(true);
			AADB = SQLiteDatabase.openDatabase(aadbName, null, SQLiteDatabase.OPEN_READONLY);
			Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable, null);
			if (c.getCount()==0){
				btnMainMeet.setEnabled(false);
				btnMainManage.setEnabled(false);
				btnMainStats.setEnabled(false);
			}else{
				c.close();
				c = AADB.rawQuery("SELECT * FROM "+ meetTable, null);
				if (c.getCount()==0){
					btnMainManage.setEnabled(false);	
					btnMainStats.setEnabled(false);
				}else{
					c.close();
					c = AADB.rawQuery("SELECT * FROM "+ scoreTable, null);
					if (c.getCount()==0){
						btnMainStats.setEnabled(false);
					}
				}
			}
			AADB.close();
			c.close();
		}catch(SQLiteException e){
			String msg = e.getMessage();
			if (msg.contains("unable to open")){
				File f = new File(Environment.getExternalStorageDirectory() + "/allaround");
		    	if (! f.exists()){f.mkdir();}
		    	File g = new File(Environment.getExternalStorageDirectory() + "/allaround/video");
		    	if (! g.exists()){g.mkdir();}
		    	
				AADB = openOrCreateDatabase(aadbName,0,null);
				AADB.execSQL("CREATE TABLE IF NOT EXISTS " + gymnastTable + 
						" (_id INT(4)," +
						" firstname VARCHAR," +
						" lastname VARCHAR," +
						" level INT(4)," +
						" target REAL);");
				
				AADB.execSQL("CREATE TABLE IF NOT EXISTS " + meetTable + 
						" (_id INT(4)," +
						" meetname VARCHAR," +
						" meetdate VARCHAR," +
						" level INT(4)," +
						" gymnasts VARCHAR);");
				
				AADB.execSQL("CREATE TABLE IF NOT EXISTS " + scoreTable +		
						" (_id INT(4)," +
						" meet VARCHAR," +
						" level INT(4)," +
						" gymnast VARCHAR," +
						" floor REAL," +
						" vault REAL," +
						" bars REAL," +
						" beam REAL);");
			
				AADB.execSQL("CREATE TABLE IF NOT EXISTS " + idTable +		
						" (_id INT(4)," +
						" type," +
						" value INT(4));");
				AADB.execSQL("INSERT INTO " + idTable + 
    					" (_id, type, value) VALUES(1,'gymnasts',1);");
				AADB.execSQL("INSERT INTO " + idTable + 
						" (_id, type, value) VALUES(2,'meets',1);");
				AADB.execSQL("INSERT INTO " + idTable + 
						" (_id, type, value) VALUES(3,'scores',1);");
			
				AADB.execSQL("CREATE TABLE IF NOT EXISTS img" +		
						" (_id INT(4)," +
						" activity VARCHAR," +
						" path VARCAR);");
				AADB.execSQL("INSERT INTO img (_id,activity,path) VALUES(0,'main','nothing');");
				AADB.execSQL("INSERT INTO img (_id,activity,path) VALUES(1,'gymnast','nothing');");
				AADB.execSQL("INSERT INTO img (_id,activity,path) VALUES(2,'meet','nothing');");
				AADB.execSQL("INSERT INTO img (_id,activity,path) VALUES(3,'score','nothing');");
				AADB.execSQL("INSERT INTO img (_id,activity,path) VALUES(4,'stat','nothing');");
				
				
				AADB.close();
			
				Toast.makeText(cntx, "Created SQLite Database", Toast.LENGTH_SHORT).show();
				btnMainMeet.setEnabled(false);
				btnMainManage.setEnabled(false);
				btnMainStats.setEnabled(false);
				showDialog(DIALOG_DISCLAIMER);
			}else{
				e.printStackTrace();
				
				//add toast message
				Toast.makeText(cntx, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}



	//---------------------------------------------------------------------------
	//***************************Dialog******************************************
	//---------------------------------------------------------------------------
	/*TODO:Dialog Routines */
		
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) { 
    	case DIALOG_DISCLAIMER:
    		// This example shows how to add a custom layout to an AlertDialog
    		LayoutInflater factory = LayoutInflater.from(this);
    		final View textEntryView = factory.inflate(R.layout.alert_dialog_disclaimer, null);
    		return new AlertDialog.Builder(cntx)
    		.setIcon(R.drawable.gymnast_small)
    		.setTitle("App Disclaimer!")
    		.setView(textEntryView)
    		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
        			Toast.makeText(cntx, "Thank You", Toast.LENGTH_LONG).show();
    			}
    		})
    		.setNegativeButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				Toast.makeText(cntx, "Thank You", Toast.LENGTH_LONG).show();
    			}
    		})
    		.create();

    	}
        	
    	return null;
    	
    }
    
    
    
    /*TODO:Add dialog to pick activity and call dbh to set backround image Routines */
    
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.main_options_menu , menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection    
		switch (item.getItemId()) {
		case R.id.menuSet:
//			setBk = true;
//			showDialog(DIALOG_SELECT);
			
			setActivityBackground("main");
			
			return true;
		case R.id.menuRemove:
//			setBk = false;
//			showDialog(DIALOG_SELECT);
			
			dbh.removeActivityBackground("main");
			
	    	return true;  
		default:
			return super.onOptionsItemSelected(item);    
		}
	}
    
    
	 public void setActivityBackground(String act){
	    	selActivity = act;
	    	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	    	i.setType("image/*");
	    	i.addCategory(Intent.CATEGORY_OPENABLE);
	    	
	    	Intent finalIntent = Intent.createChooser(i,"Select Background Image");
	    	startActivityForResult(finalIntent, 0);	
	}
	    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == RESULT_OK){
	    	if(requestCode == 0){
	    		Uri u = data.getData();
	    		
	    		dbh.addActivityBackground(selActivity, getRealPathFromURI(u));
	    		
	    	}
	    }
	}

	public String getRealPathFromURI(Uri contentUri) {
	    String[] proj = { MediaStore.Images.Media.DATA }; 
	    Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);       
	    int column_index  = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);   
	}
}
