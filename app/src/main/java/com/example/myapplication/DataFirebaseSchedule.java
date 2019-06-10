package com.example.myapplication;

import java.util.HashMap;
import java.util.Map;

public class DataFirebaseSchedule {
    public String schedule;
    public String info;
    public int start_time;
    public int start_min;
    public int fin_time;
    public int fin_min;
    public int isOpen;
    public int color;

    public DataFirebaseSchedule(){

    }
    public DataFirebaseSchedule(String schedule, String info, int startTime, int startMin, int finTime, int finMin, int isOpen, int color){
        this.schedule = schedule;
        this.fin_min = finMin;
        this.fin_time = finTime;
        this.start_min = startMin;
        this.start_time = startTime;
        this.info = info;
        this.isOpen = isOpen;
        this.color = color;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("schedule", schedule);
        result.put("info", info);
        result.put("start_time", start_time);
        result.put("start_min", start_min);
        result.put("fin_time", fin_time);
        result.put("fin_min", fin_min);
        result.put("isOpen", isOpen);
        result.put("color", color);
        return  result;
    }
}
