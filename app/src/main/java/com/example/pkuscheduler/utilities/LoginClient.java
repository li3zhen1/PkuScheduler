package com.example.pkuscheduler.utilities;

import android.widget.Toast;

import com.example.pkuscheduler.data.LoginInfoRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static com.example.pkuscheduler.utilities.StringHelper.betweenStrings;
import static com.example.pkuscheduler.utilities.StringHelper.convertStreamToString;

public class LoginClient {
    private LoginInfoRepository loginInfoRepository;

    private static String iaaaTokenBaseUrl = "https://iaaa.pku.edu.cn/iaaa/oauthlogin.do";
    private static String cookieBaseUrl = "https://course.pku.edu.cn/webapps/bb-sso-bb_bb60/execute/authValidate/campusLogin?_rand=0.5&token=";
    private static String jSessionIdBaseUrl = "https://course.pku.edu.cn/webapps/portal/frameset.jsp";

    public LoginClient(String loginInfoStudentId, String loginInfoPassword){
        loginInfoRepository=new LoginInfoRepository(loginInfoStudentId,loginInfoPassword);
    }

    public Boolean FetchIaaaToken() throws IOException {
        String urlParameters = "appid=blackboard&userName=" +
                URLEncoder.encode(loginInfoRepository.studentId, "UTF-8") +
                "&password=" + URLEncoder.encode(loginInfoRepository.password, "UTF-8")
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
            loginInfoRepository.iaaaToken = StringHelper.betweenStrings(tokenResult, "\"token\":\"", "\"}");
            return true;
        }
        else
            return false;
    }

    public Boolean FetchCookies() throws IOException {
        if(loginInfoRepository.iaaaToken==null)
            return null;
        HttpURLConnection conn = null;
        URL url = new URL(cookieBaseUrl+loginInfoRepository.iaaaToken);
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        List<String> cookiesHeader = headerFields.get("Set-Cookie");
        conn.disconnect();
        if (cookiesHeader != null)
            for (String cookie : cookiesHeader){
                if (cookie.contains("session_id=")){
                    loginInfoRepository.sessionId = betweenStrings(cookie, "session_id=", "; Path=/;");}
                if (cookie.contains("s_session_id=")){
                    loginInfoRepository.sSessionId = betweenStrings(cookie, "s_session_id=", "; Path=/;");}
                if (cookie.contains("web_client_cache_guid=")){
                    loginInfoRepository.guid = betweenStrings(cookie, "web_client_cache_guid=", "; Path=/;");
                }}
        if (loginInfoRepository.sSessionId == null||loginInfoRepository.guid==null||loginInfoRepository.sessionId==null
                ||loginInfoRepository.sSessionId.length()==0||loginInfoRepository.guid.length()==0||loginInfoRepository.sessionId.length()==0)
            return false;
        return true;
    }

    public Boolean FetchJSessionId() throws IOException{
        HttpURLConnection conn = null;
        URL url = new URL(jSessionIdBaseUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("Cookie", "session_id=" + loginInfoRepository.sessionId);
        conn.setRequestProperty("Cookie", "s_session_id=" + loginInfoRepository.sSessionId);
        conn.setRequestProperty("Cookie", "web_client_cache_guid=" + loginInfoRepository.guid);
        Map<String, List<String>> jSession_headerFields = conn.getHeaderFields();
        List<String> jSession_cookiesHeader = jSession_headerFields.get("Set-Cookie");
        loginInfoRepository.jSessionId = betweenStrings(jSession_cookiesHeader.toString(), "JSESSIONID=", "; Path=/");
        conn.disconnect();
        System.out.println(jSession_cookiesHeader);
        if(loginInfoRepository.jSessionId==null||loginInfoRepository.jSessionId.length()<=1)
            return false;
        return true;
    }

    public LoginInfoRepository GetLoginInfo(){
        return loginInfoRepository;
    }
}
