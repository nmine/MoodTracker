package be.nmine.moodtracker.controller.history;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import be.nmine.moodtracker.R;
import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.repository.Repository;
import be.nmine.moodtracker.repository.RepositoryImpl;

/**
 * Created by Nicolas Mine on 14-12-17.
 */
public class PieChartHistoryActivity extends AppCompatActivity {

    private PieChart mPieChart;
    private Repository mRepository;
    private List<Mood> mMoodsCountTemp;
    private List<Mood> mMoodWeeks;
    private List<PieEntry> mEntries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart_history);
        mPieChart = findViewById(R.id.piechart_history);
        mRepository = (Repository) getApplicationContext();
        PieData data = initPieChartDataFromPreference();
        setDataToPieChart(data);
        initPieChart();
    }

    private void setDataToPieChart(PieData data) {
        if (!mEntries.isEmpty())
            mPieChart.setData(data);
    }

    private PieData initPieChartDataFromPreference() {
        mMoodWeeks = mRepository.getMoodOfLastSevenDayBefore();
        PieDataSet pieDataSet = setMoodAndColors();
        return new PieData(pieDataSet);
    }

    @NonNull
    private PieDataSet setMoodAndColors() {
        mMoodsCountTemp = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        mEntries = new ArrayList<>();
        for (Mood mood : mMoodWeeks) {
            if (mood != null) {
                int value = numberOfItem(mood, mMoodWeeks);
                if (value != 0) {
                    mEntries.add(new PieEntry(value, getTitle(mood)));
                    colors.add(getResources().getColor(mood.getColorId()));
                }
            }
        }
        PieDataSet pieDataSet = new PieDataSet(mEntries, "");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(15);
        if (!colors.isEmpty())
            pieDataSet.setColors(colors);
        return pieDataSet;
    }

    private String getTitle(Mood mood) {
        switch (mood) {
            case SAD:
                return getResources().getString(R.string.history_pie_mood_sad);
            case DISAPPOINTED:
                return getResources().getString(R.string.history_pie_mood_disappointed);
            case NORMAL:
                return getResources().getString(R.string.history_pie_mood_normal);
            case HAPPY:
                return getResources().getString(R.string.history_pie_mood_happy);
            case SUPER_HAPPY:
                return getResources().getString(R.string.history_pie_mood_supper_happy);
        }
        throw new RuntimeException();
    }

    private void initPieChart() {
        setMetaData();
        setDescription();
        mPieChart.invalidate();
    }

    private void setMetaData() {
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleRadius(20);
        mPieChart.setTransparentCircleRadius(25);
        mPieChart.setEntryLabelColor(Color.parseColor("#000000"));
        mPieChart.setEntryLabelTextSize(15);
        mPieChart.setNoDataText(getResources().getString(R.string.history_pie_no_data_yet));
        mPieChart.setNoDataTextColor(Color.parseColor("#000000"));
    }

    private void setDescription() {
        Description description = new Description();
        description.setText("Total since : " + mMoodWeeks.size() + " day");
        description.setTextSize(20);
        mPieChart.setDescription(description);
    }

    private int numberOfItem(Mood mood, List<Mood> moods) {
        if (mMoodsCountTemp.contains(mood))
            return 0;
        List<Mood> moodsCountList = new ArrayList<>();
        for (Mood item : moods) {
            if (item == mood)
                moodsCountList.add(item);
        }
        mMoodsCountTemp.add(mood);
        return moodsCountList.size();
    }
}
