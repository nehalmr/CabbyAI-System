package com.cabbyai.notification.controller;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationControllerTest {
    private final NotificationController controller = new NotificationController();

    @Test
    void sendAcceptsNotificationAndReturnsDeliveryMetadata() {
        NotificationController.NotificationRequest request =
                new NotificationController.NotificationRequest(42L, "Ride update", "Driver arriving", "RIDE_UPDATE");

        Map<String, Object> response = controller.send(request);

        assertEquals(42L, response.get("userId"));
        assertEquals("ACCEPTED", response.get("status"));
    }
}
