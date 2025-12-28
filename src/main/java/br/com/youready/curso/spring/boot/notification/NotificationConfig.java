package br.com.youready.curso.spring.boot.notification;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NotificationProperties.class)
public class NotificationConfig {

  @Bean
  @ConditionalOnProperty(
      name = "application.notification.type",
      havingValue = "EMAIL",
      matchIfMissing = true)
  public NotificationService emailNotificationService(NotificationProperties properties) {
    return new EmailNotificationService(properties.sender());
  }

  @Bean
  @ConditionalOnProperty(name = "application.notification.type", havingValue = "SMS")
  public NotificationService smsNotificationService(NotificationProperties properties) {
    return new SmsNotificationService(properties.sender());
  }
}
