package com.example.restservice.controllers;

import com.example.restservice.exceptions.CarExistsEx;
import com.example.restservice.exceptions.CarFullListEx;
import com.example.restservice.exceptions.CarNotFoundEx;
import com.example.restservice.models.Car;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.restservice.repositories.CarRepository;
import com.example.restservice.repositories.CarRepositoryImpl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CarController {

    private CarRepository dataRepository = new CarRepositoryImpl();

    @GetMapping("/cars/{id}")
    public EntityModel<Car> getCar(@PathVariable int id) throws CarNotFoundEx {
        System.out.println("hello");
        try {
            System.out.println("...called getCar id=" + id);
            return EntityModel.of(dataRepository.getCar(id),
                    linkTo(methodOn(CarController.class).getCar(id)).withSelfRel(),
                    linkTo(methodOn(CarController.class).deleteCar(id)).withRel("delete"),
                    linkTo(methodOn(CarController.class).getAllCars()).withRel("listall")
                    );
        } catch (CarNotFoundEx e) {
            System.out.println("...GET Exception");
            throw e;
        }

    }

    @PutMapping("/cars/{id}")
    public EntityModel<Car> updateCar(@PathVariable int id, @RequestBody Car car) throws CarNotFoundEx {

        try {
            System.out.println("...called updateCar");
            return EntityModel.of(dataRepository.updateCar(car.getId(), car.getBrand(), car.getYear() ,car.isElectric()),
                    linkTo(methodOn(CarController.class).getCar(id)).withSelfRel(),
                    linkTo(methodOn(CarController.class).deleteCar(id)).withRel("delete"),
                    linkTo(methodOn(CarController.class).getAllCars()).withRel("listall")
            );
        } catch (CarNotFoundEx e) {
            System.out.println("...PUT Exception");
            throw e;
        }
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable int id) throws CarNotFoundEx {

        try {
            System.out.println("delete Car");

            dataRepository.deleteCar(id);
            return ResponseEntity.noContent().build();
        } catch (CarNotFoundEx e) {
            System.out.println("...DELETE Exception");
            throw e;
        }

    }

    @PostMapping("/cars")
    public EntityModel<?> addCar(@RequestBody Car car) throws CarExistsEx, CarFullListEx, CarNotFoundEx {

        try {
            System.out.println("add Car");
            return EntityModel.of(dataRepository.addCar(car.getId(), car.getBrand(), car.getYear() ,car.isElectric()),
                    linkTo(methodOn(CarController.class).getCar(car.getId())).withSelfRel(),
                    linkTo(methodOn(CarController.class).deleteCar(car.getId())).withRel("delete"),
                    linkTo(methodOn(CarController.class).getAllCars()).withRel("listall")
            );
        } catch (Exception e) {
            System.out.println("...POST Exception");
            throw e;
        }
    }

    @GetMapping("/cars")
    public CollectionModel<EntityModel<Car>> getAllCars() {
        try {
            System.out.println("get all Cars");
            List<EntityModel<Car>> cars =
                    dataRepository.getAllCars().stream().map( car -> {
                        try {
                            return EntityModel.of(car,
                                                    linkTo(methodOn(CarController.class).getCar(car.getId())).withSelfRel(),
                                                    linkTo(methodOn(CarController.class).deleteCar(car.getId())).withRel("delete"),
                                                    linkTo(methodOn(CarController.class).getAllCars()).withRel("list all")
                                            );
                        } catch (CarNotFoundEx e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());

            return CollectionModel.of(cars, linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
        } catch (Exception e) {
            System.out.println("...POST Exception");
            throw e;
        }
    }


}
