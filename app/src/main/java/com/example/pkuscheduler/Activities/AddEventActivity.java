package com.example.pkuscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.pkuscheduler.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.microsoft.officeuifabric.datetimepicker.DateTimePickerDialog;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import ws.vinta.pangu.Pangu;

public class AddEventActivity extends AppCompatActivity {

    private DateTimePickerDialog dateTimePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Create a new event");
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
