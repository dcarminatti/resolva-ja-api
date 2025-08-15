package dev.dcarminatti.resolva_ja_api.models.entities;

import lombok.*;
import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String creationDate;
    private String status;
    private String priority;
    private String deadline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
