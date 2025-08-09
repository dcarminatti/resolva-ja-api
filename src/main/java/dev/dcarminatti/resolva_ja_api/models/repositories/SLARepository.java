package dev.dcarminatti.resolva_ja_api.models.repositories;

import dev.dcarminatti.resolva_ja_api.models.entities.SLA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SLARepository extends JpaRepository<SLA, Long> {
}

