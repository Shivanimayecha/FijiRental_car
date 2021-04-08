package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PartnerDetail implements Serializable {

    @SerializedName("user_pic_url")
    @Expose
    private String userPicUrl;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("total_trip")
    @Expose
    private Integer totalTrip;
    @SerializedName("joined")
    @Expose
    private String joined;
    @SerializedName("ID")
    @Expose
    private Integer iD;

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getTotalTrip() {
        return totalTrip;
    }

    public void setTotalTrip(Integer totalTrip) {
        this.totalTrip = totalTrip;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }
}
