package com.example.pkuscheduler.Components;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.*;
import com.example.pkuscheduler.R;
import com.example.pkuscheduler.ViewModels.ToDoItem;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

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
        holder.mItem = items.get(position);
        holder.mTitleView.setText(items.get(position).getScheduleTitle().replace(" ",""));
        holder.mDueTimeView.setText(
                dateFormat.format( items.get(position).getScheduleDeadline())
                +"  " +timeFormat.format( items.get(position).getScheduleDeadline())
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
        //Intent i = new Intent(getContext(), TodoNotificationService.class);
        //deleteAlarm(i, mJustDeletedToDoItem.getIdentifier().hashCode());
        notifyItemRemoved(position);

/*        Log.e("",root_view.getContext().getString(R.string.LoginActivity_ForgotPassword));
        String toShow = "Todo";
        Snackbar.make(mCoordLayout, "Deleted " + toShow, Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Comment the line below if not using Google Analytics
                        app.send(this, "Action", "UNDO Pressed");
                        items.add(mIndexOfDeletedToDoItem, mJustDeletedToDoItem);
                        if (mJustDeletedToDoItem.getToDoDate() != null && mJustDeletedToDoItem.hasReminder()) {
                            Intent i = new Intent(getContext(), TodoNotificationService.class);
                            i.putExtra(TodoNotificationService.TODOTEXT, mJustDeletedToDoItem.getToDoText());
                            i.putExtra(TodoNotificationService.TODOUUID, mJustDeletedToDoItem.getIdentifier());
                            createAlarm(i, mJustDeletedToDoItem.getIdentifier().hashCode(), mJustDeletedToDoItem.getToDoDate().getTime());
                        }
                        notifyItemInserted(mIndexOfDeletedToDoItem);
                    }
                }).show();*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDueTimeView;
        public final TextView mEventTypeView;
        public final TextView mCourseSourceView;
        public ToDoItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.todo_item_title);
            mDueTimeView = (TextView) view.findViewById(R.id.todo_item_description);
            mEventTypeView =(TextView) view.findViewById(R.id.todo_item_eventtype);
            mCourseSourceView=(TextView) view.findViewById(R.id.todo_item_coursesource);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDueTimeView.getText() + "'";
        }
    }
}
