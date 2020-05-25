package com.engrave.pkuscheduler.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.engrave.pkuscheduler.Receiver.AlarmReceiver;

import java.util.Calendar;

public class SetLongTermAlarmServices extends Service {
    private AlarmManager mAlarmManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setAlarmManagerForDeadlines();
/*        Notification notification = new NotificationCompat.Builder(this, "ForegroundService")
                .setContentTitle("PKU Scheduler 正在后台运行")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_pkuscheduler_notification)

                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_pkuscheduler_notification)
                .setColor(0xff486df1)
                .build();*/
        //startForeground(23,notification);
    }

    public void setAlarmManagerForDeadlines(){

        SharedPreferences sp = this.getSharedPreferences("loginInfo", this.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, sp.getInt("PushNotificationHour",21));
        calendar.set(Calendar.MINUTE,sp.getInt("PushNotificationHour",30));
        calendar.set(Calendar.SECOND,0);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi);
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this,"Destroyed",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
