package com.example.restservice.repositories;

import com.example.restservice.exceptions.CarBadParams;
import com.example.restservice.exceptions.CarExistsEx;
import com.example.restservice.exceptions.CarFullListEx;
import com.example.restservice.exceptions.CarNotFoundEx;
import com.example.restservice.models.Car;
import com.example.restservice.models.CarStatus;

import java.util.List;

public interface CarRepository {
    List<Car> getAllCars();

    Car getCar(int id) throws CarNotFoundEx;

    Car updateCar(int id, String brand, int year, boolean isElectric) throws
            CarNotFoundEx, CarBadParams;

    Long deleteCar(int id) throws CarNotFoundEx;

    Car addCar(int id, String brand, int year, boolean isElectric) throws CarExistsEx, CarFullListEx, CarBadParams;

    Car updateStatus(int id, CarStatus carStatus) throws CarNotFoundEx;
}
