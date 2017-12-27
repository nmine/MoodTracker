package be.nmine.moodtracker.model.enumModel;

import be.nmine.moodtracker.R;

/**
 * Created by Nicolas Mine on 29-11-17.
 */

public enum Mood {

    SAD(0, R.layout.view_sad,R.color.faded_red),
    DISAPPOINTED(1, R.layout.view_disappointed,R.color.warm_grey),
    NORMAL(2, R.layout.view_normal,R.color.cornflower_blue_65),
    HAPPY(3, R.layout.view_happy,R.color.light_sage),
    SUPER_HAPPY(4, R.layout.view_super_happy,R.color.banana_yellow);

    private int mId;
    private int mLayoutId;
    private int mColorId;

    Mood(int id, int layoutId, int colorId) {
        mId = id;
        mLayoutId = layoutId;
        mColorId = colorId;
    }

    public int getId() {
        return mId;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public int getColorId() {
        return mColorId;
    }

    public Mood valueOf(int id) {
        for (Mood mood : Mood.values()) {
            if (mood.getId() == id) {
                return mood;
            }
        }
        throw new RuntimeException("No mood exist for this id");
    }

}

