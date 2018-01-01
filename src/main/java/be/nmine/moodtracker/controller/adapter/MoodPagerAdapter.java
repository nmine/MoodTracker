package be.nmine.moodtracker.controller.adapter;
/**
 * Created by Nicolas Mine on 29-11-17.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.repository.Repository;

import static be.nmine.moodtracker.model.enumModel.Mood.values;

public class MoodPagerAdapter extends PagerAdapter {


    private final Context mContext;

    public MoodPagerAdapter(Context context) {
        mContext = context;
    }


    @Override
    public Object instantiateItem(ViewGroup viewPager, int position) {
        Mood mood = values()[position];
        View viewMoodFromXml = LayoutInflater
                .from(mContext)
                .inflate(mood.getLayoutId(), null);
        viewPager.addView(viewMoodFromXml);
        return viewMoodFromXml;
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
        return mContext.getString(customPagerEnum.getId());
    }

}
