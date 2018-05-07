package zielichowski.reactive.api.controller;

import zielichowski.reactive.api.model.Repository;
import zielichowski.reactive.api.service.GitHubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/repositories", produces = "application/vnd.api.app-v1+json")
public class GitReposController {

    private final GitHubService gitHubService;

    public GitReposController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @RequestMapping(value = "/{owner}/{repositoryName}", method = RequestMethod.GET)
    public Mono<ResponseEntity<Repository>> getRepositories(@PathVariable String owner, @PathVariable String repositoryName) {

        return gitHubService
                .getRepository(owner, repositoryName)
                .map(repository -> {
                    repository.add(linkTo(methodOn(GitReposController.class).getRepositories(owner, repositoryName)).withSelfRel());
                    return new ResponseEntity<>(repository, HttpStatus.OK);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

}