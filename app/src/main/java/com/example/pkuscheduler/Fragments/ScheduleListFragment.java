package com.example.pkuscheduler.Fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.Components.EmptySpecifiedRecyclerView;
import com.example.pkuscheduler.Components.ItemTouchHelperClass;
import com.example.pkuscheduler.Components.ToDoItemRecyclerViewAdapter;
import com.example.pkuscheduler.Models.CourseDeadlineJsonModel.CourseRawToDoItemsRootObject;
import com.example.pkuscheduler.Models.CourseLoginInfoModel;
import com.example.pkuscheduler.R;
import com.example.pkuscheduler.ViewModels.ToDoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.pkuscheduler.ViewModels.ToDoItem.saveListInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View mView;
    public List<ToDoItem> toDoItems =new ArrayList<>();
    private ToDoItemRecyclerViewAdapter adapter;
    private EmptySpecifiedRecyclerView mRecyclerView;
    private FetchScheduleInfoFromStorage fetchScheduleInfoFromStorage;
    private UpdateScheduleInfoFromWebApi updateScheduleInfoFromWebApi;
    private CourseLoginInfoModel courseLoginInfoModel;
    private final Long MILLISECONDS_OF_A_WEEK = Long.valueOf(604800000);
    public ItemTouchHelper itemTouchHelper;

    public void addTodoItem(ToDoItem toDoItem){
        this.toDoItems.add(toDoItem);
        adapter.notifyDataSetChanged();
    }

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

        adapter = new ToDoItemRecyclerViewAdapter(toDoItems
        ,mView);
        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter,getContext());
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        courseLoginInfoModel =CourseLoginInfoModel.getInstanceFromSharedPreference(getContext());
        fetchScheduleInfoFromStorage = new FetchScheduleInfoFromStorage();
        fetchScheduleInfoFromStorage.execute();
        return mView;
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
                toDoItems.addAll(_toDoItems);
                toDoItems.sort(Comparator.comparing(toDoItem -> {return toDoItem.getEndTime();}));
            }
            try {
                saveListInstance(toDoItems,getContext());
            } catch (IOException e) {
                return "更新存储TODO项失败";
            }
            return "成功";
        }
        @Override
        protected void onPostExecute(final String returnStatus) {
            adapter.notifyDataSetChanged();

            //TODO handle exception
            updateScheduleInfoFromWebApi = new UpdateScheduleInfoFromWebApi();
            updateScheduleInfoFromWebApi.execute();
        }

    }
    @SuppressLint("StaticFieldLeak")
    private class UpdateScheduleInfoFromWebApi extends AsyncTask<Void, Void, String>{
        UpdateScheduleInfoFromWebApi(){

        }

        @Override
        protected String doInBackground(Void... voids) {
            List<ToDoItem> _toDoItems = new ArrayList<ToDoItem>();
            List<CourseRawToDoItemsRootObject> _courseRawToDoItemsRootObjects;
            try{
                _courseRawToDoItemsRootObjects
                        = CourseRawToDoItemsRootObject.getInstanceFromWebApi(
                        getContext(),
                        String.valueOf(System.currentTimeMillis()),
                        String.valueOf(System.currentTimeMillis()
                                +MILLISECONDS_OF_A_WEEK*4)
                );
                Log.e("",JSON.toJSONString(_courseRawToDoItemsRootObjects));
                for(CourseRawToDoItemsRootObject courseRawToDoItemsRootObject : _courseRawToDoItemsRootObjects){
                    ToDoItem newItem = new ToDoItem(courseRawToDoItemsRootObject, null);
                    _toDoItems.add(newItem);
                }
            }catch (Exception e){
                Log.e("ERR",e.getLocalizedMessage());
                return "获取CourseApi失败";
            }

            if(_toDoItems!=null)
            {
                boolean isDistinct;

                //查重
                for(ToDoItem td:_toDoItems){
                    isDistinct = true;
                    for(ToDoItem _td: toDoItems){
                        if(td.equals(_td))
                        {
                            isDistinct=false;
                            break;
                        }
                    }
                    if(isDistinct){
                        toDoItems.add(td);
                    }
                }
                toDoItems.sort(Comparator.comparing(toDoItem -> {return toDoItem.getEndTime();}));
                try {
                    saveListInstance(toDoItems,getContext());
                } catch (IOException e) {
                    return "更新存储来自CourseApi的信息失败";
                }
                return "成功";
            }
            return "未获取到有效信息";
        }
        @Override
        protected void onPostExecute(final String returnStatus) {
            Log.e("!!!!!!",returnStatus);
            toDoItems.forEach(toDoItem -> Log.e("!!!",toDoItem.getScheduleTitle()+toDoItem.getIsDone()));
            adapter.notifyDataSetChanged();
            //TODO:Handle Excpetion
        }

    }

    @Override
    public void onPause() {
        try {
            saveListInstance(toDoItems,getContext());
        } catch (IOException e) {
            //TODO:Handle Excpetion
            e.printStackTrace();
        }
        super.onPause();
    }
}
