package com.app.fijirentalcars.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BodyType implements Serializable {


    @SerializedName("body_type_id")
    @Expose
    private String body_type_id;
    @SerializedName("body_type_title")
    @Expose
    private String bodyTypeTitle;

    public String getBodyTypeTitle() {
        return bodyTypeTitle;
    }

    public void setBodyTypeTitle(String bodyTypeTitle) {
        this.bodyTypeTitle = bodyTypeTitle;
    }

    public String getBody_type_id() {
        return body_type_id;
    }

    public void setBody_type_id(String body_type_id) {
        this.body_type_id = body_type_id;
    }
}
