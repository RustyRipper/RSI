package org.example.services;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.example.jaxws.server_topdown.*;
import org.example.repositories.CarRepository2;
import org.example.repositories.CarRepositoryImpl2;

import java.util.List;

@WebService(serviceName = "CarService2",
        endpointInterface = "org.example.services.CarService2")
public class CarServiceImpl2 implements CarService2 {
    private CarRepository2 dataRepository = new CarRepositoryImpl2();

    @WebMethod
    public Car getCar(int id) throws CarNotFoundEx_Exception {
        System.out.println("...called getCar id=" + id);
        return dataRepository.getCar(id);
    }

    @WebMethod
    public Car updateCar(int id, String brand, int year, boolean isElectric) throws CarNotFoundEx_Exception {
        System.out.println("...called updateCar");
        return dataRepository.updateCar(id, brand, year, isElectric);
    }

    @WebMethod
    public boolean deleteCar(int id) throws CarNotFoundEx_Exception {
        System.out.println("delete Car");
        return dataRepository.deleteCar(id);

    }

    @WebMethod
    public Car addCar(int id, String brand, int year, boolean isElectric) throws CarExistsEx_Exception {
        System.out.println("add Car");
        return dataRepository.addCar(id, brand, year, isElectric);
    }

    @WebMethod
    public int countCars() {
        System.out.println("count Car");
        return dataRepository.countCars();
    }

    @Override
    public boolean eraseCars() {
        System.out.println("Erase cars");
        return dataRepository.eraseCars();
    }

    @WebMethod
    public List<Car> getAllCars() {
        System.out.println("get all Cars");
        return dataRepository.getAllCars();
    }

    @Override
    public void displayAllCars() {
        System.out.println("display all Cars");
        for(Car car: dataRepository.getAllCars()){
            System.out.println(car.toString());
        }
    }

    @Override
    public void displayCar(int id)throws CarNotFoundEx_Exception {
        System.out.println("...called getCar id=" + id);
        System.out.println(dataRepository.getCar(id).toString());
    }

}
