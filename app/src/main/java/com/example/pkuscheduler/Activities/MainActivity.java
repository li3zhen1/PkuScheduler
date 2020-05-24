package com.example.pkuscheduler.Activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.Fragments.ScheduleListFragment;
import com.example.pkuscheduler.R;
import com.example.pkuscheduler.Receiver.AlarmReceiver;
import com.example.pkuscheduler.ViewModels.ToDoItem;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    private ScheduleListFragment ScheduleListFrag;
    private LinearLayout mSyncingIndicatorConatiner;
    private String channelId= "CourceSync";
    private String channelName= "教学网同步";
    private int importance= NotificationManager.IMPORTANCE_HIGH;
    private AlarmManager mAlarmManagerSingleShotIdleAvailable;
    private AlarmManager mAlarmManagerRepeated;
    public static final String INTENT_ALARM_LOG = "intent_alarm_log";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        ScheduleListFrag  = (ScheduleListFragment) fragmentManager.findFragmentById(R.id.schedule_list_fragment);
        mSyncingIndicatorConatiner = findViewById(R.id.main_activity_sync_indicator);
        createNotificationChannel(channelId, channelName, importance);
        setAlarmManagerForDeadlines();
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
            Toast.makeText(getApplicationContext(), Objects.requireNonNull(data).getStringExtra("NEWSCHEDULE"),Toast.LENGTH_LONG).show();
            ScheduleListFrag.addTodoItem(JSON.parseObject(data.getStringExtra("NEWSCHEDULE"), ToDoItem.class));
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    public void setAlarmManagerForDeadlines(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        mAlarmManagerSingleShotIdleAvailable = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
        mAlarmManagerSingleShotIdleAvailable.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi);

    }
}
