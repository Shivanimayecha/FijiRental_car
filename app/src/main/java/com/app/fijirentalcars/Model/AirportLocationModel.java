package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AirportLocationModel implements Serializable {
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("selected")
    @Expose
    private Integer selected;
    @SerializedName("price")
    @Expose
    private Object price;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AirportLocationModel{" +
                "locationId=" + locationId +
                ", locationName='" + locationName + '\'' +
                ", selected=" + selected +
                ", price=" + price +
                '}';
    }
}
