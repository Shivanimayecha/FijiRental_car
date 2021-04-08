package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverDetails implements Serializable {
    @SerializedName("license_firstname")
    @Expose
    private String licenseFirstname;
    @SerializedName("license_middlename")
    @Expose
    private String licenseMiddlename;
    @SerializedName("license_lastname")
    @Expose
    private String licenseLastname;
    @SerializedName("license_country")
    @Expose
    private String licenseCountry;
    @SerializedName("license_state")
    @Expose
    private String licenseState;
    @SerializedName("license_number")
    @Expose
    private String licenseNumber;
    @SerializedName("license_expirationdate")
    @Expose
    private String licenseExpirationdate;
    @SerializedName("license_birthdate")
    @Expose
    private String licenseBirthdate;
    @SerializedName("driver_image")
    @Expose
    private String driverImage;
    @SerializedName("license_image")
    @Expose
    private String licenseImage;

    public String getLicenseFirstname() {
        return licenseFirstname;
    }

    public void setLicenseFirstname(String licenseFirstname) {
        this.licenseFirstname = licenseFirstname;
    }

    public String getLicenseMiddlename() {
        return licenseMiddlename;
    }

    public void setLicenseMiddlename(String licenseMiddlename) {
        this.licenseMiddlename = licenseMiddlename;
    }

    public String getLicenseLastname() {
        return licenseLastname;
    }

    public void setLicenseLastname(String licenseLastname) {
        this.licenseLastname = licenseLastname;
    }

    public String getLicenseCountry() {
        return licenseCountry;
    }

    public void setLicenseCountry(String licenseCountry) {
        this.licenseCountry = licenseCountry;
    }

    public String getLicenseState() {
        return licenseState;
    }

    public void setLicenseState(String licenseState) {
        this.licenseState = licenseState;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseExpirationdate() {
        return licenseExpirationdate;
    }

    public void setLicenseExpirationdate(String licenseExpirationdate) {
        this.licenseExpirationdate = licenseExpirationdate;
    }

    public String getLicenseBirthdate() {
        return licenseBirthdate;
    }

    public void setLicenseBirthdate(String licenseBirthdate) {
        this.licenseBirthdate = licenseBirthdate;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getLicenseImage() {
        return licenseImage;
    }

    public void setLicenseImage(String licenseImage) {
        this.licenseImage = licenseImage;
    }
}
