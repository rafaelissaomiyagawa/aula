package br.com.youready.curso.spring.boot.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "application.notification")
public record NotificationProperties(
    @NotNull NotificationType type,
    @NotBlank String sender,
    boolean enabled) {

  public enum NotificationType {
    EMAIL,
    SMS
  }
}
