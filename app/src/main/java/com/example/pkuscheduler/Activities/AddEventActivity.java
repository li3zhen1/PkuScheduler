package com.example.pkuscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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
        setContentView(R.layout.activity_add_event);
        AndroidThreeTen.init(this);
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
    }


    public void onClick_SelectCalendaButton(View view){
        dateTimePickerDialog.show();
    }
    private ZonedDateTime dateTime;
    public void setDateTime(ZonedDateTime val){
        if(val==null)return;
        dateTime=val;
    }
}
