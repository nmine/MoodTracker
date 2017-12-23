package be.nmine.moodtracker.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import be.nmine.moodtracker.R;
import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.repository.Repository;

/**
 * Created by n1mbus on 14-12-17.
 */

public class AutoSaveMoodReceiver extends BroadcastReceiver {

    private Repository mRepository;


    @Override
    public void onReceive(Context context, Intent intent) {
        mRepository = (Repository) context.getApplicationContext();
        String dailyMood = mRepository.getDailyMoodTemp();
        String dailyComment = mRepository.getDailyCommentTemp();
        if(dailyMood != null && !dailyMood.isEmpty()) {
            saveMood(Mood.valueOf(dailyMood));
            sendNotification(context);
        }
        if(dailyComment != null && !dailyComment.isEmpty()) {
            saveComment(dailyComment);
        }

    }

    private void saveMood(Mood dailyMood) {
        mRepository.saveMood(dailyMood,1);
        mRepository.removeMoodOfTheDayTemp();
    }

    private void saveComment(String dailyComment) {
        mRepository.saveComment(dailyComment,1);
        mRepository.removeCommentdOfTheDayTemp();
    }

    private void sendNotification(Context context){
        String NOTIFICATION_ID = "channel_id_01";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Save Done !")
                .setContentText("Your daily mood has been saved");


        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(1, builder.build());
    }
}
