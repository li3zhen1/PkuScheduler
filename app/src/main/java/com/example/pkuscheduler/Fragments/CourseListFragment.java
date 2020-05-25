package com.example.pkuscheduler.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.pkuscheduler.Components.ScheduleCourseGrid;
import com.example.pkuscheduler.Interfaces.IActionBarHeightGettable;
import com.example.pkuscheduler.Models.ScheduleJsonModel.CourseTimeRepository;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Coursetableroom;
import com.example.pkuscheduler.Models.ScheduleJsonModel.Jsap;
import com.example.pkuscheduler.Models.ScheduleJsonModel.ScheduleRootObject;
import com.example.pkuscheduler.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.pkuscheduler.Utils.UI.LengthConveter.DpToPx;

public class CourseListFragment extends Fragment {

    private int DP12;
    private int hourCorrespondingHeight;
    private int mContainerWidth;
    private int mContainerHeight;
    private final List<CharSequence> timeIndicatorText = new ArrayList<CharSequence>(){
        {
            add("更早");
            add("O8:OO");
            add("O9:OO");
            add("1O:OO");
            add("11:OO");
            add("12:OO");
            add("13:OO");
            add("14:OO");
            add("15:OO");
            add("16:OO");
            add("17:OO");
            add("18:OO");
            add("19:OO");
            add("2O:OO");
            add("21:OO");
            add("22:OO");
            add("更晚");
        }
    };


    private ScheduleRootObject scheduleRootObject;
    private View parentFragment;
    private ConstraintLayout layoutContainer;
    private ConstraintLayout TableContainer;
    private FrameLayout firstColumn;
    private FrameLayout secondColumn;
    private FrameLayout thirdColumn;
    FetchCourseInfo scheduleRootObjectTask;

    public static CourseListFragment newInstance() {
        return new CourseListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        parentFragment = inflater.inflate(R.layout.fragment_course_table, container, false);
        layoutContainer = parentFragment.findViewById(R.id.today_schedule_fragment_container);
        TableContainer = parentFragment.findViewById(R.id.today_schedule_table_container);
        firstColumn = TableContainer.findViewById(R.id.table_first_column);
        secondColumn = TableContainer.findViewById(R.id.table_second_column);
        thirdColumn = TableContainer.findViewById(R.id.table_third_column);
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        mContainerWidth = point.x;
        DP12 = DpToPx(12,getContext());
        mContainerHeight = point.y-DpToPx(144,getContext())-
                ((IActionBarHeightGettable)getActivity()).getActionBarHeight()
        ;
        hourCorrespondingHeight = mContainerHeight/timeIndicatorText.size();
        return parentFragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateTimelineAxis();
        String Token = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
                .getString("pkuHelperToken",null);
        scheduleRootObjectTask = new FetchCourseInfo("1z0civoydg0u49d1q9wbz4d6ptkz80wn"
        );
        scheduleRootObjectTask.execute();
    }

    public void updateTimelineAxis(){
        if(layoutContainer!=null){
            for(int i=0;i<timeIndicatorText.size();i++){
                TextView time = new TextView(this.getContext());
                time.setText(timeIndicatorText.get(i));
                time.setY(DP12*4+i*hourCorrespondingHeight);
                time.setX(DpToPx(16,getContext()));
                time.setTextSize(10);
                time.setWidth(DpToPx(36,getContext()));
                time.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                time.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                time.setTypeface(getContext().getResources().getFont(R.font.sfmono));
                layoutContainer.addView(time);
            }
        }
        if(TableContainer!=null){
            ((TextView)TableContainer.findViewById(R.id.text_first_date)).setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
            ((TextView)TableContainer.findViewById(R.id.text_second_date)).setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1));
            ((TextView)TableContainer.findViewById(R.id.text_third_date)).setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+2));
        }
    }

    public void updateCourseBlocks(){
        String dayOfWeek = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);
        for(Coursetableroom coursetableroom: scheduleRootObject.courseTableRoom){

            for(Jsap jsap:coursetableroom.jsap){
                if(jsap.xq.equals(dayOfWeek)){
                    Log.e("1213123124",
                            ""+(CourseTimeRepository.getEndTimeMinute(jsap.jssj)
                                    -CourseTimeRepository.getStartTimeMinute(jsap.kssj))
                                    /60
                    );                    ;
                    getCourseGrid(
                            coursetableroom.kcmc,
                            (int) ((CourseTimeRepository.getEndTimeMinute(jsap.jssj)
                                            -CourseTimeRepository.getStartTimeMinute(jsap.kssj))
                                    /60*hourCorrespondingHeight),
                            0,

                            hourCorrespondingHeight*(CourseTimeRepository.getStartTimeMinute(jsap.kssj)-420)/60
                    );
                }
            }
        }
    }


    private ScheduleCourseGrid getCourseGrid(String courseTitle,int blockHeight,float offsetX,float offsetY){
        Log.e("height",""+blockHeight);
        ScheduleCourseGrid courseBlockButton = new ScheduleCourseGrid(this.getContext());
        courseBlockButton.setDisplayButtonBackground(getResources().getDrawable(R.drawable.ripple_corner_8dp_accent_red));
        courseBlockButton.setDisplayTitleText(courseTitle);
        courseBlockButton.setDisplayHeight(1);
        courseBlockButton.setDisplayWidth(1);
        courseBlockButton.setX(offsetX);
        courseBlockButton.setY(offsetY);
        courseBlockButton.setElevation(16);
        courseBlockButton.setBackground(getResources().getDrawable(R.drawable.shape_corner_8dp_accent_red));
        firstColumn.addView(courseBlockButton);
        return courseBlockButton;
    }


    @SuppressLint("StaticFieldLeak")
    private class FetchCourseInfo extends AsyncTask<Void, Void, ScheduleRootObject> {
        private String Token;
        FetchCourseInfo(String token){Token=token;}
        @Override
        protected ScheduleRootObject doInBackground(Void... params) {
            try {
                return ScheduleRootObject.getInstance(
                        Token, getContext()
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
