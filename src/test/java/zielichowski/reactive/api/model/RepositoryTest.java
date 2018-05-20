package zielichowski.reactive.api.model;

import net.minidev.json.JSONObject;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class RepositoryTest {

    @Test
    public void shouldCreateCorrectRepository(){

        Repository repository = Repository.of(createResponseString());

        assertEquals("zielichowski",repository.getFullName());
        assertEquals("Test description",repository.getDescription());
        assertEquals("some clone url",repository.getCloneUrl());
        assertEquals(0,repository.getStars());
        assertEquals(LocalDateTime.parse("2018-03-14T14:47:03Z", DateTimeFormatter.ISO_DATE_TIME),repository.getCreatedAt());

    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowException(){
        Repository.of("Some wrong String");
    }

    private String createResponseString() {
        return getTestRepositoryObjectAsString();
    }

    public static String getTestRepositoryObjectAsString() {
        JSONObject repository = new JSONObject();
        repository.put("description", "Test description");
        repository.put("clone_url", "some clone url");
        repository.put("full_name", "zielichowski");
        repository.put("stargazers_count", "0");
        repository.put("created_at", "2018-03-14T14:47:03Z");

        return repository.toString();
    }
}
