package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FuelType implements Serializable {

    @SerializedName("fuel_type_id")
    @Expose
    private String fuelTypeId;
    @SerializedName("fuel_type_title")
    @Expose
    private String fuelTypeTitle;

    public String getFuelTypeId() {
        return fuelTypeId;
    }

    public void setFuelTypeId(String fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public String getFuelTypeTitle() {
        return fuelTypeTitle;
    }

    public void setFuelTypeTitle(String fuelTypeTitle) {
        this.fuelTypeTitle = fuelTypeTitle;
    }

}

