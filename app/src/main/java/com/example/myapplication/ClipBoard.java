package com.example.myapplication;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;

public class ClipBoard {
    ClipboardManager cm;
    int apm;
    int hour;
    int minute;
    String schedule;
    Boolean isExist;

    ClipBoard(){
        apm = 0;
        hour = 0;
        minute = 0;
        schedule = "";
        isExist = false;
    }

    public void initialize(){
        apm = 0;
        hour = 0;
        minute = 0;
        schedule = "";
        isExist = false;
    }

    public void searchDatafromClipBoard(){
        if(cm != null && cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData.Item item = cm.getPrimaryClip().getItemAt(0);
            Boolean time=false, min = false;
            String data = item.getText().toString();
            String[] words = data.split(" ");
            for (String parse : words){
                if(time){
                    if(min){
                        schedule = parse;
                        isExist = true;
                        return;
                    }else{
                        if(parse.contains("분")){
                            String first, second = "";
                            first = Character.toString(parse.charAt(0));
                            if(parse.charAt(1)>='0' && parse.charAt(1)<='9' ){
                                second = Character.toString(parse.charAt(1));
                            }
                            String mn = first+second;
                            minute = Integer.parseInt(mn);
                            min = true;
                            continue;
                        } else{
                            schedule = parse;
                            isExist = true;
                            return;
                        }
                    }
                }
                else if(parse.charAt(0)>='0' && parse.charAt(0)<='9' && (parse.contains("시"))) {
                    String first, second = "";
                    first = Character.toString(parse.charAt(0));
                    if(parse.charAt(1)>='0' && parse.charAt(1)<='9' ){
                        second = Character.toString(parse.charAt(1));
                    }
                    String hr = first+second;
                    hour = Integer.parseInt(hr);
                    if(hour>=12){
                        hour -= 12;
                        apm = 1;
                    }
                    time = true;
                }
            }
        }
    }

    public String parseToString(){
        String a_pm = "am ";
        if(apm==1)  a_pm = "pm ";
        String min = "";
        if(minute != 0) min = Integer.toString(minute) + "분 ";
        String result = a_pm + Integer.toString(hour) + "시 " + min + schedule;
        return result;
    }
}