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

import be.nmine.moodtracker.model.Comments;
import be.nmine.moodtracker.model.PagerItems;

import static android.content.Context.MODE_PRIVATE;
import static be.nmine.moodtracker.model.PagerItems.values;

public class CustomPagerAdapter extends PagerAdapter {

    public static final String MOOD_OF_THE_DAY = "MOOD_OF_THE_DAY";
    private Context mContext;
    private SharedPreferences mPrefreences;

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mPrefreences = ((Activity) mContext).getPreferences(MODE_PRIVATE);
    }


    @Override
    public Object instantiateItem(ViewGroup viewPager, int position) {
        PagerItems pagerItem = values()[position];
        setNewCommentOfDayToPreference(pagerItem.name());
        View viewMoodFromXml = LayoutInflater
                .from(mContext)
                .inflate(pagerItem.getLayoutId(), null);
        viewPager.addView(viewMoodFromXml);
        return viewMoodFromXml;
    }


    private void setNewCommentOfDayToPreference(String comment) {
        mPrefreences.edit().putString(MOOD_OF_THE_DAY, new Comments()
                .commentOfDay(comment)
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
        PagerItems customPagerEnum = values()[position];
        return mContext.getString(customPagerEnum.getTitleId());
    }

}
