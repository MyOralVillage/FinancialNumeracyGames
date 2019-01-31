/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */

package com.myoralvillage.financialnumeracygames;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class Level1Activity extends GenericActivityGame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1);

        Intent intent = getIntent();
        getExtras(intent);
    }

    public void goToLevel1QA(View v) {
        Intent intent = createIntent(Level1ActivityGameQA.class);
        startActivity(intent);
        finish();
    }

    public void goToLevel1Tracing(View v) {
        Intent intent = createIntent(Level1ActivityGameTracing.class);
        startActivity(intent);
        finish();
    }

    public void goToLevel1DualCoding(View v) {
        Intent intent = createIntent(Level1ActivityGameDualCoding.class);
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
