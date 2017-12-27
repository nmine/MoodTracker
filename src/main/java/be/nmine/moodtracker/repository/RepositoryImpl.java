package be.nmine.moodtracker.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
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

    public static final String GLOBAL_PREFRENCE = "GLOAL_PREFRENCE";

    public static final String PREF_KEY_COMMENTS = "PREF_KEY_COMMENTS";

    public static final String PREF_KEY_MOODS = "PREF_KEY_MOODS";

    private static SharedPreferences mSharedPreferences;


    public static void init(Context context) {
        initSharePreferenceGlobable(context);
        initSharedPreferenceMoods();
        initSharedPreferenceComments();

    }

    private static void initSharedPreferenceComments() {
        Comments comments = Comments.fromJson(mSharedPreferences.getString(PREF_KEY_COMMENTS, ""));
        if (comments == null) {
            mSharedPreferences.edit().putString(PREF_KEY_COMMENTS, new Comments()
                    .json())
                    .apply();
        }
    }

    private static void initSharedPreferenceMoods() {
        Moods moods = Moods.fromJson(mSharedPreferences.getString(PREF_KEY_MOODS, ""));
        if (moods == null) {
            mSharedPreferences.edit().putString(PREF_KEY_MOODS, new Moods()
                    .json())
                    .apply();
        }
    }

    private static void initSharePreferenceGlobable(Context context) {
        if (mSharedPreferences == null)
            mSharedPreferences = context.getSharedPreferences(GLOBAL_PREFRENCE, MODE_PRIVATE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init(getApplicationContext());
    }

    @Override
    public String getTodayMood() {
        String date = todayAsString();
        return getMoods().getMoodOfDay().get(date);
    }

    private String todayAsString() {
        return DATE_FORMATER.format(new Date());
    }

    @Override
    public void saveDailyMood(Mood mood) {
        saveMood(mood,0);
    }

    @Override
    public void saveMood(Mood mood, int dayBefore) {
        Moods moods = getMoods();
        String date = DATE_FORMATER.format(getSubtractDay(-dayBefore));
        moods.getMoodOfDay().put(date, mood.name());
        mSharedPreferences.edit().putString(PREF_KEY_MOODS, moods.json())
                .apply();
    }

    private Moods getMoods() {
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
        if (!TextUtils.isEmpty(mood))
            return Mood.valueOf(mood);
        else
            return null;
    }

    private Comments getComments() {
        return Comments.fromJson(mSharedPreferences.getString(PREF_KEY_COMMENTS, ""));
    }

    @Override
    public String getCommentOfDayBefore(int numberOfDayBefore) {
        String dayString = DATE_FORMATER.format(getSubtractDay(-numberOfDayBefore));
        return getComments().getMapCommentDay().get(dayString);
    }

    @Override
    public String getTodayComment() {
        String dayString = DATE_FORMATER.format(getSubtractDay(0));
        return getComments().getMapCommentDay().get(dayString);
    }

    @Override
    public void saveTodayComment(String comment) {
        String json = getComments().putToMap(todayAsString(), comment).json();
        mSharedPreferences.edit().putString(PREF_KEY_COMMENTS, json)
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

}
