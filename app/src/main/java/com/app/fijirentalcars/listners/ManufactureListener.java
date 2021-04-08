package com.app.fijirentalcars.listners;

import com.app.fijirentalcars.Model.Manufacturer;

import java.util.ArrayList;

public interface ManufactureListener {

    public void addJobs(Manufacturer vehicle);

    public ArrayList<Manufacturer> getAllJobs();

    public int getJobCount();

    public int removeAllJobs();

}
