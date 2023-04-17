package org.example;

import jakarta.xml.ws.Endpoint;
import org.example.jaxws.server_topdown.*;
import org.example.services.CarService2;
import org.example.services.CarServiceImpl2;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.exit;

public class ServiceHostTop {
    public static void main(String[] args) {
        System.out.println("Web Service CarService2 is running ...");
        CarService2 psi = new CarServiceImpl2();
        Endpoint.publish("http://localhost:8082/carservice", psi);
        System.out.println("Press ENTER to STOP CarService2 ...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exit(0);
    }
}

