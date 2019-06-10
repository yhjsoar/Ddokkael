package com.example.myapplication;

import java.util.HashMap;
import java.util.Map;

public class DataFirebaseID {
    public String id;
    public String pw;

    public DataFirebaseID(){

    }

    public DataFirebaseID(String id, String pw){
        this.id = id;
        this.pw = pw;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pw", pw);
        return  result;
    }
}
