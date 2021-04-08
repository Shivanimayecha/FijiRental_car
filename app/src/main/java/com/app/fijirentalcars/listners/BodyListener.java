package com.app.fijirentalcars.listners;

import com.app.fijirentalcars.Model.BodyType;
import com.app.fijirentalcars.Model.FuelType;

import java.util.ArrayList;

public interface BodyListener {

    public void addJobs(BodyType vehicle);

    public ArrayList<BodyType> getAllJobs();

    public int getJobCount();

    public int removeAllJobs();

}
