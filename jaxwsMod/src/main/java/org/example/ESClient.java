package org.example;

import org.example.jaxws.server_topdown.*;
import org.example.jaxws.server_topdown.Car;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class ESClient {
    public static void main(String[] args) throws MalformedURLException,
            CarNotFoundEx_Exception, CarExistsEx_Exception {
        int num = -1;
        URL addr = new URL("http://localhost:8082/personservice?wsdl");
        CarService_Service pService = new CarService_Service();
        //PersonService_Service pService = new PersonService_Service();
        org.example.jaxws.server_topdown.CarService pServiceProxy = pService.getCarServiceImplPort();
        num = pServiceProxy.countCars();
        System.out.println("Num of Persons = " + num);
        org.example.jaxws.server_topdown.Car car = pServiceProxy.getCar(2);
        System.out.println("Person " + car.getBrand() + ", id = " + car.getId());
        System.out.println(pServiceProxy.countCars());
        pServiceProxy.displayAllCars();

        System.out.println(pServiceProxy.addCar(4,"ferrari", 2022, true));
        System.out.println(pServiceProxy.getCar(4).getBrand());


        //for(Car car: pServiceProxy.getAllCars()){
        for(org.example.jaxws.server_topdown.Car car2: pServiceProxy.getAllCars()){
            System.out.println(car2.getId());
            System.out.println(car2.getBrand());
        }
        System.out.println(pServiceProxy.deleteCar(4));
    }
}
