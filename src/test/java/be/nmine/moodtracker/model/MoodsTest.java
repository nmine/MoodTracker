package be.nmine.moodtracker.model;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import be.nmine.moodtracker.model.enumModel.Mood;
import be.nmine.moodtracker.util.Constants;

import static org.junit.Assert.assertEquals;

/**
 * Created by Nicolas Mine on 02-01-18.
 */
public class MoodsTest {

    @Test
    public void can_serialize_moods_from_java_to_json() throws Exception {
        //Given
        Calendar calendar = new GregorianCalendar(2018, 1, 1, 1, 1, 1);
        Mood mood = Mood.SUPER_HAPPY;
        //When
        Moods moods = new Moods()
                .putToMap(Constants.DATE_FORMATER.format(calendar.getTime()), mood);
        //Then
        String json = moods.json();
        assertEquals(json, "{\"moodOfDay\":{\"01-02-2018\":\"SUPER_HAPPY\"}}");
    }

    @Test
    public void can_serialize_moods_from_json_to_java() throws Exception {
        //Given
        String json = "{\"moodOfDay\":{\"01-02-2018\":\"SUPER_HAPPY\"}}";
        //When
        Moods moods = Moods.fromJson(json);
        //Then
        assertEquals("SUPER_HAPPY", moods.getMoodOfDay().get("01-02-2018"));
        assertEquals(Mood.SUPER_HAPPY, Mood.valueOf(moods.getMoodOfDay().get("01-02-2018")));
    }
}