package com.example.pkuscheduler.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.pkuscheduler.Fragments.ScheduleListFragment;
import com.example.pkuscheduler.R;

public class TodayActivity extends AppCompatActivity{
    FragmentManager fragmentManager = getSupportFragmentManager();
    private ScheduleListFragment ScheduleListFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_today);
        setUpActionBar();
        ScheduleListFrag  = (ScheduleListFragment) fragmentManager.findFragmentById(R.id.schedule_list_fragment);
    }

    public void setUpActionBar(){
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(0xffffffff);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

}
