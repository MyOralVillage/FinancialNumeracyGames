/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */


package com.myoralvillage.financialnumeracygames;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by paulj on 2016-10-24.
 * <p>
 * This implements the tiny bit of data initialization that is game specific.
 */

public class Level3ActivityDemoPurchase extends Level3ActivityDemoCurrency {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
     * This contains the game and currency specific information. It is
     * essentially the only code in the game specific subclasses
     */

    void onCreateGameSpecific() {

        /*
         * And the Demo information

         * Note that no math is done in the demo so make sure the answer is right :-)
         * TODO : Change so that an exception is thrown if the math is wrong
         *
         * Also note that the demo code currently demands that the purchased_item
         * can be bought with exactly two bills.
         */
        setup_demo(false);
    }
}
