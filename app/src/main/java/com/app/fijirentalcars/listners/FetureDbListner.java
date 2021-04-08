package com.app.fijirentalcars.listners;

import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.Manufacturer;

import java.util.ArrayList;

public interface FetureDbListner {
    public void addJobs(FutureModel vehicle);

    public ArrayList<FutureModel> getAllJobs();

    public int getJobCount();

    public int removeAllJobs();
}
