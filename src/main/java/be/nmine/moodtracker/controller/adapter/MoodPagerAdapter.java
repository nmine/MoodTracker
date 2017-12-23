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
import be.nmine.moodtracker.repository.RepositoryImpl;

import static be.nmine.moodtracker.model.enumModel.Mood.values;

public class MoodPagerAdapter extends PagerAdapter {


    private Context mContext;
    private Repository mRepository;

    public MoodPagerAdapter(Context context, Repository repository) {
        mContext = context;
        mRepository = repository;
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
        mRepository.saveDailyMoodTemp(mood);
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

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

}