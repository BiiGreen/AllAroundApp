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

Filename: Score.java
Version: 3.0
Description: Activity to manage the scoring/video editing.
Changes:
12/31/2013: created header data
1/2/2014: re-factoring of new implementation
*/

package com.biig.AllAround;


import java.util.Arrays;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.biig.android.AllAround.R;

public class Score extends Activity{
	
	//context and constants
	private final Context cntx = Score.this;
	private final static int SELECT_DIALOG = 11;
	private final static int DIALOG_UPDATE_DATA = 3;

	//User Interface Elements
	TableLayout tbl;
	
	private String meetID;
	private String meetLevel;
	private String[] Gymnasts=null;
	private String[] Meets=null;
	private double[] Targets=null;
	private String[] Scores=null;
	
	private final static String[] eventList = {"None","Floor","Vault","Bars","Beam"};
	
	//editing variables
	private int editItem=0;
	
	//variables for the database management
	private DBHelper dbh = new DBHelper();
	
	//variables for updating scores
	String editName;
	String editEvent;
	TextView editView=null;
	TextView editNameTV = null;
	TextView editEventTV = null;
	
	private EditText newScore;
	private Spinner spnEvent;
	private Spinner spnGymnast;
	private Button addRecord;
	private Button getVideo;
	
	//---------------------------------------------------------------------------
	//***************************On Click/Create Routines************************
	//---------------------------------------------------------------------------
	/*TODO:Click/Create Routines */
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        
        tbl = (TableLayout) findViewById(R.id.scoreTableLayout);
        
        String p = dbh.getActivityBackground("score");
		if (p!="nothing"){
			LinearLayout ll = (LinearLayout) findViewById(R.id.score_piclayout);
			Drawable d = Drawable.createFromPath(p);
	        ll.setBackgroundDrawable(d);
	        ll.refreshDrawableState();
	        ll = null;
	        d = null;
		}
		
		
		newScore = (EditText) findViewById(R.id.scoreNewScore);
		newScore.setOnTouchListener(txtOnTouch);
		
		addRecord = (Button) findViewById(R.id.scoreAdd);
		addRecord.setOnClickListener(addOnClickListener);
		
		getVideo = (Button) findViewById(R.id.scoreVideo);
		getVideo.setOnClickListener(videoOnClickListener);
		
        spnEvent = (Spinner) findViewById(R.id.scoreEventSpinner);
        //spnEvent.setOnItemSelectedListener(eventSpnListener);
        
