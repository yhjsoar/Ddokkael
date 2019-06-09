package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleView extends LinearLayout {
    private Context context;

    TextView schedule_name;
    TextView schedule_info;
    LinearLayout schedule_layout;

    public ScheduleView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ScheduleView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        init();
        getAttrs(attrs);
    }

    public ScheduleView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
        getAttrs(attrs, defStyleAttr);
    }

    private void init(){
        String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflaterService);
        View view = layoutInflater.inflate(R.layout.schedule, ScheduleView.this, false);
        addView(view);
        schedule_name = findViewById(R.id.schedule_name);
        schedule_info = findViewById(R.id.schedule_info);
        schedule_layout = findViewById(R.id.schedule_layout);

        schedule_name.setText("name");
        schedule_info.setText("info");
    }

    private void getAttrs(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ScheduleView);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ScheduleView, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray){
        String name_string = typedArray.getString(R.styleable.ScheduleView_name);
        schedule_name.setText(name_string);
        String info_string = typedArray.getString(R.styleable.ScheduleView_info);
        schedule_info.setText(info_string);
        Integer layout_color = typedArray.getColor(R.styleable.ScheduleView_backgroundColor, 0);
        schedule_layout.setBackgroundColor(layout_color);
        schedule_layout.setBackgroundColor(R.drawable.timetable_time_edge);

        typedArray.recycle();
    }
}
