package be.nmine.moodtracker.model;

import com.google.gson.annotations.Expose;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static be.nmine.moodtracker.util.Constants.*;

/**
 * Created by n1mbus on 03-12-17.
 */

public class Comments {

    @Expose
    private HashMap<String, String> mapCommentDay;

    public Comments() {
        mapCommentDay = new HashMap<>();
    }

    public String getComment(Date date) {
        return mapCommentDay.get(date);
    }

    public String getCommentOfDayBefore(int numberOfDayBefore) {
        return mapCommentDay.get(
                DATE_FORMATER.format(getSubtractDay(-numberOfDayBefore)));
    }

    public Comments commentOfDay(String comment) {
        String format = DATE_FORMATER.format(Calendar.getInstance().getTime());
        mapCommentDay.put(format,comment);
        return this;
    }


    public String json() {
        return GSON.toJson(this);
    }

    public static Comments fromJson(String json) {
        return GSON.fromJson(json, Comments.class);
    }


}