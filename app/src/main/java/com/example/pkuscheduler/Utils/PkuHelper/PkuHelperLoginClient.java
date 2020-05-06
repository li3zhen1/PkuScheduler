package com.example.pkuscheduler.Utils.PkuHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.pkuscheduler.Utils.StringUtils.convertStreamToString;

public class PkuHelperLoginClient {
    private static Boolean isResponseSuccess(String _response){
        return _response.contains("\"success\":true");
    }
    private static Boolean isTokenResponseSuccess(String _response){
        return _response.contains("\"code\":0,\"msg\":\"ok\"");
    }
    public static Boolean AskForPin(String studentId) throws IOException {
        HttpURLConnection conn = null;
        URL url = new URL(ApiRepository.getUrl(studentId));
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
        System.out.println(ApiRepository.getTokenUrl(studentId,validCode));
        URL url = new URL(ApiRepository.getTokenUrl(studentId,validCode));
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
