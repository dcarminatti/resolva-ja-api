package dev.dcarminatti.rja_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SLAViolationException extends RuntimeException {
    
    private Long ticketId;
    private String slaName;
    private Integer expectedResponseTimeHours;
    private Integer actualResponseTimeHours;

    public SLAViolationException(Long ticketId, String slaName, Integer expectedResponseTimeHours, Integer actualResponseTimeHours) {
        super(String.format("SLA violation for ticket %d: '%s' expected %d hours, actual %d hours", 
              ticketId, slaName, expectedResponseTimeHours, actualResponseTimeHours));
        this.ticketId = ticketId;
        this.slaName = slaName;
        this.expectedResponseTimeHours = expectedResponseTimeHours;
        this.actualResponseTimeHours = actualResponseTimeHours;
    }

    public SLAViolationException(String message) {
        super(message);
    }

}
