package zielichowski.reactive.api.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import zielichowski.reactive.api.model.Repository;

@Component
public class ResponseFactory {
    public ResponseEntity<Repository> create(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .eTag(responseEntity.getHeaders().getETag())
                    .body(Repository.of(responseEntity.getBody()));

        } else {
            return ResponseEntity
                    .status(responseEntity.getStatusCode())
                    .build();

        }
    }
}
