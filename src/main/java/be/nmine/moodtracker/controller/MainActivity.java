package be.nmine.moodtracker.controller;
import android.support.v4.view.ViewPager;
/**
 * Created by Nicolas Mine on 29-11-17.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import be.nmine.moodtracker.R;
import be.nmine.moodtracker.controller.adapter.CustomPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
        viewPager.setRotation(90);
    }

}
