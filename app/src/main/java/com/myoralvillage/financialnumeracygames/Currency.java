package com.myoralvillage.financialnumeracygames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Currency extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
    }

    public void backClicked(View v){
        Intent intent = new Intent(this, AdminScreen.class);
        startActivity(intent);
        finish();
    }

    //Temporary method
    public void currencyChange(View v){
        String message = v.getTag().toString() + " currency selected.";
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }

    //Will replace currencyChange
    public void selectCurrency(View v){

    }
}
