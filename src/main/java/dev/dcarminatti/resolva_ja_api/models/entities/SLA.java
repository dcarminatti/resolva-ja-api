package dev.dcarminatti.resolva_ja_api.models.entities;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "sla")
public class SLA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @Column(name = "response_time_hours")
    private int responseTimeHours;

    @Column(name = "resolution_time_hours")
    private int resolutionTimeHours;
}
