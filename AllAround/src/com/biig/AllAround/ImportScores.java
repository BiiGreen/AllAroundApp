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

Filename: ImportScores.java
Version: 3.0
Description: Activity designed to import from CSV to SQL database.
THIS METHODOLOGY WAS NOT IMPLEMENTD 
Changes:
12/31/2013: created header data
*/
package com.biig.AllAround;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ImportScores {
	private String mName;
	private String mMeet;
	private String mFloor;
	private String mVault;
	private String mBars;
	private String mBeam;
	
	public ImportScores(String scores, Context cntx) {
		String[] s = TextUtils.split(scores, "#");
		if (s.length < 4)
		{
			Toast.makeText(cntx, "Error " + String.valueOf(s.length), Toast.LENGTH_SHORT).show();
			mFloor = "0";
			mVault = "0";
			mBars = "0";
			mBeam = "0";
		}else{
			mFloor = s[0];
			mVault = s[1];
			mBars = s[2];
			mBeam = s[3];
		}
	}


	public void setName(String aName)
	{
		mName = aName;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public void setMeet(String aMeet)
	{
		mMeet = aMeet;
	}
	
	public String getMeet()
	{
		return mMeet;
	}

	public void setScores(String scores)
	{
		String[] s = TextUtils.split(scores, "|");
		mFloor = s[1];
		mVault = s[3];
		mBars = s[5];
		mBeam = s[7];
	}
	
	public void setFloor(String aScore){mFloor = aScore;}
	public String getFloorStr(){return mFloor;}
	public double getFloorDbl(){return Double.parseDouble(mFloor);}
	
	public void setVault(String aScore){mVault = aScore;}
	public String getVaultStr(){return mVault;}
	public double getVaultDbl(){return Double.parseDouble(mVault);}
	
	public void setBars(String aScore){mBars = aScore;}
	public String getBarsStr(){return mBars;}
	public double getBarsDbl(){return Double.parseDouble(mBars);}
	
	public void setBeam(String aScore){mBeam = aScore;}
	public String getBeamStr(){return mBeam;}
	public double getBeamDbl(){return Double.parseDouble(mBeam);}
	
	public String getAAStr()
	{
		double aa=0;
		double fl=Double.parseDouble(mFloor);
		double vl=Double.parseDouble(mVault);
		double ba=Double.parseDouble(mBars);
		double bm=Double.parseDouble(mBeam);
		
		aa = fl + vl + ba + bm;
		
		String aaStr = String.valueOf(aa);
		NumberFormat formatter = new DecimalFormat("#.###");
		aaStr = formatter.format(aa);   
		return aaStr;
	}
	public double getAADbl()
	{
		double aa=0;
		double fl=Double.parseDouble(mFloor);
		double vl=Double.parseDouble(mVault);
		double ba=Double.parseDouble(mBars);
		double bm=Double.parseDouble(mBeam);
		
		aa = fl + vl + ba + bm;
		
		return aa;
	}
}
