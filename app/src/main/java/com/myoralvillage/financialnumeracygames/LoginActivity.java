/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */


package com.myoralvillage.financialnumeracygames;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * I am not completely sure, but I think that what is going on is that there is an attempt to allow multiple users of the
 * game to play for awhile, quit, and then restart the game still having credit for
 */

public class LoginActivity extends GenericActivityGame {

    List<String> userNames = new ArrayList<>();
    boolean newProfile = true;
    //String lastImageClicked = "admin";
    //int clickCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.options_toolbar);
        //setSupportActionBar(myToolbar);
        ParseFile();
        DrawProfiles();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.adminoptions, menu);
//        return true;
//    }

    @Override
    //Shows a message; unimplemented options for admin
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.become_admin:
                if (thisUser.admin) {
                    Toast.makeText(this, "Going into non Admin mode", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this, "Going into Admin mode", Toast.LENGTH_SHORT)
                            .show();
                }
                thisUser.admin = !thisUser.admin;

                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }

    /*
     * TODO - This code is almost completely atrocious. It is WAY, WAY too complicated for what it does
     *
     *        When we figure out what to save we'll almost certainly just completely rewrite this as opposed to
     *        salvaging it
     */

    public void DrawProfiles() {
//
//        //based on username size, changes weight of layouts from 0 to 0.3 to make them visible
          //If at least one user is unclaimed, show unclaimedProfiles1 layout
//        if (12 - userNames.size() > 1) {
//            LinearLayout ll = (LinearLayout) findViewById(R.id.unclaimedProfiles1);
//            LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
//            llParams.weight = 0.3f;
//            ll.setLayoutParams(llParams);
//        }
          //If less than 5 users exist, show unclaimedProfiles2 layout
//        if (12 - userNames.size() > 7) {
//            LinearLayout ll = (LinearLayout) findViewById(R.id.unclaimedProfiles2);
//            LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
//            llParams.weight = 0.3f;
//            ll.setLayoutParams(llParams);
//        }
          //If more than 5 users exist, show claimedProfiles2
//        if (userNames.size() > 5) {
//            LinearLayout ll = (LinearLayout) findViewById(R.id.claimedProfiles2);
//            LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
//            llParams.weight = 0.3f;
//            ll.setLayoutParams(llParams);
//        }
          //Show claimedProfiles1, so admin can be populated into the layout
//        LinearLayout ll = (LinearLayout) findViewById(R.id.claimedProfiles1);
//        LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
//        llParams.weight = 0.3f;
//        ll.setLayoutParams(llParams);
//
//        //end of layout weight portion
//
//
        int claimedCount = 0;
        int unclaimedCount = 0;

        //Loops 11 times
        for (int i = 1; i < 12; i++) {

            String filename = "user" + i;
            //If any users exist
            if (userNames.size() > 0) {
                if (userNames.contains(filename)) {
                    //Find user assigned image
                    int img_id = getResources().getIdentifier(filename, "drawable", getPackageName());
                    claimedCount++;

                    String imgview_name = "claimedProfile" + claimedCount;

                    //Finds invisible claimed profile from layout
                    int res_id = getResources().getIdentifier(imgview_name, "id", getPackageName());
                    ImageView iv = findViewById(res_id);
                    //Sets claimed profile image to user assigned image
                    iv.setImageResource(img_id);
                    //Change image of claimed profile to half-transparent and makes it visible
                    iv.setAlpha(0.5f);
                    iv.setVisibility(View.VISIBLE);
                    //Sets tag to user#
                    iv.setTag(filename);

                } else {
                    //If no more users exist, find remaining user images and display them
                    int img_id = getResources().getIdentifier(filename, "drawable", getPackageName());
                    unclaimedCount++;

                    String imgview_name = "unclaimedProfile" + unclaimedCount;

                    //Finds invisible unclaimed profile from layout
                    int res_id = getResources().getIdentifier(imgview_name, "id", getPackageName());
                    ImageView iv = (ImageView) findViewById(res_id);
                    iv.setImageResource(img_id);
                    iv.setVisibility(View.VISIBLE);
                    iv.setTag(filename);
                }

            } else {

                //If no users exist, display all user profiles
                int img_id = getResources().getIdentifier(filename, "drawable", getPackageName());
                unclaimedCount++;

                String imgview_name = "unclaimedProfile" + unclaimedCount;

                int res_id = getResources().getIdentifier(imgview_name, "id", getPackageName());
                ImageView iv = (ImageView) findViewById(res_id);
                iv.setImageResource(img_id);
                iv.setVisibility(View.VISIBLE);
                iv.setTag(filename);
            }
        }
        //end of for loop

        //Adds admin profile to end of claimed profiles
        int adminId = userNames.size() + 1;
        String filename = "admin";
        int img_id = getResources().getIdentifier(filename, "drawable", getPackageName());
        String imgview_name = "claimedProfile" + adminId;
        int res_id = getResources().getIdentifier(imgview_name, "id", getPackageName());
        ImageView iv = findViewById(res_id);
        iv.setImageResource(img_id);
        iv.setAlpha(0.5f);
        iv.setVisibility(View.VISIBLE);
        iv.setTag(filename);
    }

    //When a user clicks a profile
    public void hasBeenClicked(View v) {
        ImageView iv = (ImageView) v;
        String userString = (String) iv.getTag();
        iv.buildDrawingCache();

        //Putting selected User DP through to next activity using bundle
        Bitmap disPic= iv.getDrawingCache();
        Bundle extras = new Bundle();
        extras.putParcelable("display_pic", disPic);
        //end

//        //If tag equals admin (set from drawProfile method)
//        if (userString.equals(lastImageClicked)) {
//            clickCount++;
//        } else {
//            clickCount = 1;
//            lastImageClicked = userString;
//        }
        if (userString.equals("admin")) {
//            if (clickCount >= 0) {  // Was if hit it 10 times. Now should work if just come in once
                thisUser.userName = userString;
                getDataThroughFile();
                if (thisUser.userName.equals("admin")) {
                    thisUser.userId = -1;
                    for (int i = 0; i < thisUser.demosViewed.length; i++) {
                        thisUser.demosViewed[i] = false;
                    }
                    for (int i = 0; i < thisUser.availableLevels.length; i++) {
                        thisUser.availableLevels[i] = true;
                    }
                    for (int i = 0; i < thisUser.activityProgress.length; i++) {
                        thisUser.activityProgress[i] = true;
                    }
                } else if (newProfile) {
                    thisUser.userId = userNames.size();
                    WriteFile();
                }
                Intent intent = new Intent(this, loginPinScreen.class);
                intent.putExtra("USERSETTINGS_USERNAME", thisUser.userName);
                intent.putExtra("USERSETTINGS_USERID", thisUser.userId);
                intent.putExtra("USERSETTINGS_DEMOSVIEWED", thisUser.demosViewed);
                intent.putExtra("USERSETTINGS_AVAILABLELEVELS", thisUser.availableLevels);
                intent.putExtra("USERSETTINGS_ACTIVITYPROGRESS", thisUser.activityProgress);
                intent.putExtra("USERSETTINGS_ADMIN", thisUser.admin);
                //putting in bundle
                intent.putExtras(extras);
                startActivity(intent);
                finish();
//            }
        } else {
            thisUser.userName = userString;
            getDataThroughFile();
            if (thisUser.userName.equals("admin")) {
                thisUser.userId = -1;
                for (int i = 0; i < thisUser.demosViewed.length; i++) {
                    thisUser.demosViewed[i] = true;
                }
                for (int i = 0; i < thisUser.availableLevels.length; i++) {
                    thisUser.availableLevels[i] = true;
                }
                for (int i = 0; i < thisUser.activityProgress.length; i++) {
                    thisUser.activityProgress[i] = true;
                }
            } else if (newProfile) {
                thisUser.userId = userNames.size();
                WriteFile();
            }
            Intent intent = new Intent(this, loginPinScreen.class);
            intent.putExtra("USERSETTINGS_USERNAME", thisUser.userName);
            intent.putExtra("USERSETTINGS_USERID", thisUser.userId);
            intent.putExtra("USERSETTINGS_DEMOSVIEWED", thisUser.demosViewed);
            intent.putExtra("USERSETTINGS_AVAILABLELEVELS", thisUser.availableLevels);
            intent.putExtra("USERSETTINGS_ACTIVITYPROGRESS", thisUser.activityProgress);
            intent.putExtra("USERSETTINGS_ADMIN", thisUser.admin);
            //putting in bundle
            intent.putExtras(extras);
            startActivity(intent);
            finish();
        }
    }

    public void ParseFile() {
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

    public void getDataThroughFile() {
        File userSettingsFile = new File(root, "usersettings.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(userSettingsFile));
            String line;

            while ((line = br.readLine()) != null) {
                String[] thisLine = line.split(",");
                userNames.add(thisLine[0]);
                if (thisUser.userName.equals(thisLine[0])) {
                    setUserData(thisLine);
                    newProfile = false;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserData(String[] data) {
        thisUser.userId = Integer.parseInt(data[1]);
        for (int i = 0; i < thisUser.demosViewed.length; i++) {
            thisUser.demosViewed[i] = Boolean.parseBoolean(data[i + 2]);
        }
        for (int i = 0; i < thisUser.availableLevels.length; i++) {
            thisUser.availableLevels[i] = Boolean.parseBoolean(data[i + 11]);
        }
        for (int i = 0; i < thisUser.activityProgress.length; i++) {
            thisUser.activityProgress[i] = Boolean.parseBoolean(data[i + 14]);
        }
    }

    public void WriteFile() {
        try {
            if (!root.exists()) {
                root.mkdirs();
            }
            File userSettingsFile = new File(root, "usersettings.txt");

            if (!thisUser.userName.equals("admin")) {
                FileWriter writer = new FileWriter(userSettingsFile, true);
                writer.append(thisUser.userName + ",");
                writer.append(String.valueOf(thisUser.userId));
                for (int i = 0; i < thisUser.demosViewed.length; i++) {
                    writer.append("," + thisUser.demosViewed[i]);
                }
                for (int i = 0; i < thisUser.availableLevels.length; i++) {
                    writer.append("," + thisUser.availableLevels[i]);
                }
                for (int i = 0; i < thisUser.activityProgress.length; i++) {
                    writer.append("," + thisUser.activityProgress[i]);
                }
                writer.append("\n");
                writer.flush();
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
