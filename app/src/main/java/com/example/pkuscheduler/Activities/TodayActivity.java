package com.example.pkuscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.pkuscheduler.R;

public class TodayActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_today);
        setUpActionBar();

    }

    public void setUpActionBar(){
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(0xffffffff);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    public void CreateNewToDoItem(View view) {
        startActivity(new Intent(this, AddEventActivity.class));
    }
}
