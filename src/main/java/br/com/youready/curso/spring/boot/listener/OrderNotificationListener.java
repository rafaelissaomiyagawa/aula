package br.com.youready.curso.spring.boot.listener;

import br.com.youready.curso.spring.boot.event.OrderPlacedEvent;
import br.com.youready.curso.spring.boot.notification.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationListener {

  private final NotificationService notificationService;

  public OrderNotificationListener(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @EventListener
  public void handleOrderPlacedEvent(OrderPlacedEvent event) {
    notificationService.send(
        event.order().getCustomerEmail(),
        "Order " + event.order().getOrderNumber() + " placed successfully.");
  }
}
