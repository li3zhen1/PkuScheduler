package com.example.pkuscheduler.utilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

import static com.example.pkuscheduler.utilities.StringHelper.betweenStrings;
import static com.example.pkuscheduler.utilities.StringHelper.convertStreamToString;

public class PkuHelperClient {
    private static String getUrl(String studentId){
        int apiVer = (int) (2*Math.floor(new Date().getTime()/72e5));
        return "https://pkuhelper.pku.edu.cn/api_xmcp/login/send_code?user="+studentId+"&code_type=sms&PKUHelperAPI=3.0&jsapiver=200326204124-"+apiVer;
    }
    private static String getTokenUrl(String studentId, String validCode){
        int apiVer = (int) (2*Math.floor(new Date().getTime()/72e5));
        return "https://pkuhelper.pku.edu.cn/api_xmcp/login/login?user="+studentId+"&valid_code="+validCode+"&PKUHelperAPI=3.0&jsapiver=200326204124-"+apiVer;
    }
    private static Boolean isResponseSuccess(String _response){
        return _response.contains("\"success\":true");
    }
    private static Boolean isTokenResponseSuccess(String _response){
        return _response.contains("\"code\":0,\"msg\":\"ok\"");
    }
    public static Boolean AskForPin(String studentId) throws IOException {
        HttpURLConnection conn = null;
        URL url = new URL(getUrl(studentId));
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setUseCaches(false);//设置不要缓存
        conn.setInstanceFollowRedirects(true);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        try{
        conn.connect();
        //POST请求
        OutputStreamWriter out = new OutputStreamWriter(
                conn.getOutputStream());
        out.write("{\"excluded_scopes\":[]}");
        out.flush();
        InputStream in = conn.getInputStream();
        String str = convertStreamToString(in);
        return isResponseSuccess(str);
        } catch (IOException e) {
            return false;
        }
    }

    public static String FetchToken(String studentId,String validCode) throws IOException {
        HttpURLConnection conn = null;
        System.out.println(getTokenUrl(studentId,validCode));
        URL url = new URL(getTokenUrl(studentId,validCode));
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setUseCaches(false);//设置不要缓存
        conn.setInstanceFollowRedirects(true);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        try{
            conn.connect();
            //POST请求
            OutputStreamWriter out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write("{\"excluded_scopes\":[]}");
            out.flush();
            InputStream in = conn.getInputStream();
            String str = convertStreamToString(in);
            System.out.println(str);
            return str;
        } catch (IOException e) {
            return  null;
        }
    }
}
