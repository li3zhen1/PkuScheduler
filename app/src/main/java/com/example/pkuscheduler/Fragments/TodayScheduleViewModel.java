package com.example.pkuscheduler.Fragments;

import android.icu.util.Calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pkuscheduler.Models.ScheduleJsonModel.ScheduleRootObject;
import com.example.pkuscheduler.R;

public class TodayScheduleViewModel extends ViewModel {
    private MutableLiveData<Integer> dayOfWeekToday;
    private MutableLiveData<String> a;

    public void UpdateTodayScheduleViewModel(ScheduleRootObject scheduleRootObject){
        dayOfWeekToday.setValue(
                Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        );
        a.setValue(scheduleRootObject.user_token);
    }


}
