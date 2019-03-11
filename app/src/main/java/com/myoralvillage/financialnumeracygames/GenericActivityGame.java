/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */


package com.myoralvillage.financialnumeracygames;

import android.content.ClipData;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by paulj on 2016-10-17.
 * <p>
 * This class is designed to implement the code that is common to all games.
 * <p>
 * Reading and writing status, writing performance data (it is  not yet read) etc
 * <p>
 * Basically, just trying to reduce code duplication and make my life easier down the road
 */

public abstract class GenericActivityGame extends AppCompatActivity {


    /*
     * This is really grossly inadequate. We're displaying an error message to
     * an illiterate audience :-(.
     *
     * But at least it gives is a place to look if we can figure out what we SHOULD do
     *
     * At the very least, an error is going to be locale specific
     *
     * But I think as well as text I'm going to want to add a picture for each error message.
     *
     * TODO: Add some kind of pop up picture since our target audience IS illiterate
     */

    void displayError(int err) {
        Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
    }

    /*
     * List of games.
     *
     * TODO : We have the user settings set of arrays and the score file
     * names. Refactor into a single array. But right now I just want to use
     * enum constants instead of magic numbers.
     */

    public enum ActivityGame {LEVEL3PURCHASE, LEVEL3EXACTCHANGE}

    public boolean userHasViewedDemo = false;
    public int numCorrect = 0;
    public boolean correctOnFirstTry = true;

    int scoringNumAttempts = 0;

    String scoringCorrect;
    String scoringSelectedAnswer;
    String scoringQuestion;
    final String[] scoringAnswers = new String[3];
    final Locale locale = Locale.US;

    /*
     * These are used to determine the time taken to run a test
     */
    public long startTime, endTime;
    public int gameNum; // TODO - Change to an enum

    public final UserSettings thisUser = new UserSettings();
    final File root = new File(Environment.getExternalStorageDirectory(), "Notes");

    /*
     * TODO - This code needs to be radically changed
     * Made it a no-op to make searching for calls easier
     */

    public final void writeToScore(String score_name) {

    }

    public void setHomeButton(View v) {
        final Intent intent = createIntent(GameMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public final Intent createIntent(Class newActivity) {
        Intent intent = new Intent(this, newActivity);
        intent.putExtra("USERSETTINGS_USERNAME", thisUser.userName);
        intent.putExtra("USERSETTINGS_USERID", thisUser.userId);
        intent.putExtra("USERSETTINGS_DEMOSVIEWED", thisUser.demosViewed);
        intent.putExtra("USERSETTINGS_AVAILABLELEVELS", thisUser.availableLevels);
        intent.putExtra("USERSETTINGS_ACTIVITYPROGRESS", thisUser.activityProgress);
        intent.putExtra("USERSETTINGS_ADMIN", thisUser.admin);
        return intent;
    }

    public final String stringifyUserSetting() {
        String thisString = thisUser.userName + "," + String.valueOf(thisUser.userId);
        for (int i = 0; i < thisUser.demosViewed.length; i++) {
            thisString = thisString.concat(",");
            thisString = thisString.concat(String.valueOf(thisUser.demosViewed[i]));
        }
        for (int i = 0; i < thisUser.availableLevels.length; i++) {
            thisString = thisString.concat(",");
            thisString = thisString.concat(String.valueOf(thisUser.availableLevels[i]));
        }
        for (int i = 0; i < thisUser.activityProgress.length; i++) {
            thisString = thisString.concat(",");
            thisString = thisString.concat(String.valueOf(thisUser.activityProgress[i]));
        }

        return thisString;
    }

    /*
     * TODO - This file really isn't of much use. Needs to be nearly completely changed
     *        especially since we are now planning on creating much more comprehensive
     *        statistics
     */
    public final void updateUserSettings() {
        File userSettingsFile = new File(root, "usersettings.txt");

        try {
            // input the file content to the String "input"
            BufferedReader file = new BufferedReader(new FileReader(userSettingsFile));
            String line;
            String input = "";
            String newLine = "";
            String oldLine = "";

            while ((line = file.readLine()) != null) {
                String[] thisLine = line.split(",");
                if (thisLine[0].equals(thisUser.userName)) {
                    newLine = stringifyUserSetting();
                    oldLine = line;
                }
                input = line.concat("\n");
            }

            file.close();

            if (!oldLine.equals(newLine)) {
                input = input.replace(oldLine, newLine);
            }
            // write the new String with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(userSettingsFile);
            fileOut.write(input.getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    public final void getExtras(Intent intent) {
        thisUser.userName = intent.getStringExtra("USERSETTINGS_USERNAME");
        thisUser.userId = intent.getIntExtra("USERSETTINGS_USERID", -1);
        thisUser.demosViewed = intent.getBooleanArrayExtra("USERSETTINGS_DEMOSVIEWED");
        thisUser.availableLevels = intent.getBooleanArrayExtra("USERSETTINGS_AVAILABLELEVELS");
        thisUser.activityProgress = intent.getBooleanArrayExtra("USERSETTINGS_ACTIVITYPROGRESS");
        thisUser.admin = intent.getBooleanExtra("USERSETTINGS_ADMIN", false);

    }

    /**
     * ChoiceTouchListener will handle touch events on draggable views
     */
    public final class ChoiceTouchListener implements View.OnTouchListener {
        @SuppressLint("NewApi")
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                /*
                 * Drag details: we only need default behavior
                 * - clip data could be set to pass data as part of drag
                 * - shadow can be tailored
                 */
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                //start dragging the purchased_item touched
                view.startDrag(data, shadowBuilder, view, 0);
                return performClick();
            } else {
                return false;
            }
        }

        @SuppressWarnings("unused")
        public boolean onClick(View v) {
            System.out.println("Got onclick event");
            return true;
        }

        private boolean performClick() {
            System.out.println("Got performClick event");
            return true;
        }
    }

    //Updates profiles based on usersettings.txt file
    public void ParseFile(List<String> userNames) {
        File userSettingsFile = new File(root, "usersettings.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(userSettingsFile));
            String line;

            while ((line = br.readLine()) != null) {
                String[] thisLine = line.split(",");
                userNames.add(thisLine[0]);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
