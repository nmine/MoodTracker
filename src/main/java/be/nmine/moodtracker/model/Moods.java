package be.nmine.moodtracker.model;

import com.google.gson.annotations.Expose;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import be.nmine.moodtracker.model.enumModel.Mood;

import static be.nmine.moodtracker.util.Constants.DATE_FORMATER;
import static be.nmine.moodtracker.util.Constants.GSON;
import static be.nmine.moodtracker.util.Constants.getSubtractDay;

/**
 * Created by Nicolas Mine  on 03-12-17.
 */

public class Moods {

    @Expose
    private Map<String, String> moodOfDay;

    public Moods() {
        moodOfDay = new TreeMap<>();
    }

    public String json() {
        return GSON.toJson(this);
    }

    public Moods moodOfDay(Mood mood) {
        String format = DATE_FORMATER.format(Calendar.getInstance().getTime());
        moodOfDay.put(format,mood.name());
        return this;
    }

    public String getTodayMood() {
        return moodOfDay.get(
                DATE_FORMATER.format(Calendar.getInstance().getTime()));
    }

    public Mood getMoodOfDayBefore(int numberOfDayBefore) {
        String mood = moodOfDay.get(
                DATE_FORMATER.format(getSubtractDay(-numberOfDayBefore)));
        return Mood.valueOf(mood != null ? mood : "HAPPY");
    }

    //For testing
    public Moods dummyMoodOfDayBefore(Mood mood, int numberOfDayBefore) {
        moodOfDay.put(DATE_FORMATER.format(getSubtractDay(-numberOfDayBefore)),mood.name());
        return this;
    }



    public static Moods fromJson(String json) {
        return GSON.fromJson(json, Moods.class);
    }

}