        spnGymnast = (Spinner) findViewById(R.id.scoreGymnastSpinner);
		//spnGymnast.setOnItemSelectedListener(gymnastSpnListener);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        
        if (b!=null){
        	String m = b.getString("launchmeet");
        	widgetCall(m);
        }else{
        	showDialog(SELECT_DIALOG);
        }
    }
 
    //routine to handle when the widget calls the activity to start
    //will get data from bundle and show data.
    private void widgetCall(String bndl){
    	Meets mt = dbh.getMeetDataAsObject(bndl);
		Gymnasts = mt.getMeetGymnasts();
		meetID = mt.getMeetID();
		meetLevel = mt.getMeetLevel();
   		Targets = new double[Gymnasts.length];
		Arrays.fill(Targets, 0.0);
		Targets = dbh.fillGymnastTargets(Gymnasts,cntx);
		Scores = dbh.checkScores(Gymnasts, meetID, meetLevel);
		fillScoresTable();
		fillSpinners();
    }

    //routine to call the dialog to start the process of editing a score
    OnClickListener scoreOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	editView = (TextView) v;
	    	String scvdTag = editView.getTag().toString();
	    	String[] scvdAry = TextUtils.split(scvdTag, ":");
	    	String[] s = TextUtils.split(scvdAry[0],"_");
	    	editName = s[0];
	    	editEvent = s[1];
	    	showDialog(DIALOG_UPDATE_DATA);
	    }
    };
    
    
    //routine to call the dialog to start the process of editing a score
    OnClickListener addOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	if (newScore.getText().toString()!=""){
	    		String g = (String) spnGymnast.getSelectedItem().toString();
	    		String e = (String) spnEvent.getSelectedItem().toString();
	    		String ns = newScore.getText().toString();
	    		if (g!="None"){
	    			if (e!="None"){
	    				try {
							Double.parseDouble(ns);
						} catch (NumberFormatException e1) {
							e1.printStackTrace();
						}finally{
				    		dbh.updateScore(g,meetID, e, ns);
			 				TableRow tr = (TableRow) tbl.findViewById(R.id.scoreHeadderRow);
			 				tbl.removeAllViews();
			 				tbl.addView(tr);
			 				Scores = dbh.checkScores(Gymnasts, meetID, meetLevel);
			 				fillScoresTable();
						}
	    			}
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
	//***************************Database Management Routines********************
	//---------------------------------------------------------------------------
	/*TODO:Database Management Routines */
    
    
    //routine to populate the scores table from the gymnast data
    private void fillScoresTable(){
    	
    	double tScr=0;
    	double tFlr=0;
    	double tVlt=0;
    	double tBrs=0;
    	double tBem=0;
    	
    	double[] bFlr = new double[Gymnasts.length];
    	double[] bVlt = new double[Gymnasts.length];
    	double[] bBrs = new double[Gymnasts.length];
    	double[] bBem = new double[Gymnasts.length];
    	
    	Arrays.fill(bFlr, 0);
    	Arrays.fill(bVlt, 0);
    	Arrays.fill(bBrs, 0);
    	Arrays.fill(bBem, 0);
    	
    	for (int i=0;i<Gymnasts.length;i++){
    		String[] gScores = TextUtils.split(Scores[i], ":");
    		TableRow r = new TableRow(cntx);
    		
			TextView t1 = new TextView(cntx);
			t1.setTag(Gymnasts[i] + "_name");
			t1.setTextColor(Color.WHITE);
			t1.setBackgroundColor(R.drawable.translucent_background);
			String aNm = Gymnasts[i];
			if (Gymnasts[i].length()<20){
				for (int j=Gymnasts[i].length();j<20;j++){
					aNm = aNm + "";
				}
			}
			t1.setText(aNm);
			t1.setTextSize(24);
			r.addView(t1);
			
			TextView t2 = new TextView(cntx);
			t2.setTag(Gymnasts[i] + "_floor");
			t2.setOnClickListener(scoreOnClickListener);
			hookTvToVideo(t2, Gymnasts[i], "floor");
			t2.setTextColor(Color.WHITE);
			t2.setBackgroundColor(R.drawable.translucent_background);
			t2.setText("| " + gScores[0]);
			t2.setTextSize(24);
			r.addView(t2);
			
			TextView t3 = new TextView(cntx);
			t3.setTag(Gymnasts[i] + "_vault");
			t3.setOnClickListener(scoreOnClickListener);
			hookTvToVideo(t3, Gymnasts[i], "vault");
			t3.setTextColor(Color.WHITE);
			t3.setBackgroundColor(R.drawable.translucent_background);
			t3.setText("| " + gScores[1]);
			t3.setTextSize(24);
			r.addView(t3);
			
			TextView t4 = new TextView(cntx);
			t4.setTag(Gymnasts[i] + "_bars");
			t4.setTextColor(Color.WHITE);
			t4.setOnClickListener(scoreOnClickListener);
			hookTvToVideo(t4, Gymnasts[i], "bars");
			t4.setBackgroundColor(R.drawable.translucent_background);
			t4.setText("| " + gScores[2]);
			t4.setTextSize(24);
			r.addView(t4);
			
			TextView t5 = new TextView(cntx);
			t5.setTag(Gymnasts[i] + "_beam");
			t5.setOnClickListener(scoreOnClickListener);
			hookTvToVideo(t5, Gymnasts[i], "beam");
			t5.setTextColor(Color.WHITE);
			t5.setBackgroundColor(R.drawable.translucent_background);
			t5.setText("| " + gScores[3]);
			t5.setTextSize(24);
			r.addView(t5);
			
			TextView t6 = new TextView(cntx);
			t6.setTag(Gymnasts[i] + "_total");
			t6.setTextColor(Color.WHITE);
			t6.setBackgroundColor(R.drawable.translucent_background);
			t6.setText("| " + calcTotal(gScores));
			t6.setTextSize(24);
			r.addView(t6);
			
			TextView t7 = new TextView(cntx);
			t7.setTag(Gymnasts[i] + "_target");
			t7.setTextColor(Color.WHITE);
			t7.setBackgroundColor(R.drawable.translucent_background);
			t7.setText("| " + calcTarget(gScores, Targets[i]));
			t7.setTextSize(24);
			r.addView(t7);
			
			tbl.addView(r,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
			View v = new View(cntx);
			v.setPadding(0, 2, 0, 2);
			v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,2));
			v.setBackgroundColor(Color.BLACK);
			tbl.addView(v);
			
			if (Gymnasts.length>2){
				bFlr[i] = Double.valueOf(gScores[0]);
	    		bVlt[i] = Double.valueOf(gScores[1]);
	    		bBrs[i] = Double.valueOf(gScores[2]);
	    		bBem[i] = Double.valueOf(gScores[3]);
			}
    	}
    
    	if (Gymnasts.length>2){
    		TableRow r = new TableRow(cntx); //getTeamScore();
    		
    		Arrays.sort(bFlr);
        	Arrays.sort(bVlt);
        	Arrays.sort(bBrs);
        	Arrays.sort(bBem);
        	
        	int j=0;
        	for (int i=1;i<4;i++){
        		j = Gymnasts.length-i;
        		tFlr = tFlr + bFlr[j];
        		tVlt = tVlt + bVlt[j];
        		tBrs = tBrs + bBrs[j];
        		tBem = tBem + bBem[j];
        	}
        	tScr = tFlr + tVlt + tBrs + tBem;
        	
        	TextView t1 = new TextView(cntx);
    		t1.setTag("team_name");
    		t1.setTextColor(Color.WHITE);
    		t1.setBackgroundColor(R.drawable.translucent_background);
    		t1.setText("Team Score");
			t1.setTextSize(24);
    		r.addView(t1);
    		
    		TextView t2 = new TextView(cntx);
    		t2.setTag("team_floor");
    		t2.setOnClickListener(scoreOnClickListener);
    		t2.setTextColor(Color.WHITE);
    		t2.setBackgroundColor(R.drawable.translucent_background);
    		t2.setText("| " + dbh.rndDbl(tFlr));
			t2.setTextSize(24);
    		r.addView(t2);
    		
    		TextView t3 = new TextView(cntx);
    		t3.setTag("team_vault");
    		t3.setOnClickListener(scoreOnClickListener);
    		t3.setTextColor(Color.WHITE);
    		t3.setBackgroundColor(R.drawable.translucent_background);
    		t3.setText("| " + dbh.rndDbl(tVlt));
			t3.setTextSize(24);
    		r.addView(t3);
    		
    		TextView t4 = new TextView(cntx);
    		t4.setTag("team_bars");
    		t4.setTextColor(Color.WHITE);
    		t4.setOnClickListener(scoreOnClickListener);
    		t4.setBackgroundColor(R.drawable.translucent_background);
    		t4.setText("| " + dbh.rndDbl(tBrs));
			t4.setTextSize(24);
    		r.addView(t4);
    		
    		TextView t5 = new TextView(cntx);
    		t5.setTag("team_beam");
    		t5.setOnClickListener(scoreOnClickListener);
    		t5.setTextColor(Color.WHITE);
    		t5.setBackgroundColor(R.drawable.translucent_background);
    		t5.setText("| " + dbh.rndDbl(tBem));
			t5.setTextSize(24);
    		r.addView(t5);
    		
    		TextView t6 = new TextView(cntx);
    		t6.setTag("team_total");
    		t6.setTextColor(Color.WHITE);
    		t6.setBackgroundColor(R.drawable.translucent_background);
    		
    		t6.setText("| " + dbh.rndDbl(tScr));
			t6.setTextSize(24);
    		r.addView(t6);
    		
    		TextView t7 = new TextView(cntx);
    		t7.setTag("team_target");
    		t7.setTextColor(Color.WHITE);
    		t7.setBackgroundColor(R.drawable.translucent_background);
    		t7.setText("| ");
			t7.setTextSize(24);
    		r.addView(t7);
    		
    		tbl.addView(r,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    		
    		View v = new View(cntx);
    		v.setPadding(0, 2, 0, 2);
    		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,2));
    		v.setBackgroundColor(Color.BLACK);
    		tbl.addView(v);
    	}
    }
   
    //routine to hook a text view to the video on long click listener
    private void hookTvToVideo(TextView tv, String gymnast, String anEvent){
        tv.setOnLongClickListener(videoOnLongClickListener);
        String s = dbh.getMeetName(meetID).replaceAll(" ", "") + "_" +
        			dbh.getMeetDate(meetID)+ "_" +
        			dbh.getMeetLevel(meetID) + "_" +
        			gymnast.replaceAll(" ", "") + "_" + anEvent + ".mp4";
    	tv.setTag(tv.getTag().toString() + ":" + s);
    	if (dbh.checkVideo(s)){tv.setBackgroundColor(getResources().getColor(R.color.solid_green ));}        
    }
    

	//---------------------------------------------------------------------------
	//***************************Calculate and Target Routines*******************
	//---------------------------------------------------------------------------
	/*TODO:Calculate total and target Routines */
	
    //calculate the all around score
    private String calcTotal(String[] gScores){
    	double fl = Double.parseDouble(gScores[0]);
    	double vl = Double.parseDouble(gScores[1]);
    	double br = Double.parseDouble(gScores[2]);
    	double bm = Double.parseDouble(gScores[3]);
    	double v = fl + vl + br + bm;
    	
    	return dbh.rndDbl(v);
    }

    //routine to calculate the remaining target scores needed
    private String calcTarget(String[] gScores, double t){
    	double fl = Double.parseDouble(gScores[0]);
    	double vl = Double.parseDouble(gScores[1]);
    	double br = Double.parseDouble(gScores[2]);
    	double bm = Double.parseDouble(gScores[3]);
    	double v = 0;
    	int i=0;
    	double tot = 0;
    	if (fl>0){
    		i = i + 1;
    		tot = tot + fl;
    	}
    	if (vl>0){
    		i = i + 1;
    		tot = tot + vl;
    	}
    	if (br>0){
    		i = i + 1;
    		tot = tot + br;
    	}
    	if (bm>0){
    		i = i + 1;
    		tot = tot + bm;
    	}
    	if (i==4){
    		return "0";
    	}else if (i==0){
    		v = t/4;
    	}else{
    		v = (t - tot)/(4-i);
    	}
    	String s="";
    	if (v<10){
	    	v = v * 1000;
	    	s = String.valueOf(v);
	    	s = s.substring(0,4);
	    	v = Double.valueOf(s);
	    	v = v / 1000;
    	}else{
    		v = v * 100;
	    	s = String.valueOf(v);
	    	s = s.substring(0,4);
	    	v = Double.valueOf(s);
	    	v = v / 100;
    	}
    	s = String.valueOf(v);
    	return s;
    }
    
    
    
    
    
    //---------------------------------------------------------------------------
	//******************************Video Management Routines********************
	//---------------------------------------------------------------------------
	/*TODO:Video Management Routines */

    OnLongClickListener videoOnLongClickListener = new OnLongClickListener(){
		public boolean onLongClick(View v) {
			TextView t = (TextView) v;
			String scvdTag = t.getTag().toString();
	    	String[] scvdAry = TextUtils.split(scvdTag, ":");
	    	Intent helpIntent = new Intent(cntx, ManageVideo.class);
	        helpIntent.putExtra("fileName", scvdAry[1]);
	        startActivity(helpIntent);
			return false;
		}
    };
    
    //routine to call the dialog to start the process of editing a score
    OnClickListener videoOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	String g = (String) spnGymnast.getSelectedItem().toString().replace(" ", "");
    		String e = (String) spnEvent.getSelectedItem().toString().replace(" ", "");
    		
    		if (g!="None"){
    			if (e!="None"){
	    			e = e.toLowerCase(Locale.US );

	        		String s = dbh.getMeetName(meetID).replaceAll(" ", "") + "_" +
	    			dbh.getMeetDate(meetID)+ "_" +
	    			dbh.getMeetLevel(meetID) + "_" +
	    			g.replaceAll(" ", "") + "_" + e + ".mp4";
	        		Intent helpIntent = new Intent(cntx, ManageVideo.class);
			        helpIntent.putExtra("fileName", s);
			        startActivity(helpIntent);
    			}
    		}
	    }
    };
    
    

	//---------------------------------------------------------------------------
	//***************************spinner and button Routines*********************
	//---------------------------------------------------------------------------
	/*TODO:spinner and button Routines */
	private void fillSpinners(){
		ArrayAdapter<String> eAdapter = new ArrayAdapter<String>(cntx,android.R.layout.simple_spinner_item,eventList); 
        eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEvent.setAdapter(eAdapter);
        
        String[] spnLst = new String[Gymnasts.length+1];
        spnLst[0] = "None";
        for (int i = 0; i<Gymnasts.length;i++){
        	spnLst[i+1] = Gymnasts[i];
        }
        
        ArrayAdapter<String> gAdapter = new ArrayAdapter<String>(cntx,android.R.layout.simple_spinner_item,spnLst); 
        gAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGymnast.setAdapter(gAdapter);
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
	        	//Meets = TextUtils.split(getMeetsList(), ":");
		 		Meets = dbh.getMeetListAsArray(); 
	        	editItem = 0;
	        	return new AlertDialog.Builder(cntx)
	            .setIcon(R.drawable.gymnast_small)
	            .setTitle("Select Meet")
	            .setSingleChoiceItems(Meets, 0, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
	                    	editItem = whichButton;
	                }
	            })
	            .setPositiveButton("Select Meet", new DialogInterface.OnClickListener() {
	            	public void onClick(DialogInterface dialog, int whichButton) {
	            		Meets mt = dbh.getMeetDataAsObject(Meets[editItem]);
	            		Gymnasts = mt.getMeetGymnasts();
	            		meetID = mt.getMeetID();
	            		meetLevel = mt.getMeetLevel();
		           		Targets = new double[Gymnasts.length];
	            		Arrays.fill(Targets, 0.0);
	            		Targets = dbh.fillGymnastTargets(Gymnasts,cntx);
	            		Scores = dbh.checkScores(Gymnasts, meetID, meetLevel);
	            		//dbh.removeScores(Gymnasts,meetID);
	            		fillScoresTable();
	            		fillSpinners();
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            	public void onClick(DialogInterface dialog, int whichButton) {
	            		finish();
                 }
             })
            .create();	
	        
	 case DIALOG_UPDATE_DATA:
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

			    	
	 				dbh.updateScore(editName,meetID, editEvent, ns.getText().toString());
	 				TableRow tr = (TableRow) tbl.findViewById(R.id.scoreHeadderRow);
	 				tbl.removeAllViews();
	 				tbl.addView(tr);
	 				Scores = dbh.checkScores(Gymnasts, meetID, meetLevel);
	        		fillScoresTable();
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
		 case DIALOG_UPDATE_DATA: 
			 EditText ns = (EditText) dialog.findViewById(R.id.newscore_edit);
			 ns.selectAll();
			 editNameTV = (TextView) dialog.findViewById(R.id.newscore_name);
			 editEventTV = (TextView) dialog.findViewById(R.id.newscore_event);
			 editNameTV.setText(editName);
			 editEventTV.setText(editEvent);
			 break;
		 }
	 } 
}


