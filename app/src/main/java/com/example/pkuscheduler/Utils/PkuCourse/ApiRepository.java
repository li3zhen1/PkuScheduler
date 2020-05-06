package com.example.pkuscheduler.Utils.PkuCourse;

public class ApiRepository {

    static String getDeadlinesUrl(String startTimeStamp, String endTimeStamp) {
        return "https://course.pku.edu.cn/webapps/calendar/calendarData/selectedCalendarEvents?start=" + startTimeStamp
                + "&end=" + endTimeStamp
                + "&course_id=&mode=personal";
    }
}
