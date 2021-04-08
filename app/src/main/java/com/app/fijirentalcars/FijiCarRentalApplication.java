package com.app.fijirentalcars;

import androidx.multidex.MultiDexApplication;

import com.app.fijirentalcars.Model.BodyType;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.Model.TransmissionType;
import com.app.fijirentalcars.Model.VinModel;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.libraries.places.api.Places;

import io.paperdb.Paper;

public class FijiCarRentalApplication extends MultiDexApplication {

    public ListingCarModel carModel;
    VinModel vinModel;
    BodyType bodyType;
    TransmissionType transmissionType;

    public ListingCarModel getCarModel() {
        return carModel;
    }

    public void setListCarModel(ListingCarModel vinModel) {
        FijiRentalUtils.Logger("TAGS","data vin "+vinModel.toString());
        this.carModel = vinModel;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Paper.init(this);
        Places.initialize(getApplicationContext(), "AIzaSyBFeAP7LwqqnpUj4MEPFX0ZxDUlGGeiIoo");   //AIzaSyAsdt2JYu8QhwF5LBpgPU20_07t_9oztnQ
        FijiRentalUtils.getSpinnerValue();

        carModel=new ListingCarModel();
        vinModel=new VinModel();
        bodyType=new BodyType();
        transmissionType=new TransmissionType();
        carModel.setModel(vinModel);
        carModel.setBodyType(bodyType);
        carModel.setTransmissionType(transmissionType);

    }
}
