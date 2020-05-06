package com.example.pkuscheduler;

import com.example.pkuscheduler.Models.CourseDeadlineJsonModel.DeadlineInstanceObject;
import com.example.pkuscheduler.Utils.PkuCourse.DeadlineInfoClient;
import com.example.pkuscheduler.Utils.PkuCourse.PkuCourseLoginClient;
import com.example.pkuscheduler.Utils.PkuHelper.ApiRepository;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.*;
import com.example.pkuscheduler.Models.ScheduleJsonModel.ScheduleRootObject;

import static com.example.pkuscheduler.Utils.StringUtils.convertStreamToString;
import static com.example.pkuscheduler.Utils.StringUtils.getUnicodeEscaped;

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
            System.out.println(fetchDdl());
            List<DeadlineInstanceObject> userList = JSON.parseArray(fetchDdl(), DeadlineInstanceObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(4, 2 + 2);
    }
    public ScheduleRootObject fetchPKUHelperSchedule(){
        try{
        HttpURLConnection conn = null;
        String pkuLik = ApiRepository.getPKUHelperScheduleUrl("");
        URL url = new URL(pkuLik);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream in = conn.getInputStream();
        String str = convertStreamToString(in);
        return JSON.parseObject(getUnicodeEscaped(str), ScheduleRootObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String fetchDdl() throws IOException {
        PkuCourseLoginClient loginClient = new PkuCourseLoginClient("","");
        loginClient.FetchIaaaToken();
        loginClient.FetchCookies();
        loginClient.FetchJSessionId();
        String s = DeadlineInfoClient.FetchDeadlineInfo(
                loginClient.GetLoginInfo(),
                Long.toString(new Date(2020-1900,1-1,1).getTime()),
                Long.toString(new Date().getTime())
        );

        return s;
    }
}