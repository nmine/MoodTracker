package be.nmine.moodtracker.model;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by n1mbus on 03-12-17.
 */

public class Comments {
    public static final String DATE_PATTERN = "dd-MM-yyyyy";
    private HashMap<String, String> mapCommentDay;

    public Comments() {
        mapCommentDay = new HashMap<>();
    }

    public String getComment(Date date) {
        return mapCommentDay.get(date);
    }

    public Comments commentOfDay(String comment) {
        SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_PATTERN);
        String format = dateFormater.format(Calendar.getInstance().getTime());
        mapCommentDay.put(format,comment);
        return this;
    }


    public String json() {
        return new Gson().toJson(this);
    }

    public static Comments fromJson(String json) {
        return new Gson().fromJson(json, Comments.class);
    }

}