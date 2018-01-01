package be.nmine.moodtracker.model;

import com.google.gson.annotations.Expose;

import java.util.Map;
import java.util.TreeMap;

import static be.nmine.moodtracker.util.Constants.*;

/**
 * Created by Nicolas Mine on 03-12-17.
 */

public class Comments {

    @Expose
    private final Map<String, String> mapCommentDay;

    public Comments() {
        mapCommentDay = new TreeMap<>();
    }

    public Map<String, String> getMapCommentDay() {
        return mapCommentDay;
    }

    public Comments putToMap(String date, String comment) {
        mapCommentDay.put(date,comment);
        return this;
    }


    public String json() {
        return GSON.toJson(this);
    }

    public static Comments fromJson(String json) {
        return GSON.fromJson(json, Comments.class);
    }


}