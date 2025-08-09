package dev.dcarminatti.resolva_ja_api.models.repositories;

import dev.dcarminatti.resolva_ja_api.models.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

