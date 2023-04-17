package org.example.exceptions;

import jakarta.xml.ws.WebFault;
@WebFault
public class CarNotFoundEx extends Exception {
    public CarNotFoundEx() {
        super("The specified car does not exist");
    }
}