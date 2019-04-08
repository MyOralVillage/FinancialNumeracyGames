package com.myoralvillage.financialnumeracygames;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class loginPinScreen extends AppCompatActivity {

    ImageButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, clear, confirm;
    EditText editText1, editText2, editText3;
    String text;
    ImageView userDP;
    Integer boxNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pin_screen);

        //region Keypad Number Button Finders
        button0 = (ImageButton) findViewById(R.id.button0);
        button1 = (ImageButton) findViewById(R.id.button1);
        button2 = (ImageButton) findViewById(R.id.button2);
        button3 = (ImageButton) findViewById(R.id.button3);
        button4 = (ImageButton) findViewById(R.id.button4);
        button5 = (ImageButton) findViewById(R.id.button5);
        button6 = (ImageButton) findViewById(R.id.button6);
        button7 = (ImageButton) findViewById(R.id.button7);
        button8 = (ImageButton) findViewById(R.id.button8);
        button9 = (ImageButton) findViewById(R.id.button9);
        //endregion
        clear = (ImageButton) findViewById(R.id.buttonClear);
        confirm = (ImageButton) findViewById(R.id.confirm);

        userDP = (ImageView) findViewById(R.id.displayPic);

        //region temp solution for input boxes; separate edittext box finders
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        //endregion

        buttonListeners();

        Bundle extras = getIntent().getExtras();
        Bitmap bmp = extras.getParcelable("display_pic");

        //Sets user DP to image selected from previous screen
        userDP.setImageBitmap(bmp);

        //hide soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //Temporary solution until user functions implemented
    public void confirmClicked(View v) {
        text = editText1.getText().toString() + editText2.getText().toString() + editText3.getText().toString();
        //To see admin options, use "123" to enter admin view
        if (text.matches("123")) {
            Intent intent = new Intent(this, AdminScreen.class);
            startActivity(intent);
            finish();
        }
        //Default view; brings user to game screen
        else {
            Intent intent = new Intent(this, GameMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Returns to Login ID page
    public void backClicked(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //listens for button input
    public void buttonListeners() {

        //region Inputs numbers into edittext1 on click
        if (boxNo == 1) {
            //Make clear button invisible again
            clear.setVisibility(View.INVISIBLE);
            //region Change colour of edit boxes to show 1st box as selected
            editText1.setBackgroundResource(R.drawable.pin_selected);
            editText2.setBackgroundResource(R.drawable.pin_un_selected);
            editText3.setBackgroundResource(R.drawable.pin_un_selected);
            //endregion

            //Check for number selection feedback
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("1");
                    boxNo++;
                    buttonListeners();
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("2");
                    boxNo++;
                    buttonListeners();
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("3");
                    boxNo++;
                    buttonListeners();
                }
            });

            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("4");
                    boxNo++;
                    buttonListeners();
                }
            });

            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("5");
                    boxNo++;
                    buttonListeners();
                }
            });

            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("6");
                    boxNo++;
                    buttonListeners();
                }
            });

            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("7");
                    boxNo++;
                    buttonListeners();
                }
            });

            button8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("8");
                    boxNo++;
                    buttonListeners();
                }
            });

            button9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("9");
                    boxNo++;
                    buttonListeners();
                }
            });

            button0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("0");
                    boxNo++;
                    buttonListeners();
                }
            });
            //end

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText1.setText("");
                    buttonListeners();
                    Log.i("1", "box" + boxNo);
                }
            });
        }
        //endregion

        //region Inputs numbers into edittext2 on click
        else if (boxNo == 2) {
            //makes clear button visible after input
            clear.setVisibility(View.VISIBLE);
            //region Change colour of edit boxes to show 2nd box as selected
            editText2.setBackgroundResource(R.drawable.pin_selected);
            editText1.setBackgroundResource(R.drawable.pin_un_selected);
            editText3.setBackgroundResource(R.drawable.pin_un_selected);
            //endregion

            //Inputs numbers into edittext2 on click
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("1");
                    boxNo++;
                    buttonListeners();
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("2");
                    boxNo++;
                    buttonListeners();
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("3");
                    boxNo++;
                    buttonListeners();
                }
            });

            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("4");
                    boxNo++;
                    buttonListeners();
                }
            });

            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("5");
                    boxNo++;
                    buttonListeners();
                }
            });

            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("6");
                    boxNo++;
                    buttonListeners();
                }
            });

            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("7");
                    boxNo++;
                    buttonListeners();
                }
            });

            button8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("8");
                    boxNo++;
                    buttonListeners();
                }
            });

            button9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("9");
                    boxNo++;
                    buttonListeners();
                }
            });

            button0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText2.setText("0");
                    boxNo++;
                    buttonListeners();
                }
            });
            //end

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText2.getText().toString().matches("")){
                        editText1.setText("");
                        boxNo = boxNo - 1;
                        Log.i("cat", "catnip" + boxNo);
                    }
                    else{
                    editText2.setText("");
                        Log.i("2", "box" + boxNo);
                        Log.i("cat", "ita" + editText2.getText().toString()+ "end");
                    }
                    buttonListeners();

                }
            });
        }
        //endregion

        //region Inputs numbers into edittext3 on click
        else if (boxNo == 3) {
            //region Change colour of edit boxes to show 1st box as selected
            editText3.setBackgroundResource(R.drawable.pin_selected);
            editText2.setBackgroundResource(R.drawable.pin_un_selected);
            editText1.setBackgroundResource(R.drawable.pin_un_selected);
            //endregion

            //Inputs numbers into edittext3 on click
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("1");
                    buttonListeners();
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("2");
                    buttonListeners();
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("3");
                    buttonListeners();
                }
            });

            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("4");
                    buttonListeners();
                }
            });

            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("5");
                    buttonListeners();
                }
            });

            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("6");
                    buttonListeners();
                }
            });

            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("7");
                    buttonListeners();
                }
            });

            button8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("8");
                    buttonListeners();
                }
            });

            button9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("9");
                    buttonListeners();
                }
            });

            button0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText3.setText("0");
                    buttonListeners();
                }
            });
            //end

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText3.getText().toString().matches("")){
                        editText2.setText("");
                        boxNo = boxNo - 1;
                        Log.i("cat", "catnip" + boxNo);
                    }
                    else{
                        editText3.setText("");
                        Log.i("3", "box" + boxNo);
                        Log.i("cat", "ita" + editText2.getText().toString()+ "end");
                    }
                    buttonListeners();

                }
            });
        }
        //endregion

        //region If box 3 is not empty, show confirm button
        if(editText3.getText().toString().trim().length() != 0){
            confirm.setVisibility(View.VISIBLE);
            //region Change colour of edit boxes to show none selected
            editText3.setBackgroundResource(R.drawable.pin_un_selected);
            editText2.setBackgroundResource(R.drawable.pin_un_selected);
            editText1.setBackgroundResource(R.drawable.pin_un_selected);
            //endregion
        }
        else{
            confirm.setVisibility(View.INVISIBLE);
        }
        //endregion
    }

}

