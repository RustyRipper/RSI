package org.example;

import jakarta.xml.ws.Endpoint;
import org.example.jaxws.server_topdown.PersonService_Service;

import java.io.IOException;

import static java.lang.System.exit;
import static java.lang.System.lineSeparator;

public class ServiceHost2 {
    public static void main(String[] args) {
        System.out.println("Web Service PersonService is running ...");
        org.example.jaxws.server_topdown.PersonService psi = new PersonService2Impl();
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

