package zielichowski.reactive.api.response;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import zielichowski.reactive.api.model.Repository;

import static org.junit.Assert.assertEquals;
import static zielichowski.reactive.api.model.RepositoryTest.getTestRepositoryObjectAsString;

public class ResponseFactoryTest {
    private final ResponseFactory responseFactory = new ResponseFactory();

    @Test
    public void shouldCreateCorrectResponseEntity() {


        ResponseEntity<String> test = ResponseEntity
                .status(HttpStatus.OK)
                .eTag("1234")
                .body(createResponseString());

        ResponseEntity<Repository> actual = responseFactory.create(test);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(createTestRepository(), actual.getBody());
    }

    @Test
    public void shouldCreateResponseEntityWithStatus404() {
        ResponseEntity<String> test =
                ResponseEntity.status(404).build();

        ResponseEntity<Repository> actual = responseFactory.create(test);
        assertEquals(404, actual.getStatusCodeValue());
    }

    @Test
    public void shouldCreateResponseEntityWithStatus304() {
        ResponseEntity<String> test =
                ResponseEntity.status(304).build();

        ResponseEntity<Repository> actual = responseFactory.create(test);
        assertEquals(304, actual.getStatusCodeValue());
    }

    private String createResponseString() {
        return getTestRepositoryObjectAsString();
    }

    private Repository createTestRepository() {
        return Repository.of(createResponseString());
    }

}
