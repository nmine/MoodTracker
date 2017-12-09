package be.nmine.moodtracker.model;

import com.google.gson.annotations.Expose;

import java.util.Calendar;
import java.util.HashMap;

import be.nmine.moodtracker.model.enumModel.Mood;

import static be.nmine.moodtracker.util.Constants.DATE_FORMATER;
import static be.nmine.moodtracker.util.Constants.GSON;
import static be.nmine.moodtracker.util.Constants.getSubtractDay;

/**
 * Created by n1mbus on 03-12-17.
 */

public class Moods {

    @Expose
    private HashMap<String, String> moodOfDay;

    public Moods() {
        moodOfDay = new HashMap<>();
    }

    public String json() {
        return GSON.toJson(this);
    }

    public Moods moodOfDay(Mood mood) {
        String format = DATE_FORMATER.format(Calendar.getInstance().getTime());
        moodOfDay.put(format,mood.name());
        return this;
    }

    public Mood getMoodOfDayBefore(int numberOfDayBefore) {
        String mood = moodOfDay.get(
                DATE_FORMATER.format(getSubtractDay(-numberOfDayBefore)));
        return Mood.valueOf(mood != null ? mood : "HAPPY");
    }

    public static Moods fromJson(String json) {
        return GSON.fromJson(json, Moods.class);
    }

}