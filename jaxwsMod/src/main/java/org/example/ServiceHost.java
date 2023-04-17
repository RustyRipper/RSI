package org.example;

import jakarta.xml.ws.Endpoint;
import org.example.services.CarService;
import org.example.services.CarServiceImpl;

import java.io.IOException;

import static java.lang.System.exit;

public class ServiceHost {
    public static void main(String[] args) {
        System.out.println("Web Service CarService is running ...");
        CarService psi = new CarServiceImpl();
        Endpoint.publish("http://localhost:8082/carservice", psi);
        System.out.println("Press ENTER to STOP CarService ...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exit(0);
    }
}

