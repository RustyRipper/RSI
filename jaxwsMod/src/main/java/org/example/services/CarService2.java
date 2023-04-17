package org.example.services;


import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.example.jaxws.server_topdown.*;


import java.util.List;

@WebService
public interface CarService2 {
    @WebMethod
    Car getCar(int id) throws CarNotFoundEx_Exception;

    @WebMethod
    Car updateCar(int id, String brand, int year, boolean isElectric) throws CarNotFoundEx_Exception;

    @WebMethod
    boolean deleteCar(int id) throws CarNotFoundEx_Exception;

    @WebMethod
    Car addCar(int id, String brand, int year, boolean isElectric) throws CarExistsEx_Exception;

    @WebMethod
    int countCars();

    @WebMethod
    List<Car> getAllCars();

    @WebMethod
    void displayAllCars();

    @WebMethod
    void displayCar(int id) throws CarNotFoundEx_Exception;
}
