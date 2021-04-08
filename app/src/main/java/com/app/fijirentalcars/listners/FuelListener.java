package com.app.fijirentalcars.listners;

import com.app.fijirentalcars.Model.FuelType;

import java.util.ArrayList;

public interface FuelListener {

    public void addJobs(FuelType vehicle);

    public ArrayList<FuelType> getAllJobs();

    public int getJobCount();

    public int removeAllJobs();

}
