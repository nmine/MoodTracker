package be.nmine.moodtracker.model;
import be.nmine.moodtracker.R;

/**
 * Created by Nicolas Mine on 29-11-17.
 */

public enum PagerItems {

    DISAPPOINTED(1, R.layout.view_disappointed),
    SAD(2, R.layout.view_sad),
    NORMAL(3, R.layout.view_normal),
    HAPPY(4, R.layout.view_happy),
    SUPER_HAPPY(5, R.layout.view_super_happy);


    private int mTitleId;
    private int mLayoutId;

    PagerItems(int titleId, int layoutId) {
        mTitleId = titleId;
        mLayoutId = layoutId;
    }

    public int getTitleId() {
        return mTitleId;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

}

