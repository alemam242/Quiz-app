package com.farhan.quizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NoInternet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}