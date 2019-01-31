/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */

package com.myoralvillage.financialnumeracygames;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by paulj on 2016-10-28.
 */

public class Level3ActivityGameExactChange extends Level3ActivityCurrencyGame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        is_purchase = false;
        name_score_file = "level3exactchange.txt";
        super.onCreate(savedInstanceState);
    }

    void runDemo() {
        Intent intent = new Intent(this, Level3ActivityDemoExactChange.class);
        startActivity(intent);
        thisUser.demosViewed[ActivityGame.LEVEL3EXACTCHANGE.ordinal()] = true;
    }

    /*
     * Set the paid information if it is relevant (exact change)
     */

    void setPaid() {
        int[] numPaid =  input_canonicalize(tests[qNum].amount_paid);
        tenderedView.setText(String.format(locale, format_string, tests[qNum].amount_paid));
        for (int i = 0; i < cash_units.length; i++) {
            cash_units[i].getTenderedNum().setText(String.valueOf(numPaid[i]));
            if (numPaid[i] > 0) {
                cash_units[i].getTenderedImage().setImageResource(cash_units[i].getDrawableId());
            } else {
                cash_units[i].getTenderedImage().setImageResource(R.drawable.black_background);
            }
        }
    }
}
