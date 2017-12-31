package be.nmine.moodtracker.controller.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import be.nmine.moodtracker.controller.MainActivity;
import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.repository.Repository;
import butterknife.BindView;

import static android.content.DialogInterface.OnClickListener;
import static android.text.TextUtils.isEmpty;
import static butterknife.ButterKnife.bind;
import static java.util.Arrays.asList;

/**
 * Created by Nicolas Mine  on 04-12-17.
 * Activity that manage the history of last seven days with colored bar
 */

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.seek_bar_day_1)
    RelativeLayout mMoodBar1;
    @BindView(R.id.seek_bar_day_2)
    RelativeLayout mMoodBar2;
    @BindView(R.id.seek_bar_day_3)
    RelativeLayout mMoodBar3;
    @BindView(R.id.seek_bar_day_4)
    RelativeLayout mMoodBar4;
    @BindView(R.id.seek_bar_day_5)
    RelativeLayout mMoodBar5;
    @BindView(R.id.seek_bar_day_6)
    RelativeLayout mMoodBar6;
    @BindView(R.id.seek_bar_day_7)
    RelativeLayout mMoodBar7;

    @BindView(R.id.day_minus_1_text)
    TextView mTextView1;
    @BindView(R.id.day_minus_2_text)
    TextView mTextView2;
    @BindView(R.id.day_minus_3_text)
    TextView mTextView3;
    @BindView(R.id.day_minus_4_text)
    TextView mTextView4;
    @BindView(R.id.day_minus_5_text)
    TextView mTextView5;
    @BindView(R.id.day_minus_6_text)
    TextView mTextView6;
    @BindView(R.id.day_minus_7_text)
    TextView mTextView7;

    private Repository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        bind(this);
        mRepository = (Repository) getApplicationContext();
        initMoodBars();
    }

    private void initMoodBars() {
        drawMoodBar();
        addCommentToBar();
    }

    private void drawMoodBar() {
        boolean noDataForLastSevenDay = true;
        int dayBefore = 1;
        for (RelativeLayout moodBar : moodBars()) {
            if (dayHasMoodSaved(dayBefore)) {
                initBarForDay(dayBefore, moodBar);
                noDataForLastSevenDay = false;
            }
            dayBefore++;
            moodBar.refreshDrawableState();
        }
        if (noDataForLastSevenDay) {
            displayAlertDialogAndSendToMainPage();
        }
    }

    private void initBarForDay(int dayBefore, RelativeLayout moodBar) {
        setWithAndColorBar(dayBefore, moodBar);
        setBarTextVisible(dayBefore);
    }

    private void setBarTextVisible(int dayBefore) {
        textViews().get(dayBefore - 1).setVisibility(View.VISIBLE);
    }

    private boolean dayHasMoodSaved(int dayBefore) {
        return !(mRepository.getMoodOfDayBefore(dayBefore) == null);
    }

    private void setWithAndColorBar(int dayBefore, RelativeLayout moodBar) {
        Pair withAndColors = getWithAndColorForMoodBarOfTheDay(dayBefore);
        moodBar.setBackgroundResource(withAndColors.color);
        //Use of Linearlayout to avoid  java.lang.ClassCastException: android.widget.LinearLayout$LayoutParams cannot be cast to android.widget.RelativeLayout$LayoutParams
        //See https://stackoverflow.com/questions/18655940/linearlayoutlayoutparams-cannot-be-cast-to-android-widget-framelayoutlayoutpar
        moodBar.getLayoutParams().width = withAndColors.margins;
    }

    private Pair getWithAndColorForMoodBarOfTheDay(int dayBefore) {
        Mood mood = mRepository.getMoodOfDayBefore(dayBefore);
        if (mood == null)
            return new Pair(0, R.color.warm_grey);
        return getMoodBarMarginAndColor(mood);
    }

    private Pair getMoodBarMarginAndColor(Mood mood) {
        switch (mood) {
            case SAD:
                return new Pair(marginsForSad(), mood.getColorId());
            case DISAPPOINTED:
                return new Pair(marginsForDisappointed(), mood.getColorId());
            case NORMAL:
                return new Pair(marginsForNormal(), mood.getColorId());
            case HAPPY:
                return new Pair(marginsForHappy(), mood.getColorId());
            case SUPER_HAPPY:
                return new Pair(marginsForSuperHappy(), mood.getColorId());
            default:
                return null;
        }
    }

    private int getWithScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
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
        return (int) (getWithScreen() * 0.25);
    }

    private int marginsForDisappointed() {
        return (int) (getWithScreen() * 0.35);
    }

    private int marginsForNormal() {
        return (int) (getWithScreen() * 0.5);
    }

    private int marginsForHappy() {
        return (int) (getWithScreen() * 0.85);
    }

    private int marginsForSuperHappy() {
        return getWithScreen();
    }

    class Pair {
        private int margins;
        private int color;

        Pair(int margins, int color) {
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

}
