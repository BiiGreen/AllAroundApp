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

Filename: ImportMeets.java
Version: 3.0
Description: Activity designed to import from CSV to SQL database.
THIS METHODOLOGY WAS NOT IMPLEMENTD 
Changes:
12/31/2013: created header data
*/
package com.biig.AllAround;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

public class ImportMeets {

	private final static String MEET_PATH = Environment.getExternalStorageDirectory() + "/allaround/meets.txt";	
	private String[] meetNames;
	private String[] meetDates;
	private List<String> mGymnasts = new ArrayList<String>();
	private List<String> mScores = new ArrayList<String>();
	
	public ImportMeets(Context cntx) {
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(MEET_PATH));
            String rStr="";
            String mNames="";
            String mDates="";
            boolean isData = false;
            while ((rStr = in.readLine())!= null){
            	if(rStr.compareTo("empty")!=0){
	            	String[] Meets = TextUtils.split(rStr,":");
	            	mNames = mNames + Meets[0] + ";";
	            	mDates = mDates + Meets[1] + ";";
	            	
	            	//Get Gymnast data
	            	if (Meets.length == 3){
	            		Meets = TextUtils.split(Meets[2], ",");
	            	}else{
	            		Toast.makeText(cntx, "There was a problem with the Gymnasts in the Meet file!", Toast.LENGTH_LONG).show();
	            		Meets = TextUtils.split("Error@0", ",");
	            	}
	            	String gymList="";
	            	String scoreList="";
	            	String[] data;
	            	for (int i=0;i<Meets.length;i++)
	            	{
	            		
	            		data = TextUtils.split(Meets[i], "@");
	            		gymList = gymList + data[0] + ",";
	            		scoreList = scoreList + data[1] + ",";
	            		
	            	}
	            	gymList = gymList.substring(0,gymList.length()-1);
	            	scoreList = scoreList.substring(0,scoreList.length()-1);
	            	mGymnasts.add(gymList);
	            	mScores.add(scoreList);
	            	isData=true;
	            	
	            }
            }
            in.close();
            if(isData){
            	mNames = mNames.substring(0,mNames.length()-1);
            	meetNames = TextUtils.split(mNames, ";");
            	mDates = mDates.substring(0,mDates.length()-1);
            	meetDates = TextUtils.split(mDates, ";");
            }
        } catch (IOException e) {
        	Toast.makeText(cntx, "Read Meet File Failed!", Toast.LENGTH_SHORT).show();	
        }
	}

	public String[] getNames(){return meetNames;}
	public String[] getDates(){return meetDates;}
	
	public String getGymnasts(String mName)
	{
		String rStr="";
		for (int i=0;i<meetNames.length;i++)
		{
			if (meetNames[i].compareTo(mName)==0)
			{
				rStr = mGymnasts.get(i);
			}
		}
		return rStr;
	}
	
	public String getScores(String mName)
	{
		String rStr="";
		for (int i=0;i<meetNames.length;i++)
		{
			if (meetNames[i].compareTo(mName)==0)
			{
				rStr = mScores.get(i);
			}
		}
		return rStr;
	}
	public void setScores(String mName, String scores)
	{
		for (int i=0;i<meetNames.length;i++)
		{
			if (meetNames[i].compareTo(mName)==0)
			{
				mScores.set(i, scores);
			}
		}
	}
	
	public void writeData(Context cntx)
	{
		try {
   			PrintWriter out = new PrintWriter(new FileWriter(MEET_PATH));
   			String oStr="";
   			for (int i=0;i<meetNames.length;i++)
   			{
   				oStr = "";
   				oStr = oStr + meetNames[i] + ":" + meetDates[i] + ":";
   				String[] gStr = TextUtils.split(mGymnasts.get(i), ",");
   				String[] sStr = TextUtils.split(mScores.get(i), ",");
   				
   				for (int j=0;j<gStr.length;j++)
   				{
   					oStr = oStr + gStr[j] + "@" + sStr[j] + ",";
   				}
   				oStr = oStr.substring(0, oStr.length()-1);
   				out.println(oStr);
   				
   			}
    		out.close();
    		Toast.makeText(cntx, "Updated Meets!", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(cntx, "Update Meet Failed!" , Toast.LENGTH_SHORT).show();
		}
	}
}
