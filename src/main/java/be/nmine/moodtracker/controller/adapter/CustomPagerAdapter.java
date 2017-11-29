package be.nmine.moodtracker.controller.adapter;
/**
 * Created by Nicolas Mine on 29-11-17.
 */
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nmine.moodtracker.model.PagerItems;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        PagerItems pagerItem = PagerItems.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(pagerItem.getLayoutId(), collection, false);
        collection.setRotation(-90f);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return PagerItems.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        PagerItems customPagerEnum = PagerItems.values()[position];
        return mContext.getString(customPagerEnum.getTitleId());
    }

}
