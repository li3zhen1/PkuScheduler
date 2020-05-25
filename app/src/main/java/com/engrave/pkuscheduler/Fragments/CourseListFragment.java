package com.engrave.pkuscheduler.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.engrave.pkuscheduler.Components.DeadlineIndicator;
import com.engrave.pkuscheduler.Components.ScheduleCourseGrid;
import com.engrave.pkuscheduler.Interfaces.IActionBarHeightGettable;
import com.engrave.pkuscheduler.Models.ScheduleJsonModel.CourseTimeRepository;
import com.engrave.pkuscheduler.Models.ScheduleJsonModel.Coursetableroom;
import com.engrave.pkuscheduler.Models.ScheduleJsonModel.Jsap;
import com.engrave.pkuscheduler.Models.ScheduleJsonModel.ScheduleRootObject;
import com.engrave.pkuscheduler.R;
import com.engrave.pkuscheduler.ViewModels.ToDoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.engrave.pkuscheduler.Utils.UI.LengthConveter.DpToPx;

public class CourseListFragment extends Fragment {

    private List<ToDoItem> toDoItems = new ArrayList<>();
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
    private FrameLayout firstColumnForDDL;
    private FrameLayout secondColumnForDDL;
    private FrameLayout thirdColumnForDDL;
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
        firstColumnForDDL = firstColumn.findViewById(R.id.table_first_column_DDL);
        secondColumnForDDL = secondColumn.findViewById(R.id.table_second_column_DDL);
        thirdColumnForDDL = thirdColumn.findViewById(R.id.table_third_column_DDL);

        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        mContainerWidth = point.x;
        DP12 = DpToPx(12,getContext());
        mContainerHeight = point.y-DpToPx(120,getContext())-
                ((IActionBarHeightGettable)getActivity()).getActionBarHeight()
        ;
        hourCorrespondingHeight = mContainerHeight/timeIndicatorText.size();
        return parentFragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateTimelineAxis();
        runnable.run();
        String Token = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
                .getString("pkuHelperToken",null);

