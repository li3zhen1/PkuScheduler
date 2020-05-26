package com.engrave.pkuscheduler.Components;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.engrave.pkuscheduler.Activities.EventDescriptionActivity;
import com.engrave.pkuscheduler.Activities.MainActivity;
import com.engrave.pkuscheduler.Models.CourseLoginInfoModel;
import com.engrave.pkuscheduler.R;
import com.engrave.pkuscheduler.ViewModels.ToDoItem;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ws.vinta.pangu.Pangu;

public class ToDoItemRecyclerViewAdapter extends RecyclerView.Adapter<ToDoItemRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter  {
    private boolean isSyncing_Global = false;
    private final List<ToDoItem> items;
    private Pangu pangu = new Pangu();
    private Context mContext;
    private View root_view;
    private ToDoItem mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);

    public ToDoItemRecyclerViewAdapter(List<ToDoItem> _toDoItems, View rootview) {
        this.items = _toDoItems;
        root_view = rootview;
        mContext = rootview.getContext();
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
        if(holder.mItem.getIsDone()){

            holder.mView.setVisibility(View.GONE);
            ViewGroup.LayoutParams llp = holder.mLinearLayout.getLayoutParams();
            llp.height=0;llp.width=0;
            holder.mView.setLayoutParams(llp);
        }
        else{
            holder.mView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams llp = holder.mLinearLayout.getLayoutParams();
            llp.height=holder.mView.getContext().getResources().getDimensionPixelSize(R.dimen.TodoItemGridHeight);
            llp.width=ViewGroup.LayoutParams.MATCH_PARENT;
            holder.mView.setLayoutParams(llp);
        }
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
            holder.mCourseSourceView.setVisibility(View.VISIBLE);
        }else{
            holder.mCourseSourceView.setVisibility(View.GONE);
        }

        if(items.get(position).getEndTime().getTime()<new Date().getTime()){
            holder.mDueTimeView.setTextColor(0xfffa4c4b);
            holder.ddlIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_date_time_red_24));
        }
        else if(items.get(position).getEndTime().getTime()<new Date().getTime()+86400000*3){
                holder.mDueTimeView.setTextColor(0xfffbaa61);
                holder.ddlIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_date_time_orange_24));
            }
        else{
            holder.mDueTimeView.setTextColor(0xff757575);
            holder.ddlIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_date_time_grey_24));
        }

        //holder.mCheckBox.setChecked(items.get(position).getIsDone());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventDescIntent = new Intent(mContext, EventDescriptionActivity.class);
                eventDescIntent.putExtra("SCHEDULE", JSON.toJSONString(items.get(position)));
                mContext.startActivity(eventDescIntent);
