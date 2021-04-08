package com.app.fijirentalcars;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fijirentalcars.Model.CarModel;
import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeLocationChange extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private GoogleMap mMap;
    MapView mapView;
    ImageView backBtn;
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
    private static int AUTOCOMPLETE_REQUEST_CODE = 1001;
    TextView location,saveBtn;
    private CarModel carModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_location_change);

        backBtn=findViewById(R.id.iv_back);
        location=findViewById(R.id.et_location);
        saveBtn=findViewById(R.id.tv_next);

        backBtn.setOnClickListener(this);
        location.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        mapView = (MapView) findViewById(R.id.map2);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        ((FijiCarRentalApplication) getApplication()).setListCarModel(new ListingCarModel());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_back){
            finish();
        }else if(v.getId()==R.id.et_location){
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        }if(v.getId()==R.id.tv_next){

            if(location.getText().toString().equalsIgnoreCase(carModel.getModelCarLocation())) {
                finish();
            }else {
                updateLocation();
            }

        }
    }

    private void updateLocation() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        ListingCarModel ListingcarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
        APIService apiService = Config.getClient().create(APIService.class);
        RequestBody body;
        MultipartBody.Builder reqBuilder = new MultipartBody.Builder();
        reqBuilder.setType(MultipartBody.FORM);
        reqBuilder.addFormDataPart("item_id", carModel.getItemId());
        reqBuilder.addFormDataPart("item_model_latitude", String.valueOf(ListingcarModel.getLatLng().latitude));
        reqBuilder.addFormDataPart("item_model_longitude", String.valueOf(ListingcarModel.getLatLng().longitude));
        reqBuilder.addFormDataPart("location", ListingcarModel.getStreetAdrees());
        reqBuilder.addFormDataPart("car_country", ListingcarModel.getCountryCode());
        reqBuilder.addFormDataPart("car_city", ListingcarModel.getCity());
        reqBuilder.addFormDataPart("car_state", ListingcarModel.getState());
        reqBuilder.addFormDataPart("street", ListingcarModel.getStreetAdrees());
        reqBuilder.addFormDataPart("postcode", ListingcarModel.getZipCode());


        body = reqBuilder
                .build();
        Call<ResponseBody> call = apiService.updateCarListing(FijiRentalUtils.getAccessToken(this), body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";
                progressDialog.dismiss();

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            JSONObject data = jsonobject.optJSONObject("data");
                            JSONObject data_array = data.optJSONObject("data");
                            FijiRentalUtils.updateCarModel(data_array,HomeLocationChange.this);
                            ((FijiCarRentalApplication) getApplication()).setListCarModel(new ListingCarModel());
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, HomeLocationChange.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, HomeLocationChange.this, "0");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();

        carModel = Paper.book().read(FijiRentalUtils.carModel);
        if(carModel!=null){

            ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();
            if (listingCarModel != null) {
                FijiRentalUtils.Logger("TAGS","is nul "+(listingCarModel.getStreetAdrees()==null ));
                if(listingCarModel.getStreetAdrees()==null ){
                    location.setText(carModel.getModelCarLocation());
                }else {
                    location.setText(listingCarModel.getStreetAdrees() + " " + listingCarModel.getCity() + " " + listingCarModel.getState() + " " + listingCarModel.getCountryCode() + " " + listingCarModel.getZipCode());
                }
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setAllGesturesEnabled(false);

        mMap.getUiSettings().setCompassEnabled(false);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location_green);
        if (carModel != null) {
            LatLng hotel = new LatLng(Double.parseDouble(carModel.getModelLatitude()), Double.parseDouble(carModel.getModelLongitude()));
            mMap.addMarker(new
                    MarkerOptions().position(hotel).title("Hotel Ritz Inn")).setIcon(icon);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(hotel));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hotel, 16.0f));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);


                ListingCarModel listingCarModel = ((FijiCarRentalApplication) getApplication()).getCarModel();

                if (!TextUtils.isEmpty(listingCarModel.getCountry())) {
                    listingCarModel.setCountryChange(true);
                    listingCarModel.setLocationSet(false);
                }

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String countryCode = addresses.get(0).getCountryCode();

                String[] addressName = address.split(",");

                listingCarModel.setCity(city);
                listingCarModel.setState(state);
                listingCarModel.setCountry(country);
                listingCarModel.setZipCode(postalCode);
                listingCarModel.setStreetAdrees(addressName[0] + addressName[1]);
                listingCarModel.setCountryCode(countryCode);
                listingCarModel.setLatLng(place.getLatLng());
                ((FijiCarRentalApplication) getApplication()).setListCarModel(listingCarModel);


                Intent addressIntent = new Intent(this, AddressActivity.class);
                startActivity(addressIntent);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("TAGS", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}