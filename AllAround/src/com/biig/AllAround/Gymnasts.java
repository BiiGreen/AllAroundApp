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

Filename: Gymnasts.java
Version: 3.0
Description: Data structure to keep data for a gymnast
Holds: unique id, first name, last name, current level, target score
Changes:
12/31/2013: created header data
*/
package com.biig.AllAround;

public class Gymnasts {

	private String anId;
	private String aFirstName;
	private String aLastName;
	private String aLevel;
	private String aTarget;
	
	public String getAnId() {
		return anId;
	}

	public void setAnId(String anId) {
		this.anId = anId;
	}

	public String getFirstName() {
		return aFirstName;
	}

	public void setFirstName(String aFirstName) {
		this.aFirstName = aFirstName;
	}

	public String getLastName() {
		return aLastName;
	}

	public void setLastName(String aLastName) {
		this.aLastName = aLastName;
	}

	public String getLevel() {
		return aLevel;
	}

	public void setLevel(String aLevel) {
		this.aLevel = aLevel;
	}

	public String getTarget() {
		return aTarget;
	}

	public void setTarget(String aTarget) {
		this.aTarget = aTarget;
	}

	public Gymnasts(String anId, String aFirstName, String aLastName,
			String aLevel, String aTarget) {
		super();
		this.anId = anId;
		this.aFirstName = aFirstName;
		this.aLastName = aLastName;
		this.aLevel = aLevel;
		this.aTarget = aTarget;
	}
	
	
	
	
	
	
}
