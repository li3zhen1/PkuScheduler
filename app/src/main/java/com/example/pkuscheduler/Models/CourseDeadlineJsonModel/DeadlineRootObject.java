package com.example.pkuscheduler.Models.CourseDeadlineJsonModel;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.Models.CourseLoginInfoModel;
import com.example.pkuscheduler.Utils.PkuCourse.ApiRepository;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static com.example.pkuscheduler.Utils.StringUtils.convertStreamToString;

public final class DeadlineRootObject {

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
            CourseLoginInfoModel courseLoginInfoModel,
            String startTimeStamp,
            String endTimeStamp) {
        try{
            HttpURLConnection conn;
            String request = ApiRepository.getDeadlinesUrl(startTimeStamp,endTimeStamp);
            URL url = new URL(request);
            System.out.println(request);
            conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Cookie", "JSESSIONID=" + courseLoginInfoModel.jSessionId
                    +"; session_id=" + courseLoginInfoModel.sessionId
                    +"; s_session_id=" + courseLoginInfoModel.sSessionId
                    +"; web_client_cache_guid=" + courseLoginInfoModel.guid);
            conn.setRequestMethod("GET");
            InputStream in = conn.getInputStream();
            return JSON.parseArray(convertStreamToString(in), DeadlineRootObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //cache, not implemented yet
    public static List<DeadlineRootObject> getInstanceFromStorage(){
        return null;
    }
}
