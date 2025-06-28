package dev.dcarminatti.rja_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TechnicianNotAvailableException extends RuntimeException {
    
    private Long technicianId;
    private String technicianName;

    public TechnicianNotAvailableException(Long technicianId, String technicianName) {
        super(String.format("Technician '%s' (ID: %d) is not available for assignment", technicianName, technicianId));
        this.technicianId = technicianId;
        this.technicianName = technicianName;
    }

    public TechnicianNotAvailableException(Long technicianId) {
        super(String.format("Technician with ID %d is not available for assignment", technicianId));
        this.technicianId = technicianId;
    }

    public TechnicianNotAvailableException(String message) {
        super(message);
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }
}
