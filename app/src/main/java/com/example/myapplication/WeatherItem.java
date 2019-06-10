package com.example.myapplication;

import java.util.List;

public class WeatherItem {
    sysItem sys;
    List<weatherItem> weather;
    mainItem main;
    windItem wind;
    String name;

    public class sysItem{
        String country;
    }
    public class weatherItem{
        String main;
        String description;
        String icon;
    }
    public class mainItem{
        double temp;
        double humidity;
    }
    public class windItem{
        double speed;
    }
}
