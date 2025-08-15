package dev.dcarminatti.resolva_ja_api.models.entities;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    private SLA sla;
}
