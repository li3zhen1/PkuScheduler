package com.example.pkuscheduler;

import com.example.pkuscheduler.Utils.PkuCourse.PkuCourseLoginClient;
import com.example.pkuscheduler.Utils.PkuHelper.ApiRepository;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        ScheduleRootObject scheduleRootObject = ScheduleRootObject.getInstanceFromWebApi("");
        assertEquals("ok",scheduleRootObject.msg);
    }

}