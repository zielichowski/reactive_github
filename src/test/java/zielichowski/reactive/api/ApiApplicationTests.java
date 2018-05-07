package zielichowski.reactive.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiApplicationTests {

    @LocalServerPort
    private int randomServerPort;
    private WireMockServer wireMockServer;

    private static final String GITHUB_URL = "/repos/zielichowski/Test";
    private static final String GITHUB_ERROR_URL = "/repos/zielichowski/error";

    private static final String API_URL = "repositories/zielichowski/Test";
    private static final String API_ERROR_URL = "repositories/zielichowski/error";

    @Before
    public void setup() {

        startWireMockServer();
        wireMockServer.
                stubFor(get(urlEqualTo(GITHUB_ERROR_URL))
                        .willReturn(errorResponse()
                        ));
        wireMockServer.
                stubFor(get(urlEqualTo(GITHUB_URL))
                        .willReturn(OkResponse()
                        ));


    }
    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void shouldReturnRepository() {
        given().
                port(randomServerPort).
                when().
                get(API_URL).
                then().
                statusCode(200)
                .body("full_name", equalTo("zielichowski"))
                .body("description", equalTo("Test description"))
                .body("clone_url", equalTo("some clone url"));


    }

    @Test
    public void shouldReturnNotFound() {
        given().
                port(randomServerPort).
                when().
                get(API_URL + "/wrong/path").
                then().
                statusCode(404)
                .body("status", equalTo(404));
    }

    @Test
    public void shouldReturnInternalErrorMessage() {
        given().
                port(randomServerPort).
                when().
                get(API_ERROR_URL).
                then().
                statusCode(500)
                .body("status", equalTo(500));

    }

    private ResponseDefinitionBuilder errorResponse() {
        ResponseDefinitionBuilder responseDefinitionBuilder = new ResponseDefinitionBuilder();
        responseDefinitionBuilder.withBody("some message").withStatus(200).withFault(Fault.MALFORMED_RESPONSE_CHUNK).withHeader("Content-Type", "application/json");
        return responseDefinitionBuilder;
    }

    private void startWireMockServer() {
        wireMockServer = new WireMockServer(9000);
        wireMockServer.start();
    }

    private ResponseDefinitionBuilder OkResponse() {
        ResponseDefinitionBuilder responseDefinitionBuilder = new ResponseDefinitionBuilder();
        JSONObject repository = new JSONObject();
        repository.put("full_name", "zielichowski");
        repository.put("description", "Test description");
        repository.put("clone_url", "some clone url");
        repository.put("stargazers_count", "0");
        repository.put("created_at", LocalDateTime.now().toString());
        responseDefinitionBuilder
                .withBody(repository.toString()).withStatus(200).withHeader("Content-Type", "application/json");
        return responseDefinitionBuilder;
    }

}
