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

Filename: Scores.java
Version: 3.0
Description: Class designed to hold data for a gymnasts scores 
Holds: Gymnast name, Events that describe each meet and the number of meets 
Changes:
12/31/2013: created header data
1/2/2014: re-factoring of new implementation
*/

package com.biig.AllAround;

import android.database.Cursor;


//class to keep track of scores for a gymnast
//uses an Events object to store.
public class Scores {

	
	private String gymnastName;
	private Events[] meetList;
	private int numEvents=0;
	
	public String getGymnastName() {
		return gymnastName;
	}

	public Events[] getAllMeets(){
		return meetList;
	}
	public Events getMeetList(int i) {
		return meetList[i];
	}


	public int getNumEvents() {
		return numEvents;
	}

	public Scores(Cursor c) {
		super();
		numEvents = c.getCount();
		meetList = new Events[numEvents];
		
		int mnFld = c.getColumnIndex("meet");
		int flFld = c.getColumnIndex("floor");
		int vlFld = c.getColumnIndex("vault");
		int brFld = c.getColumnIndex("bars");
		int bmFld = c.getColumnIndex("beam");
		
		String mn = "";
		double fl = 0;
		double vl = 0;
		double br = 0;
		double bm = 0;
		
		int i=0;
		
		c.moveToFirst();
		do{
			mn=c.getString(mnFld);
			fl=c.getDouble(flFld);
			vl=c.getDouble(vlFld);
			br=c.getDouble(brFld);
			bm=c.getDouble(bmFld);
			
			meetList[i] = new Events(gymnastName, mn, fl, vl, br, bm);
			i=i+1;
		}while(c.moveToNext());
		
	}
}
