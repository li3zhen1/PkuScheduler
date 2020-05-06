package com.example.pkuscheduler;

import com.example.pkuscheduler.Utils.PkuHelper.ApiRepository;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.*;
import com.example.pkuscheduler.Utils.PkuHelper.ScheduleJsonStructure.ScheduleRootObject;

import static com.example.pkuscheduler.Utils.StringHelper.convertStreamToString;
import static com.example.pkuscheduler.Utils.StringHelper.getUnicodeEscaped;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        try{
            fetchPKUHelperSchedule();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(4, 2 + 2);
    }
    public void fetchPKUHelperSchedule() throws IOException {
        HttpURLConnection conn = null;
        String pkuLik = ApiRepository.getPKUHelperScheduleUrl("");
        URL url = new URL(pkuLik);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream in = conn.getInputStream();
        String str = convertStreamToString(in);
        System.out.println(str);
        System.out.println(getUnicodeEscaped(str));
        ScheduleRootObject u = JSON.parseObject(getUnicodeEscaped(str) ,ScheduleRootObject.class);
    }
}