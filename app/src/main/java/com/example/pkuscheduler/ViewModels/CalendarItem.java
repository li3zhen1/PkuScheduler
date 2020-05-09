package com.example.pkuscheduler.ViewModels;

import com.example.pkuscheduler.Interfaces.ISchedulable;

import java.util.Date;

public class CalendarItem implements ISchedulable {
    private String ScheduleTitle;
    private static final String storagePath = "CalendarItemList.json";
    private Date EndTime;
    private Date StartTime;
    private String ScheduleTag;


    public Date getStartTime(){
        return StartTime;
    }
    @Override
    public Date getEndTime() {
        return EndTime;
    }

    @Override
    public String getScheduleTitle() {
        return ScheduleTitle;
    }
}