/*                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }*/
            }
        });

        holder.mCheckBox.setOnClickListener(
                (view) -> {
                    onItemCompleted(position);
                }

        );
        if(items.get(position).isSyncing){
            holder.mSyncIndicator.setVisibility(View.VISIBLE);
        }
        else{
            holder.mSyncIndicator.setVisibility(View.GONE);
        }


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
        if(isSyncing_Global){
            notifyItemChanged(position);
            Toast.makeText(mContext,"正在和教学网同步，请稍后操作",Toast.LENGTH_LONG).show();
            return;
        }
        if(items.get(position).getFromCourse()){
            items.get(position).isSyncing = true;
            notifyItemChanged(position);
            GetHomeworkSubmissionStatus getHomeworkSubmissionStatus = new GetHomeworkSubmissionStatus(position, true);
            getHomeworkSubmissionStatus.execute();
            return;
        }
        else{

            mJustDeletedToDoItem = items.remove(position);
            mIndexOfDeletedToDoItem = position;
            if(mContext instanceof MainActivity)
                ((MainActivity)mContext).broadcastDatasetChanged(items);
            notifyItemRemoved(position);
        }
        //TODO: wakeup Snackbar to Withdraw
    }

    @Override
    public void onItemCompleted(int position) {
        if(isSyncing_Global){
            notifyItemChanged(position);

            Toast.makeText(mContext,"正在和教学网同步，请稍后操作",Toast.LENGTH_LONG).show();
            return;
        }
        if(items.get(position).getFromCourse()){
            items.get(position).isSyncing = true;
            notifyItemChanged(position);
            GetHomeworkSubmissionStatus getHomeworkSubmissionStatus = new GetHomeworkSubmissionStatus(position, false);
            getHomeworkSubmissionStatus.execute();
            return;
        }
        else
        {
            items.get(position).setIsDone(true);
            if(mContext instanceof MainActivity) {
                ((MainActivity) mContext).broadcastDatasetChanged(items);
                ((MainActivity) mContext).broadcaseNoDDL();
            }
            notifyItemChanged(position);
        }
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
        public final ConstraintLayout mSyncIndicator;
        public ImageButton mCheckBox;
        public ToDoItem mItem;
        public final ConstraintLayout mLinearLayout;
        public ImageView ddlIcon;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.todo_item_title);
            mDueTimeView = (TextView) view.findViewById(R.id.todo_item_description);
            mEventTypeView =(TextView) view.findViewById(R.id.todo_item_eventtype);
            mCourseSourceView= (TextView) view.findViewById(R.id.todo_item_coursesource);
            mCheckBox =(ImageButton) view.findViewById(R.id.todo_item_checkbox);
            mLinearLayout = (ConstraintLayout)view.findViewById(R.id.todo_item_container);
            mSyncIndicator = (ConstraintLayout)view.findViewById(R.id.submit_status_sync_indicator);
            ddlIcon = view.findViewById(R.id.todo_item_duetime_icon);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class GetHomeworkSubmissionStatus extends AsyncTask<Void, Void, Integer> {
        private int pos;
        private boolean isDelete;
        GetHomeworkSubmissionStatus(int _pos,boolean _isDelete){pos=_pos;isDelete = _isDelete;}
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                isSyncing_Global =true;
                CourseLoginInfoModel courseLoginInfoModel;
                if(mContext!=null) {
                    courseLoginInfoModel = CourseLoginInfoModel.getCookie(mContext);
                    Boolean rst =items.get(pos).getSubmissionStatus(courseLoginInfoModel);
                    if(rst==null) throw new NullPointerException();
                    if(rst){
                        return 0;
                    }
                    else{
                        return 1;
                    }
                }
                return 2;
            } catch (Exception e) {
//                e.printStackTrace();
                return 3;
            }
        }
        @Override
        protected void onPostExecute(final Integer returnStatus) {

            isSyncing_Global=false;
            items.get(pos).isSyncing=false;
                switch (returnStatus){
                    case 0:
                        items.get(pos).setIsDone(true);
                        notifyItemChanged(pos);
                        break;
                    case 1:
                        RaiseDialogIncompleteDeadline(pos);
                        break;
                    default:
                        notifyItemChanged(pos);
                        break;
                }
        }

        @Override
        protected void onCancelled() {
            notifyItemChanged(pos);
            super.onCancelled();
        }
    }
    private AlertDialog dialog ;
    public void RaiseDialogIncompleteDeadline(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View contentView = View.inflate(mContext, R.layout.alertdialog_red, null);
        TextView etName = (TextView) contentView.findViewById(R.id.alert_dialog_desc_no_submission);


        String desc = pangu.spacingText("确认完成了"+items.get(pos).getScheduleCourseSource()+"的"+items.get(pos).getScheduleDescription()+"「"+items.get(pos).getScheduleTitle()+"」吗？");

        final SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(desc);
        mSpannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD),desc.indexOf("「")
                , desc.indexOf("」")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        etName.setText(mSpannableStringBuilder);
        builder.setView(contentView);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                items.get(pos).setIsDone(true);
                if(mContext instanceof MainActivity){
                    ((MainActivity)mContext).broadcastDatasetChanged(items);
                ((MainActivity) mContext).broadcaseNoDDL();}
                notifyItemChanged(pos);
            }
        });
        builder.setNegativeButton("撤销", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                notifyItemChanged(pos);
            }
        });
        dialog = builder.create();
        builder.show();
    }
}
