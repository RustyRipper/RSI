package com.example.restservice.controllers;

import com.example.restservice.exceptions.*;
import com.example.restservice.models.Car;
import com.example.restservice.models.CarStatus;
import com.example.restservice.rabbitmq.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.restservice.repositories.CarRepository;
import com.example.restservice.repositories.CarRepositoryImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(maxAge = 3600)
@RestController
public class CarController {

    private final ProducerA producerA;

//    private final ProducerI producerI;

    private final ProducerE producerE;

//    private final ProducerAll producerAll;
//    private final ProducerEnergy producerEnergy;

    private CarRepository dataRepository = new CarRepositoryImpl();

    public CarController(ProducerA producerA, ProducerE producerE) {
        this.producerA = producerA;
        this.producerE = producerE;
//        this.producerI = producerI;
//        this.producerAll = producerAll;
//        this.producerEnergy = producerEnergy;
    }

    @GetMapping("/cars/{id}")
    public EntityModel<Car> getCar(@PathVariable int id) throws CarNotFoundEx, IOException, TimeoutException {
        System.out.println("hello");
        try {
            System.out.println("...called getCar id=" + id);
            Car car = dataRepository.getCar(id);

            EntityModel<Car> em = EntityModel.of(car);
            em.add(linkTo(methodOn(CarController.class).getCar(id)).withSelfRel());
            if(car.getCarStatus() == CarStatus.REGISTERED){
                em.add(linkTo(methodOn(CarController.class).unregisterCar(id)).withRel("unregister"));
            }
            else{
                em.add(linkTo(methodOn(CarController.class).registerCar(id)).withRel("register"));
                em.add(linkTo(methodOn(CarController.class).deleteCar(id)).withRel("delete"));
            }

            //producerAll.sendLog("GET Car: id=" + id);
            if(car.isElectric()){
                //producerEnergy.sendLog("GET Car: id=" + id);
                producerE.sendMsg("GET Car: id=" + id);
            }
            else{
                producerA.sendMsg("GET Car: id=" + id);
            }

            return em;
        } catch (CarNotFoundEx | IOException | TimeoutException e) {
            System.out.println("...GET Exception");
            throw e;
        }

    }

