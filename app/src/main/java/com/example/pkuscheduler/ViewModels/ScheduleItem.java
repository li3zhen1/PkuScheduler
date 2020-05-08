package com.example.pkuscheduler.ViewModels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pkuscheduler.Models.CourseDeadlineJsonModel.DeadlineRootObject;
import com.example.pkuscheduler.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import static android.provider.Settings.System.getString;

public class ScheduleItem implements Serializable {
    @NonNull
    private String ScheduleTitle;


    private Date ScheduleDeadline;
    private ArrayList<String> ScheduleTags;


    private boolean isFromCourse;
    private String ScheduleCourseSource;


    private boolean HasReminder;
    private ArrayList<Date> ScheduleReminderTimeList;


    //自定义
    public ScheduleItem(@NonNull String scheduleTitle, @Nullable Date scheduleDeadline,
                        @Nullable ArrayList<String> scheduleDescription, @Nullable ArrayList<Date> scheduleReminderTimeList) {
        ScheduleTitle = scheduleTitle;
        ScheduleDeadline = scheduleDeadline;
        ScheduleTags = scheduleDescription;
        isFromCourse = false;
        ScheduleCourseSource = null;
        if(scheduleReminderTimeList==null){
            HasReminder = false;
            ScheduleReminderTimeList = null;
        }
        else{
            HasReminder = true;
            ScheduleReminderTimeList = scheduleReminderTimeList;
        }
    }


    //从教学网
    public ScheduleItem(@NonNull DeadlineRootObject deadlineRootObject,@Nullable ArrayList<Date> scheduleReminderTimeList){
        ScheduleTitle = deadlineRootObject.title;
        ScheduleDeadline = deadlineRootObject.end;
        ScheduleTags = new ArrayList<String>();
        ScheduleTags.add(deadlineRootObject.eventType);
        isFromCourse = true;
        ScheduleCourseSource = deadlineRootObject.calendarName;
        if(scheduleReminderTimeList==null){
            HasReminder = false;
            ScheduleReminderTimeList = null;
        }
        else{
            HasReminder = true;
            ScheduleReminderTimeList = scheduleReminderTimeList;
        }
    }


    public void setFromCourse(boolean fromCourse) {
        isFromCourse = fromCourse;
    }

    public void setHasReminder(boolean hasReminder) {
        HasReminder = hasReminder;
    }

    public void setScheduleCourseSource(String scheduleCourseSource) {
        ScheduleCourseSource = scheduleCourseSource;
    }

    public void setScheduleDeadline(Date scheduleDeadline) {
        ScheduleDeadline = scheduleDeadline;
    }

    public void setScheduleReminderTimeList(ArrayList<Date> scheduleReminderTimeList) {
        ScheduleReminderTimeList = scheduleReminderTimeList;
    }

    public void setScheduleTags(ArrayList<String> scheduleTags) {
        ScheduleTags = scheduleTags;
    }

    public void setScheduleTitle(@NonNull String scheduleTitle) {
        ScheduleTitle = scheduleTitle;
    }

    public ArrayList<Date> getScheduleReminderTimeList() {
        return ScheduleReminderTimeList;
    }

    public ArrayList<String> getScheduleTags() {
        return ScheduleTags;
    }

    public Date getScheduleDeadline() {
        return ScheduleDeadline;
    }

    public String getScheduleCourseSource() {
        return ScheduleCourseSource;
    }

    @NonNull
    public String getScheduleTitle() {
        return ScheduleTitle;
    }

}
