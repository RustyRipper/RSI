package org.example;

import jakarta.xml.ws.Endpoint;

import java.io.IOException;

import static java.lang.System.exit;

public class ServiceHost {
    public static void main(String[] args) {
        System.out.println("Web Service PersonService is running ...");
        PersonService psi = new PersonServiceImpl();
        Endpoint.publish("http://localhost:8081/personservice", psi);
        System.out.println("Press ENTER to STOP PersonService ...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exit(0);
    }
}

