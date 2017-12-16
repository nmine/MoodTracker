package be.nmine.moodtracker.model.enumModel;

import be.nmine.moodtracker.R;

/**
 * Created by Nicolas Mine on 29-11-17.
 */

public enum Mood {

    SAD(1, R.layout.view_sad),
    DISAPPOINTED(2, R.layout.view_disappointed),
    NORMAL(3, R.layout.view_normal),
    HAPPY(4, R.layout.view_happy),
    SUPER_HAPPY(5, R.layout.view_super_happy);


    private int mTitleId;
    private int mLayoutId;

    Mood(int titleId, int layoutId) {
        mTitleId = titleId;
        mLayoutId = layoutId;
    }

    public int getTitleId() {
        return mTitleId;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public Mood valueOf(int titleId) {
        for (Mood mood : Mood.values()) {
            if (mood.getTitleId() == titleId) {
                return mood;
            }
        }
        throw new RuntimeException("No mood exist for this id");
    }

}

