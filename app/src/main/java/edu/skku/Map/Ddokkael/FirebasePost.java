package edu.skku.Map.Ddokkael;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String schedule;
    public int start_time;
    public int start_min;
    public int fin_time;
    public int fin_min;
    public int day;

    public FirebasePost(){

    }

    public FirebasePost(String schedule, int startTime, int startMin, int finTime, int finMin, int day){
        this.schedule = schedule;
        this.fin_min = finMin;
        this.fin_time = finTime;
        this.start_min = startMin;
        this.start_time = startTime;
        this.day = day;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("schedule", schedule);
        result.put("start_time", start_time);
        result.put("start_min", start_min);
        result.put("fin_time", fin_time);
        result.put("fin_min", fin_min);
        result.put("day", day);
        return result;
    }

}
