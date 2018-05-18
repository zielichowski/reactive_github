package zielichowski.reactive.api.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import reactor.core.scheduler.Schedulers;
import zielichowski.reactive.api.model.Repository;
import zielichowski.reactive.api.service.FullGitHubService;
import zielichowski.reactive.api.service.GitHubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/repositories", produces = "application/vnd.api.app-v1+json")
public class GitReposController {

    private final GitHubService gitHubService;
    private final FullGitHubService fullGitHubService;

    public GitReposController(GitHubService gitHubService, FullGitHubService fullGitHubService) {
        this.gitHubService = gitHubService;
        this.fullGitHubService = fullGitHubService;
    }

    @RequestMapping(value = "/{owner}/{repositoryName}", method = RequestMethod.GET)
    public Mono<ResponseEntity<Repository>> getRepositories(@PathVariable String owner, @PathVariable String repositoryName) {

        return gitHubService
                .findRepository(owner, repositoryName)
                .map(repository -> {
                    repository.add(linkTo(methodOn(GitReposController.class).getRepositories(owner, repositoryName)).withSelfRel());
                    return new ResponseEntity<>(repository, HttpStatus.OK);
                })
                .subscribeOn(Schedulers.elastic())
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }


    @RequestMapping(value = "/{owner}/{repositoryName}/a", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "If-None-Match", dataType = "string")

    })
    public Mono<Repository> getRepositoriess(@PathVariable String owner, @PathVariable String repositoryName, @RequestHeader HttpHeaders httpHeaders) {


        System.out.println(httpHeaders.getFirst(HttpHeaders.IF_NONE_MATCH));
        return fullGitHubService.getRepository(owner, repositoryName, httpHeaders);



    }

}