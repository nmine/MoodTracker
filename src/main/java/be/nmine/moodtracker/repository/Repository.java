package be.nmine.moodtracker.repository;

import java.util.List;

import be.nmine.moodtracker.model.Comments;
import be.nmine.moodtracker.model.Moods;
import be.nmine.moodtracker.model.enumModel.Mood;

/**
 * Created by Nicolas Mine on 23-12-17.
 */

public interface Repository {

   String getDailyMoodTemp();

   void saveDailyMoodTemp(Mood mood);

   void saveMood(Mood mood,int dayBefore);

   Moods getMoods();

   List<Mood> getMoodOfLastSevenDayBefore();

   Mood getMoodOfDayBefore(int numberOfDayBefore);

   void removeMoodOfTheDayTemp();

   Comments getComments();

   String getDailyCommentTemp();

   String getCommentOfDayBefore(int numberOfDayBefore);

   void setCommentOfDayTemp(String comment);

   void saveComment(String comment,int dayBefore);

   void removeCommentdOfTheDayTemp();}
