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

public class ToDoItem implements Serializable {
    @NonNull
    private String ScheduleTitle;


    private Date ScheduleDeadline;
    private String ScheduleTag;


    private boolean isFromCourse;
    private String ScheduleCourseSource;


    private boolean HasReminder;
    private ArrayList<Date> ScheduleReminderTimeList;


    //自定义
    public ToDoItem(@NonNull String scheduleTitle, @Nullable Date scheduleDeadline,
                    @Nullable String scheduleDescription, @Nullable ArrayList<Date> scheduleReminderTimeList) {
        ScheduleTitle = scheduleTitle;
        ScheduleDeadline = scheduleDeadline;
        ScheduleTag = scheduleDescription;
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
    public ToDoItem(@NonNull DeadlineRootObject deadlineRootObject, @Nullable ArrayList<Date> scheduleReminderTimeList){
        ScheduleTitle = deadlineRootObject.title;
        ScheduleDeadline = deadlineRootObject.end;
        ScheduleTag = deadlineRootObject.eventType;
        isFromCourse = true;
        ScheduleCourseSource = deadlineRootObject.calendarName.substring(0,deadlineRootObject.calendarName.length()-13)
                .replace("（","(").replace("）",")");
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

    public void setScheduleTag(String scheduleTag) {
        ScheduleTag = scheduleTag;
    }

    public void setScheduleTitle(@NonNull String scheduleTitle) {
        ScheduleTitle = scheduleTitle;
    }

    public ArrayList<Date> getScheduleReminderTimeList() {
        return ScheduleReminderTimeList;
    }

    public String getScheduleTag() {
        return ScheduleTag;
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
