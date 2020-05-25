package com.engrave.pkuscheduler.Models.CourseDeadlineJsonModel;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.engrave.pkuscheduler.Models.CourseLoginInfoModel;
import com.engrave.pkuscheduler.Utils.PkuCourse.ApiRepository;

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

import static com.engrave.pkuscheduler.Utils.StringUtils.convertStreamToString;

public final class CourseRawToDoItemsRootObject {
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

    public static List<CourseRawToDoItemsRootObject> getInstanceFromWebApi(
            Context context,
            String startTimeStamp,
            String endTimeStamp) throws Exception
    {
        CourseLoginInfoModel courseLoginInfoModel = CourseLoginInfoModel.getCookie(context);
        List<CourseRawToDoItemsRootObject> courseRawToDoItemsRootObjects = null;
        HttpURLConnection conn;
        String request = ApiRepository.getDeadlinesUrl(startTimeStamp,endTimeStamp);
        URL url = new URL(request);
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Cookie", "JSESSIONID=" + courseLoginInfoModel.jSessionId_Portal
                +"; session_id=" + courseLoginInfoModel.sessionId
                +"; s_session_id=" + courseLoginInfoModel.sSessionId
                +"; web_client_cache_guid=" + courseLoginInfoModel.guid);
        InputStream in = conn.getInputStream();
        courseRawToDoItemsRootObjects = JSON.parseArray(convertStreamToString(in), CourseRawToDoItemsRootObject.class);

        Log.e("@@@@@@!!!",JSON.toJSONString(courseRawToDoItemsRootObjects));

        saveListInstance(courseRawToDoItemsRootObjects,context);
        Log.e("@@@@@@","");

        return courseRawToDoItemsRootObjects;
    }


    //cache
    public static List<CourseRawToDoItemsRootObject> getInstanceFromStorage(Context context) throws IOException {
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        List<CourseRawToDoItemsRootObject> courseRawToDoItemsRootObjects = null;
        try {
            fileInputStream = context.openFileInput(storagePath);
            StringBuilder builder = new StringBuilder();
            String line;
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            courseRawToDoItemsRootObjects = JSON.parseArray(builder.toString(), CourseRawToDoItemsRootObject.class);
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
        return courseRawToDoItemsRootObjects;
    }

    public static void saveListInstance(List<CourseRawToDoItemsRootObject> courseRawToDoItemsRootObjects, Context context) throws JSONException, IOException {
        FileOutputStream fileOutputStream;
        OutputStreamWriter outputStreamWriter;
        fileOutputStream = context.openFileOutput(storagePath, Context.MODE_PRIVATE);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(JSON.toJSONString(courseRawToDoItemsRootObjects));
        outputStreamWriter.close();
        fileOutputStream.close();
        //Log.e("CourseRaw","Saved");
    }


    //TODO:Probably updated online, try update.
    public static List<CourseRawToDoItemsRootObject> getInstance(
            Context context,
            CourseLoginInfoModel courseLoginInfoModel,
            String startTimeStamp,
            String endTimeStamp){
        List<CourseRawToDoItemsRootObject> courseRawToDoItemsRootObjects = null;
        boolean isStorageValid = true;
        try{
            courseRawToDoItemsRootObjects = getInstanceFromStorage(context);
            if(courseRawToDoItemsRootObjects ==null) isStorageValid=false;
        } catch (IOException e) {
            isStorageValid=false;
            //TODO:alert
        }
        if(!isStorageValid){
            try{
                courseRawToDoItemsRootObjects = getInstanceFromWebApi(
                        context,
                        startTimeStamp,
                        endTimeStamp);
            } catch (Exception e) {
                //TODO:alert
            }
        }
        Log.e("FROM OBJ",JSON.toJSONString(courseRawToDoItemsRootObjects));
        return courseRawToDoItemsRootObjects;
    }
}