        scheduleRootObjectTask = new FetchCourseInfo(Token
        );
        scheduleRootObjectTask.execute();
        FetchScheduleInfoFromStorage fetchScheduleInfoFromStorage = new FetchScheduleInfoFromStorage();
        fetchScheduleInfoFromStorage.execute();

    }

    public void updateTimelineAxis(){
        if(layoutContainer!=null){
            for(int i=0;i<timeIndicatorText.size();i++){
                float Height = DP12*4+i*hourCorrespondingHeight;
                View lineSep = new View(getContext());
                lineSep.setY(Height);
                lineSep.setX(0);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,DpToPx(1,getContext()));
                lineSep.setLayoutParams(layoutParams);
                lineSep.setElevation(-1);
                lineSep.setBackgroundColor(getContext().getResources().getColor(R.color.colorInputField));
                layoutContainer.addView(lineSep);
                TextView time = new TextView(this.getContext());
                time.setText(timeIndicatorText.get(i));
                time.setY(Height-DpToPx(8,getContext()));
                time.setX(DpToPx(16,getContext()));
                time.setTextSize(10);
                time.setWidth(DpToPx(36,getContext()));
                time.setHeight(DpToPx(16,getContext()));
                time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                time.setGravity(Gravity.RIGHT|Gravity.CENTER);
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
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 60000);
        }
        void update() {
            updateNowIndicator();
        }
    };

    private void updateNowIndicator(){
        Calendar calendar = Calendar.getInstance();
        float whichFloor = (calendar.get(Calendar.HOUR_OF_DAY)-7+(float)calendar.get(Calendar.MINUTE)/60);
        float _Height = hourCorrespondingHeight*(whichFloor>0?whichFloor<16?whichFloor:16:0);
        layoutContainer.findViewById(R.id.now_indicator_needle).setY(_Height-DpToPx(1,getContext()));
        layoutContainer.findViewById(R.id.now_indicator).setY(_Height+DpToPx(34,getContext()));
    }

    private void clearDeadlineIndicator(){
        if(firstColumnForDDL!=null)
            firstColumnForDDL.removeAllViews();
        if(secondColumnForDDL!=null)
            secondColumnForDDL.removeAllViews();
        if(thirdColumnForDDL!=null)
            thirdColumnForDDL.removeAllViews();
    }
    private void updateCourseBlocks(){
        String dayOfWeek = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);
        String _dayOfWeek = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        String __dayOfWeek = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+1);
        for(Coursetableroom coursetableroom: scheduleRootObject.courseTableRoom){
            for(Jsap jsap:coursetableroom.jsap){
                if(jsap.xq.equals(dayOfWeek)){;
                    getCourseGrid(
                            coursetableroom.kcmc,
                            (int) ((CourseTimeRepository.getEndTimeMinute(jsap.jssj)
                                            -CourseTimeRepository.getStartTimeMinute(jsap.kssj))
                                    /60*hourCorrespondingHeight),
                            0,
                            hourCorrespondingHeight*(CourseTimeRepository.getStartTimeMinute(jsap.kssj)-420)/60,
                            firstColumn
                    );
                }
            }
        }
        for(Coursetableroom coursetableroom: scheduleRootObject.courseTableRoom){
            for(Jsap jsap:coursetableroom.jsap){
                if(jsap.xq.equals(_dayOfWeek)){
                    getCourseGrid(
                            coursetableroom.kcmc,
                            (int) ((CourseTimeRepository.getEndTimeMinute(jsap.jssj)
                                    -CourseTimeRepository.getStartTimeMinute(jsap.kssj))
                                    /60*hourCorrespondingHeight),
                            0,

                            hourCorrespondingHeight*(CourseTimeRepository.getStartTimeMinute(jsap.kssj)-420)/60,
                            secondColumn
                    );
                }
            }
        }
        for(Coursetableroom coursetableroom: scheduleRootObject.courseTableRoom){
            for(Jsap jsap:coursetableroom.jsap){
                if(jsap.xq.equals(__dayOfWeek)){   ;
                    getCourseGrid(
                            coursetableroom.kcmc,
                            (int) ((CourseTimeRepository.getEndTimeMinute(jsap.jssj)
                                    -CourseTimeRepository.getStartTimeMinute(jsap.kssj))
                                    /60*hourCorrespondingHeight),
                            0,

                            hourCorrespondingHeight*(CourseTimeRepository.getStartTimeMinute(jsap.kssj)-420)/60,
                            thirdColumn
                    );
                }
            }
        }
    }


    private ScheduleCourseGrid getCourseGrid(String courseTitle,int blockHeight,float offsetX,float offsetY,FrameLayout parentLayout){
        ScheduleCourseGrid courseBlockButton = new ScheduleCourseGrid(this.getContext());
        courseBlockButton.setDisplayButtonBackground(getResources().getDrawable(R.drawable.ripple_course_grid));
        courseBlockButton.setDisplayTitleText(courseTitle);
        courseBlockButton.setDisplayHeight(blockHeight);
        courseBlockButton.setY(offsetY);
        courseBlockButton.setX(offsetX);
        courseBlockButton.setElevation(0);
        parentLayout.addView(courseBlockButton);
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

    public void startUpdateViewAsyncTask(){

        FetchScheduleInfoFromStorage fetchScheduleInfoFromStorage = new FetchScheduleInfoFromStorage();
        fetchScheduleInfoFromStorage.execute();
    }

    public void updateDeadlineIndicators(){
        clearDeadlineIndicator();

        Calendar bacon = Calendar.getInstance();
        Calendar baconEnd = Calendar.getInstance();
        bacon.set(Calendar.HOUR_OF_DAY,8);
        bacon.set(Calendar.MINUTE,0);
        bacon.set(Calendar.SECOND,0);
        baconEnd.set(Calendar.HOUR_OF_DAY,8);
        baconEnd.set(Calendar.MINUTE,30);
        baconEnd.set(Calendar.SECOND,0);


        List<ToDoItem> tdl_today1 = collectDeadlinesForTimePeriod2(
                bacon,false
        );
        if(tdl_today1!=null&&tdl_today1.size()>0){
            DeadlineIndicator deadlineIndicator_today1 = new DeadlineIndicator(getContext());
            deadlineIndicator_today1.setToDoItemList(tdl_today1);
            deadlineIndicator_today1.setY((float) (hourCorrespondingHeight*0.5-DpToPx(14,getContext())));
            firstColumnForDDL.addView(
                deadlineIndicator_today1
        );
        }

        for(int i=0;i<=28;i++){
            List<ToDoItem> tdl = collectDeadlinesForTimePeriod(
                    bacon,baconEnd
                    );
            bacon.add(Calendar.MINUTE,30);
            baconEnd.add(Calendar.MINUTE,30);
            if(tdl==null||tdl.size()==0)continue;
            DeadlineIndicator deadlineIndicator = new DeadlineIndicator(getContext());
            deadlineIndicator.setToDoItemList(tdl);
            deadlineIndicator.setY((float) (hourCorrespondingHeight*(0.5*i+1)-DpToPx(14,getContext())));
            firstColumnForDDL.addView(
                    deadlineIndicator
            );
        }

        List<ToDoItem> tdl_today2 = collectDeadlinesForTimePeriod2(
                baconEnd,true
        );
        if(tdl_today2!=null&&tdl_today2.size()>0){
            DeadlineIndicator deadlineIndicator_today2 = new DeadlineIndicator(getContext());
            deadlineIndicator_today2.setToDoItemList(tdl_today2);
            deadlineIndicator_today2.setY((float) (hourCorrespondingHeight*15.5-DpToPx(14,getContext())));
            firstColumnForDDL.addView(
                    deadlineIndicator_today2
            );
        }

        bacon.add(Calendar.HOUR,24);
        bacon.set(Calendar.HOUR_OF_DAY,8);
        bacon.set(Calendar.MINUTE,0);

        List<ToDoItem> tdl_tomorrow1 = collectDeadlinesForTimePeriod2(
                bacon,false
        );
        if(tdl_tomorrow1!=null&&tdl_tomorrow1.size()>0){
            DeadlineIndicator deadlineIndicator_tomorrow1 = new DeadlineIndicator(getContext());
            deadlineIndicator_tomorrow1.setToDoItemList(tdl_tomorrow1);
            deadlineIndicator_tomorrow1.setY((float) (hourCorrespondingHeight*0.5-DpToPx(14,getContext())));
            secondColumnForDDL.addView(
                    deadlineIndicator_tomorrow1
            );
        }


        baconEnd.add(Calendar.HOUR,24);
        baconEnd.set(Calendar.HOUR_OF_DAY,8);
        baconEnd.set(Calendar.MINUTE,30);
        for(int i=0;i<28;i++){
            List<ToDoItem> tdl = collectDeadlinesForTimePeriod(
                    bacon,baconEnd
            );
            bacon.add(Calendar.MINUTE,30);
            baconEnd.add(Calendar.MINUTE,30);
            if(tdl==null||tdl.size()==0)continue;
            DeadlineIndicator deadlineIndicator = new DeadlineIndicator(getContext());
            deadlineIndicator.setToDoItemList(tdl);
            deadlineIndicator.setY((float) (hourCorrespondingHeight*(0.5*i+1)-DpToPx(14,getContext())));
            secondColumnForDDL.addView(
                    deadlineIndicator
            );
        }
        List<ToDoItem> tdl_tomorrow2 = collectDeadlinesForTimePeriod2(
                baconEnd,true
        );
        if(tdl_tomorrow2!=null&&tdl_tomorrow2.size()>0){
            DeadlineIndicator deadlineIndicator_tomorrow2 = new DeadlineIndicator(getContext());
            deadlineIndicator_tomorrow2.setToDoItemList(tdl_tomorrow2);
            deadlineIndicator_tomorrow2.setY((float) (hourCorrespondingHeight*15.5-DpToPx(14,getContext())));
            secondColumnForDDL.addView(
                    deadlineIndicator_tomorrow2
            );
        }

        bacon.add(Calendar.HOUR,24);
        bacon.set(Calendar.HOUR_OF_DAY,8);
        bacon.set(Calendar.MINUTE,0);

        List<ToDoItem> tdl_to1 = collectDeadlinesForTimePeriod2(
                bacon,false
        );
        if(tdl_to1!=null&&tdl_to1.size()>0){
            DeadlineIndicator deadlineIndicator_to1 = new DeadlineIndicator(getContext());
            deadlineIndicator_to1.setToDoItemList(tdl_to1);
            deadlineIndicator_to1.setY((float) (hourCorrespondingHeight*0.5-DpToPx(14,getContext())));
            thirdColumnForDDL.addView(
                    deadlineIndicator_to1
            );
        }



        baconEnd.add(Calendar.HOUR,24);
        baconEnd.set(Calendar.HOUR_OF_DAY,8);
        baconEnd.set(Calendar.MINUTE,30);
        for(int i=0;i<28;i++){
            List<ToDoItem> tdl = collectDeadlinesForTimePeriod(
                    bacon,baconEnd
            );
            bacon.add(Calendar.MINUTE,30);
            baconEnd.add(Calendar.MINUTE,30);
            if(tdl==null||tdl.size()==0)continue;
            DeadlineIndicator deadlineIndicator = new DeadlineIndicator(getContext());
            deadlineIndicator.setToDoItemList(tdl);
            deadlineIndicator.setY((float) (hourCorrespondingHeight*(0.5*i+1)-DpToPx(14,getContext())));
            thirdColumnForDDL.addView(
                    deadlineIndicator
            );
        }
        List<ToDoItem> tdl_to2 = collectDeadlinesForTimePeriod3(
                baconEnd,true
        );
        if(tdl_to2!=null&&tdl_to2.size()>0){
            DeadlineIndicator deadlineIndicator_to2 = new DeadlineIndicator(getContext());
            deadlineIndicator_to2.setToDoItemList(tdl_to2);
            deadlineIndicator_to2.setY((float) (hourCorrespondingHeight*15.5-DpToPx(14,getContext())));
            thirdColumnForDDL.addView(
                    deadlineIndicator_to2
            );
        }
    }

    private List<ToDoItem> collectDeadlinesForTimePeriod(Calendar startCld, Calendar endCld){
        long startTm = startCld.getTimeInMillis();
        long endTm = endCld.getTimeInMillis();
        List<ToDoItem> tds = toDoItems.stream()
                .filter(
                        td->
                                (td.getEndTime().getTime()<endTm&&
                                        td.getEndTime().getTime()>=startTm&&
                                        !td.getIsDone()
                                )
                )
                .collect(Collectors.toList());
        return tds;
    }

    private List<ToDoItem> collectDeadlinesForTimePeriod2(Calendar Cld, boolean isLatter){
        long Tm = Cld.getTimeInMillis();
        List<ToDoItem> tds;
        if(isLatter){
            tds = toDoItems.stream()
                    .filter(
                            td->
                                    td.getEndTime().getDate()==Cld.get(Calendar.DAY_OF_MONTH)&&
                                    td.getEndTime().getTime()>=Tm&&
                                            !td.getIsDone()
                    ).collect(Collectors.toList());
        }
        else{
            tds = toDoItems.stream()
                    .filter(
                            td->
                                    td.getEndTime().getDate()==Cld.get(Calendar.DAY_OF_MONTH)&&
                                            td.getEndTime().getTime()<Tm&&
                                            !td.getIsDone()
                    ).collect(Collectors.toList());
        }
        return tds;
    }
    private List<ToDoItem> collectDeadlinesForTimePeriod3(Calendar Cld, boolean isLatter){
        long Tm = Cld.getTimeInMillis();
        List<ToDoItem> tds;
        if(isLatter){
            tds = toDoItems.stream()
                    .filter(
                            td->
                                            td.getEndTime().getTime()>=Tm&&
                                            !td.getIsDone()
                    ).collect(Collectors.toList());
        }
        else{
            tds = toDoItems.stream()
                    .filter(
                            td->
                                            td.getEndTime().getTime()<Tm&&
                                            !td.getIsDone()
                    ).collect(Collectors.toList());
        }
        return tds;
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchScheduleInfoFromStorage extends AsyncTask<Void, Void, String>{
        FetchScheduleInfoFromStorage(){}

        @Override
        protected String doInBackground(Void... voids) {
            List<ToDoItem> _toDoItems = new ArrayList<ToDoItem>();

            try{
                _toDoItems = ToDoItem.getListInstanceFromStorage(getContext());
            } catch (IOException e) {
                return "获取存储的TODO项失败";
            }
            if(_toDoItems!=null)
            {
                toDoItems=_toDoItems;
                toDoItems.sort(Comparator.comparing(toDoItem -> {return toDoItem.getEndTime();}));
            }
            return "成功";
        }
        @Override
        protected void onPostExecute(final String returnStatus) {
            updateDeadlineIndicators();
        }
    }

}
