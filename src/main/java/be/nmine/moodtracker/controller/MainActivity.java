package be.nmine.moodtracker.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;

import be.nmine.moodtracker.R;
import be.nmine.moodtracker.controller.adapter.MoodPagerAdapter;
import be.nmine.moodtracker.controller.history.HistoryActivity;
import be.nmine.moodtracker.controller.history.PieChartHistoryActivity;
import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.repository.Repository;
import be.nmine.moodtracker.repository.RepositoryImpl;

import static android.text.TextUtils.*;
import static android.view.View.OnClickListener;
import static be.nmine.moodtracker.model.enumModel.Mood.values;

/**
 * Created by Nicolas Mine on 29-11-17.
 */

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton mAddComment;
    private FloatingActionButton mGotToHistoryBar;
    private FloatingActionButton mGotToHistoryPieChart;
    private EditText mTextComment;
    private ViewPager mViewPager;
    private Repository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mRepository = (Repository) getApplicationContext();
        setContentView(R.layout.activity_main);
        initFloatButtons();
        initPager();
    }

    private void initPager() {
        mViewPager = findViewById(R.id.viewpager);
        String dailyMood = mRepository.getTodayMood();
        mViewPager.setAdapter(new MoodPagerAdapter(this, (RepositoryImpl) getApplicationContext()));
        saveHappyMoodIfNoMoodYetForToday(dailyMood);
        mViewPager.setCurrentItem(getIdTodayMood(dailyMood));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mRepository.saveDailyMood(values()[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void saveHappyMoodIfNoMoodYetForToday(String dailyMood) {
        if (isEmpty(dailyMood))
            mRepository.saveDailyMood(Mood.HAPPY);
    }


    private int getIdTodayMood(String dailyMood) {
        if (!isEmpty(dailyMood)) {
            return Mood.valueOf(dailyMood).getId();
        } else {
            return Mood.HAPPY.getId();
        }
    }

    private void initFloatButtons() {
        initAddNoteButton();
        initHistoryPieButtons();
        initHistoryButtons();
    }

    private void initHistoryButtons() {
        mGotToHistoryBar = findViewById(R.id.float_button_item_go_to_history_bar);
        mGotToHistoryBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
    }

    private void initHistoryPieButtons() {
        mGotToHistoryPieChart = findViewById(R.id.float_button_item_go_to_history_pie_chart);
        mGotToHistoryPieChart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PieChartHistoryActivity.class));
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
        initTextComment();
        new AlertDialog.Builder(this)
                .setView(mTextComment)
                .setPositiveButton(R.string.main_dialog_submit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mRepository.saveTodayComment(mTextComment.getText().toString());
                    }
                })

                .setNegativeButton(R.string.main_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        closeContextMenu();
                    }
                })
                .show();
    }

    private void initTextComment() {
        mTextComment = new EditText(this);
        setTodayCommentIfExist();
        mTextComment.setHint(R.string.main_dialog_hint_comment);
    }

    private void setTodayCommentIfExist() {
        String todayComment = mRepository.getTodayComment();
        if (!isEmpty(todayComment))
            mTextComment.setText(todayComment);
    }

}
