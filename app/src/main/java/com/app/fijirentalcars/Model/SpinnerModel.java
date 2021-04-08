package com.app.fijirentalcars.Model;

public class SpinnerModel {
    public SpinnerModel(String vlaue, String name) {
        this.vlaue = vlaue;
        this.name = name;
    }

    public SpinnerModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVlaue() {
        return vlaue;
    }

    public void setVlaue(String vlaue) {
        this.vlaue = vlaue;
    }

    String vlaue,name;
}
