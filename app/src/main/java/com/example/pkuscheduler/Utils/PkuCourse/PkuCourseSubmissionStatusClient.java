package com.example.pkuscheduler.Utils.PkuCourse;

import com.example.pkuscheduler.Models.CourseLoginInfoModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.pkuscheduler.Utils.StringUtils.convertStreamToString;

public final class PkuCourseSubmissionStatusClient {
    public final static boolean fetchSubmissionStatus(String ObjectIdentifier, CourseLoginInfoModel courseLoginInfoModel) throws IOException {
        HttpURLConnection conn;
        String request = ApiRepository.getSubmisstionStatusUrl(ObjectIdentifier);
        URL url = new URL(request);
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Cookie", "JSESSIONID=" + courseLoginInfoModel.jSessionId
                +"; session_id=" + courseLoginInfoModel.sessionId
                +"; s_session_id=" + courseLoginInfoModel.sSessionId
                +"; web_client_cache_guid=" + courseLoginInfoModel.guid);
        conn.setRequestMethod("GET");
        InputStream in = conn.getInputStream();
        return convertStreamToString(in).contains("复查提交历史记录");
    }
}
