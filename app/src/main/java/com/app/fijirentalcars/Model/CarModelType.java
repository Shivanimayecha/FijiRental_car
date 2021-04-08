package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarModelType {
    @SerializedName("model_name")
    @Expose
    private String modelName;
    @SerializedName("model_make_id")
    @Expose
    private String modelMakeId;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelMakeId() {
        return modelMakeId;
    }

    public void setModelMakeId(String modelMakeId) {
        this.modelMakeId = modelMakeId;
    }
}
