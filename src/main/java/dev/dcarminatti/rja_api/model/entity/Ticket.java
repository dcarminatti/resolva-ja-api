package dev.dcarminatti.rja_api.model.entity;

import br.ufjf.rsj.api.model.enums.Status;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Location location;
    private TicketSubcategory associatedSubcategory;
    private Status status;
    private User requester;
    private List<User> associatedTechnicians;
    private LocalDateTime openingDateTime;
    private LocalDateTime closingDateTime;
}
