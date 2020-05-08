package com.example.pkuscheduler.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.Models.CourseLoginInfoModel;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Coursetableroom;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Jsap;
import com.example.pkuscheduler.Models.ScheduleJsonModel.ScheduleRootObject;
import com.example.pkuscheduler.R;

import com.example.pkuscheduler.Models.ScheduleJsonModel.CourseTimeRepository;
import com.example.pkuscheduler.Utils.PkuCourse.PkuCourseLoginClient;
import com.example.pkuscheduler.Views.ScheduleCourseGrid;

public class TodayScheduleFragment extends Fragment {

    private TodayScheduleViewModel mViewModel;
    private ScheduleRootObject scheduleRootObject;
    private Calendar calendar;
    private View parentFragment;
    private ConstraintLayout layoutContainer;
    private Typeface Inter;
    FetchCourseInfo scheduleRootObjectTask;

    public static TodayScheduleFragment newInstance() {
        return new TodayScheduleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        parentFragment = inflater.inflate(R.layout.today_schedule_fragment, container, false);
        layoutContainer = parentFragment.findViewById(R.id.today_schedule_fragment_container);
        Inter = ResourcesCompat.getFont(getActivity(), R.font.inter);


        return parentFragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TodayScheduleViewModel.class);
        String Token = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
                .getString("pkuHelperToken",null);
/*        System.out.println(Token);
        Log.d("Token",Token);
        Log.e("Token",Token);*/
        updateTimelineAxis();
        scheduleRootObjectTask = new FetchCourseInfo("1z0civoydg0u49d1q9wbz4d6ptkz80wn"
        );
        scheduleRootObjectTask.execute();
    }

    public void updateTimelineAxis(){
        int currentHour = Calendar.getInstance().get(Calendar.HOUR);
        int offsetY =((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                181,
                getResources().getDisplayMetrics()));
        for(int i=1;i<24;i++){
            TextView time = new TextView(this.getContext());
            time.setText(i+":00");
            time.setTextColor(0x80000000);
            int offsetX =((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    72*i,
                    getResources().getDisplayMetrics()));
            time.setX(offsetX);
            time.setY(offsetY);
            time.setTypeface(Inter);
            time.setTextSize(10);
            layoutContainer.addView(time);
        }
    }
    public int DpToPx(float dp){
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()));
    }
    public void updateCourseBlocks(){
        int blockHeight = DpToPx(180);
        float offsetX;
        String dayOfWeek = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);
        for(Coursetableroom coursetableroom: scheduleRootObject.courseTableRoom){

            for(Jsap jsap:coursetableroom.jsap){
                if(jsap.xq.equals(dayOfWeek)){
                    /*
                    System.out.println(coursetableroom.kcmc);
                    Log.d("kcmc",coursetableroom.kcmc);
                    Log.e("kcmc",coursetableroom.kcmc);*/
                    offsetX=CourseTimeRepository.getStartTimeMinute(jsap.kssj);
                    AddCourseBlock(
                            DpToPx(offsetX*1.2f),
                            0,
                            coursetableroom.kcmc,
                            DpToPx((CourseTimeRepository.getEndTimeMinute(jsap.jssj)-offsetX)*1.2f),
                            blockHeight
                    );
                }
            }
        }
    }


    public ScheduleCourseGrid AddCourseBlock(int offsetX, int offsetY, String courseTitle, int blockWidth, int blockHeight){
        ScheduleCourseGrid courseBlockButton = new ScheduleCourseGrid(this.getContext());
        courseBlockButton.setDisplayButtonBackground(getResources().getDrawable(R.drawable.ripple_corner_8dp_accent_red));
        courseBlockButton.setDisplayTitleText(courseTitle);
        courseBlockButton.setDisplayWidth(blockWidth);
        courseBlockButton.setDisplayHeight(blockHeight);
        courseBlockButton.setX(offsetX);
        courseBlockButton.setY(offsetY);
        courseBlockButton.setElevation(16);
        courseBlockButton.setBackground(getResources().getDrawable(R.drawable.shape_corner_8dp_accent_red));
        layoutContainer.addView(courseBlockButton);
        return courseBlockButton;
    }


    @SuppressLint("StaticFieldLeak")
    private class FetchCourseInfo extends AsyncTask<Void, Void, ScheduleRootObject> {
        private String Token;
        FetchCourseInfo(String token){Token=token;}
        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        protected ScheduleRootObject doInBackground(Void... params) {
            if (!isNetworkAvailable()) {
                return null;
            }
            try {
                return ScheduleRootObject.getInstanceFromWebApi(
                        Token
                );
            } catch (Exception e) {
//                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ScheduleRootObject returnStatus) {
            if(returnStatus!=null)
            {
                scheduleRootObject = returnStatus;
                updateCourseBlocks();
            }
        }


    }

}
