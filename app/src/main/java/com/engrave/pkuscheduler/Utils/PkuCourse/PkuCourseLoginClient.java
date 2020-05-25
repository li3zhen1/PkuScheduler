package com.engrave.pkuscheduler.Utils.PkuCourse;

import com.alibaba.fastjson.JSON;
import com.engrave.pkuscheduler.Models.CourseLoginInfoModel;
import com.engrave.pkuscheduler.Utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static com.engrave.pkuscheduler.Utils.StringUtils.betweenStrings;
import static com.engrave.pkuscheduler.Utils.StringUtils.convertStreamToString;


//获取访问 pkucourse 需要的4个cookie
public final class PkuCourseLoginClient {
    private CourseLoginInfoModel courseLoginInfoModel;

    private final static String iaaaTokenBaseUrl = "https://iaaa.pku.edu.cn/iaaa/oauthlogin.do";
    private final static String oathValidateBaseUrl = "https://course.pku.edu.cn/webapps/bb-sso-bb_bb60/execute/authValidate/campusLogin?_rand=0.28873561615480403&token=";
    private  static String FramesetBaseUrl = "https://course.pku.edu.cn/webapps/portal/frameset.jsp";

    public PkuCourseLoginClient(String loginInfoStudentId, String loginInfoPassword){
        courseLoginInfoModel =new CourseLoginInfoModel(loginInfoStudentId,loginInfoPassword);
    }

    //获取iaaa的token
    public Boolean FetchIaaaToken() throws IOException {
        String urlParameters = iaaaTokenBaseUrl+ "?appid=blackboard&userName=" +
                URLEncoder.encode(courseLoginInfoModel.studentId, "UTF-8") +
                "&password=" + URLEncoder.encode(courseLoginInfoModel.password, "UTF-8")
                + "&randCode=&smsCode=&otpCode=&redirUrl=https%3A%2F%2Fcourse.pku.edu.cn%2Fwebapps%2Fbb-sso-bb_bb60%2Fexecute%2FauthValidate%2FcampusLogin";

        HttpURLConnection conn = null;
        URL url = new URL(urlParameters);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Cookie", "remember=true");
        conn.setRequestProperty("Cookie", "username=" + courseLoginInfoModel.studentId);
        InputStream in = conn.getInputStream();
        String tokenResult = convertStreamToString(in);
        conn.disconnect();
        System.out.println(tokenResult);
        if (tokenResult.contains("\"success\":true")) {
            courseLoginInfoModel.iaaaToken = StringUtils.betweenStrings(tokenResult, "\"token\":\"", "\"}");
            return true;
        }
        else
            return false;
    }

    //获取教学网的Cookie
    public Boolean FetchCourseCookies_Portals() throws IOException {
        HttpURLConnection conn = null;
        URL url = new URL("https://course.pku.edu.cn/webapps/login");
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        System.out.println(JSON.toJSONString(headerFields));
        List<String> cookiesHeader = headerFields.get("Set-Cookie");
        conn.disconnect();
        if (cookiesHeader != null)
            for (String cookie : cookiesHeader){
                System.out.println(cookie);
                if (cookie.contains("session_id=")&&!cookie.contains("s_session_id=")){
                    courseLoginInfoModel.sessionId = betweenStrings(cookie, "session_id=", "; Path=/;");
                }
                if (cookie.contains("s_session_id=")){
                    courseLoginInfoModel.sSessionId = betweenStrings(cookie, "s_session_id=", "; Path=/;");
                }
                if (cookie.contains("web_client_cache_guid=")){
                    courseLoginInfoModel.guid = betweenStrings(cookie, "web_client_cache_guid=", "; Path=/;");
                }
                if (cookie.contains("JSESSIONID=")){
                    courseLoginInfoModel.jSessionId_Portal = betweenStrings(cookie.toString(), "JSESSIONID=", "; Path=/");
                }
            }
        if (courseLoginInfoModel.sSessionId == null|| courseLoginInfoModel.guid==null|| courseLoginInfoModel.sessionId==null
                || courseLoginInfoModel.sSessionId.length()==0|| courseLoginInfoModel.guid.length()==0|| courseLoginInfoModel.sessionId.length()==0)
            return false;
        return true;
    }

    //验证
    public Boolean OathValidate() throws IOException {
        if(courseLoginInfoModel.iaaaToken==null)
            return null;
        HttpURLConnection conn = null;
        URL url = new URL(oathValidateBaseUrl + courseLoginInfoModel.iaaaToken);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Cookie", "JSESSIONID=" + courseLoginInfoModel.jSessionId_Portal
                +"; s_session_id=" + courseLoginInfoModel.sSessionId
                +"; session_id=" + courseLoginInfoModel.sessionId
                +"; web_client_cache_guid=" + courseLoginInfoModel.guid);
        conn.setRequestProperty("Referer","https://iaaa.pku.edu.cn/iaaa/oauth.jsp");
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        conn.disconnect();
        if (courseLoginInfoModel.sSessionId == null|| courseLoginInfoModel.guid==null|| courseLoginInfoModel.sessionId==null
                || courseLoginInfoModel.sSessionId.length()==0|| courseLoginInfoModel.guid.length()==0|| courseLoginInfoModel.sessionId.length()==0)
            return false;
        return true;
    }

    //获取jsession
    public Boolean FetchJSessionId_FrameSet() throws IOException{
        HttpURLConnection conn = null;
        URL url = new URL(FramesetBaseUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("Cookie", "JSESSIONID=" + courseLoginInfoModel.jSessionId_Portal
                +"; s_session_id=" + courseLoginInfoModel.sSessionId
                +"; session_id=" + courseLoginInfoModel.sessionId
                +"; web_client_cache_guid=" + courseLoginInfoModel.guid);
        Map<String, List<String>> jSession_headerFields = conn.getHeaderFields();
        List<String> jSession_cookiesHeader = jSession_headerFields.get("Set-Cookie");
        courseLoginInfoModel.jSessionId_Frameset = betweenStrings(jSession_cookiesHeader.toString(), "JSESSIONID=", "; Path=/");
        conn.disconnect();
        if(courseLoginInfoModel.jSessionId_Frameset ==null|| courseLoginInfoModel.jSessionId_Frameset.length()<=1)
            return false;
        return true;
    }

    public CourseLoginInfoModel GetLoginInfo(){
        return courseLoginInfoModel;
    }
}
