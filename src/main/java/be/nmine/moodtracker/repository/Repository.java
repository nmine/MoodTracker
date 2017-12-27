package be.nmine.moodtracker.repository;

import java.util.List;

import be.nmine.moodtracker.model.enumModel.Mood;

/**
 * Created by Nicolas Mine on 23-12-17.
 */

public interface Repository {

    String getTodayMood();

    List<Mood> getMoodOfLastSevenDayBefore();

    void saveDailyMood(Mood mood);

    void saveMood(Mood mood, int dayBefore);

    Mood getMoodOfDayBefore(int dayBefore);

    String getCommentOfDayBefore(int dayBefore);


    String getTodayComment();

    void saveTodayComment(String comment);

    void saveComment(String comment, int dayBefore);
}