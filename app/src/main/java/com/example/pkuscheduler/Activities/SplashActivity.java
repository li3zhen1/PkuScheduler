package com.example.pkuscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.pkuscheduler.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();


        // TODO：sp 判断 Helper 登陆状态
        if(getSharedPreferences("loginInfo", Context.MODE_PRIVATE).getBoolean("isLogged",false)){
            startActivity(new Intent(this, TodayActivity.class));
        }
        else{
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
