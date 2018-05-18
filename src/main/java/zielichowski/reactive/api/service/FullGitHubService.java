package zielichowski.reactive.api.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import zielichowski.reactive.api.model.Repository;

import java.net.URI;

@Component
public class FullGitHubService {

    @Value("${github.api.template}")
    private String GITHUB_URL_TEMPLATE;

    private final WebClient webClient;

    public FullGitHubService() {
        this.webClient = WebClient.create();

    }

    public Mono<ClientResponse> getRepository(String owner, String repositoryName) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(GITHUB_URL_TEMPLATE)
                .buildAndExpand(owner, repositoryName)
                .toUri();

        return webClient
                .get()
                .uri(uri)
                .header(HttpHeaders.IF_NONE_MATCH, "W/\"9399dbd151a93d8ded0f00404eef8e1e\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.github.v3+json")
                .exchange();


    }

    public Mono<Repository> getRepository(String owner, String repositoryName, HttpHeaders httpHeaders) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(GITHUB_URL_TEMPLATE)
                .buildAndExpand(owner, repositoryName)
                .toUri();

        return webClient
                .get()
                .uri(uri)
                .header(HttpHeaders.IF_NONE_MATCH, httpHeaders.getFirst(HttpHeaders.IF_NONE_MATCH))
                .retrieve()
                .onStatus(HttpStatus::is3xxRedirection, clientResponse -> Mono.empty())
                .bodyToMono(Repository.class);
    }
}
