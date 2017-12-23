package be.nmine.moodtracker.controller.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import be.nmine.moodtracker.R;
import be.nmine.moodtracker.model.Comments;
import be.nmine.moodtracker.model.Moods;
import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.repository.Repository;
import be.nmine.moodtracker.repository.RepositoryImpl;

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


    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView mTextView5;
    private TextView mTextView6;
    private TextView mTextView7;

    private DisplayMetrics mDisplayMetrics;
    private Repository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mRepository = (Repository) getApplicationContext();
        initMoodBars();
    }

    private void initMoodBars() {
        initBars();
        initTextView();
        drawBarForMood();
        addCommentToBar();
    }

    private void drawBarForMood() {
        setMarginsAndColor();
    }

    private void setMarginsAndColor() {
        int dayBefore = 1;
        for (RelativeLayout moodBar : moodBars()) {
            if(!(mRepository.getMoodOfDayBefore(dayBefore) == null)) {
                setMarginsBar(dayBefore, moodBar);
                setColorBar(dayBefore, moodBar);
                textViews().get(dayBefore-1).setVisibility(View.VISIBLE);
            }
            dayBefore++;
            moodBar.refreshDrawableState();
        }
    }

    private void setColorBar(int dayBefore, RelativeLayout moodBar) {
        moodBar.setBackgroundResource(getMarginsForMoodOfTheDay(dayBefore).color);
    }

    private void setMarginsBar( int dayBefore, RelativeLayout moodBar) {
        //Use of Linearlayout to avoid  java.lang.ClassCastException: android.widget.LinearLayout$LayoutParams cannot be cast to android.widget.RelativeLayout$LayoutParams
        //See https://stackoverflow.com/questions/18655940/linearlayoutlayoutparams-cannot-be-cast-to-android-widget-framelayoutlayoutpar
        moodBar.getLayoutParams().width = getMarginsForMoodOfTheDay(dayBefore).margins;
    }

    private Tuple getMarginsForMoodOfTheDay(int dayBefore) {
        Mood mood = mRepository.getMoodOfDayBefore(dayBefore);
        if(mood == null)
            return new Tuple(0, R.color.warm_grey);
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
        Comments comments = mRepository.getComments();
        int dayBefore = 1;
        for (RelativeLayout moodBar : moodBars()) {
            String commentOfDayBefore = mRepository.getCommentOfDayBefore(dayBefore);
            if (comments != null && commentOfDayBefore != null) {
                addNoteToBar(commentOfDayBefore, dayBefore);
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

    private List<TextView> textViews() {
        return asList(mTextView1, mTextView2 , mTextView3, mTextView4, mTextView5, mTextView6, mTextView7);
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

    private void initTextView() {
        mTextView1 = findViewById(R.id.day_minus_1_text);
        mTextView2 = findViewById(R.id.day_minus_2_text);
        mTextView3 = findViewById(R.id.day_minus_3_text);
        mTextView4 = findViewById(R.id.day_minus_4_text);
        mTextView5 = findViewById(R.id.day_minus_5_text);
        mTextView6 = findViewById(R.id.day_minus_6_text);
        mTextView7 = findViewById(R.id.day_minus_7_text);
    }


}
