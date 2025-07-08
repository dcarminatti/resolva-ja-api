package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.TicketSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketSubcategoryRepository extends JpaRepository<TicketSubcategory, Long> {
}
