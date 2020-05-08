package com.example.pkuscheduler.Components;

import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pkuscheduler.R;
import com.example.pkuscheduler.ViewModels.ToDoItem;

import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ToDoItem} and makes a call to the
 * specified {link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ToDoItemRecyclerViewAdapter extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ViewHolder> {
    private final List<ToDoItem> items;
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.CHINA);
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);

    public ToDoItemRecyclerViewAdapter(List<ToDoItem> _toDoItems) {
        this.items = _toDoItems;
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
