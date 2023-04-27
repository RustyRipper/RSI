package org.example.repositories;

import org.example.jaxws.server_topdown.*;


import java.util.List;

public interface CarRepository2 {
    List<Car> getAllCars();

    Car getCar(int id) throws CarNotFoundEx_Exception;

    Car updateCar(int id, String brand, int year, boolean isElectric) throws
            CarNotFoundEx_Exception;

    boolean eraseCars();
    boolean deleteCar(int id) throws CarNotFoundEx_Exception;

    Car addCar(int id, String brand, int year, boolean isElectric) throws CarExistsEx_Exception;

    int countCars();

}
