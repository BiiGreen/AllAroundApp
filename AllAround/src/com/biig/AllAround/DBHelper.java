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

Filename: DBHelper.java
Version: 3.0
Description: Sort of like hamburger helper... but for a SQL database!
contains the main routines to write, read, etc... from the SQL database
Changes:
12/31/2013: created header data
1/2/2014: re-factoring of new implementation
*/
package com.biig.AllAround;

import java.io.File;
import java.text.DecimalFormat;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

public class DBHelper {

	//variables for the database management
	private SQLiteDatabase AADB = null;
	private final static String aadbName = Environment.getExternalStorageDirectory() + "/allaround/AllAroundScoring";
	private final static String gymnastTable = "gymnasts";
	private final static String meetTable = "meets";
	private final static String scoreTable = "scores";
	
	
	//get the next id for the given table as string
	public String getNextIdString(String tblName){
		//AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String s = "SELECT * FROM ids WHERE type='" + tblName + "'";		
		c = AADB.rawQuery(s,null);
		c.moveToFirst();
		int f = c.getColumnIndex("value");
		int i = c.getInt(f);
		s = String.valueOf(i);
		i = i + 1;
		AADB.execSQL("UPDATE ids SET value=" + i + " WHERE type='" + tblName + "'");
		//AADB.close();
		c.close();
		return s;
	}
	
	//get the next id for the given table as integer
	public int getNextIdInt(String tblName){
		//AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String s = "SELECT * FROM ids WHERE type='" + tblName + "'";		
		c = AADB.rawQuery(s,null);
		c.moveToFirst();
		int f = c.getColumnIndex("value");
		int i = c.getInt(f);
		i = i + 1;
		AADB.execSQL("UPDATE ids SET value=" + i + " WHERE type='" + tblName + "'");
		//AADB.close();
		c.close();
		return i;
	}
	
	
	
	
	

	
	//---------------------------------------------------------------------------
	//**********************************Gymnast Routines*************************
	//---------------------------------------------------------------------------
	/*TODO:Gymnast Routines */
	
