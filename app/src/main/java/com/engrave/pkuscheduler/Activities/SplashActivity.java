package com.engrave.pkuscheduler.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.engrave.pkuscheduler.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        if(getSharedPreferences("loginInfo", Context.MODE_PRIVATE).getBoolean("isLogged",false)){
            startActivity(new Intent(this, MainActivity.class));
        }
        else{
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
