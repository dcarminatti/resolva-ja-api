package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.SLA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SLARepository extends JpaRepository<SLA, Long> {
}
