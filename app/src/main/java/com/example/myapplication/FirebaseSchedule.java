package com.example.myapplication;

public class FirebaseSchedule {
    public String schedule;
    public int start_time;
    public int start_min;
    public int fin_time;
    public int fin_min;
    public int day;

    public FirebaseSchedule(){

    }
    public FirebaseSchedule(String schedule, int startTime, int startMin, int finTime, int finMin, int day){
        this.schedule = schedule;
        this.fin_min = finMin;
        this.fin_time = finTime;
        this.start_min = startMin;
        this.start_time = startTime;
        this.day = day;
    }
}
