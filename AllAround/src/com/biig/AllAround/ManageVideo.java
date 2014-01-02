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

Filename: ManageVideo.java
Version: 3.0
Description: Activity used to capture and relate video to an event for a gymnast
Changes:
12/31/2013: created header data
*/
package com.biig.AllAround;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;
import com.biig.android.AllAround.R;

public class ManageVideo extends Activity{
	
	private final Context cntx = ManageVideo.this;
	private final static String VIDEO_PATH = Environment.getExternalStorageDirectory() + "/allaround/video";
	final static int REQUEST_VIDEO_CAPTURED = 1;
	private static final int DIALOG_YES_NO_MESSAGE = 1;
	private String path;
	private Uri uriVideo = null;
	private boolean vidExists = false;
	
	//called when created to check if video exists and start 
	//the proper routine based on if it exists.
	@Override 
	public void onCreate(Bundle savedInstanceState) {      
		super.onCreate(savedInstanceState);      
		Bundle b = getIntent().getExtras();
		String cleanFileName = b.getString("fileName").replace("/", "-");
		path = VIDEO_PATH + "/" + cleanFileName;
		File f = new File(path);
		if (f.exists())
		{
			vidExists = true;
			showDialog(DIALOG_YES_NO_MESSAGE);
		}else{
			Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		    startActivityForResult(intent, REQUEST_VIDEO_CAPTURED);
		}
	}
	
	//dialog to ask user if they want to view or overwrite the video
    @Override
	 protected Dialog onCreateDialog(int id) {
		 switch (id) {
		 case DIALOG_YES_NO_MESSAGE:
			 return new AlertDialog.Builder(cntx)
			 .setIcon(R.drawable.gymnast_small)
			 .setTitle("Video Exists!!! \n Do you want to view or record?")
			 .setPositiveButton("Record Video", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int whichButton) {
					Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
				    startActivityForResult(intent, REQUEST_VIDEO_CAPTURED);
				    finish();
				 }
			 })
			 .setNegativeButton("View Video", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int whichButton) {
					Intent intentToPlayVideo = new Intent(Intent.ACTION_VIEW); 
			    	intentToPlayVideo.setDataAndType(Uri.parse(path), "video/*"); 
			    	startActivity(intentToPlayVideo); 
			    	finish();
				 }
			 })
			 .create();   
		 }
		return null;
    }
    
    //when finished recording video, this routine renames the file in the Video folder
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == RESULT_OK){
    		if(requestCode == REQUEST_VIDEO_CAPTURED){
    			uriVideo = data.getData();
    			
    			if (vidExists){
    				File f = new File(path);
    				f.delete();
    			}
    			File sf = new File(getRealPathFromURI(uriVideo));
    			File df = new File(path);
    			sf.renameTo(df);
    			finish();
    		}else{
    			finish();
    		}
    	}else if(resultCode == RESULT_CANCELED){
    		uriVideo = null; 
    		Toast.makeText(cntx,"Cancelled!",Toast.LENGTH_LONG).show();
    	}else{
    		finish();
    	}
    }
    
    //routine to get the file path from the activity result of recording new video
    public String getRealPathFromURI(Uri contentUri) {
    	String[] proj = { MediaStore.Images.Media.DATA }; 
    	Cursor cursor = managedQuery(contentUri, proj, null, null, null);       
    	int column_index  = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    	cursor.moveToFirst();
    	return cursor.getString(column_index);   
    }
}
