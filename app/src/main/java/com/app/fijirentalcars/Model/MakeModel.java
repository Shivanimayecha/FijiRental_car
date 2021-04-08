package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeModel {
    @SerializedName("make_id")
    @Expose
    private String makeId;
    @SerializedName("make_display")
    @Expose
    private String makeDisplay;
    @SerializedName("make_is_common")
    @Expose
    private String makeIsCommon;
    @SerializedName("make_country")
    @Expose
    private String makeCountry;

    public String getMakeId() {
        return makeId;
    }

    public void setMakeId(String makeId) {
        this.makeId = makeId;
    }

    public String getMakeDisplay() {
        return makeDisplay;
    }

    public void setMakeDisplay(String makeDisplay) {
        this.makeDisplay = makeDisplay;
    }

    public String getMakeIsCommon() {
        return makeIsCommon;
    }

    public void setMakeIsCommon(String makeIsCommon) {
        this.makeIsCommon = makeIsCommon;
    }

    public String getMakeCountry() {
        return makeCountry;
    }

    public void setMakeCountry(String makeCountry) {
        this.makeCountry = makeCountry;
    }
}
