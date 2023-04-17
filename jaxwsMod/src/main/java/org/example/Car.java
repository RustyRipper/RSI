package org.example;

public class Car {
    private int id;
    private String brand;
    private int year;
    private boolean isElectric;

    public Car(int id, String brand, int year, boolean isElectric) {
        this.id = id;
        this.brand = brand;
        this.year = year;
        this.isElectric = isElectric;
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
