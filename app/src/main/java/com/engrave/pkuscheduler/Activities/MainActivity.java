package com.engrave.pkuscheduler.Activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.alibaba.fastjson.JSON;
import com.engrave.pkuscheduler.Components.MainPagerAdapter;
import com.engrave.pkuscheduler.Components.NoScrollViewPager;
import com.engrave.pkuscheduler.Fragments.CourseListFragment;
import com.engrave.pkuscheduler.Fragments.ScheduleListFragment;
import com.engrave.pkuscheduler.Interfaces.IActionBarHeightGettable;
import com.engrave.pkuscheduler.Interfaces.IPageSwitcherAnimatable;
import com.engrave.pkuscheduler.R;
import com.engrave.pkuscheduler.Services.SetLongTermAlarmServices;
import com.engrave.pkuscheduler.Utils.UI.LengthConveter;
import com.engrave.pkuscheduler.ViewModels.ToDoItem;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements IActionBarHeightGettable, IPageSwitcherAnimatable {
    private ImageButton mButton;
    private TextView switcherText_ddl;
    private TextView switcherText_table;
    private View swicherButton;
    private View swicherBackground;

    private NoScrollViewPager mPager;
    private MainPagerAdapter mPagerAdapter;
    private LinearLayout mSyncingIndicatorConatiner;
    private String channelId= "CourceSync";
    private String channelName= "教学网同步";
    private String channelId_fg= "ForegroundService";
    private String channelName_fg= "提醒服务";
    private int importance= NotificationManager.IMPORTANCE_HIGH;
    private AlarmManager mAlarmManagerSingleShotIdleAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        swicherButton = findViewById(R.id.switcher_float_button);
        switcherText_ddl = findViewById(R.id.switcher_text_ddl);
        switcherText_table = findViewById(R.id.switcher_text_table);
        swicherBackground = findViewById(R.id.switcher_bakground);
        mPager = findViewById(R.id.main_activity_ViewPager);
        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mButton = findViewById(R.id.create_new_item_imagebutton);
        mSyncingIndicatorConatiner = findViewById(R.id.main_activity_sync_indicator);
        createNotificationChannel(channelId, channelName, importance);
        createNotificationChannel(channelId_fg, channelName_fg, NotificationManager.IMPORTANCE_DEFAULT);
        //setAlarmManagerForDeadlines();
        swicherButton.setOnTouchListener(new View.OnTouchListener() {
            float mPosX,mPosY,mCurPosX,mCurPosY;
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_UP:
                        if (mCurPosX - mPosX > 20&&mPager.getCurrentItem()==0) {
                            SwitchPage(null);
                        } else
                            if(mCurPosX - mPosX < -20&&mPager.getCurrentItem()==1){
                                SwitchPage(null);
                        }
                        break;
                }
                return true;
            }
        });
        try{
            startService(
                    new Intent(this, SetLongTermAlarmServices.class)
            );
        }
        catch (Exception e){

        }
    }



    public void RevealSyncingIndicator(boolean _isSyncing){
        if(mSyncingIndicatorConatiner==null)return;
        if(_isSyncing){
            mSyncingIndicatorConatiner.animate()
                .alpha(1f)
                .setDuration(300);
        }
        else{
            mSyncingIndicatorConatiner.animate()
                    .alpha(0f)
                    .setDuration(600);
        }
    }

    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
    }

    public void sendCourseSyncMsg(String NotificationTitle, String NotificationDesc,String DetailDesc) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "CourceSync")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(NotificationTitle)
                .setContentText(NotificationDesc)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_pkuscheduler_notification)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(DetailDesc))
                .setColor(0xff486df1)
                .setAutoCancel(true)
                .build();
        Objects.requireNonNull(manager).notify(2, notification);
    }

    public void SwitchPage(View view) {
        startSwitchAnimation(mPager.getCurrentItem());
        mPager.setCurrentItem(1-mPager.getCurrentItem(),true);
    }

    public void CreateNewToDoItem(View view) {
        startActivityForResult(
                new Intent(this, AddEventActivity.class),
                1
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK){
            //Toast.makeText(getApplicationContext(),data.getStringExtra("NEWSCHEDULE"),Toast.LENGTH_LONG).show();
            ((ScheduleListFragment)mPagerAdapter.getItem(0)).addTodoItem(JSON.parseObject(Objects.requireNonNull(data).getStringExtra("NEWSCHEDULE"), ToDoItem.class));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startSwitchAnimation(int id){
        if(id==0){
            ObjectAnimator animation = ObjectAnimator.ofFloat(swicherButton, "translationX", (float) LengthConveter.DpToPx(108,this));
            mButton.animate().alpha(0).setDuration(160).start();
            mButton.setClickable(false);
            switcherText_ddl.setTextColor(0xffffffff);
            switcherText_table.setTextColor(0xff757575);
            animation.setDuration(160).start();
        }
        else{
            ObjectAnimator animation = ObjectAnimator.ofFloat(swicherButton, "translationX", (float) LengthConveter.DpToPx(0,this));
            mButton.animate().alpha(1).setDuration(160).start();
            mButton.setClickable(true);
            switcherText_ddl.setTextColor(0xff757575);
            switcherText_table.setTextColor(0xffffffff);
            animation.setDuration(160).start();
        }
    }

    public int getActionBarHeight(){
        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
            return TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        return 144;
    }
    public void broadcastDatasetChanged(){
        if(mPagerAdapter!=null){
            if(mPagerAdapter.getItem(1) instanceof CourseListFragment){
                ((CourseListFragment)mPagerAdapter.getItem(1)).startUpdateViewAsyncTask();
            }
        }
    }
}
