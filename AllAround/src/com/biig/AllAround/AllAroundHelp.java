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

Filename: AllAroundHelp.java
Version: 3.0
Description: handles the display of the help information
Changes:
12/31/2013: created header data
*/
package com.biig.AllAround;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.biig.android.AllAround.R;

public class AllAroundHelp extends Activity{
	
	String help1;
	private TextView mainText;
	private Button exitBtn;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        
        help1 = 
        	"---INTRODUCTION---" + "\n" +
        	"All Around Gymnastics Score Keeper is a simple app designed to" +
        	" let users keep track of scores for multiple gymnasts. It also allows users" +
        	" to record and view video, see statistics, and view charts. The application will also" +
        	" show and calculate team scores. The application is free but please feel free" +
        	" to donate to my daughters gymnastics fund. All proceeds go to supporting her" +
        	" love of the sport." + "\n\n" +
        	
        	"---MANAGE GYMNASTS---" + "\n" +
        	"To use the app, users" +
        	" must first enter the information for one or more gymnasts. To do this, click" +
        	" the Gymnast button. There are 3 buttons that allow users to add, edit" +
        	" and delete gymnasts from the database. To add a gymnast, click the add button." +
        	" The app displays 4 text boxes for users to enter the gymnasts first and last name," +
        	" their current skill level and a target score." +
        	" The target is used in the scores section and represents the All Around" +
        	" score the gymnast would like to achieve. After entering the information, click" +
        	" the commit button to save the changes. Users can edit and delete gymnasts by" +
        	" clicking on the desired button. After making sure the edits are done, or that the" +
        	" right gymnast has been selected to delete, click the commit button." + "\n\n" +

        	"---MANAGE MEETS---" + "\n" +
        	"The next thing that needs to be done is to add a meet. Click the Meet button" +
        	" to start the activity. The meet activity is arranged like the gymnast activity" +
        	" with an Add, Edit, and Delete button. To add a meet, click the add button." +
        	" Enter a name, date and level for the meet. Then click the Select Gymnasts button to" +
        	" select the gymnasts that will be competing. Only gymnasts from the database with"+
        	" the selected level will be displayed. This stops someone from adding a level 5" +
        	" gymnast to a level 4 meet. Once all the gymnasts are selected" +
        	" and the name and date has been entered, click the Commit button to save the" +
        	" changes." + "\n\n" +
        	
        	"---MANAGE SCORES---" + "\n" +
        	"The next part is to actually enter scores during a meet with the Scores activity." +
        	" Click the Scores button to begin. Then select a meet from the pop up window." +
        	" Each gymnast is listed in a table with each event, the all around and target." +
        	" The order of the events are Floor, Vault, Bars, and Beam. The All Around score is" + 
        	" listed after the Beam score. Following the All Around score is the target value." +
        	" This value shows what the gymnast needs on the remaining events to reach their" +
        	" target. When scores are provided by the judges, click on the section for that" +
        	" gymnast and event. A dialog opens asking for the new score. Enter the new score and" +
        	" click ok on the dialog. The score will be updated in the database, the new score shown and" +
        	" a new target and all around score calculated and displayed. Continue until the meet is over." + 
        	" If there are more than 3 gymnasts in the meet, the app will calculate and display" +
        	" the team score using the best 3 scores from each event." + "\n\n" +
        	
        	"---RECORD/VIEW VIDEOS---" + "\n" +
        	"The video capture can be done through the Scores section. To record a video, long press" +
        	" the score for an event. This will open the Record activity. If a video exists, the user" +
        	" will be prompted to overwrite or view the video. If no video exists, the user will be" +
        	" taken to the video app to record a new video. Record the video and," +
        	" depending upon the device, click the OK or Done button to return to the app." +
        	" All videos are stored on the SD Card in the AllAround Viedo folder." + "\n\n" +
        	
        	"Video and scores can also be entered using the combo boxes, buttons and text box"+
        	" at the top of the screen. The first combo box shows the gymnasts, select a gymnast." +
        	" The next combo box is for the event, select an event to continue. If the user is entering" +
        	" a score, enter the score in the text box provided. Then click the PLUS/ADD button to" +
        	" update the database. If the user wants to record a video, set the combo boxes and then" +
        	" click the video camera button to record or view the video." + "\n\n" +
        	
        	"---VIEW STATS AND CHARTS---" + "\n" +
        	"The final activity is the Stats activity. Click the Stats button from the main screen" +
        	" to start. Select a gymnast from first combo box. The scores for the selected gymnast are" +
        	" then shown for each meet. The Best score from all meets for each event are listed" +
        	" along with the Average score from all meets for each event. Finally, the standard" +
        	" deviation is given for each event and the All Around scores. The Std Dev can be" +
        	" thought of as a measure of how consistent a gymnast is on an apparatus. Lower is better. The second" +
        	" second combo box allows the user to choose between all levels the gymnast has in the" +
        	" database. If they have competed for more than one level, each will show in the combo box." +
        	" Select the desired level or the ALL option and the data will be updated. The graphs for" +
        	" a gymnast can be viewed from the Stats activity. Press the menu button and then View Charts." +
        	" All the scores for each event are shown on a plot for the selected gymnast. A combo box at" +
        	" the top allows users to switch between different apparatus, All Around or all events." + "\n\n" +
        	
        	"---USE OF THE WIDGET---" + "\n" +
        	"Along with the charts, a new addition to this version is the Homescreen Widget. The widet" +
        	" allows users to control everything for an event, like scores and video, right from the" +
        	" home screen. Once a widget has been added, the user is prompted to select a meet for the" +
        	" widget. Once the meet has been selected, the widget is displayed with the meet name at the" +
        	" top with 4 buttons underneath. The buttons are: Update Score, Record/View Video, Open Scores" +
        	" View and Open The App. To open the All Around Application, click the last button. The Scores" +
        	" button will take the user to the Scores activity with the meet displayed and ready for use." +
        	" To enter scores and record video from the widget, click the desired button. Two dialogs will be displayed" +
        	" allowing the user to pick a gymnast and an event. Select the desired gymnast and event in the" +
        	" dialogs. If the user clicked the Update Score button, a text entry dialog will be display" +
        	" allowing the user to enter a score. Enter the score and press OK to update the database." +
        	" If the user selected the video button, the video activity will begin and behaves like it" +
        	" did in the scores section of the App. To switch the meet associated with the widget, press" +
        	" the Meet Name at the top of the widget and the configuration activity is displayed. Select" +
        	" the desired meet to use with the widget and the widget is updated with the new meet name.";
        
        
        mainText = (TextView) findViewById(R.id.help_mainText);
        mainText.setText(help1);
        
        exitBtn = (Button) findViewById(R.id.help_exitBtn);
        exitBtn.setOnClickListener(exitOnClickListener);
    }

	// A call-back for when the user presses the seasons button.
	OnClickListener exitOnClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	finish();
	    }
	};
}