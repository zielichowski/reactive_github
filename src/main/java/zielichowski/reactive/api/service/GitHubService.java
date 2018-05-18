package zielichowski.reactive.api.service;

import zielichowski.reactive.api.model.Repository;
import reactor.core.publisher.Mono;

public interface GitHubService {
    Mono<Repository> findRepository(String owner, String repositoryName);
}