package com.example.restservice.models;


public class Car {
    private int id;
    private String brand;
    private int year;
    private boolean isElectric;

    private CarStatus carStatus;

    public CarStatus getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }

    public Car(int id, String brand, int year, boolean isElectric, CarStatus carStatus) {
        this.id = id;
        this.brand = brand;
        this.year = year;
        this.isElectric = isElectric;
        this.carStatus = carStatus;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", year=" + year +
                ", isElectric=" + isElectric +
                '}';
    }
}
