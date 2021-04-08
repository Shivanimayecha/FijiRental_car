package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransmissionType {

    @SerializedName("transmission_type_id")
    @Expose
    private String transmissionTypeId;
    @SerializedName("transmission_type_title")
    @Expose
    private String transmissionTypeTitle;

    public String getTransmissionTypeId() {
        return transmissionTypeId;
    }

    public void setTransmissionTypeId(String transmissionTypeId) {
        this.transmissionTypeId = transmissionTypeId;
    }

    public String getTransmissionTypeTitle() {
        return transmissionTypeTitle;
    }

    public void setTransmissionTypeTitle(String transmissionTypeTitle) {
        this.transmissionTypeTitle = transmissionTypeTitle;
    }

}
