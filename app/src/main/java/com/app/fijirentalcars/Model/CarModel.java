package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarModel implements Serializable {
    @SerializedName("item_id")
    @Expose
    private String itemId;

    @SerializedName("item_name")
    @Expose
    private String itemName;

    @SerializedName("item_sku")
    @Expose
    private String itemSku;
    @SerializedName("item_page_id")
    @Expose
    private String itemPageId;
    @SerializedName("partner_id")
    @Expose
    private String partnerId;
    @SerializedName("manufacturer_id")
    @Expose
    private String manufacturerId;
    @SerializedName("body_type_id")
    @Expose
    private String bodyTypeId;
    @SerializedName("transmission_type_id")
    @Expose
    private String transmissionTypeId;
    @SerializedName("fuel_type_id")
    @Expose
    private String fuelTypeId;
    @SerializedName("item_color")
    @Expose
    private String itemColor;
    @SerializedName("model_name")
    @Expose
    private String modelName;
    @SerializedName("item_image_1")
    @Expose
    private String itemImage1;
    @SerializedName("item_image_2")
    @Expose
    private String itemImage2;
    @SerializedName("item_image_3")
    @Expose
    private String itemImage3;
    @SerializedName("mileage")
    @Expose
    private String mileage;

    @SerializedName("city_mileage")
    @Expose
    private String cityMileage;
    @SerializedName("fuel_consumption")
    @Expose
    private String fuelConsumption;
    @SerializedName("engine_capacity")
    @Expose
    private String engineCapacity;
    @SerializedName("max_passengers")
    @Expose
    private String maxPassengers;
    @SerializedName("max_luggage")
    @Expose
    private String maxLuggage;
    @SerializedName("item_doors")
    @Expose
    private String itemDoors;
    @SerializedName("min_driver_age")
    @Expose
    private String minDriverAge;
    @SerializedName("price_group_id")
    @Expose
    private String priceGroupId;
    @SerializedName("fixed_rental_deposit")
    @Expose
    private String fixedRentalDeposit;
    @SerializedName("units_in_stock")
    @Expose
    private String unitsInStock;
    @SerializedName("max_units_per_booking")
    @Expose
    private String maxUnitsPerBooking;
    @SerializedName("options_measurement_unit")
    @Expose
    private String optionsMeasurementUnit;
    @SerializedName("blog_id")
    @Expose
    private String blogId;
    @SerializedName("model_year")
    @Expose
    private String modelYear;
    @SerializedName("model_seats")
    @Expose
    private String modelSeats;
    @SerializedName("model_doors")
    @Expose
    private String modelDoors;

    @SerializedName("model_car_licensePlateNumber")
    @Expose
    private String carNumber;


    @SerializedName("model_latitude")
    @Expose
    private String modelLatitude;
    @SerializedName("model_longitude")
    @Expose
    private String modelLongitude;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("model_description")
    @Expose
    private String modelDescription;
    @SerializedName("model_car_location")
    @Expose
    private String modelCarLocation;
    @SerializedName("model_car_make")
    @Expose
    private String modelCarMake;
    @SerializedName("model_car_model")
    @Expose
    private String modelCarModel;
    @SerializedName("enabled")
    @Expose
    private String enabled;
    @SerializedName("unlist_reason")
    @Expose
    private String unlistReason;
    @SerializedName("snooze_untill")
    @Expose
    private String snoozeUntill;
    @SerializedName("guest_choosen_location")
    @Expose
    private String guestChoosenLocation;
    @SerializedName("guest_choosen_location_fee")
    @Expose
    private String guestChoosenLocationFee;
    @SerializedName("guest_choosen_up_to_miles")
    @Expose
    private String guestChoosenUpToMiles;
    @SerializedName("discount_on_miles")
    @Expose
    private String discountOnMiles;
    @SerializedName("delivery_discount_on_days")
    @Expose
    private String deliveryDiscountOnDays;
    @SerializedName("3extra_day_discount")
    @Expose
    private String _3extraDayDiscount;
    @SerializedName("7extra_day_discount")
    @Expose
    private String _7extraDayDiscount;
    @SerializedName("30extra_day_discount")
    @Expose
    private String _30extraDayDiscount;
    @SerializedName("carprice")
    @Expose
    private String carprice;

    @SerializedName("vehicle_protection")
    @Expose
    private String vehicle_protection;
    @SerializedName("model_car_isNotSalvaged")
    @Expose
    private String modelCarIsNotSalvaged;
    @SerializedName("model_car_commercialHost")
    @Expose
    private String modelCarCommercialHost;
    @SerializedName("model_car_goals_earning")
    @Expose
    private String modelCarGoalsEarning;
    @SerializedName("model_car_goals_owner_utilization")
    @Expose
    private String modelCarGoalsOwnerUtilization;
    @SerializedName("model_car_goals_renter_utilization")
    @Expose
    private String modelCarGoalsRenterUtilization;
    @SerializedName("model_car_availability_advanceNotice")
    @Expose
    private String modelCarAvailabilityAdvanceNotice;
    @SerializedName("model_car_duration_minimumTripDuration")
    @Expose
    private String modelCarDurationMinimumTripDuration;
    @SerializedName("model_car_duration_longerWeekendTripPreferred")
    @Expose
    private String modelCarDurationLongerWeekendTripPreferred;
    @SerializedName("model_car_duration_maximumTripDuration")
    @Expose
    private String modelCarDurationMaximumTripDuration;
    @SerializedName("model_car_licensePlateNumber")
    @Expose
    private String modelCarLicensePlateNumber;
    @SerializedName("model_car_licensePlateRegion")
    @Expose
    private String modelCarLicensePlateRegion;
    @Expose
    private String bookInstatnlyCarLocation;
    @SerializedName("book_instatnly_dilivery_locations")
    @Expose
    private String bookInstatnlyDiliveryLocations;
    @SerializedName("book_instatnly_guests_location")
    @Expose
    private String bookInstatnlyGuestsLocation;
    @SerializedName("model_unlimited_included")
    @Expose
    private String modelUnlimitedIncluded;
    @SerializedName("model_daily_distance_included")
    @Expose
    private String modelDailyDistanceIncluded;
    @SerializedName("advance_notice_car_location")
    @Expose
    private String advanceNoticeCarLocation;
    @SerializedName("advance_notice_dilivery_locations")
    @Expose
    private String advanceNoticeDiliveryLocations;
    @SerializedName("advance_notice_guests_location")
    @Expose
    private String advanceNoticeGuestsLocation;
    @SerializedName("trip_buffer_car_location")
    @Expose
    private String tripBufferCarLocation;
    @SerializedName("trip_buffer_dilivery_locations")
    @Expose
    private String tripBufferDiliveryLocations;
    @SerializedName("trip_buffer_guests_location")
    @Expose
    private String tripBufferGuestsLocation;
    @SerializedName("shortest_possible_trip")
    @Expose
    private String shortestPossibleTrip;
    @SerializedName("longest_possible_trip")
    @Expose
    private String longestPossibleTrip;
    @SerializedName("long_term_trips")
    @Expose
    private String longTermTrips;
    @SerializedName("pickup_date")
    @Expose
    private String pickupDate;
    @SerializedName("return_date")
    @Expose
    private String returnDate;
    @SerializedName("ratings")
    @Expose
    private RatingModel ratingModel;
    @SerializedName("futures")
    @Expose
    private List<FutureModel> futures = null;
    @SerializedName("fuel_type_title")
    @Expose
    private String fuelTypeTitle;
    @SerializedName("transmission_type_title")
    @Expose
    private String transmissionTypeTitle;
    @SerializedName("body_type_title")
    @Expose
    private String bodyTypeTitle;
    @SerializedName("partner_detail")
    @Expose
    private PartnerDetail partnerDetail;
    @SerializedName("manufacturer_title")
    @Expose
    private String manufacturerTitle;

    @SerializedName("photos")
    @Expose
    private List<String> photos = null;

    @SerializedName("driver_details")
    @Expose
    private DriverDetails driverDetails;

    @SerializedName("additionalfeatures")
    @Expose
    private String additionalfeatures;
    @SerializedName("guidelines")
    @Expose
    private String guidelines;
    @SerializedName("guest_pickup_return_instructions")
    @Expose
    private String guestPickupReturnInstructions;
    @SerializedName("guest_welcome_message")
    @Expose
    private String guestWelcomeMessage;
    @SerializedName("guest_car_message")
    @Expose
    private String guestCarMessage;
    @SerializedName("price_by_days")
    @Expose
    private List<DBModel> priceByDays = null;

    @SerializedName("is_favourites")
    @Expose
    private String is_favourites;
    @SerializedName("is_favourites")
    @Expose
    private ArrayList<AirportLocationModel> airportList;

    @SerializedName("unavailability")
    @Expose
    private List<Unavailability> unavailability = null;

    @SerializedName("total_trip")
    @Expose
    private int total_trip;
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getTotal_trip() {
        return total_trip;
    }

    public void setTotal_trip(int total_trip) {
        this.total_trip = total_trip;
    }

    public String getLast_trip() {
        return last_trip;
    }

    public void setLast_trip(String last_trip) {
        this.last_trip = last_trip;
    }

    public String getModelUnlimitedIncluded() {
        return modelUnlimitedIncluded;
    }

    public void setModelUnlimitedIncluded(String distanceIncluded) {
        this.modelUnlimitedIncluded = distanceIncluded;
    }

    public String getModelDailyDistanceIncluded() {
        return modelDailyDistanceIncluded;
    }

    public void setModelDailyDistanceIncluded(String dailyIncludedDistance) {
        this.modelDailyDistanceIncluded = dailyIncludedDistance;
    }

    public String getBookInstatnlyCarLocation() {
        return bookInstatnlyCarLocation;
    }

    public void setBookInstatnlyCarLocation(String bookInstatnlyCarLocation) {
        this.bookInstatnlyCarLocation = bookInstatnlyCarLocation;
    }

    public String getBookInstatnlyDiliveryLocations() {
        return bookInstatnlyDiliveryLocations;
    }

    public void setBookInstatnlyDiliveryLocations(String bookInstatnlyDiliveryLocations) {
        this.bookInstatnlyDiliveryLocations = bookInstatnlyDiliveryLocations;
    }

    public String getBookInstatnlyGuestsLocation() {
        return bookInstatnlyGuestsLocation;
    }

    public void setBookInstatnlyGuestsLocation(String bookInstatnlyGuestsLocation) {
        this.bookInstatnlyGuestsLocation = bookInstatnlyGuestsLocation;
    }

    public String getAdvanceNoticeCarLocation() {
        return advanceNoticeCarLocation;
    }

    public void setAdvanceNoticeCarLocation(String advanceNoticeCarLocation) {
        this.advanceNoticeCarLocation = advanceNoticeCarLocation;
    }

    public String getAdvanceNoticeDiliveryLocations() {
        return advanceNoticeDiliveryLocations;
    }

    public void setAdvanceNoticeDiliveryLocations(String advanceNoticeDiliveryLocations) {
        this.advanceNoticeDiliveryLocations = advanceNoticeDiliveryLocations;
    }

    public String getAdvanceNoticeGuestsLocation() {
        return advanceNoticeGuestsLocation;
    }

    public void setAdvanceNoticeGuestsLocation(String advanceNoticeGuestsLocation) {
        this.advanceNoticeGuestsLocation = advanceNoticeGuestsLocation;
    }

    public String getTripBufferCarLocation() {
        return tripBufferCarLocation;
    }

    public void setTripBufferCarLocation(String tripBufferCarLocation) {
        this.tripBufferCarLocation = tripBufferCarLocation;
    }

    public String getTripBufferDiliveryLocations() {
        return tripBufferDiliveryLocations;
    }

    public void setTripBufferDiliveryLocations(String tripBufferDiliveryLocations) {
        this.tripBufferDiliveryLocations = tripBufferDiliveryLocations;
    }

    public String getTripBufferGuestsLocation() {
        return tripBufferGuestsLocation;
    }

    public void setTripBufferGuestsLocation(String tripBufferGuestsLocation) {
        this.tripBufferGuestsLocation = tripBufferGuestsLocation;
    }

    public String getShortestPossibleTrip() {
        return shortestPossibleTrip;
    }

    public void setShortestPossibleTrip(String shortestPossibleTrip) {
        this.shortestPossibleTrip = shortestPossibleTrip;
    }

    public String getLongestPossibleTrip() {
        return longestPossibleTrip;
    }

    public void setLongestPossibleTrip(String longestPossibleTrip) {
        this.longestPossibleTrip = longestPossibleTrip;
    }

    public String getGuestChoosenLocation() {
        return guestChoosenLocation;
    }

    public void setGuestChoosenLocation(String guestChoosenLocation) {
        this.guestChoosenLocation = guestChoosenLocation;
    }

    public String getGuestChoosenLocationFee() {
        return guestChoosenLocationFee;
    }

    public void setGuestChoosenLocationFee(String guestChoosenLocationFee) {
        this.guestChoosenLocationFee = guestChoosenLocationFee;
    }

    public String getGuestChoosenUpToMiles() {
        return guestChoosenUpToMiles;
    }

    public void setGuestChoosenUpToMiles(String guestChoosenUpToMiles) {
        this.guestChoosenUpToMiles = guestChoosenUpToMiles;
    }

    public String getDiscountOnMiles() {
        return discountOnMiles;
    }

    public void setDiscountOnMiles(String discountOnMiles) {
        this.discountOnMiles = discountOnMiles;
    }

    public String getDeliveryDiscountOnDays() {
        return deliveryDiscountOnDays;
    }

    public void setDeliveryDiscountOnDays(String deliveryDiscountOnDays) {
        this.deliveryDiscountOnDays = deliveryDiscountOnDays;
    }

    public String get3extraDayDiscount() {
        return _3extraDayDiscount;
    }

    public void set3extraDayDiscount(String _3extraDayDiscount) {
        this._3extraDayDiscount = _3extraDayDiscount;
    }

    public String get7extraDayDiscount() {
        return _7extraDayDiscount;
    }

    public void set7extraDayDiscount(String _7extraDayDiscount) {
        this._7extraDayDiscount = _7extraDayDiscount;
    }

    public String get30extraDayDiscount() {
        return _30extraDayDiscount;
    }

    public void set30extraDayDiscount(String _30extraDayDiscount) {
        this._30extraDayDiscount = _30extraDayDiscount;
    }

    public String getCarprice() {
        return carprice;
    }

    public void setCarprice(String carprice) {
        this.carprice = carprice;
    }


    public String getVehicle_protection() {
        return vehicle_protection;
    }

    public void setVehicle_protection(String vehicle_protection) {
        this.vehicle_protection = vehicle_protection;
    }


    public String getLongTermTrips() {
        return longTermTrips;
    }

    public void setLongTermTrips(String longTermTrips) {
        this.longTermTrips = longTermTrips;
    }

    @SerializedName("last_trip")
    @Expose
    private String last_trip;


    @SerializedName("model_car_license_licenseCountry")
    @Expose
    private String modelCarLicenseLicenseCountry;
    @SerializedName("model_car_license_licenseState")
    @Expose
    private String modelCarLicenseLicenseState;
    @SerializedName("model_car_license_licenseNumber")
    @Expose
    private String modelCarLicenseLicenseNumber;
    @SerializedName("model_car_license_firstName")
    @Expose
    private String modelCarLicenseFirstName;
    @SerializedName("model_car_license_middleName")
    @Expose
    private String modelCarLicenseMiddleName;
    @SerializedName("model_car_license_lastName")
    @Expose
    private String modelCarLicenseLastName;
    @SerializedName("model_car_license_expirationMonth")
    @Expose
    private String modelCarLicenseExpirationMonth;
    @SerializedName("model_car_license_expirationDay")
    @Expose
    private String modelCarLicenseExpirationDay;
    @SerializedName("model_car_license_expirationYear")
    @Expose
    private String modelCarLicenseExpirationYear;
    @SerializedName("model_car_license_birthMonth")
    @Expose
    private String modelCarLicenseBirthMonth;
    @SerializedName("model_car_license_birthYear")
    @Expose
    private String modelCarLicenseBirthYear;
    @SerializedName("model_car_license_birthDay")
    @Expose
    private String modelCarLicenseBirthDay;

    @SerializedName("book_instatnly")
    @Expose
    private String bookInstatnly;
    @SerializedName("delivery_direct_to_you")
    @Expose
    private String deliveryDirectToYou;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getItemPageId() {
        return itemPageId;
    }

    public void setItemPageId(String itemPageId) {
        this.itemPageId = itemPageId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getBodyTypeId() {
        return bodyTypeId;
    }

    public void setBodyTypeId(String bodyTypeId) {
        this.bodyTypeId = bodyTypeId;
    }

    public String getTransmissionTypeId() {
        return transmissionTypeId;
    }

    public void setTransmissionTypeId(String transmissionTypeId) {
        this.transmissionTypeId = transmissionTypeId;
    }

    public String getFuelTypeId() {
        return fuelTypeId;
    }

    public void setFuelTypeId(String fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getItemImage1() {
        return itemImage1;
    }

    public void setItemImage1(String itemImage1) {
        this.itemImage1 = itemImage1;
    }

    public String getItemImage2() {
        return itemImage2;
    }

    public void setItemImage2(String itemImage2) {
        this.itemImage2 = itemImage2;
    }

    public String getItemImage3() {
        return itemImage3;
    }

    public void setItemImage3(String itemImage3) {
        this.itemImage3 = itemImage3;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(String fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public String getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(String engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public String getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(String maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public String getMaxLuggage() {
        return maxLuggage;
    }

    public void setMaxLuggage(String maxLuggage) {
        this.maxLuggage = maxLuggage;
    }

    public String getItemDoors() {
        return itemDoors;
    }

    public void setItemDoors(String itemDoors) {
        this.itemDoors = itemDoors;
    }

    public String getMinDriverAge() {
        return minDriverAge;
    }

    public void setMinDriverAge(String minDriverAge) {
        this.minDriverAge = minDriverAge;
    }

    public String getPriceGroupId() {
        return priceGroupId;
    }

    public void setPriceGroupId(String priceGroupId) {
        this.priceGroupId = priceGroupId;
    }

    public String getFixedRentalDeposit() {
        return fixedRentalDeposit;
    }

    public void setFixedRentalDeposit(String fixedRentalDeposit) {
        this.fixedRentalDeposit = fixedRentalDeposit;
    }

    public String getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(String unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public String getMaxUnitsPerBooking() {
        return maxUnitsPerBooking;
    }

    public void setMaxUnitsPerBooking(String maxUnitsPerBooking) {
        this.maxUnitsPerBooking = maxUnitsPerBooking;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getOptionsMeasurementUnit() {
        return optionsMeasurementUnit;
    }

    public void setOptionsMeasurementUnit(String optionsMeasurementUnit) {
        this.optionsMeasurementUnit = optionsMeasurementUnit;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getModelSeats() {
        return modelSeats;
    }

    public void setModelSeats(String modelSeats) {
        this.modelSeats = modelSeats;
    }

    public String getModelDoors() {
        return modelDoors;
    }

    public void setModelDoors(String modelDoors) {
        this.modelDoors = modelDoors;
    }

    public String getIs_favourites() {
        return is_favourites;
    }

    public void setIs_favourites(String is_favourites) {
        this.is_favourites = is_favourites;
    }

    public String getModelLatitude() {
        return modelLatitude;
    }

    public void setModelLatitude(String modelLatitude) {
        this.modelLatitude = modelLatitude;
    }

    public String getModelLongitude() {
        return modelLongitude;
    }

    public void setModelLongitude(String modelLongitude) {
        this.modelLongitude = modelLongitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    public String getModelCarLocation() {
        return modelCarLocation;
    }

    public void setModelCarLocation(String modelCarLocation) {
        this.modelCarLocation = modelCarLocation;
    }

    public String getModelCarMake() {
        return modelCarMake;
    }

    public void setModelCarMake(String modelCarMake) {
        this.modelCarMake = modelCarMake;
    }

    public String getModelCarModel() {
        return modelCarModel;
    }

    public void setModelCarModel(String modelCarModel) {
        this.modelCarModel = modelCarModel;
    }

    public String getModelCarIsNotSalvaged() {
        return modelCarIsNotSalvaged;
    }

    public void setModelCarIsNotSalvaged(String modelCarIsNotSalvaged) {
        this.modelCarIsNotSalvaged = modelCarIsNotSalvaged;
    }

    public String getModelCarCommercialHost() {
        return modelCarCommercialHost;
    }

    public void setModelCarCommercialHost(String modelCarCommercialHost) {
        this.modelCarCommercialHost = modelCarCommercialHost;
    }

    public String getModelCarLicenseLicenseCountry() {
        return modelCarLicenseLicenseCountry;
    }

    public void setModelCarLicenseLicenseCountry(String modelCarLicenseLicenseCountry) {
        this.modelCarLicenseLicenseCountry = modelCarLicenseLicenseCountry;
    }

    public String getModelCarLicenseLicenseState() {
        return modelCarLicenseLicenseState;
    }

    public void setModelCarLicenseLicenseState(String modelCarLicenseLicenseState) {
        this.modelCarLicenseLicenseState = modelCarLicenseLicenseState;
    }

    public String getModelCarLicenseLicenseNumber() {
        return modelCarLicenseLicenseNumber;
    }

    public void setModelCarLicenseLicenseNumber(String modelCarLicenseLicenseNumber) {
        this.modelCarLicenseLicenseNumber = modelCarLicenseLicenseNumber;
    }

    public String getModelCarLicenseFirstName() {
        return modelCarLicenseFirstName;
    }

    public void setModelCarLicenseFirstName(String modelCarLicenseFirstName) {
        this.modelCarLicenseFirstName = modelCarLicenseFirstName;
    }

    public String getModelCarLicenseMiddleName() {
        return modelCarLicenseMiddleName;
    }

    public void setModelCarLicenseMiddleName(String modelCarLicenseMiddleName) {
        this.modelCarLicenseMiddleName = modelCarLicenseMiddleName;
    }

    public String getModelCarLicenseLastName() {
        return modelCarLicenseLastName;
    }

    public void setModelCarLicenseLastName(String modelCarLicenseLastName) {
        this.modelCarLicenseLastName = modelCarLicenseLastName;
    }

    public String getModelCarLicenseExpirationMonth() {
        return modelCarLicenseExpirationMonth;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getUnlistReason() {
        return unlistReason;
    }

    public void setUnlistReason(String unlistReason) {
        this.unlistReason = unlistReason;
    }

    public String getSnoozeUntill() {
        return snoozeUntill;
    }

    public void setSnoozeUntill(String snoozeUntill) {
        this.snoozeUntill = snoozeUntill;
    }

    public void setModelCarLicenseExpirationMonth(String modelCarLicenseExpirationMonth) {
        this.modelCarLicenseExpirationMonth = modelCarLicenseExpirationMonth;
    }

    public String getModelCarLicenseExpirationDay() {
        return modelCarLicenseExpirationDay;
    }


    public String getCityMileage() {
        return cityMileage;
    }

    public void setCityMileage(String cityMileage) {
        this.cityMileage = cityMileage;
    }

    public void setModelCarLicenseExpirationDay(String modelCarLicenseExpirationDay) {
        this.modelCarLicenseExpirationDay = modelCarLicenseExpirationDay;
    }

    public String getModelCarLicenseExpirationYear() {
        return modelCarLicenseExpirationYear;
    }

    public void setModelCarLicenseExpirationYear(String modelCarLicenseExpirationYear) {
        this.modelCarLicenseExpirationYear = modelCarLicenseExpirationYear;
    }

    public String getModelCarLicenseBirthMonth() {
        return modelCarLicenseBirthMonth;
    }

    public void setModelCarLicenseBirthMonth(String modelCarLicenseBirthMonth) {
        this.modelCarLicenseBirthMonth = modelCarLicenseBirthMonth;
    }

    public String getModelCarLicenseBirthYear() {
        return modelCarLicenseBirthYear;
    }

    public void setModelCarLicenseBirthYear(String modelCarLicenseBirthYear) {
        this.modelCarLicenseBirthYear = modelCarLicenseBirthYear;
    }

    public String getModelCarLicenseBirthDay() {
        return modelCarLicenseBirthDay;
    }

    public void setModelCarLicenseBirthDay(String modelCarLicenseBirthDay) {
        this.modelCarLicenseBirthDay = modelCarLicenseBirthDay;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getModelCarGoalsEarning() {
        return modelCarGoalsEarning;
    }

    public void setModelCarGoalsEarning(String modelCarGoalsEarning) {
        this.modelCarGoalsEarning = modelCarGoalsEarning;
    }

    public String getModelCarGoalsOwnerUtilization() {
        return modelCarGoalsOwnerUtilization;
    }

    public void setModelCarGoalsOwnerUtilization(String modelCarGoalsOwnerUtilization) {
        this.modelCarGoalsOwnerUtilization = modelCarGoalsOwnerUtilization;
    }

    public String getModelCarGoalsRenterUtilization() {
        return modelCarGoalsRenterUtilization;
    }

    public void setModelCarGoalsRenterUtilization(String modelCarGoalsRenterUtilization) {
        this.modelCarGoalsRenterUtilization = modelCarGoalsRenterUtilization;
    }

    public String getModelCarAvailabilityAdvanceNotice() {
        return modelCarAvailabilityAdvanceNotice;
    }

    public void setModelCarAvailabilityAdvanceNotice(String modelCarAvailabilityAdvanceNotice) {
        this.modelCarAvailabilityAdvanceNotice = modelCarAvailabilityAdvanceNotice;
    }

    public String getModelCarDurationMinimumTripDuration() {
        return modelCarDurationMinimumTripDuration;
    }

    public void setModelCarDurationMinimumTripDuration(String modelCarDurationMinimumTripDuration) {
        this.modelCarDurationMinimumTripDuration = modelCarDurationMinimumTripDuration;
    }

    public String getModelCarDurationLongerWeekendTripPreferred() {
        return modelCarDurationLongerWeekendTripPreferred;
    }

    public void setModelCarDurationLongerWeekendTripPreferred(String modelCarDurationLongerWeekendTripPreferred) {
        this.modelCarDurationLongerWeekendTripPreferred = modelCarDurationLongerWeekendTripPreferred;
    }

    public String getModelCarDurationMaximumTripDuration() {
        return modelCarDurationMaximumTripDuration;
    }

    public void setModelCarDurationMaximumTripDuration(String modelCarDurationMaximumTripDuration) {
        this.modelCarDurationMaximumTripDuration = modelCarDurationMaximumTripDuration;
    }

    public String getModelCarLicensePlateNumber() {
        return modelCarLicensePlateNumber;
    }

    public void setModelCarLicensePlateNumber(String modelCarLicensePlateNumber) {
        this.modelCarLicensePlateNumber = modelCarLicensePlateNumber;
    }

    public String getModelCarLicensePlateRegion() {
        return modelCarLicensePlateRegion;
    }

    public void setModelCarLicensePlateRegion(String modelCarLicensePlateRegion) {
        this.modelCarLicensePlateRegion = modelCarLicensePlateRegion;
    }

    public String getBookInstatnly() {
        return bookInstatnly;
    }

    public void setBookInstatnly(String bookInstatnly) {
        this.bookInstatnly = bookInstatnly;
    }

    public String getDeliveryDirectToYou() {
        return deliveryDirectToYou;
    }

    public void setDeliveryDirectToYou(String deliveryDirectToYou) {
        this.deliveryDirectToYou = deliveryDirectToYou;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }


    public List<FutureModel> getFutures() {
        return futures;
    }

    public void setFutures(List<FutureModel> futures) {
        this.futures = futures;
    }

    public String getFuelTypeTitle() {
        return fuelTypeTitle;
    }

    public void setFuelTypeTitle(String fuelTypeTitle) {
        this.fuelTypeTitle = fuelTypeTitle;
    }

    public String getTransmissionTypeTitle() {
        return transmissionTypeTitle;
    }

    public void setTransmissionTypeTitle(String transmissionTypeTitle) {
        this.transmissionTypeTitle = transmissionTypeTitle;
    }

    public String getBodyTypeTitle() {
        return bodyTypeTitle;
    }

    public void setBodyTypeTitle(String bodyTypeTitle) {
        this.bodyTypeTitle = bodyTypeTitle;
    }

    public PartnerDetail getPartnerDetail() {
        return partnerDetail;
    }

    public void setPartnerDetail(PartnerDetail partnerDetail) {
        this.partnerDetail = partnerDetail;
    }

    public String getManufacturerTitle() {
        return manufacturerTitle;
    }

    public void setManufacturerTitle(String manufacturerTitle) {
        this.manufacturerTitle = manufacturerTitle;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public RatingModel getRatingModel() {
        return ratingModel;
    }

    public void setRatingModel(RatingModel ratingModel) {
        this.ratingModel = ratingModel;
    }


    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public DriverDetails getDriverDetails() {
        return driverDetails;
    }

    public void setDriverDetails(DriverDetails driverDetails) {
        this.driverDetails = driverDetails;
    }

    public String getAdditionalfeatures() {
        return additionalfeatures;
    }

    public void setAdditionalfeatures(String additionalfeatures) {
        this.additionalfeatures = additionalfeatures;
    }

    public String getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(String guidelines) {
        this.guidelines = guidelines;
    }

    public String getGuestPickupReturnInstructions() {
        return guestPickupReturnInstructions;
    }

    public void setGuestPickupReturnInstructions(String guestPickupReturnInstructions) {
        this.guestPickupReturnInstructions = guestPickupReturnInstructions;
    }

    public String getGuestWelcomeMessage() {
        return guestWelcomeMessage;
    }

    public void setGuestWelcomeMessage(String guestWelcomeMessage) {
        this.guestWelcomeMessage = guestWelcomeMessage;
    }

    public String getGuestCarMessage() {
        return guestCarMessage;
    }

    public void setGuestCarMessage(String guestCarMessage) {
        this.guestCarMessage = guestCarMessage;
    }

    public List<DBModel> getPriceByDays() {
        return priceByDays;
    }

    public void setPriceByDays(List<DBModel> priceByDays) {
        this.priceByDays = priceByDays;
    }

    public ArrayList<AirportLocationModel> getAirportList() {
        return airportList;
    }

    public void setAirportList(ArrayList<AirportLocationModel> list) {
        this.airportList = list;
    }

    public List<Unavailability> getUnavailability() {
        return unavailability;
    }

    public void setUnavailability(List<Unavailability> unavailability) {
        this.unavailability = unavailability;
    }

    @Override
    public String toString() {
        return "CarModel{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemSku='" + itemSku + '\'' +
                ", itemPageId='" + itemPageId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", manufacturerId='" + manufacturerId + '\'' +
                ", bodyTypeId='" + bodyTypeId + '\'' +
                ", transmissionTypeId='" + transmissionTypeId + '\'' +
                ", fuelTypeId='" + fuelTypeId + '\'' +
                ", modelName='" + modelName + '\'' +
                ", itemImage1='" + itemImage1 + '\'' +
                ", itemImage2='" + itemImage2 + '\'' +
                ", itemImage3='" + itemImage3 + '\'' +
                ", mileage='" + mileage + '\'' +
                ", fuelConsumption='" + fuelConsumption + '\'' +
                ", engineCapacity='" + engineCapacity + '\'' +
                ", maxPassengers='" + maxPassengers + '\'' +
                ", maxLuggage='" + maxLuggage + '\'' +
                ", itemDoors='" + itemDoors + '\'' +
                ", minDriverAge='" + minDriverAge + '\'' +
                ", priceGroupId='" + priceGroupId + '\'' +
                ", fixedRentalDeposit='" + fixedRentalDeposit + '\'' +
                ", unitsInStock='" + unitsInStock + '\'' +
                ", maxUnitsPerBooking='" + maxUnitsPerBooking + '\'' +
                ", optionsMeasurementUnit='" + optionsMeasurementUnit + '\'' +
                ", blogId='" + blogId + '\'' +
                ", modelYear='" + modelYear + '\'' +
                ", modelSeats='" + modelSeats + '\'' +
                ", modelDoors='" + modelDoors + '\'' +
                ", is_favourites='" + is_favourites + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", ratingModel=" + ratingModel +
                ", modelLatitude='" + modelLatitude + '\'' +
                ", modelLongitude='" + modelLongitude + '\'' +
                ", price='" + price + '\'' +
                ", modelDescription='" + modelDescription + '\'' +
                ", modelCarLocation='" + modelCarLocation + '\'' +
                ", modelCarMake='" + modelCarMake + '\'' +
                ", total_trip=" + total_trip +
                ", last_trip='" + last_trip + '\'' +
                ", modelCarModel='" + modelCarModel + '\'' +
                ", modelCarIsNotSalvaged='" + modelCarIsNotSalvaged + '\'' +
                ", modelCarCommercialHost='" + modelCarCommercialHost + '\'' +
                ", modelCarLicenseLicenseCountry='" + modelCarLicenseLicenseCountry + '\'' +
                ", modelCarLicenseLicenseState='" + modelCarLicenseLicenseState + '\'' +
                ", modelCarLicenseLicenseNumber='" + modelCarLicenseLicenseNumber + '\'' +
                ", modelCarLicenseFirstName='" + modelCarLicenseFirstName + '\'' +
                ", modelCarLicenseMiddleName='" + modelCarLicenseMiddleName + '\'' +
                ", modelCarLicenseLastName='" + modelCarLicenseLastName + '\'' +
                ", modelCarLicenseExpirationMonth='" + modelCarLicenseExpirationMonth + '\'' +
                ", modelCarLicenseExpirationDay='" + modelCarLicenseExpirationDay + '\'' +
                ", modelCarLicenseExpirationYear='" + modelCarLicenseExpirationYear + '\'' +
                ", modelCarLicenseBirthMonth='" + modelCarLicenseBirthMonth + '\'' +
                ", modelCarLicenseBirthYear='" + modelCarLicenseBirthYear + '\'' +
                ", modelCarLicenseBirthDay='" + modelCarLicenseBirthDay + '\'' +
                ", modelCarGoalsEarning='" + modelCarGoalsEarning + '\'' +
                ", modelCarGoalsOwnerUtilization='" + modelCarGoalsOwnerUtilization + '\'' +
                ", modelCarGoalsRenterUtilization='" + modelCarGoalsRenterUtilization + '\'' +
                ", modelCarAvailabilityAdvanceNotice='" + modelCarAvailabilityAdvanceNotice + '\'' +
                ", modelCarDurationMinimumTripDuration='" + modelCarDurationMinimumTripDuration + '\'' +
                ", modelCarDurationLongerWeekendTripPreferred='" + modelCarDurationLongerWeekendTripPreferred + '\'' +
                ", modelCarDurationMaximumTripDuration='" + modelCarDurationMaximumTripDuration + '\'' +
                ", modelCarLicensePlateNumber='" + modelCarLicensePlateNumber + '\'' +
                ", modelCarLicensePlateRegion='" + modelCarLicensePlateRegion + '\'' +
                ", bookInstatnly='" + bookInstatnly + '\'' +
                ", deliveryDirectToYou='" + deliveryDirectToYou + '\'' +
                ", pickupDate='" + pickupDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", futures=" + futures +
                ", fuelTypeTitle='" + fuelTypeTitle + '\'' +
                ", transmissionTypeTitle='" + transmissionTypeTitle + '\'' +
                ", bodyTypeTitle='" + bodyTypeTitle + '\'' +
                ", partnerDetail=" + partnerDetail +
                ", manufacturerTitle='" + manufacturerTitle + '\'' +
                ", photos=" + photos +
                '}';
    }
}
