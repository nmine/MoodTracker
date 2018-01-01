package be.nmine.moodtracker.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nicolas Mine on 08-12-17.
 */

public class Constants {

    private static final String DATE_PATTERN = "dd-MM-yyyy";

    public static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat(DATE_PATTERN);


    public static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static Date getSubtractDay(int dayBefore) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, dayBefore);
        return cal.getTime();
    }
}
