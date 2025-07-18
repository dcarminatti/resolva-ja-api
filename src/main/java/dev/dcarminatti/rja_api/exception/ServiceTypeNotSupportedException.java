package dev.dcarminatti.rja_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ServiceTypeNotSupportedException extends RuntimeException {
    
    private Long serviceTypeId;
    private String serviceTypeName;
    private Long technicianId;
    private String technicianName;

    public ServiceTypeNotSupportedException(Long serviceTypeId, String serviceTypeName, Long technicianId, String technicianName) {
        super(String.format("Service type '%s' (ID: %d) is not supported by technician '%s' (ID: %d)", 
              serviceTypeName, serviceTypeId, technicianName, technicianId));
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeName = serviceTypeName;
        this.technicianId = technicianId;
        this.technicianName = technicianName;
    }

    public ServiceTypeNotSupportedException(String message) {
        super(message);
    }

}
