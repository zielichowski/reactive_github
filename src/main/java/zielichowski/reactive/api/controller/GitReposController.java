package zielichowski.reactive.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import zielichowski.reactive.api.model.Repository;
import zielichowski.reactive.api.response.ResponseFactory;
import zielichowski.reactive.api.service.GitHubService;


@RestController
@RequestMapping(value = "/repositories", produces = "application/vnd.api.app-v1+json")
public class GitReposController {

    private final GitHubService gitHubService;
    private final ResponseFactory responseFactory;

    public GitReposController(GitHubService gitHubService, ResponseFactory responseFactory) {
        this.gitHubService = gitHubService;
        this.responseFactory = responseFactory;
    }


    @RequestMapping(value = "/{owner}/{repositoryName}", method = RequestMethod.GET)
    public Mono<ResponseEntity<Repository>> getRepositories(@PathVariable String owner, @PathVariable String repositoryName, @RequestHeader HttpHeaders httpHeaders) {


        Mono<ResponseEntity<String>> repository = gitHubService.findRepository(owner, repositoryName, httpHeaders);
        return repository
                .map(responseFactory::create);

    }

}