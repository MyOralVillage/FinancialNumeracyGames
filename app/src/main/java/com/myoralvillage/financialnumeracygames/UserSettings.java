/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */

package com.myoralvillage.financialnumeracygames;

/**
 * Created by paulj on 2016-10-16.
 * <p>
 * This just keeps track of some user inputted data
 */

public class UserSettings {

    /*
     * In order to add a new country a few steps have to be followed
     *   1) Extend the Country enum below
     *   2) Add the new currency information in CurrencyActiveGame
     *   3) Make sure the appropriate images with the correct names have been added to
     *      the drawables directory
     *   4) Add a new currency to the onCreateCurrencySpecific functions
     *      in the files Level3ActivityCurrencyGame, Level3ActivityDemoCurrency, Level3ActuvityDemoExactChange
     *   5) TODO : Extend the mechanism to run time select the country
     *
     *   That should be all
     */
    /*
     * Note that we are separating countries by mcc code. Country specific items will
     * be placed in appropriate directories (eg, drawable-mcc639 for Kenya
     *
     * TODO - Figure out how to run the emulator in a specific country. And H/W for that matter.
     * TODO - For now, we just copy the appropriate country to the default directory.
     */
    /*
     * We support 4 countries right now
     *   Kenya - mcc639 - Shilling, KZ-   Approximate value 0.013 Canadian
     *   Tanzania - mcc640 Shilling, TZ-
     *   Vanuatu - mcc541 Vatu VT
     *   Tonga - mcc539 Pa'Anga T$
     */

    /*
     * This is dead code right now. At this point EVERYTHING comes from the strings files.
     * TODO - Implement code to choose country. AT momment, comes from locale which comes from non existant
     * TODO - SIMS card. So, comes from default. For moment, am copying appropriate files into default directory

    public enum Country {KENYA, TANZANIA, VANUATU, TONGA}
    Country actual_country = Country.TANZANIA;
    */
    public String userName = "admin"; // Make this the default for now. CHANGE BEFORE RELEASE
    public int userId; // An internal ID. TODO - Map this to more than one fruit
    public boolean[] demosViewed = {false, false, false, false, false, false, false, false, false};
    public boolean[] availableLevels = {true, false, false};
    public boolean[] activityProgress = {false, false, false, false, false, false, false, false, false};
    public boolean admin = true;
}
