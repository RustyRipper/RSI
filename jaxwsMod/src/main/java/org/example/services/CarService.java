package org.example.services;


import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.example.Car;
import org.example.exceptions.CarExistsEx;
import org.example.exceptions.CarNotFoundEx;

import java.util.List;

@WebService
public interface CarService {
    @WebMethod
    Car getCar(int id) throws CarNotFoundEx;

    @WebMethod
    Car updateCar(int id, String brand, int year, boolean isElectric) throws CarNotFoundEx;

    @WebMethod
    boolean deleteCar(int id) throws CarNotFoundEx;

    @WebMethod
    Car addCar(int id, String brand, int year, boolean isElectric) throws CarExistsEx;

    @WebMethod
    int countCars();

    @WebMethod
    List<Car> getAllCars();

    @WebMethod
    void displayAllCars();

    @WebMethod
    void displayCar(int id) throws CarNotFoundEx;
}
