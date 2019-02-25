package com.myoralvillage.financialnumeracygames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdminScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);
    }

    public void backClicked(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void groupClicked(View v){
        Toast.makeText(this, "Option not yet implemented", Toast.LENGTH_SHORT).show();
    }

    public void currencyClicked(View v){
        Intent intent = new Intent(this, Currency.class);
        startActivity(intent);
        finish();
    }

    public void gameClicked(View v){
        Intent intent = new Intent(this, GameMenuActivity.class);
        startActivity(intent);
        finish();
    }

}
