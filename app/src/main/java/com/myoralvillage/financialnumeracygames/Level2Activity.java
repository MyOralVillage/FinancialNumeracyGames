/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */


package com.myoralvillage.financialnumeracygames;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class Level2Activity extends GenericActivityGame {
    File root = new File(Environment.getExternalStorageDirectory(), "Notes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);

        Intent intent = getIntent();
        getExtras(intent);
    }

    public void goToLevel2PlaceValue(View v) {
        Intent intent = createIntent(Level2ActivityGamePV.class);
        startActivity(intent);
        finish();
    }

    public void goToLevel2Ordering(View v) {
        Intent intent = createIntent(Level2ActivityGameOrdering.class);
        startActivity(intent);
        finish();
    }

    public void goToLevel2FillInTheBlanks(View v) {
        Intent intent = createIntent(Level2ActivityGameFillInTheBlanks.class);
        startActivity(intent);
        finish();
    }

    public void setHomeButton(View v) {
        Intent intent = createIntent(GameMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        Intent intent = createIntent(GameMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
