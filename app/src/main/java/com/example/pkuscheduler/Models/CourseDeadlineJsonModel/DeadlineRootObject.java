package com.example.pkuscheduler.Models.CourseDeadlineJsonModel;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.pkuscheduler.Models.CourseLoginInfoModel;
import com.example.pkuscheduler.Utils.PkuCourse.ApiRepository;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static com.example.pkuscheduler.Utils.StringUtils.convertStreamToString;

public final class DeadlineRootObject {
    public final static String storagePath ="DeadlineRootObjectCache.json";

    public Boolean allDay;
    public Boolean gradable;
    public String itemSourceType;
    public String itemSourceId;
    public Boolean userCreated;
    public Boolean repeat;
    public String calendarId;
    public Boolean recur;
    public String calendarName;
    public String color;
    public Boolean editable;
    public Calendarnamelocalizable calendarNameLocalizable;
    public Boolean attemptable;
    public Boolean isDateRangeLimited;
    public Boolean disableResizing;
    public Boolean isUltraEvent;
    public String id;
    public Date start;
    public Date end;
    public Object location;
    public String title;
    public Date startDate;
    public Date endDate;
    public String eventType;

    public static List<DeadlineRootObject> getInstanceFromWebApi(
            Context context,
            CourseLoginInfoModel courseLoginInfoModel,
            String startTimeStamp,
            String endTimeStamp)
            throws Exception
    {
        List<DeadlineRootObject> deadlineRootObjects = null;
        HttpURLConnection conn;
        String request = ApiRepository.getDeadlinesUrl(startTimeStamp,endTimeStamp);
        URL url = new URL(request);
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("Cookie", "JSESSIONID=" + courseLoginInfoModel.jSessionId
                +"; session_id=" + courseLoginInfoModel.sessionId
                +"; s_session_id=" + courseLoginInfoModel.sSessionId
                +"; web_client_cache_guid=" + courseLoginInfoModel.guid);
        conn.setRequestMethod("GET");
        InputStream in = conn.getInputStream();
        deadlineRootObjects = JSON.parseArray(convertStreamToString(in), DeadlineRootObject.class);


        saveListInstance(deadlineRootObjects,context);
        return deadlineRootObjects;
    }


    //cache
    public static List<DeadlineRootObject> getInstanceFromStorage(Context context) throws IOException {
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        List<DeadlineRootObject> deadlineRootObjects = null;
        try {
            fileInputStream = context.openFileInput(storagePath);
            StringBuilder builder = new StringBuilder();
            String line;
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            deadlineRootObjects = JSON.parseArray(builder.toString(), DeadlineRootObject.class);
        } catch (FileNotFoundException fnfe) {
            throw new IOException();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }

        }
        return deadlineRootObjects;
    }

    public static void saveListInstance(List<DeadlineRootObject> deadlineRootObjects, Context context) throws JSONException, IOException {
        FileOutputStream fileOutputStream;
        OutputStreamWriter outputStreamWriter;
        fileOutputStream = context.openFileOutput(storagePath, Context.MODE_PRIVATE);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(JSON.toJSONString(deadlineRootObjects));
        outputStreamWriter.close();
        fileOutputStream.close();
    }


    //TODO:Probably updated online, try update.
    public static List<DeadlineRootObject> getInstance(
            Context context,
            CourseLoginInfoModel courseLoginInfoModel,
            String startTimeStamp,
            String endTimeStamp){
        List<DeadlineRootObject> deadlineRootObjects = null;
        boolean isStorageValid = true;
        try{
            deadlineRootObjects = getInstanceFromStorage(context);
            if(deadlineRootObjects==null) isStorageValid=false;
        } catch (IOException e) {
            isStorageValid=false;
            //TODO:alert
        }
        if(!isStorageValid){
            try{
                deadlineRootObjects = getInstanceFromWebApi(
                        context,
                        courseLoginInfoModel,
                        startTimeStamp,
                        endTimeStamp);
            } catch (Exception e) {
                //TODO:alert
            }
        }
        Log.e("FROM OBJ",JSON.toJSONString(deadlineRootObjects));
        return deadlineRootObjects;
    }
}
