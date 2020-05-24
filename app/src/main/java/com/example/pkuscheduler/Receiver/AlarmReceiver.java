package com.example.pkuscheduler.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.pkuscheduler.R;
import com.example.pkuscheduler.ViewModels.ToDoItem;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private String channelId= "CourceSync";
    private String channelName= "教学网同步";
    private int importance= NotificationManager.IMPORTANCE_HIGH;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            List<ToDoItem> toDoItems = ToDoItem.getListInstanceFromStorage(context);
            int cnt=0;
            String dtl = "";
            for(ToDoItem td:toDoItems){
                if(!td.getIsDone()){
                    cnt++;
                    if(cnt<5)
                        dtl+=td.getScheduleTitle()+"、";
                }
            }
            if(cnt>0)
                sendCourseSyncMsg(context,"查看近期的 Deadline","还有 "+cnt+" 项待完成",
                        "还有 "+dtl.substring(0,dtl.length()-1)+" 等 "+cnt+" 项待完成。"

                        );
        } catch (IOException e) {
            sendCourseSyncMsg(context,"查看近期的 Deadline","","");
            e.printStackTrace();
        }
    }
    public void sendCourseSyncMsg(Context context, String NotificationTitle, String NotificationDesc,String DetailDesc) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context, "CourceSync")
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
        Objects.requireNonNull(manager).notify(1, notification);
    }
}
