package be.nmine.moodtracker.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import be.nmine.moodtracker.R;
import be.nmine.moodtracker.model.Comments;
import be.nmine.moodtracker.model.Moods;
import be.nmine.moodtracker.model.enumModel.Mood;

import static be.nmine.moodtracker.util.Constants.COMMENT_OF_THE_DAY;
import static be.nmine.moodtracker.util.Constants.MOOD_OF_THE_DAY;
import static java.util.Arrays.asList;

/**
 * Created by Nicolas Mine  on 04-12-17.
 */

public class HistoryActivity extends AppCompatActivity {

    private RelativeLayout mMoodBar1;
    private RelativeLayout mMoodBar2;
    private RelativeLayout mMoodBar3;
    private RelativeLayout mMoodBar4;
    private RelativeLayout mMoodBar5;
    private RelativeLayout mMoodBar6;
    private RelativeLayout mMoodBar7;

    private SharedPreferences mPrefreences;
    private DisplayMetrics mDisplayMetrics;
    private Comments mComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mPrefreences = PreferenceManager.getDefaultSharedPreferences(this);
        initMoodBars();
    }

    private void initMoodBars() {
        initBars();
        drawBarForMood();
        addCommentToBar();
    }

    private void drawBarForMood() {
        setMarginsAndColor();
    }

    private void setMarginsAndColor() {
        Moods moods = Moods.fromJson(mPrefreences.getString(MOOD_OF_THE_DAY, ""));
        int dayBefore = 1;
        for (RelativeLayout moodBar : moodBars()) {
            setMarginsBar(moods, dayBefore, moodBar);
            setColorBar(moods, dayBefore, moodBar);
            dayBefore++;
            moodBar.refreshDrawableState();
        }
    }

    private void setColorBar(Moods moods, int dayBefore, RelativeLayout moodBar) {
        moodBar.setBackgroundResource(getMarginsForMoodOfTheDay(moods, dayBefore).color);
    }

    private void setMarginsBar(Moods moods, int dayBefore, RelativeLayout moodBar) {
        //Use of Linearlayout to avoid  java.lang.ClassCastException: android.widget.LinearLayout$LayoutParams cannot be cast to android.widget.RelativeLayout$LayoutParams
        //See https://stackoverflow.com/questions/18655940/linearlayoutlayoutparams-cannot-be-cast-to-android-widget-framelayoutlayoutpar
        moodBar.getLayoutParams().width = getMarginsForMoodOfTheDay(moods, dayBefore).margins;
    }

    private Tuple getMarginsForMoodOfTheDay(Moods moods, int dayBefore) {
        Mood mood = moods.getMoodOfDayBefore(dayBefore);
        return getMoodBarMargin(mood);
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
                return new Tuple(marginsForSuperHappy(), R.color.banana_yellow);
            default:
                return null;
        }
    }

    private int marginsForSad() {
        return (int) (mDisplayMetrics.widthPixels * 0.25);
    }

    private int marginsForDisappointed() {
        return (int) (mDisplayMetrics.widthPixels * 0.35);
    }

    private int marginsForNormal() {
        return (int) (mDisplayMetrics.widthPixels * 0.5);
    }

    private int marginsForHappy() {
        return (int) (mDisplayMetrics.widthPixels * 0.85);
    }

    private int marginsForSuperHappy() {
        return mDisplayMetrics.widthPixels;
    }

    class Tuple {
        private int margins;
        private int color;

        public Tuple(int margins, int color) {
            this.margins = margins;
            this.color = color;
        }
    }


    private void addCommentToBar() {
        mComments = Comments.fromJson(mPrefreences.getString(COMMENT_OF_THE_DAY, ""));
        Comments comments = Comments.fromJson(mPrefreences.getString(COMMENT_OF_THE_DAY, ""));
        int dayBefore = 1;
        for (RelativeLayout moodBar : moodBars()) {
            if (comments.getCommentOfDayBefore(dayBefore) != null) {
                addNoteToBar(comments.getCommentOfDayBefore(dayBefore), dayBefore);
                dayBefore++;
            }
        }
    }

    private void addNoteToBar(final String comment, int dayBefore) {
        View view = commentTextViews().get(dayBefore-1);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToast(comment);
            }
        });
    }

    private void displayToast(String comment) {
        Toast.makeText(this, comment,
                Toast.LENGTH_LONG).show();
    }

    @NonNull
    private List<RelativeLayout> moodBars() {
        return asList(mMoodBar1, mMoodBar2, mMoodBar3, mMoodBar4, mMoodBar5, mMoodBar6, mMoodBar7);
    }

    private List<View> commentTextViews() {
        return asList(findViewById(R.id.day_minus_1_image),
                findViewById(R.id.day_minus_2_image),
                findViewById(R.id.day_minus_3_image),
                findViewById(R.id.day_minus_4_image),
                findViewById(R.id.day_minus_5_image),
                findViewById(R.id.day_minus_6_image),
                findViewById(R.id.day_minus_7_image));
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


}
