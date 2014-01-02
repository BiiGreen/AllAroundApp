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

Filename: Chart.java
Version: 3.0
Description: allows the user to see plots of scores over time
Changes:
12/31/2013: created header data
*/
package com.biig.AllAround;


import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import com.biig.android.AllAround.R;


public class Chart extends Activity{
	private final Context cntx = Chart.this;	
	private String selectedGymnast = "";
	
	private LinearLayout plotArea;
	private GraphicalView chartView;
	
	private Spinner eventSpinner;
	private String[] eventsList = {"All","Total","Floor","Vault","Bars","Beam"};
	
	private DBHelper dbh = new DBHelper();
	
	private LayoutParams params = new LayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.FILL_PARENT));
	
	//---------------------------------------------------------------------------
	//***************************On Click/Create Routines************************
	//---------------------------------------------------------------------------
	/*TODO:Click/Create Routines */
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        
        Bundle b = getIntent().getExtras();
		selectedGymnast = b.getString("gymnast");
        
        //get UI and initialize
        eventSpinner = (Spinner) findViewById(R.id.chartLevelSpinner);
        eventSpinner.setOnItemSelectedListener(eventListener);
        setEventSpinner();
            
        plotArea = (LinearLayout) findViewById(R.id.chart);
        chartView = getView(selectedGymnast, "All");
        //chartView.setBackgroundColor(Color.WHITE);
        plotArea.addView(chartView, params);
        
    }
    
  //called when user selects a level
    OnItemSelectedListener eventListener = new OnItemSelectedListener(){
    	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
    		if (position >= 0)
    		{
    			plotArea.removeAllViews();
    			chartView = getView(selectedGymnast, eventsList[position]);
    	        plotArea.addView(chartView, params);
    		}
       	}
		public void onNothingSelected(AdapterView<?> arg0) {}
    }; 
   
    //set the data in the gymnast spinner
    private void setEventSpinner() {
        ArrayAdapter<String> eAdapter = new ArrayAdapter<String>(cntx,android.R.layout.simple_spinner_item,eventsList); 
        eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(eAdapter);
	}
    
    
    //routine to actually create and return the graphical view object.
    private GraphicalView getView(String aGymnast, String anEvent){
    	TimeSeries series = null;
    	XYMultipleSeriesDataset dataSeries = new XYMultipleSeriesDataset();
    	int yMax = 10;
    	int yMin = 5;
    	if (anEvent=="Total"){
    		yMax = 40;
    		yMin = 25;
    	}
    	XYMultipleSeriesRenderer aRenderer = getXYMR(yMax, yMin);
    	
    	XYSeriesRenderer renderer = null;
		Scores sc = dbh.getGymnastScores(aGymnast, "");
    	if (anEvent=="All"){
    		String thisEvent = "Floor";
    		for (int j = 0;j<4;j++){
    			int sColor = Color.BLUE;
	    		series = new TimeSeries(thisEvent);
				for (int i=0;i<sc.getNumEvents();i++){
					Date d = sc.getMeetList(i).getMeetDate();
					if (d==null){
						Toast.makeText(cntx, "The meet " + sc.getMeetList(i).getMeetName()+ " has an invalid date.", Toast.LENGTH_LONG).show();
						finish();
					}
					if (thisEvent=="Floor"){
			    		series.add(d, sc.getMeetList(i).getFloor());
			    		sColor = Color.CYAN;
			    	}else if (thisEvent=="Vault"){
			    		series.add(d, sc.getMeetList(i).getVault());
			    		sColor = Color.RED;
			    	}else if (thisEvent=="Bars"){
			    		series.add(d, sc.getMeetList(i).getBars());
			    		sColor = Color.YELLOW;
			    	}else{
			    		series.add(d, sc.getMeetList(i).getBeam());
			    		sColor = Color.GREEN;
			    	}
				}
				dataSeries.addSeries(series);
	
				renderer = new XYSeriesRenderer();
				
				renderer.setLineWidth(4);
				renderer.setColor(sColor);
				renderer.setDisplayChartValues(true);
				renderer.setPointStyle(PointStyle.CIRCLE);
			    renderer.setFillPoints(true);
			    
				aRenderer.addSeriesRenderer(renderer);
				
				if (j==0){thisEvent="Vault";}
				if (j==1){thisEvent="Bars";}
				if (j==2){thisEvent="Beam";}
    		}
    	}else{
			series = new TimeSeries(anEvent);
			for (int i=0;i<sc.getNumEvents();i++){
				Date d = sc.getMeetList(i).getMeetDate();
				if (d==null){
					Toast.makeText(cntx, "The meet " + sc.getMeetList(i).getMeetName()+ " has an invalid date.", Toast.LENGTH_LONG).show();
					finish();
				}
				if (anEvent=="Total"){
					series.add(d, sc.getMeetList(i).getTotal());
		    	}else if (anEvent=="Floor"){
		    		series.add(d, sc.getMeetList(i).getFloor());	
		    	}else if (anEvent=="Vault"){
		    		series.add(d, sc.getMeetList(i).getVault());
		    	}else if (anEvent=="Bars"){
		    		series.add(d, sc.getMeetList(i).getBars());
		    	}else{
		    		series.add(d, sc.getMeetList(i).getBeam());
		    	}
			}
			dataSeries.addSeries(series);

			renderer = new XYSeriesRenderer();
			renderer.setLineWidth(2);
			renderer.setColor(Color.GREEN);
			renderer.setDisplayChartValues(true);
			renderer.setPointStyle(PointStyle.CIRCLE);
		    renderer.setFillPoints(true);
			aRenderer.addSeriesRenderer(renderer);
    	}
		
    	return ChartFactory.getTimeChartView(cntx, dataSeries, aRenderer, "MMM yyyy");
    }
    
    //routine to set the properties of the time series with the renderer
    private XYMultipleSeriesRenderer getXYMR(int yMax, int yMin){
    	XYMultipleSeriesRenderer aRenderer = new XYMultipleSeriesRenderer();
    
    	aRenderer.setApplyBackgroundColor(true);
        aRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        aRenderer.setAxisTitleTextSize(16);
        aRenderer.setChartTitleTextSize(20);
        aRenderer.setLabelsTextSize(15);
        aRenderer.setLegendTextSize(15);
        aRenderer.setYAxisMax(yMax);
        aRenderer.setYAxisMin(yMin);
        aRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        aRenderer.setZoomButtonsVisible(true);
        aRenderer.setPointSize(5);
        return aRenderer;
    }
}
