package br.com.youready.curso.spring.boot.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsNotificationService implements NotificationService {

  private static final Logger log = LoggerFactory.getLogger(SmsNotificationService.class);
  private final String sender;

  public SmsNotificationService(String sender) {
    this.sender = sender;
  }

  @Override
  public void send(String recipient, String message) {
    log.info("[SMS] From: {} | To: {} | Message: {}", sender, recipient, message);
  }
}
