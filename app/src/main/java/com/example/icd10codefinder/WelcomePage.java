package com.example.icd10codefinder;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.icd10codefinder.R.layout.activity_welcome_page;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_welcome_page);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(WelcomePage.this, CodeFinder.class);
                WelcomePage.this.startActivity(myIntent);
                WelcomePage.this.finish();

                overridePendingTransition(0, android.R.anim.slide_out_right);
            }
        }, 3000);

    }
}
