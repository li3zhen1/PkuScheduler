package com.example.pkuscheduler.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.pkuscheduler.Models.ScheduleJsonModel.ScheduleRootObject;
import com.example.pkuscheduler.R;

public class TodayScheduleFragment extends Fragment {

    private TodayScheduleViewModel mViewModel;
    private ScheduleRootObject scheduleRootObject;

    public static TodayScheduleFragment newInstance() {
        return new TodayScheduleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_schedule_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TodayScheduleViewModel.class);
        scheduleRootObject = ScheduleRootObject.getInstanceFromWebApi(
                getActivity()
                        .getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
                        .getString("pkuHelperToken",null)
        );
        mViewModel.UpdateTodayScheduleViewModel(scheduleRootObject);
    }

}
