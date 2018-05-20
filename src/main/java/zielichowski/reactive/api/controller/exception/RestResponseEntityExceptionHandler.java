package zielichowski.reactive.api.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {


    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ApiErrorResponse> restClientResponseError(WebClientResponseException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ApiErrorResponse(ex.getRawStatusCode(), ex.getMessage()), HttpStatus.valueOf(ex.getRawStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> generalException(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ApiErrorResponse(500, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}