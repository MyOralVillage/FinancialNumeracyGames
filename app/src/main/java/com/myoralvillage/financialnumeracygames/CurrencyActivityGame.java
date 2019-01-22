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
 * <p>
 * This class is the superclass that contains the currency specific functionality
 * <p>
 * The intent is that almost all of the currency specific data will be defined here
 * <p>
 * The changes between the exact change and PV games (the only two subclasses) should eventually
 * be VERY minor (with luck, eliminated totally although that remains to be seen)
 * <p>
 * Originally I used subclasses of exactChange and PV to hold this information. While arguably
 * more object oriented that lead to two problems
 * 1) Code Duplication
 * 2) Having to update multiple places when a new currency was added
 * <p>
 * So, instead I have brought the information up here in the form of arrays
 * TODO : Should I use maps or the like instead?
 * <p>
 * There are two distinct types of information here
 * 1) An array of information required for each cash unit. Each unit
 * can be a coin or a bill. At this moment, there are 5 units per currency
 * This may change to 6. More than that might be too many as the images become
 * too small. TODO : Investigate
 * 2) The per currency information. The image and value of each unit of cash
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
     *        This probably isn't true of countries with hyperinflation (eg, Venezuala).
     *
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
            int cash_val_in_cents = (int) (cash_unit.value * 100);
            if (val_in_cents < cash_val_in_cents) {
                continue;
            }
            int num = (val_in_cents / cash_val_in_cents);
            val_in_cents -= num * cash_val_in_cents;
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
     *
     * This class contains the information required for BOTH the payment and change games.
     *
     * The information in the cash_paid fiels is ONLY used in the exact change game
     */

    public class PerCash { // Despite the warning this pretty much DOES have to be public

        /*
         * Some of these are cached for efficiencies sake
         *
         * They are created the first time that they are needed
         *
         * The drawable is the actual image of the currency unit (bill or coin)
         * All other IDs are IDs for layout elements
         */

        /*
         * The various places where we display bills together with the number of the bill
         * are all built as two separate fields, one the image of the bill and the other
         * the text field containing the number
         *
         * I tried using a single compound drawable but was just unable to get it to work
         * properly. According to others, this is a reasonably common issue with there being
         * a lack of control
         *
         * TODO - At some point should reinvestigate to see if we can end up with a single view
         */

        private ImageView bill_image; // The cash unit at the bottom of the screen.
        private int bill_id; // The ID of the cash unit at the bottom of the screen

        public ImageView getBillImage(){
            return bill_image;
        }

        public int getBillId() {
            return bill_id;
        }

        /*
         * The input areaa in the middle of the screen. The
         * area where bills are swiped to and swiped from.
         *
         * Present in BOTH games
         */

        private int input_id ;              // The Resource ID of the input holding the input image
        private ImageView input_image;
        private int input_num_id;
        private TextView input_numView;  // the layout hodling the image of the input purchased_item
        private int input_num;  // The number actually input

        public ImageView getInputView() {
            if(input_image == null) {
                input_image = findViewById(input_id);
            }
            return input_image;
        }

        public TextView getInputNumView() {
            if(input_numView == null) {
                input_numView = findViewById(input_num_id);
            }
            return input_numView;
        }

        public int getInputNum() {
            return input_num;
        }

        public void increaseInputNum() {
            input_num++;
        }

        /*
         * The presentation areaa at the top of the screen.
         *
         * This is ONLY used in the exact change game where
         * it is used to present the information for the amoubt
         * the user has actually tendered.
         *
         * It is NOT an interaction area. All values here are set when
         * a game is created and then ignored
         */

        private int tendered_id;              // The Resource ID of the tendered currency
        private ImageView tendered_image;  // the layout holding the image of the tendered currency
        private int tendered_num_id;          // The Resource ID of the number of tendered currency
        private TextView tendered_numView; // the layout holding the number of tendered currency
        private int tendered_num;   // The actual number of tendered items

        public ImageView getTenderedImage() {
            if(tendered_image == null) {
                tendered_image = findViewById(tendered_id);
            }
            return tendered_image;
        }

        public TextView getTenderedNum() {
            if(tendered_numView == null) {
                tendered_numView = findViewById(tendered_num_id);
            }
            return tendered_numView;
        }

        private int num;    // Number transferred
        private float value; // The value of the bill
        private int drawable_id; // The ID of the actual image for the bill. All other IDs are for layout elements

        /*
         * TODO - Should I just cache the image and NOT export drawable_id? Maybe
         */

        public int getDrawableId() {
            return drawable_id;
        }

        public float getValue() {
            return value;
        }

        public void clearInput()
        {
            getInputNumView().setText("0");
            num = 0;
            getInputView().setImageResource(R.drawable.black_background);
        }

        @SuppressLint("ClickableViewAccessibility")
        PerCash(
                int r_drawable, float r_val,
                int r_bill,
                int r_input, int r_input_num_id,
                int r_tendered, int r_tendered_num_id)
        {
            drawable_id = r_drawable;
            value = r_val;
            bill_id = r_bill;
            bill_image = findViewById(bill_id);
            bill_image.setImageDrawable(getResources().getDrawable(drawable_id));
            bill_image.setOnTouchListener(new ChoiceTouchListener());

            input_id = r_input;
            input_num_id = r_input_num_id;

            tendered_id = r_tendered;
            tendered_num_id = r_tendered_num_id;
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
        /*
         * TODO There is probably a way to make this an array using string lookups or something
         * TODO AT the moment, this is good enough for me
         *
         * TODO - I want to merge the image and the number into a single NumView. Reducing elements
         *      - seems like a QUITE good idea.
         */

        cash_units = new PerCash[]{
                new PerCash(
                        R.drawable.cur_1, // The resource of the ACTUAL image. NOT the layout
                        Float.parseFloat(money_values[0]),
                        R.id.currency_1_bill,   // Bill at bottom - contains ACTUAL image
                        R.id.currency_1_input_cash_view, R.id.currency_1_input_num_view,
                        R.id.currency_1_tendered_cash_view, R.id.currency_1_tendered_num_view
                ),
                new PerCash(
                        R.drawable.cur_2, // The resource of the ACTUAL image. NOT the layout
                        Float.parseFloat(money_values[1]),
                        R.id.currency_2_bill,   // Bill at bottom - contains ACTUAL image
                        R.id.currency_2_input_cash_view, R.id.currency_2_input_num_view,
                        R.id.currency_2_tendered_cash_view, R.id.currency_2_tendered_num_view
                ),
                new PerCash(
                        R.drawable.cur_3, // The resource of the ACTUAL image. NOT the layout
                        Float.parseFloat(money_values[2]),
                        R.id.currency_3_bill,   // Bill at bottom - contains ACTUAL image
                        R.id.currency_3_input_cash_view, R.id.currency_3_input_num_view,
                        R.id.currency_3_tendered_cash_view, R.id.currency_3_tendered_num_view
                ),
                new PerCash(
                        R.drawable.cur_4, // The resource of the ACTUAL image. NOT the layout
                        Float.parseFloat(money_values[3]),
                        R.id.currency_4_bill,   // Bill at bottom - contains ACTUAL image
                        R.id.currency_4_input_cash_view, R.id.currency_4_input_num_view,
                        R.id.currency_4_tendered_cash_view, R.id.currency_4_tendered_num_view
                ),
                new PerCash(
                        R.drawable.cur_5, // The resource of the ACTUAL image. NOT the layout
                        Float.parseFloat(money_values[4]),
                        R.id.currency_5_bill,   // Bill at bottom - contains ACTUAL image
                        R.id.currency_5_input_cash_view, R.id.currency_5_input_num_view,
                        R.id.currency_5_tendered_cash_view, R.id.currency_5_tendered_num_view
                ),
                new PerCash(
                        R.drawable.cur_6, // The resource of the ACTUAL image. NOT the layout
                        Float.parseFloat(money_values[5]),
                        R.id.currency_6_bill,   // Bill at bottom - contains ACTUAL image
                        R.id.currency_6_input_cash_view, R.id.currency_6_input_num_view,
                        R.id.currency_6_tendered_cash_view, R.id.currency_6_tendered_num_view
                )
        };
    }

}
