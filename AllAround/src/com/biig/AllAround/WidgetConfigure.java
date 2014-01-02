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

Filename: WidgetConfigure.java
Version: 3.0
Description: Activity called when a widget is created
Changes:
12/31/2013: created header data
*/


package com.biig.AllAround;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import com.biig.android.AllAround.R;

public class WidgetConfigure  extends Activity{
	 final Context cntx = WidgetConfigure.this;
	 int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	 private static final String PREF_PREFIX_KEY = "prefix_";
	 private static final String PREFS_NAME = "com.biig.AllAround.WidgetProvider";
	 
	 private Spinner meetSpinner; 
	 private DBHelper dbh = new DBHelper();
	 private String selectedMeet;
	 
	 public WidgetConfigure() {
		 super();
	 }

	 @Override
	 public void onCreate(Bundle icicle) {
		 super.onCreate(icicle);

		 // Set the result to CANCELED.  This will cause the widget host to cancel
		 // out of the widget placement if they press the back button.
		 setResult(RESULT_CANCELED);

		 // Set the view layout resource to use.
		 setContentView(R.layout.main_aw);

		 // Find the EditText
		 meetSpinner = (Spinner)findViewById(R.id.widgetMeetSpinner);
		 meetSpinner.setOnItemSelectedListener(spnListener);
		 loadMeetSpinner();
		 
		 // Find the widget id from the intent. 
		 Intent intent = getIntent();
		 Bundle extras = intent.getExtras();
		 if (extras != null) {
			 mAppWidgetId = extras.getInt(
					 AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		 }

		 // If they gave us an intent without the widget id, just bail.
		 if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			 finish();
		 }
	 }
	 
	 //routine to populate the select meet spinner with the meet list
	 private void loadMeetSpinner(){
		 String[] olst = dbh.getMeetListAsArray();
		 String[] lst = new String[olst.length+1];
		 lst[0] = "Select Meet";
		 for (int i=0;i<olst.length;i++){
			 lst[i+1] = olst[i];
		 }
		 ArrayAdapter<String> eAdapter = new ArrayAdapter<String>(cntx,android.R.layout.simple_spinner_item,lst); 
	     eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     meetSpinner.setAdapter(eAdapter);
	 }
	 
	 //called when user selects gymnast
	 OnItemSelectedListener spnListener = new OnItemSelectedListener(){
		 public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
			 if (position >= 0)
			 {
				 selectedMeet = meetSpinner.getItemAtPosition(position).toString();
				 if (selectedMeet != "Select Meet"){
					 saveTitlePref(cntx, mAppWidgetId, selectedMeet);
					 saveMeetName(cntx, mAppWidgetId, selectedMeet);
					// Push widget update to surface with newly set prefix
			        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(cntx);
			        WidgetProvider.updateAppWidget(cntx, appWidgetManager, mAppWidgetId, selectedMeet);
	
			        // Make sure we pass back the original appWidgetId
			        Intent resultValue = new Intent();
			        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			        setResult(RESULT_OK, resultValue);
			            
			        finish();
				 }
			 }
		 }
		 public void onNothingSelected(AdapterView<?> arg0) {finish();}
	 }; 

	 // Write the prefix to the SharedPreferences object for this widget
	 static void saveTitlePref(Context context, int appWidgetId, String text) {
		 SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
		 prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
		 prefs.commit();
	 }

	 // Read the prefix from the SharedPreferences object for this widget.
	 // If there is no preference saved, get the default from a resource
	 static String loadTitlePref(Context context, int appWidgetId) {
		 SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		 String prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
		 if (prefix != null) {
			 return prefix;
		 } else {
			 return "Select Meet";
		 }
	 }

	 //routine to remove all title preferences for a widget
	 static void deleteAllTitlePref(Context context, int appWidgetId) {
		 SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		 prefs.edit().remove("meetname" + appWidgetId);
		 prefs.edit().commit();
	 }

	 //routine to retrieve all title preferences for a widget
	 static void loadAllTitlePrefs(Context context, ArrayList<Integer> appWidgetIds,
			 ArrayList<String> texts) {
	 }

	 //routine to save the current meet name for a widget
	 static void saveMeetName(Context context, int appWidgetId, String text) {
		 SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
		 prefs.putString("meetname" + appWidgetId, text);
		 prefs.commit();
	 }
	 static String loadMeetName(Context context, int appWidgetId) {
		 SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		 String prefix = prefs.getString("meetname" + appWidgetId, null);
		 if (prefix != null) {
			 return prefix;
		 } else {
			 return "Select Meet";
		 }
	 }
}
