package edu.skku.Map.Ddokkael;

import java.util.HashMap;
import java.util.Map;

public class FirebaseName {
    public String name;

    public FirebaseName(){

    }

    public FirebaseName(String name){
        this.name = name;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        return result;
    }
}
