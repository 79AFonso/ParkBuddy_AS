package com.example.parkbuddy;

public class VehicleModel {
    String img,model,plate;

    public VehicleModel(String img, String model, String plate) {
        this.img = img;
        this.model = model;
        this.plate = plate;
    }

    public VehicleModel(String model, String plate) {
        this.model = model;
        this.plate = plate;
    }

}
