/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */

package com.myoralvillage.financialnumeracygames;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Level1ActivityDemoDualCoding extends GenericActivityGame {
    int currentNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1_demodualcoding);
        startDemo();
    }

    public void startDemo() {

    }

    public void exitDemo(View v) {
        finish();
    }

    /*
     * Helper functon to reduce code duplication
     */

    private void show_images() {
        String numberFileName = "game1_demo_dualcoding_" + currentNumber;
        String representationFileName = "game1_dualcoding_" + currentNumber;

        int img_id_number = getResources().getIdentifier(numberFileName, "drawable", getPackageName());
        int img_id_representation = getResources().getIdentifier(representationFileName, "drawable", getPackageName());

        ImageView ivNumber = findViewById(R.id.img_number);
        ImageView ivRepresentation = findViewById(R.id.img_representation);

        ivNumber.setImageResource(img_id_number);
        ivRepresentation.setImageResource(img_id_representation);
    }

    public void nextNumber(View v) {

        if (currentNumber == 9) {
            finish();
        } else {
            currentNumber++;
            show_images();
            ImageView iv = findViewById(R.id.btn_lvl1_dualCoding_previous);
            iv.setVisibility(ImageView.VISIBLE);
        }
    }

    public void prevNumber(View v) {
        if (currentNumber == 1) {
            ImageView iv = findViewById(R.id.btn_lvl1_dualCoding_previous);
            iv.setVisibility(ImageView.INVISIBLE);
        } else {

            ImageView iv = findViewById(R.id.btn_lvl1_dualCoding_previous);
            iv.setVisibility(ImageView.VISIBLE);
        }
        currentNumber--;
        show_images();
    }

}
