/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */


package com.myoralvillage.financialnumeracygames;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by paulj on 2016-10-26.
 *
 * This class is the superclass that contains the currency specific functionality
 *
 * The intent is that almost all of the currency specific data will be defined here
 *
 * The changes between the exact change and PV games (the only two subclasses) should eventually
 * be VERY minor (with luck, eliminated totally although that remains to be seen)
 *
 * Originally I used subclasses of exactChange and PV to hold this information. While arguably
 * more object oriented that lead to two problems
 *       1) Code Duplication
 *       2) Having to update multiple places when a new currency was added
 *
 * So, instead I have brought the information up here in the form of arrays
 * TODO : Should I use maps or the like instead?
 *
 * There are two distinct types of information here
 *     1) An array of information required for each cash unit. Each unit
 *        can be a coin or a bill. At this moment, there are 5 units per currency
 *        This may change to 6. More than that might be too many as the images become
 *        too small. TODO : Investigate
 *     2) The per currency information. The image and value of each unit of cash
 */

public abstract class CurrencyActivityGame extends GenericActivityGame {

    /*
     * This is a helper function. It takes a value in currency and comes up with the most
     * efficient set of bills to make up that currency
     *
     * Eg, 7,500 might be 1 5,000, 1 2,000 and 1 500
     *
     * Note that it is a major error for the amount to NOT be able to represented by
     * the units of a currency
     *
     * Because float arithmetic positively sucks, we instead calculate in integers using pennies.
     *
     * TODO : This fundamentlly assumes that an integer is large enough to hold all values in pennies.
     *        This probably isn't true of countries with hyperinflation (eg, Brazil).
     *
     */

    /*
     * I want a single entry point to handle errors.
     *
     * At the very least, an error is going to be locale specific
     *
     * But I think as well as text I'm going to want to add a picture for each error message.
     *
     * TODO: Add some kind of pop up picture since our target audience IS illiterate
     */


    public int[] input_canonicalize(float val) {
        System.out.println("input canonicalize Val is " + val);
        if (val < 0.0) {
            displayError(R.string.negative_error_message);
            return null;
        }

        int[] num_vals = new int[cash_units.length];
        for (int i = 0; i < cash_units.length; i++) {
            num_vals[i] = 0;
        }

        /*
         * Unfortunately, inaccuracies creep into our calculations with floats
         *
         * The following assumes that the float values have EXACTLY 2 digits (for cents).
         * I'm pretty sure that this is a safe assumption worldwide.
         *
         * TODO: BitCoin and the like??
         *
         * So, we do all our math on integers that represent pennies. Sigh.
         */

        /*
         * From now on we are going to do all calculations in pennies.
         */
        int val_in_cents = (int) (val * 100 + 0.0001f);
        /*
         * Iterate over currency units in order from highest value to lowest value
         *
         * At the moment this is hardcoded assuming currency units go from left to right in ascending order
         *
         * TODO : Make sure this can handle currencies done in the reverse order
         * TODO : Definitely need to add unit tests to test currencies to ensure assumptions are met
         */
        for (int i = cash_units.length - 1; i >= 0; i--) {
            PerCash cash_unit = cash_units[i];
            System.out.println("Loop entry " + i + " bill has value " + cash_unit.value);
            int cash_val_in_cents = (int) (cash_unit.value * 100);
            System.out.println("cash val in cents is " + cash_val_in_cents + " while val is now " + val_in_cents);
            if (val_in_cents < cash_val_in_cents) {
                continue;
            }
            int num = (val_in_cents / cash_val_in_cents);
            val_in_cents -= num * cash_val_in_cents;
            System.out.println("Decremented val to " + val_in_cents + " and num is " + num);
            num_vals[i] = num;
        }
        if (val_in_cents > 0) {
            displayError(R.string.cannot_canonicalize);
        }
        return num_vals;

    }

    /*
     *
     * So, I need a class that has the appropriate information for each unit of cash
     *
     * Note that all the information here is independent of the currency. It basically
     * holds layout elements created from the activity_level3_currency_games.xml file
     *
     * I'd like to lift this into a separate class and not be part of an Activity
     * but that really doesn't seem to be the model that Android uses.
     *
     * TODO : Can I create an Intent that doesn't actually do anything just to map the resource
     * files?
     */

    public class PerCash { // Despite the warning this pretty much DOES have to be public
        TextView numView;  // The layout holding the number of paid items
        TextView paidView; // The layout holding the total amount paid
        ImageView paid;    // The actual drawable. Maybe?
        ImageView bill; // Image ?? Note, includes coins despite the name
        ImageView snap;   // The image of the bill as it is being moved from the bottom of the
        // screen to the paid area
        int num;    // Number transferred
        String resource_name; // This is the string in the resource file used by the code to
        // determine which image was selected and moved
        float value;
        int drawable_id;


        @SuppressLint("ClickableViewAccessibility")
        PerCash(int r_num, int r_paid_view, int r_paid, int r_bill, int r_snap,
                String r_nam, int drawable, float val) {
            numView =  findViewById(r_num);
            paidView =  findViewById(r_paid_view);
            paid =  findViewById(r_paid);
            bill =   findViewById(r_bill);
            snap =  findViewById(r_snap);
            num = 0;
            resource_name = r_nam;
            value = val;
            drawable_id = drawable;
            bill.setImageDrawable(getResources().getDrawable(drawable_id));
            bill.setOnTouchListener(new ChoiceTouchListener());
        }
    }

    public void advanceGame(View v) {
        finish();
    }

    public PerCash[] cash_units;

    String format_string; // Used to print the locale and currency specific numbers.
    String[] money_values;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level3_currency_games);
        format_string = getString(R.string.Money_format);
        money_values = getResources().getStringArray(R.array.Value_Each_Currency);
        cash_units = new PerCash[] {
                new PerCash(R.id.currency_1_num, R.id.currency_1_paidview, R.id.currency_1_paid,
                        R.id.currency_1_bill, R.id.currency_1_billSnap,
                        "com.myoralvillage.financialnumeracygames:id/currency_1_bill",
                        R.drawable.cur_1, Float.parseFloat(money_values[0])),
                new PerCash(R.id.currency_2_num, R.id.currency_2_paidview, R.id.currency_2_paid,
                        R.id.currency_2_bill, R.id.currency_2_billSnap,
                        "com.myoralvillage.financialnumeracygames:id/currency_2_bill",
                        R.drawable.cur_2, Float.parseFloat(money_values[1])),
                new PerCash(R.id.currency_3_num, R.id.currency_3_paidview, R.id.currency_3_paid,
                        R.id.currency_3_bill, R.id.currency_3_billSnap,
                        "com.myoralvillage.financialnumeracygames:id/currency_3_bill",
                        R.drawable.cur_3, Float.parseFloat(money_values[2])),
                new PerCash(R.id.currency_4_num, R.id.currency_4_paidview, R.id.currency_4_paid,
                        R.id.currency_4_bill, R.id.currency_4_billSnap,
                        "com.myoralvillage.financialnumeracygames:id/currency_4_bill",
                        R.drawable.cur_4, Float.parseFloat(money_values[3])),
                new PerCash(R.id.currency_5_num, R.id.currency_5_paidview, R.id.currency_5_paid,
                        R.id.currency_5_bill, R.id.currency_5_billSnap,
                        "com.myoralvillage.financialnumeracygames:id/currency_5_bill",
                        R.drawable.cur_5, Float.parseFloat(money_values[4]))
        };
    }

    }
