package org.example.repositories;

import org.example.Car;
import org.example.exceptions.CarExistsEx;
import org.example.exceptions.CarNotFoundEx;

import java.util.ArrayList;
import java.util.List;

public class CarRepositoryImpl implements CarRepository{
    private List<Car> carList;
    public CarRepositoryImpl() {
        carList = new ArrayList<>();
        carList.add(new Car(1, "audi" , 2009, true));
        carList.add(new Car(2, "bmw", 2000, false));
        carList.add(new Car(3, "vw", 2011, true));
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
            CarNotFoundEx {
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                theCar.setBrand(brand);
                theCar.setYear(year);
                return theCar;
            }
        }
        throw new CarNotFoundEx();
    }
    public boolean deleteCar(int id) throws CarNotFoundEx {
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                carList.remove(theCar);
                return true;
            }
        }
        throw new CarNotFoundEx();
    }
    public Car addCar(int id, String brand, int year, boolean isElectric) throws
            CarExistsEx {
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                throw new CarExistsEx();
            }
        }
        Car car = new Car(id, brand, year, isElectric);
        carList.add(car);
        return car;
    }
    public int countCars() {
        return carList.size();
    }
}
