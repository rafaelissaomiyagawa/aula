package br.com.youready.curso.spring.boot.notification;

public interface NotificationService {
  void send(String recipient, String message);
}
