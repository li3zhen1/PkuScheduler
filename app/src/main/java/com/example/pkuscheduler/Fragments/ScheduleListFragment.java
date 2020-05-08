package com.example.pkuscheduler.Fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.Components.EmptySpecifiedRecyclerView;
import com.example.pkuscheduler.Components.ToDoItemRecyclerViewAdapter;
import com.example.pkuscheduler.Models.CourseDeadlineJsonModel.DeadlineRootObject;
import com.example.pkuscheduler.Models.CourseLoginInfoModel;
import com.example.pkuscheduler.R;
import com.example.pkuscheduler.ViewModels.ToDoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View mView;
    private List<ToDoItem> toDoItems =new ArrayList<>();
    private ToDoItemRecyclerViewAdapter adapter;
    private EmptySpecifiedRecyclerView mRecyclerView;
    private FetchScheduleInfo fetchScheduleInfo;
    private final Long MILLISECONDS_OF_A_WEEK = Long.valueOf(604800000);


    public ScheduleListFragment() {
    }

    public static ScheduleListFragment newInstance() {
        ScheduleListFragment fragment = new ScheduleListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_schedule_list, container, false);


        mRecyclerView = (EmptySpecifiedRecyclerView) mView.findViewById(R.id.today_schedule_recycler_view);
        mRecyclerView.setEmptyView(mView.findViewById(R.id.schedule_list_empty_view_container));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ToDoItemRecyclerViewAdapter(toDoItems);
        mRecyclerView.setAdapter(adapter);
        fetchScheduleInfo = new FetchScheduleInfo(
                CourseLoginInfoModel.getInstanceFromSharedPreference(getContext())
        );
        fetchScheduleInfo.execute();
        return mView;
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchScheduleInfo extends AsyncTask<Void, Void, List<ToDoItem>>{
        private CourseLoginInfoModel courseLoginInfoModel;
        FetchScheduleInfo(CourseLoginInfoModel _){
            courseLoginInfoModel=_;
        }

        @Override
        protected List<ToDoItem> doInBackground(Void... voids) {
            List<ToDoItem> _toDoItems = new ArrayList<ToDoItem>();
            List<DeadlineRootObject> _deadlineRootObjects = DeadlineRootObject.getInstance(
                    getContext(),
                    courseLoginInfoModel,
                    String.valueOf(System.currentTimeMillis()),
                    String.valueOf(System.currentTimeMillis()
                            +MILLISECONDS_OF_A_WEEK*4)
            );
            Log.e(JSON.toJSONString(_deadlineRootObjects),"");
            for(DeadlineRootObject deadlineRootObject:_deadlineRootObjects){
                ToDoItem newItem = new ToDoItem(deadlineRootObject, null);
                Log.e("DDL",JSON.toJSONString(newItem));
                _toDoItems.add(newItem);
            }
            Log.e("DDLALL",JSON.toJSONString(_toDoItems));
            return _toDoItems;
        }
        @Override
        protected void onPostExecute(final List<ToDoItem> returnStatus) {
            if(returnStatus!=null)
            {
                for(ToDoItem __toDoItem :returnStatus){
                    toDoItems.add(__toDoItem);
                }
                adapter.notifyDataSetChanged();

            }
        }

    }
}
