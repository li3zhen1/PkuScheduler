package com.example.pkuscheduler.Fragments;

import android.icu.util.Calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pkuscheduler.Models.ScheduleJsonModel.ScheduleRootObject;
import com.example.pkuscheduler.R;

public class TodayScheduleViewModel extends ViewModel {
    private MutableLiveData<Integer> dayOfWeekToday;

    public void setDayOfWeekToday(Integer _dayOfWeekToday){
        if(dayOfWeekToday==null)dayOfWeekToday=new MutableLiveData<>();
        dayOfWeekToday.setValue(_dayOfWeekToday);
    }
    public void UpdateTodayScheduleViewModel(ScheduleRootObject scheduleRootObject){
        setDayOfWeekToday(
                Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        );
    }


}
