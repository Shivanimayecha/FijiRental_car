package com.app.fijirentalcars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.app.fijirentalcars.Model.ListingCarModel;
import com.app.fijirentalcars.fragments.ChatFragment;
import com.app.fijirentalcars.fragments.HomeFragment;
import com.app.fijirentalcars.fragments.HostFragment;
import com.app.fijirentalcars.fragments.HostStart;
import com.app.fijirentalcars.fragments.ProfileFragment;
import com.app.fijirentalcars.fragments.TripFragment;
import com.app.fijirentalcars.service.APIService;
import com.app.fijirentalcars.service.Config;
import com.app.fijirentalcars.util.FijiRentalUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
    BottomNavigationView bottomNavigation;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = this.getWindow();
        Log.e("MainActivity", "MainActivity");
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.ic_action_home_selected);
        bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.ic_action_trip_selected);
        bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.ic_action_chat_selected);
        bottomNavigation.getMenu().getItem(3).setIcon(R.drawable.ic_action_host_selected);
        bottomNavigation.getMenu().getItem(4).setIcon(R.drawable.ic_action_profile_selected);

        if(FijiRentalUtils.getRoles(this)!=null) {
            if (FijiRentalUtils.getRoles(this).equalsIgnoreCase(FijiRentalUtils.Owner)) {
                bottomNavigation.getMenu().getItem(3).setVisible(true);
            } else {
                bottomNavigation.getMenu().getItem(3).setVisible(false);
            }
        }else {
            bottomNavigation.getMenu().getItem(3).setVisible(false);
        }

        if(FijiRentalUtils.isNetworkAvailable(this)){
            if(!TextUtils.isEmpty(FijiRentalUtils.getAccessToken(MainActivity.this))){
                getprofiledetails();
            }
        }




        openFragment(HomeFragment.newInstance("", ""));
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            openFragment(HomeFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_trip:
                            if (TextUtils.isEmpty(FijiRentalUtils.getAccessToken(MainActivity.this))) {
                                Intent i = new Intent(MainActivity.this, LetsStartActivity.class);
                                startActivity(i);
                                return false;
                            }
                            openFragment(TripFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_chat:
                            if (TextUtils.isEmpty(FijiRentalUtils.getAccessToken(MainActivity.this))) {
                                Intent i = new Intent(MainActivity.this, LetsStartActivity.class);
                                startActivity(i);
                                return false;
                            }
                            openFragment(ChatFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_host:
                            if (TextUtils.isEmpty(FijiRentalUtils.getAccessToken(MainActivity.this))) {
                                Intent i = new Intent(MainActivity.this, LetsStartActivity.class);
                                startActivity(i);
                                return false;
                            }
                            if (!FijiRentalUtils.getCarListedVal(MainActivity.this).equalsIgnoreCase("1")) {
                                openFragment(HostStart.newInstance("", ""));

                            } else {
                                openFragment(HostFragment.newInstance("", ""));

                            }
                            return true;
                        case R.id.navigation_profile:
                            openFragment(ProfileFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                Paper.book().write("PLACE", place);

                Intent i = new Intent(MainActivity.this, CarListActivity.class);
                startActivity(i);


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void searchPlace() {
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(MainActivity.this);

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    public void startCarListing() {

        bottomNavigation.setSelectedItemId(bottomNavigation.getMenu().getItem(3).getItemId());
    }

    private void getprofiledetails() {


        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.getprofiledetails(FijiRentalUtils.getAccessToken(MainActivity.this));


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";

                try {
                    String jstr = response.body().string();
                    JSONObject jsonobject;
                    try {
                        jsonobject = new JSONObject(jstr);
                        message = jsonobject.optString("message");
                        if (jsonobject.optString("code").matches("200")) {
                            JSONObject data = jsonobject.optJSONObject("data");


                            FijiRentalUtils.savePreferences(FijiRentalUtils.ID, "" + data.optString("ID"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_login, "" + data.optString("user_login"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_nicename, "" + data.optString("user_nicename"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_email, "" + data.optString("user_email"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.display_name, "" + data.optString("display_name"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.accessToken, "" + data.optString("accessToken"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.user_pic_url, "" + data.optString("user_pic_url"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.roles, "" + data.optString("role"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.about_desc, "" + data.optString("description"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.work, "" + data.optString("work"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.school, "" + data.optString("school"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.language, "" + data.optString("languages"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.about_desc, "" + data.optString("description"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.work, "" + data.optString("work"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.school, "" + data.optString("school"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.language, "" + data.optString("languages"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.push_notification, "" + data.optString("push_notification"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.mail_notification, "" + data.optString("mail_notification"), MainActivity.this);
                            FijiRentalUtils.savePreferences(FijiRentalUtils.is_car_listed, "" + data.optString("iscarlisted"), MainActivity.this);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MainActivity.this, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MainActivity.this, "0");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FijiRentalUtils.ShowValidation(FijiRentalUtils.errorMessage, MainActivity.this, "0");
                FijiRentalUtils.v("Response is:- " + t.getMessage());
            }
        });
    }

}

