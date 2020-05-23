package com.example.pkuscheduler;


import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.Models.CourseDeadlineJsonModel.CourseRawToDoItemsRootObject;
import com.example.pkuscheduler.Models.CourseLoginInfoModel;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Coursetableroom;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Jsap;
import com.example.pkuscheduler.Models.ScheduleJsonModel.ScheduleRootObject;
import com.example.pkuscheduler.Utils.PkuCourse.ApiRepository;
import com.example.pkuscheduler.Utils.PkuCourse.PkuCourseInformationClient;
import com.example.pkuscheduler.Utils.PkuCourse.PkuCourseLoginClient;
import com.example.pkuscheduler.Utils.PkuCourse.PkuCourseSubmissionStatusClient;

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

import static com.example.pkuscheduler.Utils.StringUtils.convertStreamToString;
import static com.example.pkuscheduler.Utils.StringUtils.getUnicodeEscaped;
import static org.junit.Assert.assertEquals;

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

    @Test
    public void TestPkuCourse() throws IOException {
        PkuCourseLoginClient pkuCourseLoginClient = new PkuCourseLoginClient("1800013025","");
        pkuCourseLoginClient.FetchCourseCookies_Portals();
        pkuCourseLoginClient.FetchIaaaToken();
        pkuCourseLoginClient.OathValidate();
        pkuCourseLoginClient.FetchJSessionId_FrameSet();
        CourseLoginInfoModel courseLoginInfoModel = pkuCourseLoginClient.GetLoginInfo();

/*        URL taburl = new URL("https://course.pku.edu.cn/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_3_1");
        HttpURLConnection conn3;
        conn3 =(HttpURLConnection)taburl.openConnection();
        conn3.setRequestMethod("GET");
        conn3.setRequestProperty("Referer","https://iaaa.pku.edu.cn/iaaa/oauth.jsp");
        conn3.setRequestProperty("Cookie", "JSESSIONID=" + courseLoginInfoModel.jSessionId_Frameset
                +"; JSESSIONID=" + courseLoginInfoModel.jSessionId_Portal
                +"; s_session_id="+ courseLoginInfoModel.sSessionId
                +"; session_id=" + courseLoginInfoModel.sessionId
                +"; web_client_cache_guid=" + courseLoginInfoModel.guid);
        Map<String, List<String>> headerFields = conn3.getHeaderFields();
        System.out.println("\n\nConn3\n"+JSON.toJSONString(headerFields));
        InputStream in = conn3.getInputStream();
        System.out.println(convertStreamToString(in));*/


/*
        String url1 = "https://course.pku.edu.cn/webapps/calendar/viewMyBb?globalNavigation=false";
        HttpURLConnection conn2;
        URL url__ = new URL(url1);
        conn2 =(HttpURLConnection)url__.openConnection();
        conn2.setRequestMethod("GET");

        conn2.setRequestProperty("Referer","https://course.pku.edu.cn/webapps/bb-social-learning-bb_bb60/execute/mybb?cmd=display&toolId=calendar-mybb_____calendar-tool");

        conn2.setRequestProperty("Sec-Fetch-Dest","iframe");
        conn2.setRequestProperty("Sec-Fetch-Mode","navigate");
        conn2.setRequestProperty("Sec-Fetch-Site","same-origin");
        conn2.setRequestProperty("Upgrade-Insecure-Requests","1");

        conn2.setRequestProperty("Cookie", "JSESSIONID=" + courseLoginInfoModel.mainPageJSessionId
                +"; s_session_id=" + courseLoginInfoModel.sSessionId
                +"; session_id=" + courseLoginInfoModel.sessionId
                +"; web_client_cache_guid=" + courseLoginInfoModel.guid);
        Map<String, List<String>> headerFields = conn2.getHeaderFields();
        System.out.println(JSON.toJSONString(headerFields));
        String newJSid = betweenStrings(headerFields.get("Set-Cookie").toString(),"JSESSIONID=", "; Path=");
        System.out.println(newJSid);*/


        List<CourseRawToDoItemsRootObject> courseRawToDoItemsRootObjects = null;
        HttpURLConnection conn;
        String request = ApiRepository.getDeadlinesUrl("1587830400000","1591459200000");
        URL url = new URL(request);
        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Cookie",
                "JSESSIONID=" + courseLoginInfoModel.jSessionId_Portal
                +"; s_session_id="+ courseLoginInfoModel.sSessionId
                +"; session_id=" + courseLoginInfoModel.sessionId
                +"; web_client_cache_guid=" + courseLoginInfoModel.guid);
        InputStream in2 = conn.getInputStream();
        System.out.println(convertStreamToString(in2));
        courseRawToDoItemsRootObjects = JSON.parseArray(convertStreamToString(in2), CourseRawToDoItemsRootObject.class);
    }



    @Test
    public void todoItemParseTest(){
        String str = "[{\"allDay\":false,\"gradable\":false,\"userCreated\":false,\"repeat\":false,\"itemSourceType\":\"blackboard.platform.gradebook2.GradableItem\",\"itemSourceId\":\"_142996_1\",\"calendarId\":\"048-00131480-0006175098-1\",\"recur\":false,\"calendarName\":\"概率统计 （A）(19-20学年第2学期)\",\"color\":\"#ba0665\",\"editable\":false,\"calendarNameLocalizable\":{\"rawValue\":\"概率统计 （A）(19-20学年第2学期)\"},\"disableResizing\":true,\"attemptable\":true,\"isDateRangeLimited\":false,\"isUltraEvent\":false,\"id\":\"_blackboard.platform.gradebook2.GradableItem-_142996_1\",\"start\":\"2020-05-24T23:59:00\",\"end\":\"2020-05-24T23:59:00\",\"startDate\":\"2020-05-24T15:59:00.000Z\",\"eventType\":\"作业\",\"location\":null,\"title\":\"Homework_18(Lecture_PS10_2)\",\"endDate\":\"2020-05-24T15:59:00.000Z\"},{\"allDay\":false,\"gradable\":false,\"userCreated\":false,\"repeat\":false,\"itemSourceType\":\"blackboard.platform.gradebook2.GradableItem\",\"itemSourceId\":\"_143244_1\",\"calendarId\":\"048-04832580-0006174068-1\",\"recur\":false,\"calendarName\":\"算法设计与分析（研讨型小班）(19-20学年第2学期)\",\"color\":\"#de1934\",\"editable\":false,\"calendarNameLocalizable\":{\"rawValue\":\"算法设计与分析（研讨型小班）(19-20学年第2学期)\"},\"disableResizing\":true,\"attemptable\":true,\"isDateRangeLimited\":false,\"isUltraEvent\":false,\"id\":\"_blackboard.platform.gradebook2.GradableItem-_143244_1\",\"start\":\"2020-05-22T15:05:00\",\"end\":\"2020-05-22T15:05:00\",\"startDate\":\"2020-05-22T07:05:00.000Z\",\"eventType\":\"作业\",\"location\":null,\"title\":\"期中考试\",\"endDate\":\"2020-05-22T07:05:00.000Z\"}]";
        List<CourseRawToDoItemsRootObject> lc = JSON.parseArray(str,CourseRawToDoItemsRootObject.class);
        System.out.println(JSON.toJSONString(lc.get(0)));
    }

    @Test
    public void testHw() throws IOException {
        PkuCourseLoginClient pkuCourseLoginClient = new PkuCourseLoginClient("1800013025","19991005lee");
        pkuCourseLoginClient.FetchCourseCookies_Portals();
        pkuCourseLoginClient.FetchIaaaToken();
        pkuCourseLoginClient.OathValidate();
        CourseLoginInfoModel courseLoginInfoModel = pkuCourseLoginClient.GetLoginInfo();
        boolean bl =PkuCourseSubmissionStatusClient.fetchSubmissionStatus("_blackboard.platform.gradebook2.GradableItem-_142996_1",courseLoginInfoModel);
        if(bl)
            System.out.println("H");
    }
}