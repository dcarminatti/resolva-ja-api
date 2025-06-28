package dev.dcarminatti.rja_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    public Long getTicketId() {
        return ticketId;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public String getReason() {
        return reason;
    }
}
