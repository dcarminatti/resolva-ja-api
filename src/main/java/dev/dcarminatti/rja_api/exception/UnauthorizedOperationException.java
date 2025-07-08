package dev.dcarminatti.rja_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedOperationException extends RuntimeException {
    
    private String userId;
    private String operation;
    private String resource;

    public UnauthorizedOperationException(String userId, String operation, String resource) {
        super(String.format("User '%s' is not authorized to perform '%s' on '%s'", userId, operation, resource));
        this.userId = userId;
        this.operation = operation;
        this.resource = resource;
    }

    public UnauthorizedOperationException(String message) {
        super(message);
    }

}
