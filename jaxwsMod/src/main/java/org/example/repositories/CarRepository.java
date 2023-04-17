package org.example.repositories;

import org.example.Car;
import org.example.exceptions.CarExistsEx;
import org.example.exceptions.CarNotFoundEx;

import java.util.List;

public interface CarRepository {
    List<Car> getAllCars();

    Car getCar(int id) throws CarNotFoundEx;

    Car updateCar(int id, String brand, int year, boolean isElectric) throws
            CarNotFoundEx;

    boolean deleteCar(int id) throws CarNotFoundEx;

    Car addCar(int id, String brand, int year, boolean isElectric) throws CarExistsEx;

    int countCars();

}
