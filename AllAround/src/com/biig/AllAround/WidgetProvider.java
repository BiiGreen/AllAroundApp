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

Filename: WidgetProvider.java
Version: 3.0
Description: Provider for the widget to set the interaction with the display
when the user updates a widget
Changes:
12/31/2013: created header data
1/2/2014: re-factoring of new implementation
*/

package com.biig.AllAround;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.biig.android.AllAround.R;


public class WidgetProvider extends AppWidgetProvider{
	
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // For each widget that needs an update, get the text that we should display:
        //   - Create a RemoteViews object for it
        //   - Set the text in the RemoteViews object
        //   - Tell the AppWidgetManager to show that views object for the widget.
		
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            String titlePrefix = WidgetConfigure.loadTitlePref(context, appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId, titlePrefix);
        }
    }
    
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            WidgetConfigure.deleteAllTitlePref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // When the first widget is created, register for the TIMEZONE_CHANGED and TIME_CHANGED
        // broadcasts.  We don't want to be listening for these if nobody has our widget active.
        // This setting is sticky across reboots, but that doesn't matter, because this will
        // be called after boot if there is a widget instance for this provider.
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("com.biig.AllAround", ".WidgetProvider"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onDisabled(Context context) {
        // When the first widget is created, stop listening for the TIMEZONE_CHANGED and
        // TIME_CHANGED broadcasts.
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("com.biig.AllAround", ".WidgetProvider"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String titlePrefix) {
    	
    	//get title pref
        String s = WidgetConfigure.loadTitlePref(context, appWidgetId);
        
        //get the meet name, date and level
        String[] meetInfo = new String[3];
        
        WidgetConfigure.saveTitlePref(context, appWidgetId, s);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider);
        if (s.compareTo("Select Meet")!=0){
        	meetInfo = TextUtils.split(s, "_");
        	views.setTextViewText(R.id.appwidget_meetName, meetInfo[0] + " " + meetInfo[1]);
        	// Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, WidgetConfigure.class);
            
            //set pending intent to reconfigure widget
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(ContentUris.withAppendedId(Uri.EMPTY,appWidgetId));
            PendingIntent pendingIntent = PendingIntent.getActivity (context, appWidgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_meetName, pendingIntent);
             
            //create pending intent to launch scores activity with widiget's meet
            Intent scoresIntent = new Intent(context, Score.class);
            scoresIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            scoresIntent.putExtra("launchmeet", s);
            Uri data = Uri.withAppendedPath(Uri.parse("ABCD://widget/id/"),String.valueOf(appWidgetId));
            scoresIntent.setData(data); 
            PendingIntent scoresPendIntent = PendingIntent.getActivity(context,appWidgetId, scoresIntent,PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_activity, scoresPendIntent);
            
            //create pending intent to launch video activity with widiget's meet
            Intent videoIntent = new Intent(context, WidgetVideo.class);
            videoIntent.putExtra("launchmeet", s);
            Uri videoData = Uri.withAppendedPath(Uri.parse("VIDEO://widget/id/"),String.valueOf(appWidgetId));
            videoIntent.setData(videoData); 
            PendingIntent videoPendIntent = PendingIntent.getActivity(context,appWidgetId, videoIntent,PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_video, videoPendIntent);            
            
            //create intent to launch main app
            Intent mainIntent = new Intent(context, AllAround.class);
	        Uri mainData = Uri.withAppendedPath(Uri.parse("MAIN://widget/id/"),String.valueOf(appWidgetId));
            mainIntent.setData(mainData); 
            PendingIntent mainPendIntent = PendingIntent.getActivity(context,appWidgetId, mainIntent,PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_app, mainPendIntent);
            
            //create pending intent to launch enter score activity with widiget's meet
            Intent enterIntent = new Intent(context, WidgetScore.class);
            enterIntent.putExtra("launchmeet", s);
            Uri enterData = Uri.withAppendedPath(Uri.parse("SCORE://widget/id/"),String.valueOf(appWidgetId));
            enterIntent.setData(enterData); 
            PendingIntent enterPendIntent = PendingIntent.getActivity(context,appWidgetId, enterIntent,PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_score, enterPendIntent);            
            
        }else{
        	meetInfo[0]="name";
        	meetInfo[1]="date";
        	meetInfo[2]="level";
        }
        	
        // Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
