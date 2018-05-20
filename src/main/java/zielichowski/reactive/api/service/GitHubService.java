package zielichowski.reactive.api.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class GitHubService {

    @Value("${github.api.template}")
    private String GITHUB_URL_TEMPLATE;

    private final WebClient webClient;

    public GitHubService() {
        this.webClient = WebClient.create();

    }

    public Mono<ResponseEntity<String>> findRepository(String owner, String repositoryName, HttpHeaders httpHeaders) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(GITHUB_URL_TEMPLATE)
                .buildAndExpand(owner, repositoryName)
                .toUri();


        return webClient
                .get()
                .uri(uri)
                .header(HttpHeaders.IF_NONE_MATCH, httpHeaders.getFirst(HttpHeaders.IF_NONE_MATCH))
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(String.class));


    }

}
