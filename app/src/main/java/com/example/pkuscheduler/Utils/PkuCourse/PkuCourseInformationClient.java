package com.example.pkuscheduler.Utils.PkuCourse;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.pkuscheduler.Utils.StringUtils.convertStreamToString;

//输入   ScheduleRootObject.Coursetableroom.zxjhbh   的课程号获取信息
//例如 https://elective.pku.edu.cn/elective2008/edu/pku/stu/elective/controller/courseDetail/getCourseDetail.do?kclx=BK&course_seq_no=BZ1819104031651128499
//TODO: 正则匹配/XML解析出 课程的各个字段
public class PkuCourseInformationClient {
    public final static String fetchSubmissionStatus(String courseId_zxjhbh) throws IOException {
        HttpURLConnection conn;
        String request = ApiRepository.getCourseInfomationUrl(courseId_zxjhbh);
        System.out.println(request);
        URL url = new URL(request);
        conn = (HttpURLConnection) url.openConnection();
        InputStream in = conn.getInputStream();
        return convertStreamToString(in);
    }
}
