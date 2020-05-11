package com.example.pkuscheduler;


import android.util.Log;

import com.example.pkuscheduler.Interfaces.ISchedulable;
import com.example.pkuscheduler.Models.CourseDeadlineJsonModel.CourseRawToDoItemsRootObject;
import com.example.pkuscheduler.Models.CourseLoginInfoModel;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Coursetableroom;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Jsap;
import com.example.pkuscheduler.Utils.PkuCourse.ApiRepository;
import com.example.pkuscheduler.Utils.PkuCourse.PkuCourseInformationClient;
import com.example.pkuscheduler.Utils.PkuCourse.PkuCourseLoginClient;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.*;
import com.example.pkuscheduler.Models.ScheduleJsonModel.ScheduleRootObject;

import static com.example.pkuscheduler.Utils.PkuCourse.PkuCourseSubmissionStatusClient.fetchSubmissionStatus;
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

    @Test
    public void reg(){
        String regex = "\\（.*\\）";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("算法设计与分析（研讨性）");
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    public class A{
        public int a;
        A(int x){
            a=x;
        }
        public void seta(int x){
            a=x;
        }
    }
    public class B extends A{
        public int b;
        B(int x,int y){
            super(y);
            b=x;
        }
    }
    public class C extends A{
        public int c;
        C(int x,int y){
            super(y);
            c=x;
        }

    }
    @Test
    public void test2(){
        List<A> a= new ArrayList<A>();
        a.add(new A(2));
        a.get(0).seta(3);
        System.out.println(a.get(0).a);
    }

    @Test
    public void SubT() throws Exception{
        System.out.println(PkuCourseInformationClient.fetchSubmissionStatus("BZ1819104031651128499"));
    }

}