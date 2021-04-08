package com.app.fijirentalcars.Model;

import java.io.Serializable;

public class DBModel implements Serializable {
    String fare;
    String date;
    int id;

    public DBModel() {

    }

    public DBModel(String title, String date) {
        this.fare = title;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return fare;
    }

    public void setTitle(String title) {


        this.fare = String .valueOf((int)Double.parseDouble(title));
    }

}
