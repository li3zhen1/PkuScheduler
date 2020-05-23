package com.example.pkuscheduler.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.Fragments.ScheduleListFragment;
import com.example.pkuscheduler.R;
import com.example.pkuscheduler.ViewModels.ToDoItem;

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


    public void CreateNewToDoItem(View view) {
        startActivityForResult(
                new Intent(this, AddEventActivity.class),
                1
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK){
            Toast.makeText(getApplicationContext(),data.getStringExtra("NEWSCHEDULE"),Toast.LENGTH_LONG).show();
            ScheduleListFrag.addTodoItem(JSON.parseObject(data.getStringExtra("NEWSCHEDULE"), ToDoItem.class));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
