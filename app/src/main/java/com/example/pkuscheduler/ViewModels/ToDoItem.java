package com.example.pkuscheduler.ViewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.pkuscheduler.Interfaces.ISchedulable;
import com.example.pkuscheduler.Models.CourseDeadlineJsonModel.CourseRawToDoItemsRootObject;
import com.example.pkuscheduler.Models.CourseLoginInfoModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.pkuscheduler.Utils.PkuCourse.PkuCourseSubmissionStatusClient.fetchSubmissionStatus;

public class ToDoItem implements ISchedulable {
    @NonNull
    private String ScheduleTitle;

    private static final String userDefinedStoragePath = "ToDoItemList.json";
    private static final String courseStoragePath = "CourseToDoItemList.json";
    private Date EndTime;
    private String ScheduleDescription;


    private boolean isFromCourse;
    private String ScheduleCourseSource;
    public String CourseObjectIdentifier;


    private boolean HasReminder;
    private Date ScheduleReminderTime;
    private boolean isDone;

    public ToDoItem(){}
    //自定义
    public ToDoItem(@NonNull String scheduleTitle, @Nullable Date endTime,
                    @Nullable String scheduleDescription, @Nullable Date scheduleReminderTime) {
        ScheduleTitle = scheduleTitle;
        EndTime = endTime;
        ScheduleDescription = scheduleDescription;
        isFromCourse = false;
        isDone=false;
        ScheduleCourseSource = null;
        if(scheduleReminderTime==null){
            HasReminder = false;
            ScheduleReminderTime = null;
        }
        else{
            HasReminder = true;
            ScheduleReminderTime = scheduleReminderTime;
        }
    }

    //从教学网
    public ToDoItem(@NonNull CourseRawToDoItemsRootObject courseRawToDoItemsRootObject, @Nullable Date scheduleReminderTime){
        ScheduleTitle = courseRawToDoItemsRootObject.title;
        EndTime = courseRawToDoItemsRootObject.end;
        ScheduleDescription = courseRawToDoItemsRootObject.eventType;
        isFromCourse = true;
        CourseObjectIdentifier = courseRawToDoItemsRootObject.id;
        isDone = false;
        ScheduleCourseSource = courseRawToDoItemsRootObject.calendarName.substring(0, courseRawToDoItemsRootObject.calendarName.length()-13)
                .replace("（","(").replace("）",")");
        if(scheduleReminderTime==null){
            HasReminder = false;
            ScheduleReminderTime = null;
        }
        else{
            HasReminder = true;
            ScheduleReminderTime = scheduleReminderTime;
        }
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean done) {
        isDone = done;
    }

    public boolean getFromCourse() {
        return isFromCourse;
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

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public void setScheduleReminderTimeList(Date scheduleReminderTime) {
        ScheduleReminderTime = scheduleReminderTime;
    }


    public void setScheduleDescription(String scheduleDescription) {
        ScheduleDescription = scheduleDescription;
    }

    public void setScheduleTitle(@NonNull String scheduleTitle) {
        ScheduleTitle = scheduleTitle;
    }

    public Date getScheduleReminderTimeList() {
        return ScheduleReminderTime;
    }

    public String getScheduleDescription() {
        return ScheduleDescription;
    }

    @Override
    public Date getEndTime() {
        return EndTime;
    }

    public String getScheduleCourseSource() {
        return ScheduleCourseSource;
    }

    @NonNull
    public String getScheduleTitle() {
        return ScheduleTitle;
    }


    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }

        if(obj == null){
            return false;
        }

        if(obj instanceof ToDoItem){
            ToDoItem other = (ToDoItem) obj;
            if(isFromCourse!=((ToDoItem) obj).isFromCourse){
                return false;
            }
            if(isFromCourse&&((ToDoItem) obj).isFromCourse){
                //System.out.println(CourseObjectIdentifier+"\n"+((ToDoItem) obj).CourseObjectIdentifier+"\n");
                return CourseObjectIdentifier.equals(((ToDoItem) obj).CourseObjectIdentifier);
            }


            if(this.getEndTime() == other.getEndTime()
            && this.getScheduleTitle()==other.getScheduleTitle()
            && this.getScheduleDescription()==other.getScheduleDescription()){
                return true;
            }
        }
        return false;
    }

    public Boolean getSubmissionStatus(CourseLoginInfoModel courseLoginInfoModel){
        if(!isFromCourse)return true;
        Boolean result =null;
        try{
            fetchSubmissionStatus(this.CourseObjectIdentifier, courseLoginInfoModel);
        }catch(Exception e){
        }
        return result;
    }


    public static void saveListInstance(List<ToDoItem> partialToDoItems, Context context, boolean isFromCourse) throws JSONException, IOException {
        FileOutputStream fileOutputStream;
        OutputStreamWriter outputStreamWriter;
        fileOutputStream = context.openFileOutput(isFromCourse? courseStoragePath: userDefinedStoragePath, Context.MODE_PRIVATE);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(JSON.toJSONString(partialToDoItems));
        outputStreamWriter.close();
        fileOutputStream.close();
    }
    public static void saveListInstance(List<ToDoItem> allToDoItems, Context context) throws JSONException, IOException {
        saveListInstance(allToDoItems.stream().filter(
                (itm)->itm.getFromCourse()
        ).collect(Collectors.toList()), context,true);
        saveListInstance(allToDoItems.stream().filter(
                (itm)->!itm.getFromCourse()
        ).collect(Collectors.toList()), context,false);
    }

    public static List<ToDoItem> getListInstanceFromStorage(Context context, boolean isFromCourse) throws IOException {
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        List<ToDoItem> toDoItems = null;
        try {
            fileInputStream = context.openFileInput(isFromCourse? courseStoragePath: userDefinedStoragePath);
            StringBuilder builder = new StringBuilder();
            String line;
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            toDoItems = JSON.parseArray(builder.toString(), ToDoItem.class);
        } catch (FileNotFoundException fnfe) {
            throw new IOException();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }

        }
        return toDoItems;
    }

    public static List<ToDoItem> getListInstanceFromStorage(Context context) throws IOException {
        List<ToDoItem> lt = getListInstanceFromStorage(context, false);
        lt.addAll(getListInstanceFromStorage(context, true));
        return lt;
    }
}