	//check to see if there are gymnasts in database
	public boolean doGymnastsExist(){
		boolean rslt = true;
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable, null);
		c.moveToFirst();
		if (c.getCount()==0){
			rslt=false;
		}
		AADB.close();
		c.close();
		return rslt;
	}
	
	
	//get a list of gymnasts from the database as : split string
	public String getGymnastListString(){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable, null);
		int fnFld = c.getColumnIndex("firstname");
		int lnFld = c.getColumnIndex("lastname");
		c.moveToFirst();
		String lst = "";
		do{
			lst = lst + c.getString(fnFld) + " " + c.getString(lnFld) + ":";
		}while(c.moveToNext());
		lst = lst.substring(0, lst.length()-1);
		AADB.close();
		c.close();
		return lst;	 
	 }
	
	//get a list of gymnasts from the database as : split string by level
	public String getGymnastListStringByLevel(String lvl){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable + " WHERE level="+lvl, null);
		int fnFld = c.getColumnIndex("firstname");
		int lnFld = c.getColumnIndex("lastname");
		c.moveToFirst();
		String lst = "";
		do{
			lst = lst + c.getString(fnFld) + " " + c.getString(lnFld) + ":";
		}while(c.moveToNext());
		lst = lst.substring(0, lst.length()-1);
		AADB.close();
		c.close();
		return lst;	 
	 }
	
	//get a list of gymnasts from the database as array by level
	public String[] getGymnastListArrayByLevel(String lvl){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable + " WHERE level="+lvl, null);
		int fnFld = c.getColumnIndex("firstname");
		int lnFld = c.getColumnIndex("lastname");
		try{
			c.moveToFirst();
			String lst = "";
			do{
				lst = lst + c.getString(fnFld) + " " + c.getString(lnFld) + ":";
			}while(c.moveToNext());
			lst = lst.substring(0, lst.length()-1);
			AADB.close();
			c.close();
			return TextUtils.split(lst,":");
		}
		catch (CursorIndexOutOfBoundsException e){
			return null;
		}
	 }
	//get a list of gymnasts from the database as a String Array
	public String[] getGymnastListArray(){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable, null);
		int fnFld = c.getColumnIndex("firstname");
		int lnFld = c.getColumnIndex("lastname");
		c.moveToFirst();
		String lst = "";
		do{
			lst = lst + c.getString(fnFld) + " " + c.getString(lnFld) + ":";
		}while(c.moveToNext());
		lst = lst.substring(0, lst.length()-1);
		AADB.close();
		c.close();
		return TextUtils.split(lst,":");	 
	 }
	
	//get data for a gymnast by the record id in an array
	public String[] getGymnastDataById(String aID){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable + " WHERE" +
				" _id=" + aID , null);
		int idFld = c.getColumnIndex("_id");
		int fnFld = c.getColumnIndex("firstname");
		int lnFld = c.getColumnIndex("lastname");
		int tgFld = c.getColumnIndex("target");
		int lvFld = c.getColumnIndex("level");
		c.moveToFirst();
		String lst = c.getString(idFld) + ":" +
					c.getString(fnFld) + " " + 
					c.getString(lnFld) + ":" +
					c.getString(tgFld) + ":" +
					c.getString(lvFld);
		AADB.close();
		c.close();
		return TextUtils.split(lst,":");
	}

	//get data for a gymnast by name in an array
	public String[] getGymnastDataByName(String aGymnast){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		String[] s = TextUtils.split(aGymnast, " ");
		Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable + " WHERE" +
				" firstname='" + s[0] + "'  AND" + 
				" lastname='" + s[1] + "'", null);
		try{
			int idFld = c.getColumnIndex("_id");
			int fnFld = c.getColumnIndex("firstname");
			int lnFld = c.getColumnIndex("lastname");
			int tgFld = c.getColumnIndex("target");
			int lvFld = c.getColumnIndex("level");
			c.moveToFirst();
			String lst = c.getString(idFld) + ":" +
						c.getString(fnFld) + ":" + 
						c.getString(lnFld) + ":" +
						c.getString(tgFld) + ":" +
						c.getString(lvFld);
			AADB.close();
			c.close();
			return TextUtils.split(lst,":");
		}
		catch(CursorIndexOutOfBoundsException e){
			String[] sNone = TextUtils.split("false,dude", ","); 
			return sNone;
		}
	}
	
	//get data for a gymnast by name in a Gymnasts object
	public Gymnasts getGymnastDataByObject(String aGymnast, Context cntx){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		String[] s = TextUtils.split(aGymnast, " ");
		Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable + " WHERE" +
				" firstname='" + s[0] + "'  AND" + 
				" lastname='" + s[1] + "'", null);
		int idFld = c.getColumnIndex("_id");
		int fnFld = c.getColumnIndex("firstname");
		int lnFld = c.getColumnIndex("lastname");
		int tgFld = c.getColumnIndex("target");
		int lvFld = c.getColumnIndex("level");
		try{
			c.moveToFirst();
			Gymnasts g = new Gymnasts(c.getString(idFld),
										c.getString(fnFld), 
										c.getString(lnFld),
										c.getString(lvFld),
										c.getString(tgFld));
			AADB.close();
			c.close();
			return g;
		}catch(CursorIndexOutOfBoundsException e){
			Toast.makeText(cntx, "Could not find gymnast: " + s[0] + " " + s[1] , Toast.LENGTH_LONG ).show();
			AADB.close();
			c.close();
			return new Gymnasts(aGymnast, aGymnast, aGymnast, aGymnast, aGymnast);
		}
			
	}

	//insert a new gymnast into the database given a Gymnasts object
	public void gymnastInsert(Gymnasts g){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		String s = "INSERT INTO " + gymnastTable + 
				" (_id, firstname, lastname, level, target) VALUES(" +
				getNextIdString(gymnastTable) + "," +
				"'" + g.getFirstName() +"'," +
				"'" + g.getLastName() + "'," +
				g.getLevel() + "," +
				g.getTarget()+ ");";
		AADB.execSQL(s);
		AADB.close();
	}
	
	//update an existing gymnast in the database given a Gymnasts object.
	public void gymnastUpdate(Gymnasts g){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		
		
		Cursor c = AADB.rawQuery("SELECT * FROM "+ gymnastTable + " WHERE" +
				" firstname='" + g.getFirstName() + "'  AND" + 
				" lastname='" + g.getLastName() + "'", null);
		try{
			int idFld = c.getColumnIndex("_id");
			c.moveToFirst();
			c.getString(idFld);
		}catch(CursorIndexOutOfBoundsException e){
			//if it doesn't, then they changed the name and we need to 
			//look at all meets and scores to update name.
			//create new void to update name
			updateNameChange(g);
		}finally{
		
			
			AADB.execSQL("UPDATE "  + gymnastTable + " SET"+
					" firstname='" + g.getFirstName() +"'," +
					" lastname='" + g.getLastName() + "'," +
					" level=" + g.getLevel() + "," +
					" target=" + g.getTarget() + 
					" WHERE _id="+ g.getAnId() +";");
			AADB.close();
		}
	}

	//delete a gymnast from the database given a record id
	public void gymnastDelete(String anID){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		AADB.execSQL("DELETE FROM "  + gymnastTable +  
					" WHERE _id="+ anID +";");
		AADB.close();
	}
	
	
	//routine to fill a double array with the gymnasts targets
	//given an array of gymnasts
	public double[] fillGymnastTargets(String[] Gymnasts, Context cntx){
		double[] Targets = new double[Gymnasts.length];
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		for (int i=0;i<Gymnasts.length;i++){
			String[] n = TextUtils.split(Gymnasts[i], " ");
			String s = "SELECT * FROM "+ gymnastTable + " WHERE" +
				" firstname='" + n[0] + "' AND" +
				" lastname='" + n[1] + "'";
				c = AADB.rawQuery(s, null);
				int tgFld = c.getColumnIndex("target");
				try{
					c.moveToFirst();
				
					Targets[i] = c.getDouble(tgFld);
					
				}catch(CursorIndexOutOfBoundsException e){
					Toast.makeText(cntx, "Could not find gymnast: " + n[0] + " " + n[1] , Toast.LENGTH_LONG ).show();
					Targets[i] = 36;
				}
				c.close();
		}
		AADB.close();
		c.close();
		return Targets;
	}

	
	
	
	public void updateNameChange(Gymnasts g){
		//get original name from database using g.getAnId();
		//Select all from score table
		//loop over each and see if name was there.
		//if it was, change it
		
		//select all from meet table
		//loop over each and see if name was there.
		//if it was, change it.
		
	}

	
	//---------------------------------------------------------------------------
	//************************************Meet Routines**************************
	//---------------------------------------------------------------------------
	/*TODO:Meet Routines */

	
	//check to see if there are meets in database
	public boolean doMeetsExist(){
		boolean rslt = true;
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = AADB.rawQuery("SELECT * FROM "+ meetTable, null);
		c.moveToFirst();
		if (c.getCount()==0){
			rslt=false;
		}
		AADB.close();
		c.close();
		return rslt;
	}	
	
	//routine to get a list of meets as string
	public String getMeetListAsString(){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = AADB.rawQuery("SELECT * FROM "+ meetTable, null);
		int mnFld = c.getColumnIndex("meetname");
		int mdFld = c.getColumnIndex("meetdate");
		int mlFld = c.getColumnIndex("level");
		c.moveToFirst();
		String lst = "";
		do{
			lst = lst + c.getString(mnFld) + "_" + c.getString(mdFld) + "_" + c.getString(mlFld) + ":";
		}while(c.moveToNext());
		lst = lst.substring(0, lst.length()-1);
		AADB.close();
		c.close();
		return lst;
	}
	
	//routine to get a list of meets as array
	public String[] getMeetListAsArray(){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = AADB.rawQuery("SELECT * FROM "+ meetTable, null);
		int mnFld = c.getColumnIndex("meetname");
		int mdFld = c.getColumnIndex("meetdate");
		int mlFld = c.getColumnIndex("level");
		c.moveToFirst();
		String lst = "";
		do{
			lst = lst + c.getString(mnFld) + "_" + c.getString(mdFld) + "_" + c.getString(mlFld) + ":";
		}while(c.moveToNext());
		lst = lst.substring(0, lst.length()-1);
		AADB.close();
		c.close();
		return TextUtils.split(lst,":");	
	}
	
	//get data for a meet by name in a meets object
	public Meets getMeetDataAsObject(String aMeet){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		String[] s = TextUtils.split(aMeet, "_");
		Cursor c = AADB.rawQuery("SELECT * FROM "+ meetTable + " WHERE" +
				" meetname='" + s[0] + "' AND" + 
				" meetdate='" + s[1] + "' AND" +
				" level=" + s[2], null);
		
		int idFld = c.getColumnIndex("_id");
		int mnFld = c.getColumnIndex("meetname");
		int mdFld = c.getColumnIndex("meetdate");
		int mlFld = c.getColumnIndex("level");
		int gyFld = c.getColumnIndex("gymnasts");
		c.moveToFirst();
		Meets m = new Meets(c.getString(idFld),
							c.getString(mnFld), 
							c.getString(mdFld),
							c.getString(mlFld),
							c.getString(gyFld));
		AADB.close();
		c.close();
		return m;	
	}


	//insert a new meet into the database given a meets object
	public void meetInsert(Meets m){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		String s = "INSERT INTO " + meetTable + 
				" (_id, meetname, meetdate, level, gymnasts) VALUES(" +
				getNextIdString(meetTable) + "," +
				"'" + m.getMeetName() +"'," +
				"'" + m.getMeetDate() + "'," +
				m.getMeetLevel() + "," +
				"'" + m.getMeetGymnastsAsString()+ "');";
		AADB.execSQL(s);
		AADB.close();
	}
	
	//update an existing meet in the database given a meets object.
	public void meetUpdate(Meets m){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		AADB.execSQL("UPDATE "  + meetTable + " SET"+
				" meetname='" + m.getMeetName() +"'," +
				" meetdate='" + m.getMeetDate() + "'," +
				" level=" + m.getMeetLevel() + "," +
				" gymnasts='" + m.getMeetGymnastsAsString() + "'" +  
				" WHERE _id="+ m.getMeetID() +";");
		AADB.close();
	}

	//delete a meet from the database given a record id
	public void meetDelete(String anID){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		AADB.execSQL("DELETE FROM "  + meetTable +  
					" WHERE _id="+ anID +";");
		AADB.close();
	}
	
	
	//routine to get a meet name given a meet id
    public String getMeetName(String mID){
    	AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String s = "SELECT * FROM " + meetTable + " WHERE" +
		" _id=" + mID;
		c=AADB.rawQuery(s,null);
		int f = c.getColumnIndex("meetname");
		c.moveToFirst();
		s = "error";
		if (c.getCount()>0){
			s = c.getString(f);
		}
		AADB.close();
		c.close();
		return s;
    }
    
	//routine to get a meet name given a meet id
    public String getMeetDate(String mID){
    	AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String s = "SELECT * FROM " + meetTable + " WHERE" +
		" _id=" + mID;
		c=AADB.rawQuery(s,null);
		int f = c.getColumnIndex("meetdate");
		c.moveToFirst();
		s = "error";
		if (c.getCount()>0){
			s = c.getString(f);
		}
		AADB.close();
		c.close();
		return s;
    }    
	

	//routine to return the level of a meet given its ID
	public String getMeetLevel(String mID){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String s = "SELECT * FROM " + meetTable + " WHERE" +
		" _id=" + mID;
		c=AADB.rawQuery(s,null);
		int f = c.getColumnIndex("level");
		c.moveToFirst();
		s = "error";
		if (c.getCount()>0){
			s = c.getString(f);
		}
		AADB.close();
		c.close();
		return s;
	}
	

	//---------------------------------------------------------------------------
	//***********************************Score Routines**************************
	//---------------------------------------------------------------------------
	/*TODO:Score Routines */
	
	//check to see if the scores exist.
	//if not, create new record in database
	//if exist, then fill string.
    public String[] checkScores(String[] Gymnasts, String meetID, String meetLevel){
		AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String st="";
    	for (int i=0;i<Gymnasts.length;i++){
    		String s = "SELECT * FROM " + scoreTable + " WHERE" +
    			" gymnast='" + Gymnasts[i] + "' AND" + 
    			" meet=" + meetID;
    		c=AADB.rawQuery(s,null);
    		if (c.getCount()==0){
    			//make a new record
    			String nextID = getNextIdString(scoreTable);
    			s= "INSERT INTO " + scoreTable + 
    					" (_id, meet, level, gymnast, floor, vault, bars, beam) VALUES(" +
    					nextID + "," + 
    					meetID + "," +
    					meetLevel + ",'" +
    					Gymnasts[i] + "',0,0,0,0);";
    					
    			st = st + "0:0:0:0_";
    			AADB.execSQL(s);
    		}else{
    			c.moveToFirst();
    			int flFld = c.getColumnIndex("floor");
    			int vlFld = c.getColumnIndex("vault");
    			int brFld = c.getColumnIndex("bars");
    			int bmFld = c.getColumnIndex("beam");
    			st= st + c.getString(flFld) + ":" + c.getString(vlFld) + ":" +
    				c.getString(brFld) + ":" + c.getString(bmFld) + "_";
    		}
        	c.close();
    	}
    	AADB.close();
    	st = st.substring(0,st.length()-1);
    	return TextUtils.split(st,"_");
    }
    
    //routine to update the score for a gymnast for a meet id for an event given a score
    public void updateScore(String aGymnast, String aMeetID, String anEvent, String aScore){
    	AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
    	String s = "UPDATE " + scoreTable + 
    				" SET " + anEvent + "=" + aScore + 
    				" WHERE" + " gymnast='" + aGymnast + "' AND" + 
    				" meet=" + aMeetID;
    	AADB.execSQL(s);
    	AADB.close();
    }
    
    //remove scores... not called and may not call it but left in anyway
    public void removeScores(String[] Gymnasts, String meetID){
    	AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String s = "SELECT * FROM " + scoreTable + " WHERE" +
		" meet=" + meetID;
		c=AADB.rawQuery(s,null);
		if (c.getCount()>0){
			c.moveToFirst();
			int gyFld = c.getColumnIndex("gymnast");
			int idFld = c.getColumnIndex("_id");
			do
			{
				String g = c.getString(gyFld);
				String id = c.getString(idFld);
				boolean fnd = false;
				for (int i=0;i<Gymnasts.length;i++){
					if (Gymnasts[i].compareTo(g)==0){
						fnd = true;
					}
				}
				if (! fnd){
					//delete record
					s="REMOVE FROM " + scoreTable + " WHERE" +
						" _id=" + id;
				}
			}while(c.moveToNext());
		}
		AADB.close();
		c.close();
    }
   
    //get all the levels for a gymnast
    public String[] getGymnastLevels(String aGymnast){
    	AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String s = "SELECT DISTINCT level FROM " + scoreTable + " WHERE" +
		" gymnast='" + aGymnast +"'";
		c=AADB.rawQuery(s,null);
		String[] lvls;
		if (c.getCount()!=0){
			lvls = new String[c.getCount()];
			c.moveToFirst();
			int i = 0;
			int f = c.getColumnIndex("level");
			do
			{
				lvls[i] = c.getString(f);
				i=i+1;
			}while(c.moveToNext());
	    }else{
			lvls= new String[1];
			lvls[0]="";
		}
		c.close();
    	AADB.close();
	
    	return lvls;
    }
    
    

	//---------------------------------------------------------------------------
	//************************************Stat Routines**************************
	//---------------------------------------------------------------------------
	/*TODO:Stat Routines */
	
    //get the scores for a gymnast given a level 
    public Scores getGymnastScores(String aGymnast, String lvl){
    	Scores scr=null;
    	AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String s = "SELECT * FROM " + scoreTable + " WHERE" +
		" gymnast='" + aGymnast +"'";
		if (lvl!=""){
			s = s + " AND level=" + lvl;
		}
		c=AADB.rawQuery(s,null);
		if (c.getCount() > 0) {
			scr = new Scores(c);
		}
    	AADB.close();
    	c.close();
    	return scr;
    }
    
    //round a double to a gymnastics value
    public String rndDbl(double v){
//    	String s="";
    	DecimalFormat df = new DecimalFormat("#.###");
    	
//    	if (v==0){
//    		v = 0;
//    	}else if (v<10){
//	    	v = v * 1000;
//	    	s = String.valueOf(v);
//	    	s = s.substring(0,4);
//	    	v = Double.valueOf(s);
//	    	v = v / 1000;
//    	}else if (v>100){
//    		s = String.valueOf(v);
//    		String t[] = s.split(".");
//    		String g = t[1].substring(0,2);
//    		s = t[0] + g;
//    		return s;
//    	}else{
//    		v = v * 100;
//	    	s = String.valueOf(v);
//	    	s = s.substring(0,4);
//	    	v = Double.valueOf(s);
//	    	v = v / 100;
//    	}
//    	s = String.valueOf(v);
    	
//    	return s;
    	return df.format(v);
    }
    
    
    //---------------------------------------------------------------------------
	//******************************Background Routines**************************
	//---------------------------------------------------------------------------
	/*TODO:Background Routines */

    //get a filename to set as background for an activity
    public String getActivityBackground(String act){
    	
    	String r = "";
    	AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		Cursor c = null;
		String s = "SELECT * FROM img WHERE activity='"+act+"'";
		c=AADB.rawQuery(s,null);
		c.moveToFirst();
		int f = c.getColumnIndex("path");
		r = c.getString(f);
		File aFile = new File(r);
		if (! aFile.exists()){
			s = "UPDATE img SET path='nothing' WHERE activity='" + act + "'";
			AADB.execSQL(s);
			r = "nothing";
		}
		c.close();
		AADB.close();
		return r;
    }

    //remove the current background given an activity
    public void removeActivityBackground(String act){
    	AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
		String s = "UPDATE img SET path='nothing' WHERE activity='" + act + "'";
		AADB.execSQL(s);
		AADB.close();
    }
    
    //add a filename for the background given an activity
   public void addActivityBackground(String act, String pth){
	   AADB = SQLiteDatabase.openOrCreateDatabase(aadbName, null);
	   String s = "UPDATE img SET path='" + pth + "' WHERE activity='" + act + "'";
	   AADB.execSQL(s);
	   AADB.close();
   }
   
   
   
   //routine to see if a video or file exists.
   public boolean checkVideo(String fName){
   	File f = new File(fName);
   	if (f.exists())
   	{
   		return true;
   	}else{
   		return false;
   	}
   }
   
   
   
   
   
   
   
   
   
  
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
}
