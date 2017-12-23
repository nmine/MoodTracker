package be.nmine.moodtracker.controller;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.repository.Repository;
import be.nmine.moodtracker.repository.RepositoryImpl;
import be.nmine.moodtracker.service.AutoSaveMoodReceiver;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static android.view.View.OnClickListener;

/**
 * Created by Nicolas Mine on 29-11-17.
 */

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton mAddComment;
    private FloatingActionButton mGotToHistoryBar;
    private FloatingActionButton mGotToHistoryPieChart;
    private EditText mTextComment;
    private ViewPager mViewPager;
    private boolean isAutoSaveHasBeenInit;
    private Repository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mRepository = (Repository) getApplicationContext();
        setContentView(R.layout.activity_main);
        initElement();
        initDummyData();
        setPagerMoodOfTheDay();
        if(!isAutoSaveHasBeenInit)
            initAutoSaveEvent();
    }

    private void setPagerMoodOfTheDay() {
        mViewPager = findViewById(R.id.viewpager);
        String dailyMood = mRepository.getDailyMoodTemp();
        mViewPager.setAdapter(new MoodPagerAdapter(this, (RepositoryImpl)getApplicationContext()));
        View moodView = LayoutInflater
                .from(this)
                .inflate(getIdTodayLayoutMood(dailyMood), null);
        mViewPager.addView(moodView);
        mViewPager.setCurrentItem(getIdPagerTodayMood(dailyMood));
    }

    private int getIdTodayLayoutMood(String dailyMood) {
        if(dailyMood != null && !dailyMood.isEmpty()) {
            return Mood.valueOf(dailyMood).getLayoutId();
        }else {
            return Mood.HAPPY.getLayoutId();
        }
    }

    private int getIdPagerTodayMood(String dailyMood) {
        if(dailyMood != null && !dailyMood.isEmpty()) {
            return Mood.valueOf(dailyMood).getTitleId();
        }else {
            return Mood.HAPPY.getTitleId();
        }
    }

    private void initDummyData() {
//        mPrefreences.edit().putString(PREF_KEY_COMMENT_OF_THE_DAY, new Comments()
//                .dummytCommentOfDay("commentDay-1", 1)
////                .dummytCommentOfDay("commentDay-2", 2)
////                .dummytCommentOfDay("commentDay-3", 3)
////                .dummytCommentOfDay("commentDay-4", 4)
////                .dummytCommentOfDay("commentDay-5", 5)
////                .dummytCommentOfDay("commentDay-6", 6)
////                .dummytCommentOfDay("commentDay-7", 7)
//                .json())
//                .apply();
//        mPrefreences.edit().putString(PREF_KEY_MOODS, new Moods()
//                .dummyMoodOfDayBefore(Mood.NORMAL, 1)
////                .dummyMoodOfDayBefore(Mood.HAPPY, 2)
//                .dummyMoodOfDayBefore(Mood.DISAPPOINTED, 3)
////                .dummyMoodOfDayBefore(Mood.SUPER_HAPPY, 4)
//                .dummyMoodOfDayBefore(Mood.SAD, 5)
////                .dummyMoodOfDayBefore(Mood.SUPER_HAPPY, 6)
//                .dummyMoodOfDayBefore(Mood.NORMAL, 7)
//                .json())
//                .apply();
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
        mTextComment.setHint(R.string.main_dialog_hint_comment);
        //TODO set exiting comment of the day to dialog edittext
        new AlertDialog.Builder(this)
                .setView(mTextComment)
                .setPositiveButton(R.string.main_dialog_submit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mRepository.setCommentOfDayTemp(mTextComment.getText().toString());
                    }
                })

                .setNegativeButton(R.string.main_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeContextMenu();
                    }
                })
                .show();
    }

    private void initAutoSaveEvent() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = getBroadcast(this, 0, new Intent(this, AutoSaveMoodReceiver.class), FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        isAutoSaveHasBeenInit = true;
    }
}
