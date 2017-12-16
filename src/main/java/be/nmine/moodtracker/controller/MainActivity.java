package be.nmine.moodtracker.controller;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;

import java.util.Calendar;

import be.nmine.moodtracker.R;
import be.nmine.moodtracker.controller.adapter.MoodPagerAdapter;
import be.nmine.moodtracker.controller.history.HistoryActivity;
import be.nmine.moodtracker.controller.history.PieChartHistoryActivity;
import be.nmine.moodtracker.model.Comments;
import be.nmine.moodtracker.model.Moods;
import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.service.AutoSaveMoodReceiver;
import be.nmine.moodtracker.util.Constants;

import static android.app.PendingIntent.*;
import static android.text.TextUtils.isEmpty;
import static android.view.View.OnClickListener;
import static be.nmine.moodtracker.model.Comments.fromJson;
import static be.nmine.moodtracker.util.Constants.COMMENT_OF_THE_DAY;
import static be.nmine.moodtracker.util.Constants.MOOD_OF_THE_DAY;

/**
 * Created by Nicolas Mine on 29-11-17.
 */

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton mAddComment;
    private FloatingActionButton mGotToHistoryBar;
    private FloatingActionButton mGotToHistoryPieChart;
    private EditText mTextComment;
    private SharedPreferences mPrefreences;
    private ViewPager mViewPager;
    private boolean mAutoSaveInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViewPager();
        initPrefereneces();
        initElement();
        initDummyData();
        setPagerMoodOfTheDay();
        if(!mAutoSaveInit)
            initAutoSaveIntent();
    }

    private void setPagerMoodOfTheDay() {
        View moodView = LayoutInflater
                .from(this)
                .inflate(layoutMoodOfTheDay(), null);
        mViewPager.addView(moodView);
    }

    private int layoutMoodOfTheDay() {
        mPrefreences = PreferenceManager.getDefaultSharedPreferences(this);
        Moods moods = Moods.fromJson(mPrefreences.getString(Constants.MOOD_OF_THE_DAY, "HAPPY"));
        String todayMood = moods.getTodayMood() != null ? moods.getTodayMood() : Mood.HAPPY.name();
        return Mood.valueOf(todayMood).getLayoutId();
    }

    private void initDummyData() {
        mPrefreences.edit().putString(COMMENT_OF_THE_DAY, new Comments()
                .dummytCommentOfDay("commentDay-1", 1)
                .dummytCommentOfDay("commentDay-2", 2)
                .dummytCommentOfDay("commentDay-3", 3)
                .dummytCommentOfDay("commentDay-4", 4)
                .dummytCommentOfDay("commentDay-5", 5)
                .dummytCommentOfDay("commentDay-6", 6)
                .dummytCommentOfDay("commentDay-7", 7)
                .json())
                .apply();
        mPrefreences.edit().putString(MOOD_OF_THE_DAY, new Moods()
                .dummyMoodOfDayBefore(Mood.NORMAL, 1)
                .dummyMoodOfDayBefore(Mood.HAPPY, 2)
                .dummyMoodOfDayBefore(Mood.DISAPPOINTED, 3)
                .dummyMoodOfDayBefore(Mood.SUPER_HAPPY, 4)
                .dummyMoodOfDayBefore(Mood.SAD, 5)
                .dummyMoodOfDayBefore(Mood.SUPER_HAPPY, 6)
                .dummyMoodOfDayBefore(Mood.NORMAL, 7)
                .json())
                .apply();
    }

    private void initPrefereneces() {
        mPrefreences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MoodPagerAdapter(this));
    }

    private void initElement() {
        initAddNoteButton();
        initGoToHistoryButton();
    }

    private void initGoToHistoryButton() {
        mGotToHistoryPieChart = findViewById(R.id.float_button_item_go_to_history_pie_chart);
        mGotToHistoryPieChart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PieChartHistoryActivity.class));
            }
        });

        mGotToHistoryBar = findViewById(R.id.float_button_item_go_to_history_bar);
        mGotToHistoryBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });


    }

    private void initAddNoteButton() {
        mAddComment = findViewById(R.id.float_button_item_add_note);
        mAddComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAlertDialog();
            }
        });
    }

    private void displayAlertDialog() {
        mTextComment = new EditText(this);
        mTextComment.setHint("Commentaire");
        //TODO set exiting comment of the day to dialog edittext
        new AlertDialog.Builder(this)
                .setView(mTextComment)
                .setPositiveButton(R.string.main_dialog_submit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        saveCommentForDay();
                    }
                })

                .setNegativeButton(R.string.main_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeContextMenu();
                    }
                })
                .show();
    }

    private void saveCommentForDay() {
        Comments commentOfTheDay = null;
        String comment = mTextComment.getText().toString();
        if (!isEmpty(comment)) {
            if (preferenceIsEmpty())
                commentOfTheDay = new Comments();
            else
                commentOfTheDay = getCommentsOfDaysFromPreference();
            setNewCommentOfDayToPreference(comment, commentOfTheDay);
        }
    }

    private void setNewCommentOfDayToPreference(String comment, Comments commentOfTheDay) {
        mPrefreences.edit().putString(COMMENT_OF_THE_DAY, commentOfTheDay
                .commentOfDay(comment).json())
                .apply();
    }

    private Comments getCommentsOfDaysFromPreference() {
        return fromJson(mPrefreences.getString(COMMENT_OF_THE_DAY, ""));
    }

    private boolean preferenceIsEmpty() {
        return mPrefreences.getString(COMMENT_OF_THE_DAY, "").isEmpty();
    }


    private void initAutoSaveIntent() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 1);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        PendingIntent pi = getBroadcast(this, 0, new Intent(this, AutoSaveMoodReceiver.class), FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*60*60*24, pi);
        mAutoSaveInit = true;
    }
}
