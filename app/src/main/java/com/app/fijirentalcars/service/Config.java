package com.app.fijirentalcars.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by p51 on 08-Mar-18.
 */

public class Config {

    public static String myBaseURL = "https://fijirentalcars.siddhidevelopment.com/";

    private static Retrofit retrofit = null;

    public static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setLenient()
            .create();


    public static Retrofit getClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(80, TimeUnit.SECONDS);
        httpClient.writeTimeout(90, TimeUnit.SECONDS);
        /*httpClient.connectTimeout(100000, TimeUnit.SECONDS);
        httpClient.readTimeout(50000, TimeUnit.SECONDS);
        httpClient.writeTimeout(50000,TimeUnit.SECONDS);*/

// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(myBaseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}
