package dev.dcarminatti.resolva_ja_api.api.dtos;

import java.util.Date;

public record NotificationDTO(Long id, String message, String sendDate, String type, Long userId, Long ticketId) {}

