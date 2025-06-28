package dev.dcarminatti.rja_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTicketStatusException extends RuntimeException {
    
    private String currentStatus;
    private String requestedStatus;
    private Long ticketId;

    public InvalidTicketStatusException(Long ticketId, String currentStatus, String requestedStatus) {
        super(String.format("Cannot change ticket %d status from '%s' to '%s'", ticketId, currentStatus, requestedStatus));
        this.ticketId = ticketId;
        this.currentStatus = currentStatus;
        this.requestedStatus = requestedStatus;
    }

    public InvalidTicketStatusException(String message) {
        super(message);
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getRequestedStatus() {
        return requestedStatus;
    }

    public Long getTicketId() {
        return ticketId;
    }
}
