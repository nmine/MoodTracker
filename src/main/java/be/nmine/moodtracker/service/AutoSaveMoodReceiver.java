package be.nmine.moodtracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import be.nmine.moodtracker.model.Moods;
import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.util.Constants;

/**
 * Created by n1mbus on 14-12-17.
 */

public class AutoSaveMoodReceiver extends BroadcastReceiver {

    private SharedPreferences mPrefreences;

    @Override
    public void onReceive(Context context, Intent intent) {
        mPrefreences = PreferenceManager.getDefaultSharedPreferences(context);
        Moods moods = Moods.fromJson(mPrefreences.getString(Constants.MOOD_OF_THE_DAY, ""));
        moods.moodOfDay(Mood.HAPPY);
    }
}
