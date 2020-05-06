package com.example.pkuscheduler.Utils;

import com.example.pkuscheduler.Models.CourseLoginInfoModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static com.example.pkuscheduler.Utils.StringHelper.betweenStrings;
import static com.example.pkuscheduler.Utils.StringHelper.convertStreamToString;

public class CourseLoginClient {
    private CourseLoginInfoModel courseLoginInfoModel;

    private static String iaaaTokenBaseUrl = "https://iaaa.pku.edu.cn/iaaa/oauthlogin.do";
    private static String cookieBaseUrl = "https://course.pku.edu.cn/webapps/bb-sso-bb_bb60/execute/authValidate/campusLogin?_rand=0.5&token=";
    private static String jSessionIdBaseUrl = "https://course.pku.edu.cn/webapps/portal/frameset.jsp";

    public CourseLoginClient(String loginInfoStudentId, String loginInfoPassword){
        courseLoginInfoModel =new CourseLoginInfoModel(loginInfoStudentId,loginInfoPassword);
    }

    public Boolean FetchIaaaToken() throws IOException {
        String urlParameters = "appid=blackboard&userName=" +
                URLEncoder.encode(courseLoginInfoModel.studentId, "UTF-8") +
                "&password=" + URLEncoder.encode(courseLoginInfoModel.password, "UTF-8")
                + "&randCode=&smsCode=&otpCode=&redirUrl=https%3A%2F%2Fcourse.pku.edu.cn%2Fwebapps%2Fbb-sso-bb_bb60%2Fexecute%2FauthValidate%2FcampusLogin";

        HttpURLConnection conn = null;
        byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
        URL url = new URL(iaaaTokenBaseUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.getOutputStream().write(postData);
        InputStream in = conn.getInputStream();
        String tokenResult = convertStreamToString(in);
        conn.disconnect();
        if (tokenResult.contains("\"success\":true")) {
            courseLoginInfoModel.iaaaToken = StringHelper.betweenStrings(tokenResult, "\"token\":\"", "\"}");
            return true;
        }
        else
            return false;
    }

    public Boolean FetchCookies() throws IOException {
        if(courseLoginInfoModel.iaaaToken==null)
            return null;
        HttpURLConnection conn = null;
        URL url = new URL(cookieBaseUrl+ courseLoginInfoModel.iaaaToken);
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        List<String> cookiesHeader = headerFields.get("Set-Cookie");
        conn.disconnect();
        if (cookiesHeader != null)
            for (String cookie : cookiesHeader){
                if (cookie.contains("session_id=")){
                    courseLoginInfoModel.sessionId = betweenStrings(cookie, "session_id=", "; Path=/;");}
                if (cookie.contains("s_session_id=")){
                    courseLoginInfoModel.sSessionId = betweenStrings(cookie, "s_session_id=", "; Path=/;");}
                if (cookie.contains("web_client_cache_guid=")){
                    courseLoginInfoModel.guid = betweenStrings(cookie, "web_client_cache_guid=", "; Path=/;");
                }}
        if (courseLoginInfoModel.sSessionId == null|| courseLoginInfoModel.guid==null|| courseLoginInfoModel.sessionId==null
                || courseLoginInfoModel.sSessionId.length()==0|| courseLoginInfoModel.guid.length()==0|| courseLoginInfoModel.sessionId.length()==0)
            return false;
        return true;
    }

    public Boolean FetchJSessionId() throws IOException{
        HttpURLConnection conn = null;
        URL url = new URL(jSessionIdBaseUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("Cookie", "session_id=" + courseLoginInfoModel.sessionId);
        conn.setRequestProperty("Cookie", "s_session_id=" + courseLoginInfoModel.sSessionId);
        conn.setRequestProperty("Cookie", "web_client_cache_guid=" + courseLoginInfoModel.guid);
        Map<String, List<String>> jSession_headerFields = conn.getHeaderFields();
        List<String> jSession_cookiesHeader = jSession_headerFields.get("Set-Cookie");
        courseLoginInfoModel.jSessionId = betweenStrings(jSession_cookiesHeader.toString(), "JSESSIONID=", "; Path=/");
        conn.disconnect();
        System.out.println(jSession_cookiesHeader);
        if(courseLoginInfoModel.jSessionId==null|| courseLoginInfoModel.jSessionId.length()<=1)
            return false;
        return true;
    }

    public CourseLoginInfoModel GetLoginInfo(){
        return courseLoginInfoModel;
    }
}
