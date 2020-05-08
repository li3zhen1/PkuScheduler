package com.example.pkuscheduler;


import com.example.pkuscheduler.Models.ScheduleJsonModel.CourseTimeRepository;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Coursetableroom;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Jsap;
import com.example.pkuscheduler.Utils.PkuCourse.PkuCourseLoginClient;
import com.example.pkuscheduler.Utils.PkuHelper.ApiRepository;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

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
    public void ScheduleRootObjectTest() {
        ScheduleRootObject scheduleRootObject = null;//ScheduleRootObject.getInstanceFromWebApi("");
        System.out.println(JSON.toJSONString(scheduleRootObject.courseTableRoom));
        assertEquals("ok",scheduleRootObject.msg);
    }
    @Test
    public void esc(){
        System.out.println(getUnicodeEscaped("{\"code\":1,\"msg\":\"ISOP\\u4e0a\\u6e38\\u6570\\u636e\\u9519\\u8bef (2-b)\"}"));
    }

    @Test
    public void test(){
        ScheduleRootObject scheduleRootObject = null;//ScheduleRootObject.getInstanceFromWebApi("");
        String dayOfWeek = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);
        for(Coursetableroom coursetableroom: scheduleRootObject.courseTableRoom){
            for(Jsap jsap:coursetableroom.jsap){
                if(jsap.xq.equals(dayOfWeek)){
                    System.out.println(coursetableroom.kcmc);
                }
            }
        }
    }


}