package dev.dcarminatti.rja_api.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalRequestsHandled;
    private int averageResponseTime;
    private int averageResolutionTime;
    private int totalResolvedRequests;
    private int totalPendingRequests;
    private int totalEscalatedRequests;

    private User technician;
}
