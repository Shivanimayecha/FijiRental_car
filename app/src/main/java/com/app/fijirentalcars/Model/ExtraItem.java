package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExtraItem implements Serializable {
    @SerializedName("extra_name")
    @Expose
    private String extraName;
    @SerializedName("id")
    @Expose
    private String id;

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ExtraItem{" +
                "extraName='" + extraName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
