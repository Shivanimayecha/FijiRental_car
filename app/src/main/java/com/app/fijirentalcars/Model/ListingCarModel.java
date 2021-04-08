package com.app.fijirentalcars.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class ListingCarModel implements Serializable {
    VinModel model;
    BodyType bodyType;
    FuelType fuelType;
    boolean isFilterModel=false;



    TransmissionType transmissionType;
    LatLng latLng;
    String country;
    String countryCode;
    String state;
    String city;
    String region;
    String seatCount;


    String doorCount;
    boolean isCountryChange = false;
    boolean isUnderYear = false;
    boolean isLocationSet = false;
    String zipCode;
    String streetAdrees;
    String userProfile;

    String itemId;

    String primary_goal_of_sharing;
    String often_use_of_car;
    String advance_notice;
    String often_share_car;
    String min_trip_duration;
    String max_trip_duration;
    String licensePlateNumber;
    String carFeatures;
    String carDescription;
    String licenceFirstname;
    String licenceMiddlename;
    String licenceLastname;
    String licenceCountryname;
    String licenceStatename;
    String licenceNumber;
    String licenceDOb;
    String driverLicenseImage;

    public String getDriverLicenseImage() {
        return driverLicenseImage;
    }

    public void setDriverLicenseImage(String driverLicenseImage) {
        this.driverLicenseImage = driverLicenseImage;
    }


    public String getLicenceFirstname() {
        return licenceFirstname;
    }

    public void setLicenceFirstname(String licenceFirstname) {
        this.licenceFirstname = licenceFirstname;
    }

    public String getLicenceMiddlename() {
        return licenceMiddlename;
    }

    public void setLicenceMiddlename(String licenceMiddlename) {
        this.licenceMiddlename = licenceMiddlename;
    }
    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }
    public String getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(String seatCount) {
        this.seatCount = seatCount;
    }

    public String getDoorCount() {
        return doorCount;
    }

    public void setDoorCount(String doorCount) {
        this.doorCount = doorCount;
    }

    public String getLicenceLastname() {
        return licenceLastname;
    }

    public void setLicenceLastname(String licenceLastname) {
        this.licenceLastname = licenceLastname;
    }


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean isFilterModel() {
        return isFilterModel;
    }

    public void setFilterModel(boolean filterModel) {
        isFilterModel = filterModel;
    }

    public String getLicenceCountryname() {
        return licenceCountryname;
    }

    public void setLicenceCountryname(String licenceCountryname) {
        this.licenceCountryname = licenceCountryname;
    }

    public String getLicenceStatename() {
        return licenceStatename;
    }

    public void setLicenceStatename(String licenceStatename) {
        this.licenceStatename = licenceStatename;
    }

    public String getMax_trip_duration() {
        return max_trip_duration;
    }

    public void setMax_trip_duration(String max_trip_duration) {
        this.max_trip_duration = max_trip_duration;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getLicenceDOb() {
        return licenceDOb;
    }

    public void setLicenceDOb(String licenceDOb) {
        this.licenceDOb = licenceDOb;
    }



    public String getCarDescription() {
        return carDescription;
    }

    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }


    public String getCarFeatures() {
        return carFeatures;
    }

    public void setCarFeatures(String carFeatures) {
        this.carFeatures = carFeatures;
    }

    public String getMin_trip_duration() {
        return min_trip_duration;
    }

    public void setMin_trip_duration(String min_trip_duration) {
        this.min_trip_duration = min_trip_duration;
    }


    public String getAdvance_notice() {
        return advance_notice;
    }

    public void setAdvance_notice(String advance_notice) {
        this.advance_notice = advance_notice;
    }


    public String getPrimary_goal_of_sharing() {
        return primary_goal_of_sharing;
    }

    public void setPrimary_goal_of_sharing(String primary_goal_of_sharing) {
        this.primary_goal_of_sharing = primary_goal_of_sharing;
    }

    public String getOften_use_of_car() {
        return often_use_of_car;
    }

    public void setOften_use_of_car(String often_use_of_car) {
        this.often_use_of_car = often_use_of_car;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }


    public String getOften_share_car() {
        return often_share_car;
    }

    public void setOften_share_car(String often_share_car) {
        this.often_share_car = often_share_car;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(TransmissionType transmissionType) {
        this.transmissionType = transmissionType;
    }


    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public boolean isUnderYear() {
        return isUnderYear;
    }

    public void setUnderYear(boolean underYear) {
        isUnderYear = underYear;
    }


    public boolean isLocationSet() {
        return isLocationSet;
    }

    public void setLocationSet(boolean locationSet) {
        isLocationSet = locationSet;
    }


    public boolean isCountryChange() {
        return isCountryChange;
    }

    public void setCountryChange(boolean countryChange) {
        isCountryChange = countryChange;
    }


    public String getStreetAdrees() {
        return streetAdrees;
    }

    public void setStreetAdrees(String streetAdrees) {
        this.streetAdrees = streetAdrees;
    }


    public VinModel getModel() {
        return model;
    }

    public void setModel(VinModel model) {
        this.model = model;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "ListingCarModel{" +
                "model=" + model +
                ", bodyType=" + bodyType +
                ", transmissionType=" + transmissionType +
                ", latLng=" + latLng +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", isCountryChange=" + isCountryChange +
                ", isUnderYear=" + isUnderYear +
                ", isLocationSet=" + isLocationSet +
                ", zipCode='" + zipCode + '\'' +
                ", streetAdrees='" + streetAdrees + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", itemId='" + itemId + '\'' +
                ", primary_goal_of_sharing='" + primary_goal_of_sharing + '\'' +
                ", often_use_of_car='" + often_use_of_car + '\'' +
                ", advance_notice='" + advance_notice + '\'' +
                ", often_share_car='" + often_share_car + '\'' +
                ", min_trip_duration='" + min_trip_duration + '\'' +
                ", max_trip_duration='" + max_trip_duration + '\'' +
                ", licensePlateNumber='" + licensePlateNumber + '\'' +
                ", carFeatures='" + carFeatures + '\'' +
                ", carDescription='" + carDescription + '\'' +
                ", licenceFirstname='" + licenceFirstname + '\'' +
                ", licenceMiddlename='" + licenceMiddlename + '\'' +
                ", licenceLastname='" + licenceLastname + '\'' +
                ", licenceCountryname='" + licenceCountryname + '\'' +
                ", licenceStatename='" + licenceStatename + '\'' +
                ", licenceNumber='" + licenceNumber + '\'' +
                ", licenceDOb='" + licenceDOb + '\'' +
                '}';
    }
}
