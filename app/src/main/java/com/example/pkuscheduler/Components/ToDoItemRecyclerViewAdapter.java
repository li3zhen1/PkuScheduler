package com.example.pkuscheduler.Components;

import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.R;
import com.example.pkuscheduler.Utils.UI.LengthConveter;
import com.example.pkuscheduler.ViewModels.ToDoItem;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ToDoItemRecyclerViewAdapter extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter  {
    private final List<ToDoItem> items;

    private View root_view;
    private ToDoItem mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.CHINA);
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.CHINA);

    public ToDoItemRecyclerViewAdapter(List<ToDoItem> _toDoItems, View rootview) {
        this.items = _toDoItems;
        root_view=rootview;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        System.out.println(position);
        System.out.println(holder.mView.getClass());
        holder.mItem = items.get(position);
        //Log.e("NotDone", JSON.toJSONString(holder.mItem.getIsDone()));
        //System.out.println(JSON.toJSONString(holder.mLinearLayout.getLayoutParams().));
        if(holder.mItem.getIsDone()){

            holder.mView.setVisibility(View.GONE);
            ViewGroup.LayoutParams llp = holder.mLinearLayout.getLayoutParams();
            llp.height=0;llp.width=0;
            holder.mView.setLayoutParams(llp);
        }
        else{

            holder.mView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams llp = holder.mLinearLayout.getLayoutParams();
            System.out.println(R.dimen.TodoItemGridHeight);
            llp.height=holder.mView.getContext().getResources().getDimensionPixelSize(R.dimen.TodoItemGridHeight);
            llp.width=ViewGroup.LayoutParams.MATCH_PARENT;
            holder.mView.setLayoutParams(llp);
        }
        holder.mTitleView.setText(holder.mItem.getScheduleTitle().replace(" ",""));
        holder.mDueTimeView.setText(
                dateFormat.format( items.get(position).getEndTime())
                +"  " +timeFormat.format( items.get(position).getEndTime())
        );
        holder.mEventTypeView.setText(items.get(position).getScheduleTag());
        holder.mCourseSourceView.setText(items.get(position).getScheduleCourseSource());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }*/
            }
        });

        holder.mCheckBox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    holder.mItem.setIsDone(isChecked);
                    notifyItemChanged(position);
                }

        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRemoved(final int position) {
        mJustDeletedToDoItem = items.remove(position);
        mIndexOfDeletedToDoItem = position;
        notifyItemRemoved(position);
        //TODO: wakeup Snackbar to Withdraw
    }

    @Override
    public void onItemCompleted(int position) {
        items.get(position).setIsDone(true);
        notifyItemChanged(position);
    }

    public int getIncompletedCount() {
        int ret =0;
        for(ToDoItem itm:items){
            if(!itm.getIsDone()){
                ret++;
            }
        }
        return ret;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDueTimeView;
        public final TextView mEventTypeView;
        public final TextView mCourseSourceView;
        public CheckBox mCheckBox;
        public ToDoItem mItem;
        public final LinearLayout mLinearLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.todo_item_title);
            mDueTimeView = (TextView) view.findViewById(R.id.todo_item_description);
            mEventTypeView =(TextView) view.findViewById(R.id.todo_item_eventtype);
            mCourseSourceView=(TextView) view.findViewById(R.id.todo_item_coursesource);
            mCheckBox =(CheckBox) view.findViewById(R.id.todo_item_checkbox);
            mLinearLayout = (LinearLayout)view.findViewById(R.id.todo_item_container);
        }

    }
}
