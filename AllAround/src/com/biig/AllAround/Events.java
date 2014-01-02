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

Filename: Events.java
Version: 3.0
Description: Data structure for a gymnast for an event
Holds: meet name, date, level, gymnast and scores
Changes:
12/31/2013: created header data
1/2/2014: re-factoring of new implementation
*/
package com.biig.AllAround;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Events {

	private DBHelper dbh = new DBHelper();
	private String gymnastName;
	private String meetName;
	private Date meetDate;
	private double floor;
	private double vault;
	private double bars;
	private double beam;
	public Events(String gymnastName, String meetName, double floor,
			double vault, double bars, double beam) {
		super();
		this.gymnastName = gymnastName;
		this.meetName = dbh.getMeetName(meetName);
		String d = dbh.getMeetDate(meetName);
		Date dt = null;
		try{
			dt = new SimpleDateFormat("M/d/yyyy",Locale.ENGLISH).parse(d);
		}catch(Exception e){
			
		}
		
		
		this.meetDate = dt;
		this.floor = floor;
		this.vault = vault;
		this.bars = bars;
		this.beam = beam;
		
	}
	public String getGymnastName() {
		return gymnastName;
	}
	public void setGymnastName(String gymnastName) {
		this.gymnastName = gymnastName;
	}
	public String getMeetName() {
		return meetName;
	}
	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}
	public Date getMeetDate() {
		return meetDate;
	}
	public double getFloor() {
		return floor;
	}
	public void setFloor(double floor) {
		this.floor = floor;
	}
	public double getVault() {
		return vault;
	}
	public void setVault(double vault) {
		this.vault = vault;
	}
	public double getBars() {
		return bars;
	}
	public void setBars(double bars) {
		this.bars = bars;
	}
	public double getBeam() {
		return beam;
	}
	public void setBeam(double beam) {
		this.beam = beam;
	}
	public double getTotal() {
		
		return floor + vault + bars + beam;
	}
	
	
}
