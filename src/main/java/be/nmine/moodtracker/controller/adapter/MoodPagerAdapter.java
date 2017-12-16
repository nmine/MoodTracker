package be.nmine.moodtracker.controller.adapter;
/**
 * Created by Nicolas Mine on 29-11-17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.model.Moods;
import be.nmine.moodtracker.util.Constants;

import static android.content.Context.MODE_PRIVATE;
import static be.nmine.moodtracker.model.enumModel.Mood.values;
import static be.nmine.moodtracker.util.Constants.*;

public class MoodPagerAdapter extends PagerAdapter {


    private Context mContext;
    private SharedPreferences mPrefreences;

    public MoodPagerAdapter(Context context) {
        mContext = context;
        mPrefreences = ((Activity) mContext).getPreferences(MODE_PRIVATE);
    }


    @Override
    public Object instantiateItem(ViewGroup viewPager, int position) {
        Mood mood = values()[position];
        setNewMoodOfDayToPreference(mood);
        View viewMoodFromXml = LayoutInflater
                .from(mContext)
                .inflate(mood.getLayoutId(), null);
        viewPager.addView(viewMoodFromXml);
        return viewMoodFromXml;
    }


    private void setNewMoodOfDayToPreference(Mood mood) {
        mPrefreences.edit().putString(MOOD_OF_THE_DAY, new Moods()
                .moodOfDay(mood)
                .json())
                .apply();
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Mood customPagerEnum = values()[position];
        return mContext.getString(customPagerEnum.getTitleId());
    }

}
