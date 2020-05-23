package com.example.pkuscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.pkuscheduler.R;
import com.example.pkuscheduler.ViewModels.ToDoItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.microsoft.officeuifabric.datetimepicker.DateTimePickerDialog;
import com.microsoft.officeuifabric.popupmenu.PopupMenu;
import com.microsoft.officeuifabric.popupmenu.PopupMenuItem;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ws.vinta.pangu.Pangu;

public class AddEventActivity extends AppCompatActivity {

    private DateTimePickerDialog dateTimePickerDialog;
    private ToDoItem newlyCreatedToDoItem = new ToDoItem();


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
    private void showPopupMenu(
            View anchorView,
            ArrayList<PopupMenuItem> items ,
            PopupMenu.ItemCheckableBehavior itemCheckableBehavior,
            PopupMenuItem.OnClickListener onItemClickListener
    ) {
        PopupMenu popupMenu = new PopupMenu(this, anchorView, items, itemCheckableBehavior);
        popupMenu.setOnItemClickListener(onItemClickListener);
        popupMenu.show();
    }
    public void onClick_SelectDeadlineButton(View view) {
        Date dt = new Date();
        ArrayList<PopupMenuItem> deadlinePopupMenuItems = new ArrayList<PopupMenuItem>(){
            {
                add(new PopupMenuItem(0, getString(R.string.AddNewEvent_DateTime_Today),R.drawable.ic_today_24));
                add(new PopupMenuItem(1, getString(R.string.AddNewEvent_DateTime_Tomorrow),R.drawable.ic_tomorrow_24));
                add(new PopupMenuItem(2, getString(R.string.AddNewEvent_DateTime_Weekend),R.drawable.ic_next_week_24));
                add(new PopupMenuItem(3, getString(R.string.AddNewEvent_DateTime_SelectADate),R.drawable.ic_date_time_24));
            }
        };
        PopupMenuItem.OnClickListener onPopupMenuItemClickListener = new PopupMenuItem.OnClickListener(){
            @Override
            public void onPopupMenuItemClicked(@NotNull PopupMenuItem popupMenuItem) {
                switch (popupMenuItem.getId()){
                    case 0:
                        newlyCreatedToDoItem.setEndTime(
                                new Date(dt.getYear(),dt.getMonth(),dt.getDate(),22,0)
                        );
                        break;
                    case 1:
                        newlyCreatedToDoItem.setEndTime(
                                new Date(dt.getYear(),dt.getMonth(),dt.getDate()+1,12,0)
                        );
                        break;
                    case 2:
                        newlyCreatedToDoItem.setEndTime(
                                new Date(dt.getYear(),dt.getMonth(),dt.getDate()+2,12,0)
                        );
                        break;
                    case 3:
                        onClick_SelectCalendaButton(null);
                        break;
                }
            }
        };
        showPopupMenu(findViewById(R.id.select_date_time), deadlinePopupMenuItems, PopupMenu.ItemCheckableBehavior.NONE, onPopupMenuItemClickListener);
    }

    public void onClick_SelectReminderButton(View view) {
        ArrayList<PopupMenuItem> reminderPopupMenuItems = new ArrayList<PopupMenuItem>(){
            {
                add(new PopupMenuItem(0, getString(R.string.AddNewEvent_Reminder_Today),R.drawable.ic_remind_today_24));
                add(new PopupMenuItem(1, getString(R.string.AddNewEvent_Reminder_Tomorrow),R.drawable.ic_remind_tomorrow_24));
                add(new PopupMenuItem(2, getString(R.string.AddNewEvent_Reminder_ToTomorrow),R.drawable.ic_remind_next_week_24));
                add(new PopupMenuItem(3, getString(R.string.AddNewEvent_Reminder_SelectADate),R.drawable.ic_reminder_24));
            }
        };
        Date dt = new Date();
        PopupMenuItem.OnClickListener onPopupMenuItemClickListener = new PopupMenuItem.OnClickListener(){
            @Override
            public void onPopupMenuItemClicked(@NotNull PopupMenuItem popupMenuItem) {
                newlyCreatedToDoItem.setHasReminder(true);
                switch (popupMenuItem.getId()){
                    case 0:

                        newlyCreatedToDoItem.setEndTime(
                                new Date(dt.getYear(),dt.getMonth(),dt.getDate(),22,0)
                        );
                        break;
                    case 1:
                        newlyCreatedToDoItem.setEndTime(
                                new Date(dt.getYear(),dt.getMonth(),dt.getDate()+1,12,0)
                        );
                        break;
                    case 2:
                        newlyCreatedToDoItem.setEndTime(
                                new Date(dt.getYear(),dt.getMonth(),dt.getDate()+2,12,0)
                        );
                        break;
                    case 3:
                        onClick_SelectCalendaButton(null);
                        break;
                }
            }
        };
        showPopupMenu(findViewById(R.id.select_reminder), reminderPopupMenuItems, PopupMenu.ItemCheckableBehavior.NONE, onPopupMenuItemClickListener);
    }

}
