package com.example.pkuscheduler.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pkuscheduler.R;

import java.util.Objects;

public class ScheduleCourseGrid extends FrameLayout {
    private Context mContext;
    private View mView;
    private FrameLayout mLayout;


    private TextView textView;
    private Button button;
    private String displayTitleText;
    private Drawable displayButtonBackground;

    private int displayWidth;
    private int displayHeight;


    public ScheduleCourseGrid(@NonNull Context context) {
        this(context, null);
    }

    public ScheduleCourseGrid(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScheduleCourseGrid(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        init(context, attrs);
    }

    public ScheduleCourseGrid(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public String getDisplayTitleText() {
        return displayTitleText;
    }

    public void setDisplayTitleText(String _text) {
        if (_text != null) {
            this.displayTitleText = _text;
            int leftBracketEn = _text.indexOf("(");
            int leftBracketCh = _text.indexOf("（");
            int rightBracketEn = _text.lastIndexOf(")");
            int rightBracketCh = _text.lastIndexOf("）");
            if(leftBracketCh!=-1&&rightBracketCh!=-1){
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xa0ffffff);
                final SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(
                        _text.replace("（"," ").replace("）"," ")
                );
                mSpannableStringBuilder.setSpan(foregroundColorSpan,leftBracketCh,rightBracketCh+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                textView.setText(mSpannableStringBuilder);
            }
            else if(leftBracketEn!=-1&&rightBracketEn!=-1){
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xa0ffffff);
                final SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(
                        _text.replace("("," ").replace(")"," ")
                );
                mSpannableStringBuilder.setSpan(foregroundColorSpan,leftBracketEn,rightBracketEn+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                textView.setText(mSpannableStringBuilder);
            }
            else textView.setText(displayTitleText);
        }
    }
    public Drawable getDisplayButtonBackground() {
        return displayButtonBackground;
    }

    public void setDisplayButtonBackground(Drawable drawable) {
        if (drawable != null) {
            this.displayButtonBackground = drawable;
            button.setBackground(drawable);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = Objects.requireNonNull(inflater).inflate(R.layout.schedule_course_grid, this, true);
        textView = mView.findViewById(R.id.display_title);
        button = mView.findViewById(R.id.display_background);
        //mLayout = mView.findViewById(R.id.course_grid_parent_container);
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ScheduleCourseGrid);
        setDisplayButtonBackground(a.getDrawable(R.styleable.ScheduleCourseGrid_displayBackgroundResource));
        setDisplayTitleText(a.getString(R.styleable.ScheduleCourseGrid_displayTitleText));
        setDisplayHeight(a.getInteger(R.styleable.ScheduleCourseGrid_displayHeight, 200));
        setDisplayWidth(a.getInteger(R.styleable.ScheduleCourseGrid_displayWidth, 180));
        a.recycle();
    }

    public void setDisplayWidth(int _width) {
        displayWidth=_width;
        button.setWidth(_width);
        textView.setWidth(_width);
    }
    public int getDisplayWidth(){return displayWidth;}
    public int getDisplayHeight(){return displayHeight;}
    public void setDisplayHeight(int _height){
        displayHeight=_height;
        button.setHeight(_height);
        textView.setHeight(_height);
    }
}
