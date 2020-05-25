package com.engrave.pkuscheduler.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.engrave.pkuscheduler.Activities.MainActivity;

public class DataChangedReceiver extends BroadcastReceiver {
    private MainActivity mMainActivity;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(context instanceof MainActivity){
            mMainActivity = (MainActivity)context;
            Log.e("BroadCast","Received");
        }
    }
}
