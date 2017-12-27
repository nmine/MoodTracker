package be.nmine.moodtracker.controller.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import be.nmine.moodtracker.R;
import be.nmine.moodtracker.controller.MainActivity;
import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.repository.Repository;

import static android.content.DialogInterface.*;
import static android.text.TextUtils.*;
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
        initMoodDayBars();
        initTextView();
        drawMoodBar();
        addCommentToBar();
    }

    private void drawMoodBar() {
        boolean noData = true;
        int dayBefore = 1;
        for (RelativeLayout moodBar : moodBars()) {
            if (moodExistForThisDay(dayBefore)) {
                initBarForThisDay(dayBefore, moodBar);
                noData = false;
            }
            dayBefore++;
            moodBar.refreshDrawableState();
        }
        if (noData) {
            displayAlertDialogAndSendToMainPage();
        }
    }

    private void initBarForThisDay(int dayBefore, RelativeLayout moodBar) {
        setMarginsBar(dayBefore, moodBar);
        setColorBar(dayBefore, moodBar);
        setBarTextVisible(dayBefore);
    }

    private void setBarTextVisible(int dayBefore) {
        textViews().get(dayBefore - 1).setVisibility(View.VISIBLE);
    }

    private boolean moodExistForThisDay(int dayBefore) {
        return !(mRepository.getMoodOfDayBefore(dayBefore) == null);
    }

    private void setColorBar(int dayBefore, RelativeLayout moodBar) {
        moodBar.setBackgroundResource(getMarginsForMoodOfTheDay(dayBefore).color);
    }

    private void setMarginsBar(int dayBefore, RelativeLayout moodBar) {
        //Use of Linearlayout to avoid  java.lang.ClassCastException: android.widget.LinearLayout$LayoutParams cannot be cast to android.widget.RelativeLayout$LayoutParams
        //See https://stackoverflow.com/questions/18655940/linearlayoutlayoutparams-cannot-be-cast-to-android-widget-framelayoutlayoutpar
        moodBar.getLayoutParams().width = getMarginsForMoodOfTheDay(dayBefore).margins;
    }

    private Tuple getMarginsForMoodOfTheDay(int dayBefore) {
        Mood mood = mRepository.getMoodOfDayBefore(dayBefore);
        if (mood == null)
            return new Tuple(0, R.color.warm_grey);
        return getMoodBarMargin(mood);
    }

    private Tuple getMoodBarMargin(Mood mood) {
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        switch (mood) {
            case SAD:
                return new Tuple(marginsForSad(), mood.getColorId());
            case DISAPPOINTED:
                return new Tuple(marginsForDisappointed(), mood.getColorId());
            case NORMAL:
                return new Tuple(marginsForNormal(), mood.getColorId());
            case HAPPY:
                return new Tuple(marginsForHappy(), mood.getColorId());
            case SUPER_HAPPY:
                return new Tuple(marginsForSuperHappy(), mood.getColorId());
            default:
                return null;
        }
    }

    private void displayAlertDialogAndSendToMainPage() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.history_pie_no_data_yet)
                .setPositiveButton(R.string.main_dialog_submit, new OnClickListener() {
                    public void onClick(DialogInterface d, int whichButton) {
                        startActivity(new Intent(HistoryActivity.this, MainActivity.class));
                    }
                })
                .show();
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
        for (int dayBefore = 1; dayBefore <= 7; dayBefore++) {
            String comment = mRepository.getCommentOfDayBefore(dayBefore);
            if (!isEmpty(comment)) {
                addNoteToBar(comment, dayBefore);
            }
        }
    }

    private void addNoteToBar(final String comment, int dayBefore) {
        View view = setCommentIconVisible(dayBefore);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToast(comment);
            }
        });
    }

    @NonNull
    private View setCommentIconVisible(int dayBefore) {
        View view = commentTextViews().get(dayBefore - 1);
        view.setVisibility(View.VISIBLE);
        return view;
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
        return asList(mTextView1, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6, mTextView7);
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


    private void initMoodDayBars() {
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
