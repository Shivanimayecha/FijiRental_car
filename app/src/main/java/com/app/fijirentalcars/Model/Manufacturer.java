package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Manufacturer {

//    @SerializedName("feature_id")
//    @Expose
//    private String featureId;
//    @SerializedName("feature_title")
//    @Expose
//    private String featureTitle;
//
//    public String getFeatureId() {
//        return featureId;
//    }
//
//    public void setFeatureId(String featureId) {
//        this.featureId = featureId;
//    }
//
//    public String getFeatureTitle() {
//        return featureTitle;
//    }
//
//    public void setFeatureTitle(String featureTitle) {
//        this.featureTitle = featureTitle;
//    }

    @SerializedName("manufacturer_id")
    @Expose
    private String manufacturerId;
    @SerializedName("manufacturer_title")
    @Expose
    private String manufacturerTitle;
    @SerializedName("manufacturer_logo")
    @Expose
    private String manufacturerLogo;

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerTitle() {
        return manufacturerTitle;
    }

    public void setManufacturerTitle(String manufacturerTitle) {
        this.manufacturerTitle = manufacturerTitle;
    }

    public String getManufacturerLogo() {
        return manufacturerLogo;
    }

    public void setManufacturerLogo(String manufacturerLogo) {
        this.manufacturerLogo = manufacturerLogo;
    }


}
