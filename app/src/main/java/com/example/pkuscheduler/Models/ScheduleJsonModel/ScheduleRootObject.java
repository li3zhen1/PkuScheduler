package com.example.pkuscheduler.Models.ScheduleJsonModel;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.Utils.PkuHelper.ApiRepository;

import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.pkuscheduler.Utils.StringUtils.convertStreamToString;
import static com.example.pkuscheduler.Utils.StringUtils.getUnicodeEscaped;

public final class ScheduleRootObject{
    public int code;
    public String msg;
    public String uid;
    public String user_token;
    public Coursetable[] courseTable;
    public Coursetableroom[] courseTableRoom;


    //
    public static ScheduleRootObject getInstanceFromWebApi(String helperToken){
        if(helperToken==null)
            throw new AssertionError();
        try{
            HttpURLConnection conn = null;
            URL url = new URL(ApiRepository.getPKUHelperScheduleUrl(helperToken));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            String jsonResponse = convertStreamToString(conn.getInputStream());
            ScheduleRootObject scheduleRootObject = JSON.parseObject(getUnicodeEscaped(jsonResponse), ScheduleRootObject.class);
            if (scheduleRootObject.msg == "ok")
                throw new AssertionError();
            return scheduleRootObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //cache, not implemented yet
    public static ScheduleRootObject getInstanceFromStorage(){
        return null;
    }


}
