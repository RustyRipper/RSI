package org.example.repositories;

import org.example.jaxws.server_topdown.*;
import org.example.jaxws.server_topdown.CarExistsEx_Exception;
import org.example.jaxws.server_topdown.CarNotFoundEx_Exception;

import java.util.ArrayList;
import java.util.List;

public class CarRepositoryImpl2 implements CarRepository2{

    private List<Car> carList;
    public CarRepositoryImpl2() {
        carList = new ArrayList<>();
        Car car1 =new Car();
        car1.setId(1);
        car1.setBrand("audi");
        car1.setYear(2000);
        car1.setElectric(false);
        carList.add(car1);
        Car car2 =new Car();
        car2.setId(2);
        car2.setBrand("bmw");
        car2.setYear(2001);
        car2.setElectric(false);
        carList.add(car2);
        Car car3 =new Car();
        car3.setId(3);
        car3.setBrand("vw");
        car3.setYear(2003);
        car3.setElectric(true);
        carList.add(car3);
    }
    @Override
    public List<Car> getAllCars() {
        return carList;
    }

    @Override
    public Car getCar(int id) throws CarNotFoundEx_Exception {
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                return theCar;
            }
        }
        //throw new CarNotFoundEx();
        return null;
    }

    @Override
    public Car updateCar(int id, String brand, int year, boolean isElectric) throws CarNotFoundEx_Exception {
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                theCar.setBrand(brand);
                theCar.setYear(year);
                return theCar;
            }
        }
        //throw new CarNotFoundEx();
        return null;
    }

    @Override
    public boolean eraseCars() {
        if(carList.size() > 0){
            carList = new ArrayList<>();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCar(int id) throws CarNotFoundEx_Exception {
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                carList.remove(theCar);
                return true;
            }
        }
        //throw new CarNotFoundEx();
        return false;
    }

    @Override
    public Car addCar(int id, String brand, int year, boolean isElectric) throws CarExistsEx_Exception {
        for (Car theCar : carList) {
            if (theCar.getId() == id) {
                //throw new CarExistsEx();
            }
        }
        Car car = new Car();
        car.setId(id);
        car.setBrand(brand);
        car.setYear(year);
        car.setElectric(isElectric);
        carList.add(car);
        return car;
    }

    @Override
    public int countCars() {
        return carList.size();
    }
}
