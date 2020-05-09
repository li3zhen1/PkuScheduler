package com.example.pkuscheduler.Utils.PkuCourse;

public final class ApiRepository {

    public final static String getDeadlinesUrl(String startTimeStamp, String endTimeStamp) {
        return "https://course.pku.edu.cn/webapps/calendar/calendarData/selectedCalendarEvents?start=" + startTimeStamp
                + "&end=" + endTimeStamp
                + "&course_id=&mode=personal";
    }
    public final static String getSubmisstionStatusUrl(String ObjectIdentifier){
        return "https://course.pku.edu.cn/webapps/calendar/launch/attempt/"+ObjectIdentifier;
    }
}
