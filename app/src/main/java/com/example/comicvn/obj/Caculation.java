package com.example.comicvn.obj;

import java.util.Calendar;
import java.util.Date;

public class Caculation {

  public static String getMessageDate(Date date){
        String message;
        Date now = Calendar.getInstance().getTime();
        if (now.getYear() == date.getYear() && now.getMonth() == date.getMonth()){
            if (now.getDay() == date.getDay()) {
                int hours = now.getHours() - date.getHours();
                if (hours == 0){
                    int minutes = now.getMinutes() - date.getMinutes();
                    if (minutes == 0) message = now.getSeconds() - date.getSeconds() + " giây trước";
                    else message = minutes + " phút trước";
                }else{
                    message = hours + " giờ trước";
                }
            }
            else {
                int days = now.getDay() - date.getDay();
                if (days >= 7) message = days / 7 + " tuần trước";
                else message = days + " ngày trước";
            }
        }else{
            message = date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
        }

        return message;
    }
}
