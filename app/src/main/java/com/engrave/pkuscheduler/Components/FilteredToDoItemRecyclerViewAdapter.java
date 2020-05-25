package com.engrave.pkuscheduler.Components;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.engrave.pkuscheduler.Activities.EventDescriptionActivity;
import com.engrave.pkuscheduler.R;
import com.engrave.pkuscheduler.ViewModels.ToDoItem;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import ws.vinta.pangu.Pangu;

public class FilteredToDoItemRecyclerViewAdapter extends RecyclerView.Adapter<FilteredToDoItemRecyclerViewAdapter.ViewHolder> {

    private final List<ToDoItem> items;
    private Pangu pangu = new Pangu();
    private Context mContext;
    private View root_view;
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);

    public FilteredToDoItemRecyclerViewAdapter( List<ToDoItem> _toDoItems,View rootview) {
        this.items = _toDoItems;
        root_view = rootview;
        mContext = rootview.getContext();
        Log.e("FilteredToDoItemRecyclerViewAdapter","saasdas"+JSON.toJSONString(items));
    }

    @Override
    public FilteredToDoItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_fixed_item, parent, false);
        return new FilteredToDoItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FilteredToDoItemRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.e("Hello","saasdas"+JSON.toJSONString(items.get(position)));
        holder.mItem = items.get(position);
        holder.mView.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams llp = holder.mLinearLayout.getLayoutParams();
        llp.height=holder.mView.getContext().getResources().getDimensionPixelSize(R.dimen.TodoItemGridHeight);
        llp.width=ViewGroup.LayoutParams.MATCH_PARENT;
        holder.mView.setLayoutParams(llp);
        holder.mTitleView.setText(
                pangu.spacingText(
                        holder.mItem.getScheduleTitle().replace(" ","")
                )
        );
        holder.mDueTimeView.setText(
                pangu.spacingText(dateFormat.format( items.get(position).getEndTime())
                        +"  " +timeFormat.format( items.get(position).getEndTime()))
        );
        if(items.get(position).getScheduleDescription()!=null&&items.get(position).getScheduleDescription().length()>0){
            holder.mEventTypeView.setText(
                    items.get(position).getScheduleDescription()
            );
        }else{
            holder.mEventTypeView.setText(
                    "没有描述"
            );
        }
        if(items.get(position).getScheduleCourseSource()!=null&&items.get(position).getScheduleCourseSource().length()>0){
            holder.mCourseSourceView.setText(
                    items.get(position).getScheduleCourseSource()
            );
        }else{
            holder.mCourseSourceView.setVisibility(View.GONE);
        }

        if(items.get(position).getEndTime().getTime()<new Date().getTime()){
            holder.mDueTimeView.setTextColor(0xfffa4c4b);
            holder.ddlIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_date_time_red_24));
        }
        else{
            if(items.get(position).getEndTime().getTime()<new Date().getTime()+86400000*3){
                holder.mDueTimeView.setTextColor(0xfffbaa61);
                holder.ddlIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_date_time_orange_24));
            }
        }

        holder.mView.setOnClickListener(v -> {
            Intent eventDescIntent = new Intent(mContext, EventDescriptionActivity.class);
            eventDescIntent.putExtra("SCHEDULE", JSON.toJSONString(items.get(position)));
            mContext.startActivity(eventDescIntent);
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDueTimeView;
        public final TextView mEventTypeView;
        public final TextView mCourseSourceView;
        public ToDoItem mItem;
        public final ConstraintLayout mLinearLayout;
        public ImageView ddlIcon;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.filtered_todo_item_title);
            mDueTimeView = (TextView) view.findViewById(R.id.filtered_todo_item_description);
            mEventTypeView =(TextView) view.findViewById(R.id.filtered_todo_item_eventtype);
            mCourseSourceView= (TextView) view.findViewById(R.id.filtered_todo_item_coursesource);
            mLinearLayout = (ConstraintLayout)view.findViewById(R.id.filtered_todo_item_container);
            ddlIcon = view.findViewById(R.id.filtered_todo_item_duetime_icon);
        }

    }
}
