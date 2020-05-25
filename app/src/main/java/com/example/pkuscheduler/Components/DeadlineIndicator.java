package com.example.pkuscheduler.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.R;
import com.example.pkuscheduler.ViewModels.ToDoItem;
import com.microsoft.officeuifabric.drawer.Drawer;

import java.util.List;

public class DeadlineIndicator extends FrameLayout {
    private List<ToDoItem> toDoItemList;
    private Context mContext;
    private TextView mCountText;
    private View mView;
    public DeadlineIndicator(@NonNull Context context) {
        this(context,null);
    }

    public DeadlineIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeadlineIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public DeadlineIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ScheduleCourseGrid);
        mView = inflater.inflate(R.layout.deadline_indicator, this, true);
        mCountText = mView.findViewById(R.id.deadline_indicator_count);
        mCountText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context instanceof AppCompatActivity){
                    Drawer drawer = Drawer.newInstance(R.layout.drawer_privacy_policy);
                    drawer.show(((AppCompatActivity)context).getSupportFragmentManager(),null);
                }
            }
        });
        a.recycle();
    }
    public void setToDoItemList(List<ToDoItem> _toDoItemList) {
        Log.e("JSON", JSON.toJSONString(toDoItemList));
        this.toDoItemList = _toDoItemList;
        mCountText.setText(String.valueOf(toDoItemList.size()));
    }

    public List<ToDoItem> getToDoItemList() {
        return toDoItemList;
    }
    public void setClickIndicatorListener(TextView.OnClickListener tvoc){
        mCountText.setOnClickListener(tvoc);
    }


}
