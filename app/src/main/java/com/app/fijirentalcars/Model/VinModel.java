package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VinModel implements Serializable {
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("manufacturer_name")
    @Expose
    private String manufacturerName;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("year")
    @Expose
    private String year;
    String trim;
    String vehicleType;
    String bodyclass;
    String door;
    String grossVehicleWeightRating;
    String driveType;
    String breakeSystem;
    String engineCylinder;
    String fuelType;
    String seatbeltType;
    String odometer;

    String manufactureID;

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getCarPriceRange() {
        return carPriceRange;
    }

    public void setCarPriceRange(String carPriceRange) {
        this.carPriceRange = carPriceRange;
    }

    String carPriceRange;

    private String  vinNumber;

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBodyclass() {
        return bodyclass;
    }

    public void setBodyclass(String bodyclass) {
        this.bodyclass = bodyclass;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getGrossVehicleWeightRating() {
        return grossVehicleWeightRating;
    }

    public void setGrossVehicleWeightRating(String grossVehicleWeightRating) {
        this.grossVehicleWeightRating = grossVehicleWeightRating;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getBreakeSystem() {
        return breakeSystem;
    }

    public void setBreakeSystem(String breakeSystem) {
        this.breakeSystem = breakeSystem;
    }

    public String getEngineCylinder() {
        return engineCylinder;
    }

    public void setEngineCylinder(String engineCylinder) {
        this.engineCylinder = engineCylinder;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getSeatbeltType() {
        return seatbeltType;
    }

    public void setSeatbeltType(String seatbeltType) {
        this.seatbeltType = seatbeltType;
    }



    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufactureID() {
        return manufactureID;
    }

    public void setManufactureID(String manufactureID) {
        this.manufactureID = manufactureID;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "VinModel{" +
                "make='" + make + '\'' +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", trim='" + trim + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", bodyclass='" + bodyclass + '\'' +
                ", door='" + door + '\'' +
                ", grossVehicleWeightRating='" + grossVehicleWeightRating + '\'' +
                ", driveType='" + driveType + '\'' +
                ", breakeSystem='" + breakeSystem + '\'' +
                ", engineCylinder='" + engineCylinder + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", seatbeltType='" + seatbeltType + '\'' +
                ", odometer='" + odometer + '\'' +
                ", carPriceRange='" + carPriceRange + '\'' +
                ", vinNumber='" + vinNumber + '\'' +
                '}';
    }
}
