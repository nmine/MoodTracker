package be.nmine.moodtracker.model;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import be.nmine.moodtracker.util.Constants;

import static be.nmine.moodtracker.model.Comments.fromJson;
import static org.junit.Assert.assertEquals;

/**
 * Created by Nicolas Mine on 01-01-18.
 */
public class CommentsTest {

    @Test
    public void can_serialize_comments_from_java_to_json() throws Exception {
        //Given
        Calendar calendar = new GregorianCalendar(2018,1,1,1,1,1);
        String comments_to_test = "comments_to_test";
        //When
        String json = new Comments()
                .putToMap(Constants.DATE_FORMATER.format(calendar.getTime()), comments_to_test)
                .json();
        //Then
        assertEquals(json,"{\"mapCommentDay\":{\"01-02-2018\":\"comments_to_test\"}}" );
    }

    @Test
    public void can_serialize_comments_from_json_to_jav() throws Exception {
        //Given
        String json = "{\"mapCommentDay\":{\"01-02-2018\":\"comments_to_test\"}}";
        //When
        Comments comments = fromJson(json);
        //Then
        assertEquals("comments_to_test", comments.getMapCommentDay().get("01-02-2018"));
    }
}