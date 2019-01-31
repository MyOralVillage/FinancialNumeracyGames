/*
 * Copyright 2016, 2019 MyOralVillage
 * All Rights Reserved
 */


package com.myoralvillage.financialnumeracygames;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by paulj on 2016-10-16.
 * <p>
 * This is the superclass incorporating the vast majority of the code for the Exact Change games
 * <p>
 * This code makes the assumption that there are exactly 5 different currency units in any test. These may be coins or bills:/d
 * <p>
 * Some currencies have decimal points, some do not.
 * <p>
 * I suspect that what I really should be doing is to create the base layout out of a single xml file and then to explicitly add the images for
 * each countries currencies. That may be my next step. But, since I don't really know how to do that yet for the moment I'll just copy almost all of the xml file and change just the image parts.
 * <p>
 * Hmm. Possible kludgy work around would be to create most of the image and then set the Drawable. Wow, that may work.
 */

public abstract class Level3ActivityCurrencyGame extends CurrencyActivityGame {


    //text views being dragged and dropped onto

    public TextView item;
    public ViewGroup imageSandbox;
    int qNum;
    double totalCash;
    public TextView cashView;
    public TextView tenderedView;


    /*
     * Each individual test has the resource representing what is being bought, the answer the user
     * is expected to input, and the number of each currency unit that is being displayed
     *
     * Rather than rely on the user to do the math, this inputs the actual value of the purchased_item
     * and calculates the correct change. Seems safer
     *
     * Note that the same set of tests is used in both the purchase and exact change games
     *
     * While arguably not optimal thisn significantly reduces the code duplication and makes it
     * much easier to add currencies.
     *
     * For the purchase game, we care about the cost of the purchased_item
     * For the exact change, we calculate the exact change
     */

    boolean is_purchase; // Set in sub classes so know which answer to use
    String name_score_file;

    public class Individual_Test {
        int bought_item; // ID of the image
        float cost_item;
        float correct_amount;
        float amount_paid; // On;y used in exact change game

        Individual_Test(int ques, float cost, float paid) {
            bought_item = ques;
            amount_paid = paid;
            cost_item = cost;

            if (is_purchase) {
                correct_amount = cost_item;
                if (correct_amount <= 0) {
                    throw new AssertionError("Correct amount must be >0");
                }
            } else {
                correct_amount = paid - cost_item;
                if (paid <= 0)
                    throw new AssertionError("paid should be > 0");
                if (correct_amount < 0)
                    throw new AssertionError("Change should be >=");
            }


        }
    }

    Individual_Test[] tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        getExtras(intent);

        userHasViewedDemo = thisUser.demosViewed[8];

        cashView = findViewById(R.id.inputAmountView);
        tenderedView = findViewById(R.id.tenderedAmountView);

        //views to drop onto
        imageSandbox = findViewById(R.id.inputArea);

        //set drag listeners
        imageSandbox.setOnDragListener(new Level3ActivityCurrencyGame.ChoiceDragListener());

        //setup question
        item = findViewById(R.id.purchased_item);
        onCreateCurrencySpecific();

