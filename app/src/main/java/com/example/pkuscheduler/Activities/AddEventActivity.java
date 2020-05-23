package com.example.pkuscheduler.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import java.util.Locale;

import ws.vinta.pangu.Pangu;

public class AddEventActivity extends AppCompatActivity {

    private DateTimePickerDialog dateTimePickerDialog;
    private String todoItemTitle;
    private String todoItemDesc;
    private Date todoItemDeadline;
    private Date todoItemReminderTime;
    private Intent mIntent;
    private EditText mEditText_title;
    private EditText mEditText_desc;
    private TextView mTextView_Deadline;
    private TextView mTextView_Reminder;



    private DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_event);
        setUpActionBar();
        AndroidThreeTen.init(this);
        mIntent = getIntent();
        mEditText_title = findViewById(R.id.new_event_title);
        mEditText_desc = findViewById(R.id.todo_description);
        mTextView_Deadline = findViewById(R.id.select_date_time);
        mTextView_Reminder = findViewById(R.id.select_reminder);
        mEditText_title.requestFocus();
        mEditText_title.requestFocusFromTouch();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText_title, InputMethodManager.SHOW_IMPLICIT);
    }


    public void onClick_SelectCalendaButton(int id){
        dateTimePickerDialog = new DateTimePickerDialog(
                this,
                DateTimePickerDialog.Mode.TIME_DATE,
                DateTimePickerDialog.DateRangeMode.NONE,
                ZonedDateTime.now(),
                Duration.ZERO
        );
        if(id==0){
            dateTimePickerDialog.setOnDateTimePickedListener(
                    new DateTimePickerDialog.OnDateTimePickedListener() {
                        @Override
                        public void onDateTimePicked(@NotNull ZonedDateTime zonedDateTime, @NotNull Duration duration) {
                            todoItemDeadline = new Date(zonedDateTime.toInstant().toEpochMilli());
                            mTextView_Deadline.setText(FormatDate(todoItemDeadline));
                        }
                    }
            );
        }
        else{
            dateTimePickerDialog.setOnDateTimePickedListener(
                    new DateTimePickerDialog.OnDateTimePickedListener() {
                        @Override
                        public void onDateTimePicked(@NotNull ZonedDateTime zonedDateTime, @NotNull Duration duration) {
                            todoItemReminderTime = new Date(zonedDateTime.toInstant().toEpochMilli());
                            mTextView_Reminder.setText(FormatDate(todoItemReminderTime));
                        }
                    }
            );
        }
        dateTimePickerDialog.show();
    }

    public void setUpActionBar(){
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("添加新的 Deadline");
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED,mIntent);
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

    public String FormatDate( Date dt){
        return  dateFormat.format(dt)+"  " +timeFormat.format(dt);
    }
    public void onClick_SelectDeadlineButton(View view) {
        Date dt = new Date();
        Date dt_today = new Date(dt.getYear(),dt.getMonth(),dt.getDate(),22,0);
        Date dt_tomorrow = new Date(dt.getYear(),dt.getMonth(),dt.getDate()+1,14,0);
        Date dt_totomorrow = new Date(dt.getYear(),dt.getMonth(),dt.getDate()+2,14,0);
        String str_today = getString(R.string.AddNewEvent_DateTime_Today)+"  ("+FormatDate(dt_today)+")";
        String str_tomorrow = getString(R.string.AddNewEvent_DateTime_Tomorrow)+"  ("+FormatDate(dt_tomorrow)+")";
        String str_totomorrow  = getString(R.string.AddNewEvent_DateTime_Weekend)+"  ("+FormatDate(dt_totomorrow)+")";
        ArrayList<PopupMenuItem> deadlinePopupMenuItems = new ArrayList<PopupMenuItem>(){
            {
                add(new PopupMenuItem(0, str_today,R.drawable.ic_today_24));
                add(new PopupMenuItem(1, str_tomorrow,R.drawable.ic_tomorrow_24));
                add(new PopupMenuItem(2, str_totomorrow,R.drawable.ic_next_week_24));
                add(new PopupMenuItem(3, getString(R.string.AddNewEvent_DateTime_SelectADate),R.drawable.ic_date_time_24));
            }
        };
        PopupMenuItem.OnClickListener onPopupMenuItemClickListener = new PopupMenuItem.OnClickListener(){
            @Override
            public void onPopupMenuItemClicked(@NotNull PopupMenuItem popupMenuItem) {
                switch (popupMenuItem.getId()){
                    case 0:
                        todoItemDeadline=dt_today;
                        mTextView_Deadline.setText(str_today);
                        break;
                    case 1:
                        todoItemDeadline=dt_tomorrow;
                        mTextView_Deadline.setText(str_tomorrow);
                        break;
                    case 2:
                        todoItemDeadline=dt_totomorrow;
                        mTextView_Deadline.setText(str_totomorrow);
                        break;
                    case 3:
                        onClick_SelectCalendaButton(0);
                        break;
                }
            }
        };
        showPopupMenu(findViewById(R.id.select_date_time), deadlinePopupMenuItems, PopupMenu.ItemCheckableBehavior.NONE, onPopupMenuItemClickListener);
    }

    public void onClick_SelectReminderButton(View view) {

        Date dt = new Date();
        Date dt_today = new Date(dt.getYear(),dt.getMonth(),dt.getDate(),22,0);
        Date dt_tomorrow = new Date(dt.getYear(),dt.getMonth(),dt.getDate()+1,14,0);
        Date dt_totomorrow = new Date(dt.getYear(),dt.getMonth(),dt.getDate()+2,14,0);
        String str_today = getString(R.string.AddNewEvent_Reminder_Today)+"  ("+FormatDate(dt_today)+")";
        String str_tomorrow = getString(R.string.AddNewEvent_Reminder_Tomorrow)+"  ("+FormatDate(dt_tomorrow)+")";
        String str_totomorrow  = getString(R.string.AddNewEvent_Reminder_ToTomorrow)+"  ("+FormatDate(dt_totomorrow)+")";

        ArrayList<PopupMenuItem> reminderPopupMenuItems = new ArrayList<PopupMenuItem>(){
            {
                add(new PopupMenuItem(0, str_today,R.drawable.ic_remind_today_24));
                add(new PopupMenuItem(1, str_tomorrow,R.drawable.ic_remind_tomorrow_24));
                add(new PopupMenuItem(2, str_totomorrow,R.drawable.ic_remind_next_week_24));
                add(new PopupMenuItem(3, getString(R.string.AddNewEvent_Reminder_SelectADate),R.drawable.ic_reminder_24));
            }
        };
        PopupMenuItem.OnClickListener onPopupMenuItemClickListener = new PopupMenuItem.OnClickListener(){
            @Override
            public void onPopupMenuItemClicked(@NotNull PopupMenuItem popupMenuItem) {
                switch (popupMenuItem.getId()){
                    case 0:
                        todoItemReminderTime=dt_today;
                        mTextView_Reminder.setText(str_today);
                        break;
                    case 1:
                        todoItemReminderTime=dt_tomorrow;
                        mTextView_Reminder.setText(str_tomorrow);
                        break;
                    case 2:
                        todoItemReminderTime=dt_totomorrow;
                        mTextView_Reminder.setText(str_totomorrow);
                        break;
                    case 3:
                        onClick_SelectCalendaButton(1);
                        break;
                }

            }
        };
        showPopupMenu(findViewById(R.id.select_reminder), reminderPopupMenuItems, PopupMenu.ItemCheckableBehavior.NONE, onPopupMenuItemClickListener);
    }

    public void FinishActivityWithNewlyCreatedItem(View view){
        todoItemTitle=mEditText_title.getText().toString();
        todoItemDesc = mEditText_desc.getText().toString();
        if(todoItemDeadline==null){
            Snackbar.make(findViewById(R.id.add_new_event_page_container),"请选择截止日期",Snackbar.LENGTH_LONG).show();
            return;
        }
        if(
                (todoItemTitle!=null&&todoItemTitle.length()>0)
        ){
            mIntent.putExtra("NEWSCHEDULE", JSON.toJSONString(
                    new ToDoItem(todoItemTitle,todoItemDeadline,todoItemDesc,todoItemReminderTime)
            ));
            setResult(RESULT_OK, mIntent);
            finish();
        }
        else{
            Snackbar.make(findViewById(R.id.add_new_event_page_container),"请填写事件名称",Snackbar.LENGTH_LONG).show();
        }
    }


}
