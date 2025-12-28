package br.com.youready.curso.spring.boot.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotificationService implements NotificationService {

  private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);
  private final String sender;

  public EmailNotificationService(String sender) {
    this.sender = sender;
  }

  @Override
  public void send(String recipient, String message) {
    log.info("[EMAIL] From: {} | To: {} | Message: {}", sender, recipient, message);
  }
}
