package zielichowski.reactive.api.service;

import zielichowski.reactive.api.model.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class DefaultGitHubService implements GitHubService {

    @Value("${github.api.template}")
    private String GITHUB_URL_TEMPLATE;

    private final WebClient webClient;

    public DefaultGitHubService() {
        this.webClient = WebClient.create();

    }

    @Override
    public Mono<Repository> findRepository(String owner, String repositoryName) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(GITHUB_URL_TEMPLATE)
                .buildAndExpand(owner, repositoryName)
                .toUri();

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Repository.class);


    }
}
