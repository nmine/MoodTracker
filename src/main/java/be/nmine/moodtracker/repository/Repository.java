package be.nmine.moodtracker.repository;

import java.util.List;

import be.nmine.moodtracker.model.enumModel.Mood;

/**
 * Created by Nicolas Mine on 23-12-17.
 */

public interface Repository {

    /**
     * Get the mood saved for the current day.
     * @return the @{@link String} value of the @{@link Mood} for this day
     * or null if no @{@link Mood} has been saved.
     */
    String getTodayMood();

    /**
     * Get the moods of the last seven day before current day.
     * @return @{@link List} of @{@link Mood} for the last seven day
     * the size of the list will be the numbers of @{@link Mood} saved.
     * @{@link List} cannot contain null value.
     */
    List<Mood> getMoodOfLastSevenDayBefore();

    /**
     * Save the @{@link Mood} for the current day
     * If a @{@link Mood} is already saved, it will be replaced by the new one
     * @param mood the @{@link Mood} to save
     */
    void saveDailyMood(Mood mood);

    /**
     * Save the @{@link Mood} for a day in the day (or today if second param is 0)
     * @param mood the @{@link Mood} to save
     * @param dayBefore number of day before today to sabe the @{@link Mood}
     */
    void saveMood(Mood mood, int dayBefore);

    /**
     * Get the @{@link Mood} saved for the day today minus the param
     * @param dayBefore the number of day in the past for this mood (0 for today)
     * @return the @{@link Mood} for this day
     */
    Mood getMoodOfDayBefore(int dayBefore);

    /**
     * Get the comment for the day minus the param (0 for today)
     * @param dayBefore
     * @return the String comment of the day
     */
    String getCommentOfDayBefore(int dayBefore);


    /**
     * Get the comment for the day minus the param (0 for today)
     * @return the String comment for today
     */
    String getTodayComment();

    /**
     * Save the comment for the current day
     */
    void saveTodayComment(String comment);

}