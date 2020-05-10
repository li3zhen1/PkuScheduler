package com.example.pkuscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.pkuscheduler.R;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.microsoft.officeuifabric.datetimepicker.DateTimePickerDialog;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

public class AddEventActivity extends AppCompatActivity {

    private DateTimePickerDialog dateTimePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_event);
        setUpActionBar();
        AndroidThreeTen.init(this);
    }


    public void onClick_SelectCalendaButton(View view){
        dateTimePickerDialog = new DateTimePickerDialog(
                this,
                DateTimePickerDialog.Mode.DATE_TIME,
                DateTimePickerDialog.DateRangeMode.NONE,
                ZonedDateTime.now(),
                Duration.ZERO
        );
        dateTimePickerDialog.setOnDateTimePickedListener(
                (zonedDateTime, duration) -> dateTime = zonedDateTime
        );
        dateTimePickerDialog.show();
    }
    private ZonedDateTime dateTime;
    public void setDateTime(ZonedDateTime val){
        if(val==null)return;
        dateTime=val;
    }

    public void setUpActionBar(){
        /*getSupportActionBar().hide();
        getWindow().setStatusBarColor(0xffffffff);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);*/


        getSupportActionBar().hide();
        //getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(0x00ffffff);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
