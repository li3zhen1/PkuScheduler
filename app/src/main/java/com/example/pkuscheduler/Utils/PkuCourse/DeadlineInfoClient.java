package com.example.pkuscheduler.Utils.PkuCourse;

import com.example.pkuscheduler.Models.CourseLoginInfoModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.pkuscheduler.Utils.StringUtils.convertStreamToString;

public class DeadlineInfoClient {

    public static String FetchDeadlineInfo(CourseLoginInfoModel courseLoginInfoModel, String startTimeStamp, String endTimeStamp) throws IOException {
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
        return convertStreamToString(in);
    }
}
