package com.example.myapplication;

import java.util.HashMap;
import java.util.Map;

public class DataFirebaseFriend {
    String name;

    public DataFirebaseFriend(){

    }
    public DataFirebaseFriend(String name){
        this.name = name;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        return result;
    }
}
