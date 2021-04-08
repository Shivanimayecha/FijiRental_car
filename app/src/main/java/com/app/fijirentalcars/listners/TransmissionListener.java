package com.app.fijirentalcars.listners;

import com.app.fijirentalcars.Model.FuelType;
import com.app.fijirentalcars.Model.TransmissionType;

import java.util.ArrayList;

public interface TransmissionListener {

    public void addJobs(TransmissionType vehicle);

    public ArrayList<TransmissionType> getAllJobs();

    public int getJobCount();

    public int removeAllJobs();

}
