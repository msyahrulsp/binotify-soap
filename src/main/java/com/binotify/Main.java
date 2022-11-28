package com.binotify;

import com.binotify.controllers.CheckStatusController;
import com.binotify.controllers.GenerateAPIKeyController;

import javax.xml.ws.*;

public class Main {
    public static void main(String[] args) {
        try {
            Endpoint.publish("http://0.0.0.0:3002/ws", new Kumaha());
            Endpoint.publish("http://0.0.0.0:3002/subscription-status", new CheckStatusController());
            Endpoint.publish("http://0.0.0.0:3002/generate-api-key", new GenerateAPIKeyController());
            System.out.println("Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
