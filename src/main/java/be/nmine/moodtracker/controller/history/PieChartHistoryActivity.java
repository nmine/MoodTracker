package be.nmine.moodtracker.controller.history;

import android.graphics.Color;
import android.os.Bundle;
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

/**
 * Created by Nicolas Mine on 14-12-17.
 */
public class PieChartHistoryActivity extends AppCompatActivity {

    private PieChart mPieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart_history);
        mPieChart = findViewById(R.id.piechart_history);

        // Set chart style
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleRadius(20);
        mPieChart.setTransparentCircleRadius(25);
        mPieChart.setEntryLabelColor(Color.parseColor("#000000"));
        //mPieChart.setUsePercentValues(true);
        mPieChart.setEntryLabelTextSize(15);
        mPieChart.setNoDataText("No data yet");
        mPieChart.setNoDataTextColor(Color.parseColor("#000000"));

        // Create array for colors + add data for pie display
        List<Integer> colors = new ArrayList<>();
        List<PieEntry> entries = new ArrayList<>();

        // Dynamically set Entries and colors
//        if(mSuperHappyNumber != 0){
        entries.add(new PieEntry(2, "Super Happy"));
        colors.add(getResources().getColor(R.color.banana_yellow));
//        }
//        if(mHappyNumber != 0){
        entries.add(new PieEntry(1, "Happy"));
        colors.add(getResources().getColor(R.color.light_sage));
//        }
//        if(mNormalNumber != 0){
        entries.add(new PieEntry(2, "Normal"));
        colors.add(getResources().getColor(R.color.cornflower_blue_65));
//        }
//        if(mDisappointedNumber != 0){
        entries.add(new PieEntry(1, "Disappointed"));
        colors.add(getResources().getColor(R.color.warm_grey));
//        }
//        if(mSadNumber != 0){
        entries.add(new PieEntry(1, "Sad"));
        colors.add(getResources().getColor(R.color.faded_red));
//        }

        // Create a dataSet to separate our data categories
        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(15);
        //pieDataSet.setValueFormatter(new PercentFormatter());

        // Control if color is empty
        if (!colors.isEmpty())
            pieDataSet.setColors(colors);

        // Create Data from our categories
        PieData data = new PieData(pieDataSet);

        // Control if entries are empty -> allow us to display or not the "noDataText"
        if (!entries.isEmpty())
            mPieChart.setData(data);

        // Add description using the total day since user use our app
//        mTotalDay = mSuperHappyNumber + mHappyNumber + mNormalNumber + mDisappointedNumber + mSadNumber;
        Description description = new Description();
//        if(mTotalDay <= 1){
        description.setText("Total since : " + 1 + " day");
//        } else {
//            description.setText("Total since : " + mTotalDay + " days");
//        }
        description.setTextSize(20);

        // Set the description + refresh the pieChart
        mPieChart.setDescription(description);
        mPieChart.invalidate();
    }
}
