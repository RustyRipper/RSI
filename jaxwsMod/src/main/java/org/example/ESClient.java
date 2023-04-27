package org.example;

import org.example.jaxws.server_topdown.*;
import org.example.jaxws.server_topdown.Car;
import org.example.services.*;
import org.example.exceptions.CarExistsEx;
import org.example.exceptions.CarNotFoundEx;
import org.example.jaxws.server_topdown.CarService_Service;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class ESClient {
    public static void main(String[] args) throws MalformedURLException,
            CarNotFoundEx, CarExistsEx, CarNotFoundEx_Exception, CarExistsEx_Exception {
        int num = -1;
        URL addr = new URL("http://localhost:8082/personservice?wsdl");
        CarService_Service pService = new CarService_Service();
        //PersonService_Service pService = new PersonService_Service();
        org.example.jaxws.server_topdown.CarService pServiceProxy = pService.getCarServiceImplPort();
        num = pServiceProxy.countCars();
        System.out.println("Num of Cars = " + num);
        System.out.println("getAll");
        for(Car car2: pServiceProxy.getAllCars()){
            System.out.println(car2.getId() + " Brand:" + car2.getBrand() + " Year:" + car2.getYear() + " isElectric:"+ car2.isElectric());
        }
        System.out.println("getOne");
        org.example.jaxws.server_topdown.Car car = pServiceProxy.getCar(2);
        System.out.println(car.getId() + " Brand:" + car.getBrand() + " Year:" + car.getYear() + " isElectric:"+ car.isElectric());

        System.out.println("add");
        System.out.println(pServiceProxy.addCar(4,"ferrari", 2022, true));
        System.out.println("delete");
        System.out.println(pServiceProxy.deleteCar(2));
        System.out.println("delete");
        try{
            System.out.println(pServiceProxy.deleteCar(2));
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        System.out.println("getAll");
        for(Car car2: pServiceProxy.getAllCars()){
            System.out.println(car2.getId() + " Brand:" + car2.getBrand() + " Year:" + car2.getYear() + " isElectric:"+ car2.isElectric());
        }

        System.out.println("MODIFICATIONS");
        System.out.println("update");
        System.out.println(pServiceProxy.updateCar(1,"audi2", 2022, true));
        System.out.println("getOne");
        org.example.jaxws.server_topdown.Car car3 = pServiceProxy.getCar(1);
        System.out.println("Car " + car3.getBrand() + ", id = " + car3.getId() + " " + car3.getYear() + " "+ car3.isElectric());
        try{
            System.out.println("add");
            System.out.println(pServiceProxy.addCar(5,"ferrari", 2022, true));
            System.out.println("add");
            System.out.println(pServiceProxy.addCar(6,"ferrari", 2022, true));
            System.out.println("add");
            System.out.println(pServiceProxy.addCar(7,"ferrari", 2022, true));
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        System.out.println("getAll");
        for(Car car2: pServiceProxy.getAllCars()){
            System.out.println(car2.getId() + " Brand:" + car2.getBrand() + " Year:" + car2.getYear() + " isElectric:"+ car2.isElectric());
        }
        System.out.println("eraseCars");
        System.out.println(pServiceProxy.eraseCars());
        System.out.println("eraseCars");
        System.out.println(pServiceProxy.eraseCars());
        System.out.println("getAll");
        for(Car car2: pServiceProxy.getAllCars()){
            System.out.println(car2.getId() + " Brand:" + car2.getBrand() + " Year:" + car2.getYear() + " isElectric:"+ car2.isElectric());
        }
    }
}
