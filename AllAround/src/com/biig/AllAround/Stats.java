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

Filename: Stats.java
Version: 3.0
Description: Activity to allow users to evaluate statistics for a gymnast
Changes:
12/31/2013: created header data
*/

package com.biig.AllAround;


import java.util.Arrays;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import com.biig.android.AllAround.R;


public class Stats extends Activity{
	private final Context cntx = Stats.this;	
	private Spinner spnGymnastList;
	private Spinner spnLevelList;
	private String[] gymnastsList;
	private String selectedGymnast;
	private TableLayout tbl = null;
	private final static int dfltTxtSize = 15;
	
	private DBHelper dbh = new DBHelper();
	
	//---------------------------------------------------------------------------
	//***************************On Click/Create Routines************************
	//---------------------------------------------------------------------------
	/*TODO:Click/Create Routines */
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);
        
        gymnastsList = dbh.getGymnastListArray();
        
        //get UI and initialize
        spnGymnastList = (Spinner) findViewById(R.id.statsGymnastSpinner);
        spnGymnastList.setOnItemSelectedListener(spnListener);
        setGymnastSpinner();
        
        spnLevelList = (Spinner) findViewById(R.id.statsLevelSpinner);
        spnLevelList.setOnItemSelectedListener(lvlListener);
        spnLevelList.setEnabled(false);
        tbl = (TableLayout) findViewById(R.id.statsTableView);
        
        String p = dbh.getActivityBackground("stat");
		if (p!="nothing"){
			LinearLayout ll = (LinearLayout) findViewById(R.id.stat_piclayout);
			Drawable d = Drawable.createFromPath(p);
	        ll.setBackgroundDrawable(d);
	        ll.refreshDrawableState();
	        ll = null;
	        d = null;
		}
    }
    
    //called when user selects gymnast
    OnItemSelectedListener spnListener = new OnItemSelectedListener(){
    	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
    		if (position >= 0)
    		{
	    		selectedGymnast = (String) spnGymnastList.getItemAtPosition(position);
	    		if (selectedGymnast.compareTo("Select Gymnast")!=0)
	    		{
	    			spnLevelList.setEnabled(true);
	    			setLevelSpinner(selectedGymnast);
	    			displayStats(selectedGymnast,"");
	    		}
    		}
       	}
		public void onNothingSelected(AdapterView<?> arg0) {}
    }; 
    
    //called when user selects a level
    OnItemSelectedListener lvlListener = new OnItemSelectedListener(){
    	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
    		if (position >= 0)
    		{
	    		if (selectedGymnast.compareTo("Select Gymnast")!=0)
	    		{
	    			String selectedLevel = (String) spnLevelList.getItemAtPosition(position).toString().trim();
	    			if (selectedLevel=="All"){selectedLevel="";}
	    			displayStats(selectedGymnast,selectedLevel);
	    		}
    		}
       	}
		public void onNothingSelected(AdapterView<?> arg0) {}
    }; 
   
    //set the data in the gymnast spinner
    private void setGymnastSpinner() {
    	String str = "Select Gymnast,";
    	String[] lst;
    	for (int i=0;i<gymnastsList.length;i++)
    	{
    		str = str + gymnastsList[i] + ",";
    	}
    	str = str.substring(0,str.length()-1);
    	lst = TextUtils.split(str, ",");
        ArrayAdapter<String> eAdapter = new ArrayAdapter<String>(cntx,android.R.layout.simple_spinner_item,lst); 
        eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGymnastList.setAdapter(eAdapter);
	}
    
    //routine to set the data in the level spinner given a gymnast
    private void setLevelSpinner(String selectedGymnast){
    	String[] lvls = dbh.getGymnastLevels(selectedGymnast);
    	String[] lst = new String[lvls.length+1];
    	lst[0] = "All";
    	for(int i=1;i<lvls.length+1;i++){
    		lst[i] = lvls[i-1];
    	}
    	ArrayAdapter<String> eAdapter = new ArrayAdapter<String>(cntx,android.R.layout.simple_spinner_item,lst); 
        eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLevelList.setAdapter(eAdapter);
    }
    
    
	//---------------------------------------------------------------------------
	//**********************Display and calculate Routines***********************
	//---------------------------------------------------------------------------
	/*TODO:Display and calculate Routines */
	
    
    //main routine to control adding the data to the table view
    private void displayStats(String gName, String lvl){
    	Scores scrs = dbh.getGymnastScores(gName, lvl);
    	tbl.removeAllViews();
    	if (scrs!=null){
    		setBestScores(scrs);
    		setAverageScores(scrs);
    		setStdDevScores(scrs);
    		addSplitterRow();
    		setAllScores(scrs);
    		
    	}else{
    		Toast.makeText(cntx, "No scores were found for that gymnast.", Toast.LENGTH_LONG).show();
    	}
    	scrs = null;
    }
    
    //routine to add all of a gymnasts scores to the table view
    private void setAllScores(Scores scrs){
    	addRow("Meet Name","Floor","Vault","Bars","Beam","Total");
    	for (int i=0;i<scrs.getNumEvents();i++){
    		addRow(scrs.getMeetList(i).getMeetName(),
    				String.valueOf(scrs.getMeetList(i).getFloor()),
    				String.valueOf(scrs.getMeetList(i).getVault()),
    				String.valueOf(scrs.getMeetList(i).getBars()),
    				String.valueOf(scrs.getMeetList(i).getBeam()),
    				dbh.rndDbl(scrs.getMeetList(i).getTotal()));
    	}
    }
    
    //routine to add the best of a gymnasts scores for each event to the table view
    private void setBestScores(Scores scrs){
    	int i=0;
    	int j = scrs.getNumEvents();
    	addRow("","Floor","Vault","Bars","Beam","Total");
    	
    	double[] fl = new double[scrs.getNumEvents()];
    	double[] vl = new double[scrs.getNumEvents()];
    	double[] br = new double[scrs.getNumEvents()];
    	double[] bm = new double[scrs.getNumEvents()];
    	double[] tl = new double[scrs.getNumEvents()];
    	Arrays.fill(fl, 0);
    	Arrays.fill(vl, 0);
    	Arrays.fill(br, 0);
    	Arrays.fill(bm, 0);
    	Arrays.fill(tl, 0);
    	
    	for (i=0;i<j;i++){
    		fl[i] = scrs.getMeetList(i).getFloor();
    		vl[i] = scrs.getMeetList(i).getVault();
    		br[i] = scrs.getMeetList(i).getBars();
    		bm[i] = scrs.getMeetList(i).getBeam();
    		tl[i] = scrs.getMeetList(i).getTotal();
    	}
    	
    	Arrays.sort(fl);
    	Arrays.sort(vl);
    	Arrays.sort(br);
    	Arrays.sort(bm);
    	Arrays.sort(tl);
    	
    	addRow("Best Scores",
    			String.valueOf(fl[j-1]),
    			String.valueOf(vl[j-1]),
    			String.valueOf(br[j-1]),
    			String.valueOf(bm[j-1]),
    			String.valueOf(tl[j-1]));
    }

    //routine to add the average of a gymnasts scores for each event to the table view
    private void setAverageScores(Scores scrs){
    	int i=0;
    	int j = scrs.getNumEvents();
    	double[] fl = new double[scrs.getNumEvents()];
    	double[] vl = new double[scrs.getNumEvents()];
    	double[] br = new double[scrs.getNumEvents()];
    	double[] bm = new double[scrs.getNumEvents()];
    	double[] tl = new double[scrs.getNumEvents()];
    	Arrays.fill(fl, 0);
    	Arrays.fill(vl, 0);
    	Arrays.fill(br, 0);
    	Arrays.fill(bm, 0);
    	Arrays.fill(tl, 0);
    	
    	for (i=0;i<j;i++){
    		fl[i] = scrs.getMeetList(i).getFloor();
    		vl[i] = scrs.getMeetList(i).getVault();
    		br[i] = scrs.getMeetList(i).getBars();
    		bm[i] = scrs.getMeetList(i).getBeam();
    		tl[i] = scrs.getMeetList(i).getTotal();
    	}
    	
    	for (i=1;i<j;i++){
    		fl[0] = fl[0] + fl[i];
    		vl[0] = vl[0] + vl[i];
    		br[0] = br[0] + br[i];
    		bm[0] = bm[0] + bm[i];
    		tl[0] = tl[0] + tl[i];
    	}
    	fl[0] = fl[0]/j;
    	vl[0] = vl[0]/j;
    	br[0] = br[0]/j;
    	bm[0] = bm[0]/j;
    	tl[0] = tl[0]/j;
    	
    	addRow("Avg Scores",
    			dbh.rndDbl(fl[0]),
    			dbh.rndDbl(vl[0]),
    			dbh.rndDbl(br[0]),
    			dbh.rndDbl(bm[0]),
    			dbh.rndDbl(tl[0]));
    }
    
    
    //routine to add the standard deviation of a gymnasts scores for each event to the table view
    private void setStdDevScores(Scores scrs){
    	int i = 0;
		double[] results = {0,0,0,0,0};
		int nEvents=scrs.getNumEvents();
		for (i=0;i<nEvents;i++)
		{
			results[0] = results[0] + scrs.getMeetList(i).getFloor();
			results[1] = results[1] + scrs.getMeetList(i).getVault();
			results[2] = results[2] + scrs.getMeetList(i).getBars();
			results[3] = results[3] + scrs.getMeetList(i).getBeam();
			results[4] = results[4] + scrs.getMeetList(i).getTotal();
		}
		results[0] = results[0]/nEvents;
		results[1] = results[1]/nEvents;
		results[2] = results[2]/nEvents;
		results[3] = results[3]/nEvents;
		results[4] = results[4]/nEvents;
		
		double[] diffMean = {0,0,0,0,0};
		double dSq = 0; 
		for (i=0;i<nEvents;i++)
		{
			dSq = scrs.getMeetList(i).getFloor() - results[0];
			dSq = dSq * dSq;
			diffMean[0] = diffMean[0] + dSq;
			
			dSq = scrs.getMeetList(i).getVault() - results[1];
			dSq = dSq * dSq;
			diffMean[1] = diffMean[1] + dSq;
			
			dSq = scrs.getMeetList(i).getBars() - results[2];
			dSq = dSq * dSq;
			diffMean[2] = diffMean[2] + dSq;
			
			dSq = scrs.getMeetList(i).getBeam() - results[3];
			dSq = dSq * dSq;
			diffMean[3] = diffMean[3] + dSq;
			
			dSq = scrs.getMeetList(i).getTotal() - results[4];
			dSq = dSq * dSq;
			diffMean[4] = diffMean[4] + dSq;
		}
		
		diffMean[0] = diffMean[0] / nEvents;
		diffMean[1] = diffMean[1] / nEvents;
		diffMean[2] = diffMean[2] / nEvents;
		diffMean[3] = diffMean[3] / nEvents;
		diffMean[4] = diffMean[4] / nEvents;
		
		diffMean[0] = Math.sqrt(diffMean[0]);
		diffMean[1] = Math.sqrt(diffMean[1]);
		diffMean[2] = Math.sqrt(diffMean[2]);
		diffMean[3] = Math.sqrt(diffMean[3]);
		diffMean[4] = Math.sqrt(diffMean[4]);
		
		addRow("Std Dev",
    			dbh.rndDbl(diffMean[0]),
    			dbh.rndDbl(diffMean[1]),
    			dbh.rndDbl(diffMean[2]),
    			dbh.rndDbl(diffMean[3]),
    			dbh.rndDbl(diffMean[4]));
    }
		

	//---------------------------------------------------------------------------
	//***************************Populate Table View Routines********************
	//---------------------------------------------------------------------------
	/*TODO:Populate table view with rows Routines */
	
    //routine to add a row to the table view given the propper information
    private void addRow(String title, String flr, String vlt, String brs, String bem, String tot){
    	TableRow r = new TableRow(cntx);

		TextView t = new TextView(cntx);
		t.setTextColor(Color.WHITE);
		t.setBackgroundColor(R.drawable.translucent_background);
		t.setText(title);
		t.setTextSize(dfltTxtSize);
		r.addView(t);
		
		TextView t1 = new TextView(cntx);
		t1.setTextColor(Color.WHITE);
		t1.setBackgroundColor(R.drawable.translucent_background);
		t1.setText("| " + flr);
		t1.setTextSize(dfltTxtSize);
		r.addView(t1);
		
		TextView t2 = new TextView(cntx);
		t2.setTextColor(Color.WHITE);
		t2.setBackgroundColor(R.drawable.translucent_background);
		t2.setText("| " + vlt);
		t2.setTextSize(dfltTxtSize);
		r.addView(t2);
		
		TextView t3 = new TextView(cntx);
		t3.setTextColor(Color.WHITE);
		t3.setBackgroundColor(R.drawable.translucent_background);
		t3.setText("| " + brs);
		t3.setTextSize(dfltTxtSize);
		r.addView(t3);
		
		TextView t4 = new TextView(cntx);
		t4.setTextColor(Color.WHITE);
		t4.setBackgroundColor(R.drawable.translucent_background);
		t4.setText("| " + bem);
		t4.setTextSize(dfltTxtSize);
		r.addView(t4);
		
		TextView t5 = new TextView(cntx);
		t5.setTextColor(Color.WHITE);
		t5.setBackgroundColor(R.drawable.translucent_background);
		t5.setText("| " + tot);
		t5.setTextSize(dfltTxtSize);
		r.addView(t5);
	
		tbl.addView(r,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		View v = new View(cntx);
		v.setPadding(0, 2, 0, 2);
		v.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,2));
		v.setBackgroundColor(Color.BLACK);
		tbl.addView(v);
    }
    
    //routine to add a splitter row to the table to divide information up
    private void addSplitterRow(){
    	TableRow r = new TableRow(cntx);

		TextView t = new TextView(cntx);
		t.setTextColor(Color.WHITE);
		t.setBackgroundColor(R.drawable.translucent_background);
		t.setText("----------");
		t.setTextSize(dfltTxtSize);
		r.addView(t);
		
		TextView t1 = new TextView(cntx);
		t1.setTextColor(Color.WHITE);
		t1.setBackgroundColor(R.drawable.translucent_background);
		t1.setText("------");
		t1.setTextSize(dfltTxtSize);
		r.addView(t1);
		
		TextView t2 = new TextView(cntx);
		t2.setTextColor(Color.WHITE);
		t2.setBackgroundColor(R.drawable.translucent_background);
		t2.setText("------");
		t2.setTextSize(dfltTxtSize);
		r.addView(t2);
		
		TextView t3 = new TextView(cntx);
		t3.setTextColor(Color.WHITE);
		t3.setBackgroundColor(R.drawable.translucent_background);
		t3.setText("------");
		t3.setTextSize(dfltTxtSize);
		r.addView(t3);
		
		TextView t4 = new TextView(cntx);
		t4.setTextColor(Color.WHITE);
		t4.setBackgroundColor(R.drawable.translucent_background);
		t4.setText("------");
		t4.setTextSize(dfltTxtSize);
		r.addView(t4);
		
		TextView t5 = new TextView(cntx);
		t5.setTextColor(Color.WHITE);
		t5.setBackgroundColor(R.drawable.translucent_background);
		t5.setText("------");
		t5.setTextSize(dfltTxtSize);
		r.addView(t5);
	
		tbl.addView(r,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		View v = new View(cntx);
		v.setPadding(0, 2, 0, 2);
		v.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,2));
		v.setBackgroundColor(Color.BLACK);
		tbl.addView(v);
    }
    
    
    
    
    /*TODO:Add dialog to pick activity and call dbh to set backround image Routines 
     * Need to also add the check in checkSystem() for setting background to other
     * activities. Then get some images on the disk and check*/
    
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		String s = spnGymnastList.getSelectedItem().toString();
		if (s != "Select Gymnast"){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.layout.stats_options_menu , menu);
			return true;
		}else{
			Toast.makeText(cntx, "Please select a gymnast", Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	//routine to start the charts activity when selected in the menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection    
		switch (item.getItemId()) {
		case R.id.menuChart:
			String s = spnGymnastList.getSelectedItem().toString();
			if (s != "Select Gymnast"){
			
			Intent helpIntent = new Intent(cntx, Chart.class);
	        helpIntent.putExtra("gymnast", s);
	        startActivity(helpIntent);
			return true; 
			}else{
				return false;
			}
		default:
			return super.onOptionsItemSelected(item);    
		}
	}
}
