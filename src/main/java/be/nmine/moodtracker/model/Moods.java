package be.nmine.moodtracker.model;

import com.google.gson.annotations.Expose;

import java.util.Map;
import java.util.TreeMap;

import static be.nmine.moodtracker.util.Constants.GSON;

/**
 * Created by Nicolas Mine on 03-12-17.
 */

public class Moods {

    @Expose
    private final Map<String, String> moodOfDay;

    public Moods() {
        moodOfDay = new TreeMap<>();
    }

    public String json() {
        return GSON.toJson(this);
    }

    public Map<String, String> getMoodOfDay() {
        return moodOfDay;
    }

//    //For testing
//    public Moods dummyMoodOfDayBefore(Mood mood, int numberOfDayBefore) {
//        moodOfDay.put(DATE_FORMATER.format(getSubtractDay(-numberOfDayBefore)),mood.name());
//        return this;
//    }



    public static Moods fromJson(String json) {
        if(json == null || json.isEmpty())
            return new Moods();
        return GSON.fromJson(json, Moods.class);
    }

}