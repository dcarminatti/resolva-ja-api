package dev.dcarminatti.rja_api.model.entity;


import dev.dcarminatti.rja_api.model.enums.LocationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocationType locationType;
    private String description;
    private Location parentLocation; // Optional
}
