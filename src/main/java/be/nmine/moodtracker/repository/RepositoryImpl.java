package be.nmine.moodtracker.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import be.nmine.moodtracker.model.Comments;
import be.nmine.moodtracker.model.Moods;
import be.nmine.moodtracker.model.enumModel.Mood;

import static be.nmine.moodtracker.util.Constants.DATE_FORMATER;
import static be.nmine.moodtracker.util.Constants.getSubtractDay;

/**
 * Created by Nicolas Mine on 21-12-17.
 */

public class RepositoryImpl extends Application implements Repository {

    public static final String GLOBAL_PREFRENCE = "GLOBAL_PREFRENCE";

    public static final String PREF_KEY_COMMENT_OF_THE_DAY = "PREF_KEY_COMMENT_OF_THE_DAY";

    public static final String PREF_KEY_COMMENTS = "PREF_KEY_COMMENTS";

    public static final String PREF_KEY_MOODS = "PREF_KEY_MOODS";

    public static final String PREF_KEY_DAILY_MOOD = "PREF_KEY_DAILY_MOOD";
    public static final String EMPTY_STRING = "";

    private static SharedPreferences mSharedPreferences;


    public static void init(Context context) {
        if (mSharedPreferences == null)
            mSharedPreferences = context.getSharedPreferences(GLOBAL_PREFRENCE, MODE_PRIVATE);
        Moods moods = Moods.fromJson(mSharedPreferences.getString(PREF_KEY_MOODS, ""));
        if (moods == null) {
            mSharedPreferences.edit().putString(PREF_KEY_MOODS, new Moods()
                    .json())
                    .apply();
        }
        Comments comments = Comments.fromJson(mSharedPreferences.getString(PREF_KEY_COMMENTS, ""));
        if (comments == null) {
            mSharedPreferences.edit().putString(PREF_KEY_COMMENTS, new Comments()
                    .json())
                    .apply();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        init(getApplicationContext());
    }

    @Override
    public String getDailyMoodTemp() {
        String dailyMood = mSharedPreferences.getString(PREF_KEY_DAILY_MOOD, "");
        return dailyMood;
    }

    @Override
    public void saveDailyMoodTemp(Mood mood) {
        mSharedPreferences.edit().putString(PREF_KEY_DAILY_MOOD, mood.name()).apply();
    }

    @Override
    public void saveMood(Mood mood, int dayBefore) {
        Moods moods = getMoods();
        String date = DATE_FORMATER.format(getSubtractDay(-dayBefore));
        moods.getMoodOfDay().put(date, mood.name());
        mSharedPreferences.edit().putString(PREF_KEY_MOODS, moods.json())
                .apply();

    }

    @Override
    public Moods getMoods() {
        return Moods.fromJson(mSharedPreferences.getString(PREF_KEY_MOODS, ""));
    }


    @Override
    public List<Mood> getMoodOfLastSevenDayBefore() {
        List<Mood> moods = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            if (getMoodOfDayBefore(i) == null)
                continue;
            moods.add(getMoodOfDayBefore(i));
        }
        return moods;
    }

    @Override
    public Mood getMoodOfDayBefore(int numberOfDayBefore) {
        String mood = getMoods().getMoodOfDay().get(
                DATE_FORMATER.format(getSubtractDay(-numberOfDayBefore)));
        if (mood != null)
            return Mood.valueOf(mood);
        else
            return null;
    }

    @Override
    public void removeMoodOfTheDayTemp() {
        mSharedPreferences.edit().putString(PREF_KEY_DAILY_MOOD, EMPTY_STRING)
                .apply();
    }

    @Override
    public Comments getComments() {
        return Comments.fromJson(mSharedPreferences.getString(PREF_KEY_COMMENTS, ""));
    }

    @Override
    public String getDailyCommentTemp() {
        return mSharedPreferences.getString(PREF_KEY_COMMENT_OF_THE_DAY, "");
    }

    @Override
    public String getCommentOfDayBefore(int numberOfDayBefore) {
        String dayString = DATE_FORMATER.format(getSubtractDay(-numberOfDayBefore));
        return getComments().getMapCommentDay().get(dayString);
    }

    @Override
    public void setCommentOfDayTemp(String comment) {
        mSharedPreferences.edit().putString(PREF_KEY_COMMENT_OF_THE_DAY, comment)
                .apply();
    }

    @Override
    public void saveComment(String comment, int dayBefore) {
        Comments comments = getComments();
        String date = DATE_FORMATER.format(getSubtractDay(-dayBefore));
        comments.getMapCommentDay().put(date, comment);
        mSharedPreferences.edit().putString(PREF_KEY_COMMENTS, comments.json())
                .apply();

    }

    @Override
    public void removeCommentdOfTheDayTemp() {
        mSharedPreferences.edit().putString(PREF_KEY_COMMENT_OF_THE_DAY, EMPTY_STRING)
                .apply();
    }
}
