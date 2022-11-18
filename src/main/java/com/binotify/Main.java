package com.binotify;
import javax.xml.ws.*;

public class Main {
    public static void main(String[] args) {
        try {
            Endpoint.publish("http://0.0.0.0:3003/hello", new Kumaha());
            System.out.println("Kordas WBD Ganteng");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