        if (!userHasViewedDemo) {
            runDemo();
        }
        setQuestion(qNum);

    }

    abstract void runDemo();

    /*
     * Set the paid information if it is relevant (exact change)
     */
    abstract void setPaid();

    public void setQuestion(int qNum) {
        correctOnFirstTry = true;
        scoringNumAttempts = 0;
        scoringCorrect = "error";
        scoringSelectedAnswer = "error";
        scoringQuestion = "error";
        scoringAnswers[0] = "error";
        scoringAnswers[1] = "error";
        scoringAnswers[2] = "error";

        startTime = System.currentTimeMillis();
        System.out.println("setQuestion::" + qNum);
        item.setBackgroundResource(tests[qNum].bought_item);
        item.setText(String.format(locale, format_string, tests[qNum].cost_item));
        totalCash = 0;
        setPaid();
        for (PerCash cash_unit : cash_units) {
            cash_unit.clearInput();
        }

        cashView.setText(String.format(locale, format_string, totalCash));

    }

    public void resetBoard() {
        for (PerCash cash_unit : cash_units) {
            cash_unit.clearInput();
        }
        totalCash = 0;
        cashView.setText(String.format(locale, format_string, totalCash));
    }

    public void advanceGame(View v) {
        scoringAnswers[0] = "selectCash";
        scoringAnswers[1] = "selectCash";
        scoringAnswers[2] = "selectCash";

        scoringNumAttempts++;
        scoringQuestion = String.valueOf(tests[qNum].correct_amount);
        scoringSelectedAnswer = String.valueOf(totalCash);

        System.out.println("***** In advanceGame");
        if (totalCash == tests[qNum].correct_amount) {
            scoringCorrect = "correct";
            if (correctOnFirstTry) {
                numCorrect++;
                String score_name = "starb" + numCorrect;
                int score_id = getResources().getIdentifier(score_name, "drawable", getPackageName());
                ImageView tv = findViewById(R.id.score);
                tv.setImageResource(score_id);
                endTime = System.currentTimeMillis();
                PerformanceData.getInstance().set_time(gameNum, qNum, endTime - startTime);
            } else {
                PerformanceData.getInstance().test_failed(gameNum, qNum);
            }
            writeToScore(name_score_file);
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.applause);
            mediaPlayer.start();
            ++qNum;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (qNum < tests.length) {
                setQuestion(qNum);
            } else {
                onBackPressed();
            }

        } else {
            scoringCorrect = "incorrect";
            PerformanceData.getInstance().test_failed(gameNum, qNum);
            writeToScore(name_score_file);
            resetBoard();
        }
    }


    @Override
    public void onBackPressed() {
        if (!thisUser.userName.equals("admin")) {
            updateUserSettings();
        }

        Intent intent = createIntent(Level3Activity.class); // TODO Pop all the way out ? Probably
        // TODO : Make sure to test non admin behaviour
        startActivity(intent);
        finish();
    }

    /**
     * DragListener will handle dragged views being dropped on the drop area
     * - only the drop action will have processing added to it as we are not
     * - amending the default behavior for other parts of the drag process
     */
    @SuppressLint("NewApi")
    public class ChoiceDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DROP:

                    //handle the dragged view being dropped over a drop view
                    View view = (View) event.getLocalState();
                    //stop displaying the view where it was before it was dragged
                    view.setVisibility(View.VISIBLE);

                    TextView cashView = findViewById(R.id.inputAmountView);

                    ImageView dropped = (ImageView) view;
                    String droppedId = dropped.getResources().getResourceName(dropped.getId());
                    //String boxId = imageBox1.getResources().getResourceName(imageBox1.getId());
                    System.out.println("Wow oh wow On Drag + " + droppedId);

                    for (PerCash cash_unit : cash_units) {
                        if(dropped.getId() == cash_unit.getBillId()) {
                            System.out.println("Found bill!!!");
                            cash_unit.getInputView().setImageResource(cash_unit.getDrawableId());
                            cash_unit.increaseInputNum();
                            totalCash = totalCash + cash_unit.getValue();
                            cashView.setText(String.format(locale, format_string, totalCash));
                            cash_unit.getInputNumView().setText(String.valueOf(cash_unit.getInputNum()));
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //no action necessary
                    break;
                default:
                    break;
            }
            return true;
        }
    }


    /*
     * This is a helper function designed to make the initialization code clearer. Put all the
     * currency specific test details here into one place
     */

    void onCreateCurrencySpecific() {

        String[] currency_tests = getResources().getStringArray(R.array.Currency_tests);
        format_string = getString(R.string.Money_format);
        tests = new Individual_Test[currency_tests.length];

        for (int i = 0; i < currency_tests.length; i++) {
            String[] parts = currency_tests[i].split(",");
            int resId = getResources().getIdentifier(parts[0], "drawable", getPackageName());
            tests[i] = new Individual_Test(resId,
                    Float.parseFloat(parts[1]),
                    Float.parseFloat(parts[2]));
        }


        /*
         * Old, soon to be totally obsolete, code

		  *
		  * Each constructor has an image, the value for the change, and the numbers for each of the 5 currencies
		  *
		  * Note that a subset of the information is used for the PurchaseGame. But both ExactChange and PurchaseGames
		  * use the same data structure


		switch(thisUser.actual_country) {
			case KENYA:
				// units 50, 100, 200, 500, 1000
				tests = new Individual_Test[] {
						new Individual_Test(R.drawable.bananas_shilling_500, 500f, new int[] {0, 1, 0, 0, 0}),
						new Individual_Test(R.drawable.basket_fish_shilling_5500, 5500f, new int[] {0, 0, 1, 1, 0}),
						new Individual_Test(R.drawable.basketpears_shilling_3500,  3500f, new int[] {0, 0, 0, 1, 0}),
						new Individual_Test(R.drawable.bike_shilling_150000, 150000f, new int[] {0, 0, 3, 1, 14}),
						new Individual_Test(R.drawable.calculator_shilling_5000, 5000f, new int[] {0, 0, 0, 0, 1}),
						new Individual_Test(R.drawable.chair_shilling_50000, 50000.0f, new int[] {0, 0, 3, 1, 4}),
						new Individual_Test(R.drawable.chicken_shilling_10000, 10000.0f, new int[] {0, 0, 0, 0, 1}),
						new Individual_Test(R.drawable.clock_shilling_20000, 20000f, new int[] {0, 0, 3, 3, 0}),
						new Individual_Test(R.drawable.corn_shilling_2500, 2500f, new int[] {0, 0, 0, 1, 0}),
						new Individual_Test(R.drawable.flipflops_shilling_30000, 30000f, new int[] {0, 0, 3, 1, 2}),
						new Individual_Test(R.drawable.notebook_shilling_8000,  8000.0f, new int[] {0, 0, 0, 2, 0}),
						new Individual_Test(R.drawable.pencil_shilling_500,  500f, new int[] {0, 0, 1, 0, 0}),
						new Individual_Test(R.drawable.mobilephone_shilling_200000, 200000.0f, new int[] {0, 0, 0, 0, 20})
				};
				break;
			case TONGA:
				// For the Panga, the units are 50 Centi, 1 Pa_Anga, 5, 10, 20
				tests = new Individual_Test[] {
						new Individual_Test(R.drawable.bananas_pa_anga_0_50, 0.5f, new int[] {0, 1, 0, 0, 0}),
						new Individual_Test(R.drawable.basket_fish_pa_anga_5_50, 5.5f, new int[] {0, 0, 0, 1, 0}),
						new Individual_Test(R.drawable.basketoranges_pa_anga_3_50,  3.5f, new int[] {0, 0, 1, 0, 0}), // This is also the Demo question
						new Individual_Test(R.drawable.bike_pa_anga_150, 150.0f, new int[] {0, 0, 0, 0, 8}),
						new Individual_Test(R.drawable.calculator_pa_anga_12, 12.0f, new int[] {0, 0, 1, 1, 0}),
						new Individual_Test(R.drawable.chair_pa_anga_47, 47.0f, new int[] {0, 0, 2, 2, 1}),
						new Individual_Test(R.drawable.chicken_pa_anga_25, 25.0f, new int[] {0, 0, 0, 1, 1}),
						// new Individual_Test(R.drawable.clock_pa_anga, 17.2f, new int[] {0, 0, 2, 1, 0}),
						//    new Individual_Test(R.drawable.flipflops_pa_anga, 14.8f, new int[] {0, 0, 0, 1, 1}),
						new Individual_Test(R.drawable.notebook_pa_anga_8,  8.0f, new int[] {0, 0, 0, 0, 1}),
						new Individual_Test(R.drawable.pencil_pa_anga_0_50,  0.5f, new int[] {0, 0, 1, 0, 0}),
						new Individual_Test(R.drawable.mobilephone_pa_anga_200, 200.0f, new int[] {0, 0, 0, 0, 10})
				};
				break;

			case VANUATU:
				tests = new Individual_Test[] {
						//  new Individual_Test(R.drawable.bananas_vatu_25, 25f, new int[] {0, 0, 0, 0, 0}),
						//  new Individual_Test(R.drawable.basket_fish_vatu_275, 275f, new int[] {0, 0, 0, 0, 0}),
						new Individual_Test(R.drawable.basketpears_vatu_800,  800f, new int[] {0, 0, 2, 0, 0}),
						new Individual_Test(R.drawable.basketpears_vatu_800,  800f, new int[] {0, 0, 0, 1, 0}),
						new Individual_Test(R.drawable.basketpears_vatu_800,  800f, new int[] {0, 0, 0, 0, 1}),
						new Individual_Test(R.drawable.bike_vatu_7500, 7500f, new int[] {0, 0, 0, 0, 2}),
						new Individual_Test(R.drawable.calculator_vatu_600, 600f, new int[] {0, 0, 1, 1, 0}),
						// new Individual_Test(R.drawable.chair_pa_anga_47, 47.0f, new int[] {0, 0, 2, 2, 1}),
						//  new Individual_Test(R.drawable.chicken_pa_anga_25, 25.0f, new int[] {0, 0, 0, 1, 1}),
						new Individual_Test(R.drawable.clock_vatu_900, 900f, new int[] {0, 0, 2, 0, 0}),
						new Individual_Test(R.drawable.clock_vatu_900, 900f, new int[] {0, 0, 0, 1, 0}),
						new Individual_Test(R.drawable.clock_vatu_900, 900f, new int[] {0, 0, 0, 0, 1}),
						new Individual_Test(R.drawable.clock_vatu_900, 900f, new int[] {0, 0, 2, 0, 0}),
						//     new Individual_Test(R.drawable.flipflops_pa_anga, 14.8f, new int[] {0, 0, 0, 1, 1}),
						new Individual_Test(R.drawable.notebook_vatu_400,  400.0f, new int[] {0, 0, 1, 0, 0}),
						//     new Individual_Test(R.drawable.pencil_vatu_25,  25f, new int[] {0, 0, 1, 0, 0}),
						new Individual_Test(R.drawable.mobilephone_vatu_10000, 10000.0f, new int[] {0, 0, 0, 0, 2})
				};
				break;
			case TANZANIA:
				// units 500, 1000, 2000, 5000, 10000
				tests = new Individual_Test[] {
						new Individual_Test(R.drawable.bananas_shilling_500, 500f, new int[] {0, 1, 0, 0, 0}),
						new Individual_Test(R.drawable.basket_fish_shilling_5500, 5500f, new int[] {0, 0, 1, 1, 0}),
						new Individual_Test(R.drawable.basketpears_shilling_3500,  3500f, new int[] {0, 0, 0, 1, 0}),
						new Individual_Test(R.drawable.bike_shilling_150000, 150000f, new int[] {0, 0, 3, 1, 14}),
						new Individual_Test(R.drawable.calculator_shilling_5000, 5000f, new int[] {0, 0, 0, 0, 1}),
						new Individual_Test(R.drawable.chair_shilling_50000, 50000.0f, new int[] {0, 0, 3, 1, 4}),
						new Individual_Test(R.drawable.chicken_shilling_10000, 10000.0f, new int[] {0, 0, 0, 0, 1}),
						new Individual_Test(R.drawable.clock_shilling_20000, 20000f, new int[] {0, 0, 3, 3, 0}),
						new Individual_Test(R.drawable.corn_shilling_2500, 2500f, new int[] {0, 0, 0, 1, 0}),
						new Individual_Test(R.drawable.flipflops_shilling_30000, 30000f, new int[] {0, 0, 3, 1, 2}),
						new Individual_Test(R.drawable.notebook_shilling_8000,  8000.0f, new int[] {0, 0, 0, 2, 0}),
						new Individual_Test(R.drawable.pencil_shilling_500,  500f, new int[] {0, 0, 1, 0, 0}),
						new Individual_Test(R.drawable.mobilephone_shilling_200000, 200000.0f, new int[] {0, 0, 0, 0, 20})
				};
				break;
			default:
				throw new AssertionError("Unrecognized Country");

		}
		*/

    }

}

