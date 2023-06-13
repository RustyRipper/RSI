package com.example.restservice.repositories;



import com.example.restservice.exceptions.CarBadParams;
import com.example.restservice.exceptions.CarExistsEx;
import com.example.restservice.exceptions.CarFullListEx;
import com.example.restservice.exceptions.CarNotFoundEx;
import com.example.restservice.models.Car;
import com.example.restservice.models.CarStatus;

import java.util.ArrayList;
import java.util.List;

public class CarRepositoryImpl implements CarRepository{
    private List<Car> carList;
    private int maxCarsInList = 5;
    public CarRepositoryImpl() {
        carList = new ArrayList<>();
        carList.add(new Car(1, "audi" , 2009, true, CarStatus.NOT_REGISTERED));
        carList.add(new Car(2, "bmw", 2000, false, CarStatus.CRASHED));
        carList.add(new Car(3, "vw", 2011, true, CarStatus.REGISTERED));
    }
    public List<Car> getAllCars() {
        return carList;
    }
    public Car getCar(int id) throws CarNotFoundEx {
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                return theCar;
            }
        }
        throw new CarNotFoundEx();
    }
    public Car updateCar(int id, String brand, int year, boolean isElectric) throws
            CarNotFoundEx, CarBadParams {
        if(brand == null) throw new CarBadParams();
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                theCar.setBrand(brand);
                theCar.setYear(year);
                return theCar;
            }
        }
        throw new CarNotFoundEx();
    }
    public Long deleteCar(int id) throws CarNotFoundEx {
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                carList.remove(theCar);
                return Long.valueOf(id);
            }
        }
        throw new CarNotFoundEx();
    }
    public Car addCar(int id, String brand, int year, boolean isElectric) throws
            CarExistsEx, CarFullListEx, CarBadParams {

        if(brand == null) throw new CarBadParams();
        if(maxCarsInList == carList.size()){
            throw new CarFullListEx();
        }
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                throw new CarExistsEx();
            }
        }
        Car car = new Car(id, brand, year, isElectric, CarStatus.NOT_REGISTERED);
        carList.add(car);
        return car;
    }
    public Car updateStatus(int id, CarStatus carStatus) throws CarNotFoundEx{
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                theCar.setCarStatus(carStatus);
                return theCar;
            }
        }
        throw new CarNotFoundEx();
    }
}
