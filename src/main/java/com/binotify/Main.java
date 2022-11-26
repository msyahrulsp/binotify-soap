package com.binotify;
import com.binotify.controllers.CheckStatusController;

import javax.xml.ws.*;

public class Main {
    public static void main(String[] args) {
        try {
            Endpoint.publish("http://localhost:4789/ws", new Kumaha());
            Endpoint.publish("http://localhost:4789/subscription-status", new CheckStatusController());
            System.out.println("Kordas WBD Ganteng");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}