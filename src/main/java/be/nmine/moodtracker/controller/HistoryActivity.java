package be.nmine.moodtracker.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import java.util.List;

import be.nmine.moodtracker.R;
import be.nmine.moodtracker.model.Comments;
import be.nmine.moodtracker.model.Moods;
import be.nmine.moodtracker.model.enumModel.Mood;

import static be.nmine.moodtracker.util.Constants.*;
import static java.util.Arrays.*;

/**
 * Created by n1mbus on 04-12-17.
 */

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout mMoodBar1;
    private LinearLayout mMoodBar2;
    private LinearLayout mMoodBar3;
    private LinearLayout mMoodBar4;
    private LinearLayout mMoodBar5;
    private LinearLayout mMoodBar6;
    private LinearLayout mMoodBar7;

    private SharedPreferences mPrefreences;
    private DisplayMetrics mDisplayMetrics;
    private Comments mComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mPrefreences = getPreferences(MODE_PRIVATE);
        initMoodBars();
    }

    private void initMoodBars() {
        initBars();
        drawBarForMood();
        addCommentToBar();
    }

    private void addCommentToBar() {
        mComments = Comments.fromJson(mPrefreences.getString(COMMENT_OF_THE_DAY, ""));
        Comments comments = Comments.fromJson(mPrefreences.getString(COMMENT_OF_THE_DAY, ""));
        int dayBefore = 1;
        for (LinearLayout moodBar : moodBars()) {
            if(comments.getCommentOfDayBefore(dayBefore) != null) {
                addNoteToBar(comments.getCommentOfDayBefore(dayBefore),moodBar);
            }
        }
    }

    private void addNoteToBar(String moodOfDayBefore, LinearLayout moodBar) {

    }

    @NonNull
    private List<LinearLayout> moodBars() {
        return asList(mMoodBar1, mMoodBar2, mMoodBar3, mMoodBar4, mMoodBar5, mMoodBar6, mMoodBar7);
    }

    private void drawBarForMood() {
        setMarginsAndColor();
    }

    private void setMarginsAndColor() {
        Moods moods = Moods.fromJson(mPrefreences.getString(MOOD_OF_THE_DAY, ""));
        int dayBefore = 1;
        for (LinearLayout moodBar : moodBars()) {
            ((LinearLayout.LayoutParams) moodBar.getLayoutParams()).setMargins(0, 0, getMarginsForMoodOfTheDay(moods, dayBefore).margins, 0);
            moodBar.setBackgroundResource(getMarginsForMoodOfTheDay(moods, dayBefore++).color);
            moodBar.refreshDrawableState();
        }
    }

    private Tuple getMarginsForMoodOfTheDay(Moods moods, int dayBefore) {
        return getMoodBarMargin(moods.getMoodOfDayBefore(dayBefore));
    }

    private void initBars() {
        mMoodBar1 = findViewById(R.id.seek_bar_day_1);
        mMoodBar2 = findViewById(R.id.seek_bar_day_2);
        mMoodBar3 = findViewById(R.id.seek_bar_day_3);
        mMoodBar4 = findViewById(R.id.seek_bar_day_4);
        mMoodBar5 = findViewById(R.id.seek_bar_day_5);
        mMoodBar6 = findViewById(R.id.seek_bar_day_6);
        mMoodBar7 = findViewById(R.id.seek_bar_day_7);
    }

    private Tuple getMoodBarMargin(Mood mood) {
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        switch (mood) {
            case SAD:
                return new Tuple(marginsForSad(), R.color.faded_red);
            case DISAPPOINTED:
                return new Tuple(marginsForDisappointed(), R.color.warm_grey);
            case NORMAL:
                return new Tuple(marginsForNormal(), R.color.cornflower_blue_65);
            case HAPPY:
                return new Tuple(marginsForHappy(), R.color.light_sage);
            case SUPER_HAPPY:
                return new Tuple(marginsForSuperHappy(), R.color.light_sage);
            default:
                return null;
        }
    }

    private int marginsForSad() {
        return (int) (mDisplayMetrics.widthPixels * 0.9);
    }

    private int marginsForDisappointed() {
        return (int) (mDisplayMetrics.widthPixels * 0.7);
    }

    private int marginsForNormal() {
        return (int) (mDisplayMetrics.widthPixels * 0.5);
    }

    private int marginsForHappy() {
        return (int) (mDisplayMetrics.widthPixels * 0.3);
    }

    private int marginsForSuperHappy() {
        return 1;
    }

    class Tuple {
        private int margins;
        private int color;

        public Tuple(int margins, int color) {
            this.margins = margins;
            this.color = color;
        }
    }


}
