package com.binotify;

import com.binotify.controllers.CheckStatusController;
import com.binotify.controllers.NewSubscriberController;
import com.binotify.controllers.SubscriptionListController;
import com.binotify.controllers.UpdateSubscriptionController;

import javax.xml.ws.*;

public class Main {
    public static void main(String[] args) {
        try {
            Endpoint.publish("http://0.0.0.0:3003/subscription-status", new CheckStatusController());
            Endpoint.publish("http://0.0.0.0:3003/subscribe", new NewSubscriberController());
            Endpoint.publish("http://0.0.0.0:3003/subscription-list", new SubscriptionListController());
            Endpoint.publish("http://0.0.0.0:3003/subscription-update", new UpdateSubscriptionController());
            System.out.println("Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
