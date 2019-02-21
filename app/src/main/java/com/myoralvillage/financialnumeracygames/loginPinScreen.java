package com.myoralvillage.financialnumeracygames;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class loginPinScreen extends AppCompatActivity {

    ImageButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, clear;
    EditText editText;
    String text;
    ImageView userDP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pin_screen);

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
        clear = (ImageButton) findViewById(R.id.buttonClear);
        editText = (EditText) findViewById(R.id.editText);
        userDP = (ImageView) findViewById(R.id.displayPic);

        buttonListeners();

        Bundle extras = getIntent().getExtras();
        Bitmap bmp = extras.getParcelable("display_pic");

        userDP.setImageBitmap(bmp );

    }

    public void confirmClicked(View v){
        text = editText.getText().toString();
        if(text.matches("123") ){
            Intent intent = new Intent(this, AdminScreen.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this, GameMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void backClicked(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void buttonListeners(){
        //Inputs numbers into textview on click
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "3");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "9");
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText() + "0");
            }
        });
        //end

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }
}
