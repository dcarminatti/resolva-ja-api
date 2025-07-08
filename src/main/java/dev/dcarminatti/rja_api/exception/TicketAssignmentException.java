package dev.dcarminatti.rja_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class TicketAssignmentException extends RuntimeException {
    
    private Long ticketId;
    private Long technicianId;
    private String reason;

    public TicketAssignmentException(Long ticketId, Long technicianId, String reason) {
        super(String.format("Cannot assign ticket %d to technician %d: %s", ticketId, technicianId, reason));
        this.ticketId = ticketId;
        this.technicianId = technicianId;
        this.reason = reason;
    }

    public TicketAssignmentException(String message) {
        super(message);
    }

}
