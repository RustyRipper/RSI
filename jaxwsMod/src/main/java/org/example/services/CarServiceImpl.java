package org.example.services;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.example.Car;
import org.example.exceptions.CarExistsEx;
import org.example.exceptions.CarFullListEx;
import org.example.exceptions.CarNotFoundEx;
import org.example.repositories.CarRepository;
import org.example.repositories.CarRepositoryImpl;

import java.util.List;

@WebService(serviceName = "CarService",
        endpointInterface = "org.example.services.CarService")
public class CarServiceImpl implements CarService {
    private CarRepository dataRepository = new CarRepositoryImpl();

    @WebMethod
    public Car getCar(int id) throws CarNotFoundEx {
        System.out.println("...called getCar id=" + id);
        return dataRepository.getCar(id);
    }

    @WebMethod
    public Car updateCar(int id, String brand, int year, boolean isElectric) throws CarNotFoundEx {
        System.out.println("...called updateCar");
        return dataRepository.updateCar(id, brand, year, isElectric);
    }

    @WebMethod
    public boolean deleteCar(int id) throws CarNotFoundEx {
        System.out.println("delete Car");
        return dataRepository.deleteCar(id);

    }

    @WebMethod
    public Car addCar(int id, String brand, int year, boolean isElectric) throws CarExistsEx, CarFullListEx {
        System.out.println("add Car");
        return dataRepository.addCar(id, brand, year, isElectric);
    }

    @WebMethod
    public int countCars() {
        System.out.println("count Car");
        return dataRepository.countCars();
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
    public void displayCar(int id)throws CarNotFoundEx {
        System.out.println("...called getCar id=" + id);
        System.out.println(dataRepository.getCar(id).toString());
    }

}