    @PutMapping("/cars/{id}")
    public EntityModel<Car> updateCar(@PathVariable int id, @RequestBody Car car) throws CarNotFoundEx, CarBadParams {

        try {
            System.out.println("...called updateCar");

//            producerAll.sendLog("PUT Car: id=" + id);

            if(car.isElectric()){
//                producerEnergy.sendLog("PUT Car: id=" + id);
                producerE.sendMsg("PUT Car: id=" + id);
            }
            else{
                producerA.sendMsg("PUT Car: id=" + id);
            }
            return EntityModel.of(dataRepository.updateCar(car.getId(), car.getBrand(), car.getYear() ,car.isElectric()),
                    linkTo(methodOn(CarController.class).getCar(id)).withSelfRel(),
                    linkTo(methodOn(CarController.class).deleteCar(id)).withRel("delete"),
                    linkTo(methodOn(CarController.class).getAllCars()).withRel("listall")
            );
        } catch (CarNotFoundEx e) {
            System.out.println("...PUT Exception");
            throw e;
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable int id) throws CarNotFoundEx {

        try {
            System.out.println("delete Car");
            Car car = dataRepository.getCar(id);
//            producerAll.sendLog("DELETE Car: id=" + id);

            if(car.isElectric()){
                producerE.sendMsg("DELETE Car: id=" + id);
//                producerEnergy.sendLog("DELETE Car: id=" + id);
            }
            else{
                producerA.sendMsg("DELETE Car: id=" + id);
            }
            dataRepository.deleteCar(id);

            return ResponseEntity.noContent().build();
        } catch (CarNotFoundEx e) {
            System.out.println("...DELETE Exception");
            throw e;
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/cars")
    public EntityModel<?> addCar(@RequestBody Car car) throws CarNotFoundEx {

        try {
            if(car.getBrand() == null) throw new CarBadParams();


            if(car.isElectric()){
                producerE.sendMsg("POST Car: id=" + car.getId());
            }
            else {
                producerA.sendMsg("POST Car: id=" + car.getId());
            }

//            producerAll.sendLog("POST Car: id=" + car.getId());
//            if(car.isElectric()){
//                producerEnergy.sendLog("POST Car: id=" + car.getId());
//            }
            System.out.println("add Car");
            return EntityModel.of(dataRepository.addCar(car.getId(), car.getBrand(), car.getYear() ,car.isElectric()),
                    linkTo(methodOn(CarController.class).getCar(car.getId())).withSelfRel(),
                    linkTo(methodOn(CarController.class).deleteCar(car.getId())).withRel("delete"),
                    linkTo(methodOn(CarController.class).getAllCars()).withRel("listall")
            );
        } catch (CarBadParams | CarExistsEx | CarFullListEx  e) {
            System.out.println("...POST Exception");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
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
                        } catch (CarNotFoundEx | IOException | TimeoutException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());

            return CollectionModel.of(cars, linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
        } catch (Exception e) {
            System.out.println("...POST Exception");
            throw e;
        }
    }


    @PatchMapping("/cars/{id}/register")
    public ResponseEntity<?> registerCar(@PathVariable int id){
        try {
            Car car = dataRepository.getCar(id);

            if(car.getCarStatus() == CarStatus.NOT_REGISTERED){
                dataRepository.updateStatus(id, CarStatus.REGISTERED);
                ArrayList<Link> list = new ArrayList<>();
                list.add(linkTo(methodOn(CarController.class).getCar(car.getId())).withSelfRel());
                list.add(linkTo(methodOn(CarController.class).unregisterCar(car.getId())).withRel("unregister"));
                list.add(linkTo(methodOn(CarController.class).getAllCars()).withRel("list all"));

                if(car.isElectric()){
                    producerE.sendMsg("REGISTER Car: id=" + id);
                }
                else{
                    producerA.sendMsg("REGISTER Car: id=" + id);
                }
//                producerAll.sendLog("REGISTER Car: id=" + id);
//                if(car.isElectric()){
//                    producerEnergy.sendLog("REGISTER Car: id=" + id);
//                }
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(list);
            }
            else{
                throw new ConflictEx();
            }
        } catch (CarNotFoundEx | ConflictEx | IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
    @PatchMapping("/cars/{id}/unregister")
    public ResponseEntity<?> unregisterCar(@PathVariable int id){
        try {
            Car car = dataRepository.getCar(id);
            if(car.getCarStatus() == CarStatus.REGISTERED){
                dataRepository.updateStatus(id, CarStatus.NOT_REGISTERED);
                ArrayList<Link> list = new ArrayList<>();
                list.add(linkTo(methodOn(CarController.class).getCar(car.getId())).withSelfRel());
                list.add(linkTo(methodOn(CarController.class).registerCar(car.getId())).withRel("register"));
                list.add( linkTo(methodOn(CarController.class).deleteCar(car.getId())).withRel("delete"));
                list.add(linkTo(methodOn(CarController.class).getAllCars()).withRel("list all"));

                if(car.isElectric()){
                    producerE.sendMsg("UNREGISTER Car: id=" + id);
                }
                else{
                    producerA.sendMsg("UNREGISTER Car: id=" + id);
                }
//                producerAll.sendLog("UNREGISTER Car: id=" + id);
//                if(car.isElectric()){
//                    producerEnergy.sendLog("UNREGISTER Car: id=" + id);
//                }

                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(list);
            }
            else{
                throw new ConflictEx();
            }
        } catch (CarNotFoundEx | ConflictEx | IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/items/{id}/image")
    public ResponseEntity<byte[]> getItemImage(@PathVariable("id") String itemId) throws Exception {
        System.out.println("image!");
        ProducerI producerI = null;
        try{
            producerI = new ProducerI();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(producerI.getImg(itemId));
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            assert producerI != null;
            producerI.close();
            System.out.println("close");
        }
        return null;
    }
}
