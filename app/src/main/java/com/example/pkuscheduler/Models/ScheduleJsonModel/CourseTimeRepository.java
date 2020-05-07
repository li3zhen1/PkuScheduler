package com.example.pkuscheduler.Models.ScheduleJsonModel;

public class CourseTimeRepository {
    static public float[] StartTimeMinute={
            480,
            540,
            610,
            670,
            780,
            840,
            910,
            970,
            1030,
            1120,
            1180,
            1240,
    };
    static public float getStartTimeMinute(int nthClass){
        return StartTimeMinute[nthClass-1];
    }
    static public float getEndTimeMinute(int nthClass){
        return StartTimeMinute[nthClass-1]+50;
    }
    static public float getStartTimeMinute(String nthClass){
        return StartTimeMinute[Integer.valueOf(nthClass)-1];
    }
    static public float getEndTimeMinute(String nthClass){
        return StartTimeMinute[Integer.valueOf(nthClass)-1]+50;
    }
    static public String[] StartTime={
            "8:00",
            "9:00",
            "10:10",
            "11:10",
            "13:00",
            "14:00",
            "15:10",
            "16:10",
            "17:10",
            "18:40",
            "19:40",
            "20:40"
    };
    static public String[] EndTime={
            "8:50",
            "9:50",
            "11:00",
            "12:00",
            "13:50",
            "14:50",
            "16:00",
            "17:00",
            "18:00",
            "19:30",
            "20:30",
            "21:30"
    };
}
