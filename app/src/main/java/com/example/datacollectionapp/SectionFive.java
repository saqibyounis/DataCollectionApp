package com.example.datacollectionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SectionFive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_five);
    }
    public void next(View view){
        startActivity(new Intent(this,SectionThree.class));

    }
}
