package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Unavailability implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("car_item_id")
    @Expose
    private String carItemId;
    @SerializedName("to_date_time")
    @Expose
    private String toDateTime;
    @SerializedName("from_date_time")
    @Expose
    private String fromDateTime;
    @SerializedName("isrepeat")
    @Expose
    private String isrepeat;
    @SerializedName("every_sunday")
    @Expose
    private String everySunday;
    @SerializedName("every_mondy")
    @Expose
    private String everyMondy;
    @SerializedName("every_tuesday")
    @Expose
    private String everyTuesday;
    @SerializedName("every_wednesday")
    @Expose
    private String everyWednesday;
    @SerializedName("every_thursday")
    @Expose
    private String everyThursday;
    @SerializedName("every_friday")
    @Expose
    private String everyFriday;
    @SerializedName("every_saturday")
    @Expose
    private String everySaturday;
    @SerializedName("until_date_time")
    @Expose
    private String untilDateTime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarItemId() {
        return carItemId;
    }

    public void setCarItemId(String carItemId) {
        this.carItemId = carItemId;
    }

    public String getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(String toDateTime) {
        this.toDateTime = toDateTime;
    }

    public String getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(String fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public String getIsrepeat() {
        return isrepeat;
    }

    public void setIsrepeat(String isrepeat) {
        this.isrepeat = isrepeat;
    }

    public String getEverySunday() {
        return everySunday;
    }

    public void setEverySunday(String everySunday) {
        this.everySunday = everySunday;
    }

    public String getEveryMondy() {
        return everyMondy;
    }

    public void setEveryMondy(String everyMondy) {
        this.everyMondy = everyMondy;
    }

    public String getEveryTuesday() {
        return everyTuesday;
    }

    public void setEveryTuesday(String everyTuesday) {
        this.everyTuesday = everyTuesday;
    }

    public String getEveryWednesday() {
        return everyWednesday;
    }

    public void setEveryWednesday(String everyWednesday) {
        this.everyWednesday = everyWednesday;
    }

    public String getEveryThursday() {
        return everyThursday;
    }

    public void setEveryThursday(String everyThursday) {
        this.everyThursday = everyThursday;
    }

    public String getEveryFriday() {
        return everyFriday;
    }

    public void setEveryFriday(String everyFriday) {
        this.everyFriday = everyFriday;
    }

    public String getEverySaturday() {
        return everySaturday;
    }

    public void setEverySaturday(String everySaturday) {
        this.everySaturday = everySaturday;
    }

    public String getUntilDateTime() {
        return untilDateTime;
    }

    public void setUntilDateTime(String untilDateTime) {
        this.untilDateTime = untilDateTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
