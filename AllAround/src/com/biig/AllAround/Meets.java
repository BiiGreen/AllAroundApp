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

Filename: Meets.java
Version: 3.0
Description:Data structure designed to hold data for a meet
Holds: meet id, meet name, meet date, meet level, # of gymnasts and gymnasts names 
Changes:
12/31/2013: created header data
1/2/2014: re-factoring of new implementation
*/

package com.biig.AllAround;

import android.text.TextUtils;


//Class to keep track of a meets information
//like meet name, date, level, number of gymnasts and list of gymnasts
public class Meets {
	private String meetID;
	private String meetName;
	private String meetDate;
	private String meetLevel;
	private String[] meetGymnasts;
	private int numGymnasts;
	
	
	
	public Meets(String meetID, String meetName, String meetDate,
			String meetLevel, String meetGymnasts) {
		super();
		this.meetID = meetID;
		this.meetName = meetName;
		this.meetDate = meetDate;
		this.meetLevel = meetLevel;
		this.meetGymnasts = TextUtils.split(meetGymnasts,":");
		this.numGymnasts = meetGymnasts.length();
	}


	public String getMeetID() {
		return meetID;
	}


	public void setMeetID(String meetID) {
		this.meetID = meetID;
	}


	public String getMeetName() {
		return meetName;
	}


	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}


	public String getMeetDate() {
		return meetDate;
	}


	public void setMeetDate(String meetDate) {
		this.meetDate = meetDate;
	}


	public String getMeetLevel() {
		return meetLevel;
	}


	public void setMeetLevel(String meetLevel) {
		this.meetLevel = meetLevel;
	}


	public String[] getMeetGymnasts() {
		return meetGymnasts;
	}


	public void setMeetGymnasts(String[] meetGymnasts) {
		this.meetGymnasts = meetGymnasts;
	}

	public String getMeetGymnastsAsString(){
		String s = "";
		for (int i=0;i<meetGymnasts.length;i++){
			s = s + meetGymnasts[i] + ":";
		}
		s = s.substring(0,s.length()-1);
		return s;
	}
	

	public int getNumGymnasts() {
		return numGymnasts;
	}


	public void setNumGymnasts(int numGymnasts) {
		this.numGymnasts = numGymnasts;
	}
	
	
}
