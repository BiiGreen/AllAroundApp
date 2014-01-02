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

Filename: ImportGymnasts.java
Version: 3.0
Description: Activity designed to import from CSV to SQL database.
THIS METHODOLOGY WAS NOT IMPLEMENTD 
Changes:
12/31/2013: created header data
*/

package com.biig.AllAround;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

public class ImportGymnasts {
	private final static String GYMNAST_PATH = Environment.getExternalStorageDirectory() + "/allaround/gymnasts.txt";
	
	private static String mNames[];
	private static String mTargets[];
	
	public ImportGymnasts(Context cntx) {
			
		try {
            BufferedReader in = new BufferedReader(new FileReader(GYMNAST_PATH));
            String rStr="";
            String tNames="";
            String tTargets="";
            boolean isData = false;
            while ((rStr = in.readLine())!= null){
            	if(rStr.compareTo("empty")!=0)
            	{
	            	String[] gData = TextUtils.split(rStr, ":");
	            	tNames = tNames + gData[0] +";";
	            	tTargets = tTargets + gData[1] + ";";
	            	isData=true;
	            }
            }
            in.close();
            if(isData){
            	tNames = tNames.substring(0,tNames.length()-1);
            	tTargets = tTargets.substring(0,tTargets.length()-1);
            	mNames = TextUtils.split(tNames, ";");
            	mTargets = TextUtils.split(tTargets, ";");
            }
        } catch (IOException e) {
        	Toast.makeText(cntx, "Read Gymnast File Failed!", Toast.LENGTH_SHORT).show();
        }
		
	}

	public int getCount(){return mNames.length;}
	public String[] getNames(){return mNames;}
	public String[] getTargets(){return mTargets;}
	public String getName(String gName)
	{
		String aName = "none";
		for (int i=0;i<mNames.length;i++)
		{
			if (mNames[i].compareTo(gName)==0)
			{
				aName =  mNames[i];
			}
		}
		return aName;
	}
	public String getTargetString(String gName)
	{
		String aTarget = "none";
		for (int i=0;i<mNames.length;i++)
		{
			if (mNames[i].compareTo(gName)==0)
			{
				aTarget =  mTargets[i];
			}
		}
		
		return aTarget;
	}
	public double getTargetDbl(String gName)
	{
		double aTarget = 0;
		for (int i=0;i<mNames.length;i++)
		{
			if (mNames[i].compareTo(gName)==0)
			{
				aTarget = Double.parseDouble(mTargets[i]);
			}
		}
		return aTarget;
	}
}
