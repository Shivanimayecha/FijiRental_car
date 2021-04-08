package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FutureModel implements Serializable {
    @SerializedName("feature_id")
    @Expose
    private String featureId;
    @SerializedName("feature_title")
    @Expose
    private String featureTitle;
    @SerializedName("feature_icon")
    @Expose
    private String featureIcon;


    boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeatureTitle() {
        return featureTitle;
    }

    public void setFeatureTitle(String featureTitle) {
        this.featureTitle = featureTitle;
    }

    public String getFeatureIcon() {
        return featureIcon;
    }

    public void setFeatureIcon(String featureIcon) {
        this.featureIcon = featureIcon;
    }

}
