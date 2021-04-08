package com.app.fijirentalcars.service;


import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIService {

    // all api used in all are below
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/login")
    Call<ResponseBody> app_login(@Field("deviceId") String deviceId,
                                 @Field("deviceType") String deviceType,
                                 @Field("deviceToken") String deviceToken,
                                 @Field("email") String email,
                                 @Field("password") String password);

    //http://fijirentalcars.siddhidevelopment.com/wp-json/wpsisapi/v1/fblogin

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/fblogin")
    Call<ResponseBody> fblogin(@Field("deviceId") String deviceId,
                               @Field("deviceType") String deviceType,
                               @Field("deviceToken") String deviceToken,
                               @FieldMap HashMap<String,String> data);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/loginwithgoogle")
    Call<ResponseBody> googlelogin(@Field("deviceId") String deviceId,
                                   @Field("deviceType") String deviceType,
                                   @Field("deviceToken") String deviceToken,
                                   @FieldMap HashMap<String,String> data);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/register")
    Call<ResponseBody> register(@Field("deviceId") String deviceId,
                                @Field("deviceType") String deviceType,
                                @Field("deviceToken") String deviceToken,
                                @FieldMap HashMap<String, String> data);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/forgotpassword")
    Call<ResponseBody> forgotpassword(@Field("email") String email);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @GET("wp-json/wpsisapi/v1/getprofiledetails")
    Call<ResponseBody> getprofiledetails(@Header("accessToken") String accessToken);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/getcarslists")
    Call<ResponseBody> getcarslists(@Header("accessToken") String accessToken,
                                    @Field("page") String page,
                                    @Field("limit") String limit,
                                    @Field("user_id") String user_id,
                                    @FieldMap HashMap<String, String> data);


    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/getfavouritescarlists")
    Call<ResponseBody> getfavouritescarlists(@Header("accessToken") String accessToken,
                                             @Field("page") String page,
                                             @Field("limit") String limit);

    //http://fijirentalcars.siddhidevelopment.com/wp-json/wpsisapi/v1/getgeneraloptions

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @POST("wp-json/wpsisapi/v1/getgeneraloptions")
    Call<ResponseBody> getgeneraloptions();


    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/addto_favourites")
    Call<ResponseBody> addto_favourites(@Header("accessToken") String accessToken,
                                        @Field("item_id") String item_id);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/removefrom_favourites")
    Call<ResponseBody> removefrom_favourites(@Header("accessToken") String accessToken,
                                             @Field("item_id") String item_id);


    @Headers({

            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @POST("wp-json/wpsisapi/v1/updateprofile")
    Call<ResponseBody> updateprofile(@Header("accessToken") String accessToken,
                                     @Body RequestBody body);


    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/getcardetails")
    Call<ResponseBody> getCarDetails(@Header("accessToken") String accessToken,
                                     @FieldMap HashMap<String, String> body);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/getreviews")
    Call<ResponseBody> getReviewList(@Header("accessToken") String accessToken,
                                     @FieldMap HashMap<String, String> body);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })

    @GET("wp-json/wpsisapi/v1/get_countries")
    Call<ResponseBody> getCountryList(@Header("accessToken") String accessToken);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @GET("wp-json/wpsisapi/v1/getvehicleprotection")
    Call<ResponseBody> getInsuranceplan(@Header("accessToken") String accessToken);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @POST("wp-json/wpsisapi/v1/logout")
    Call<ResponseBody> logOut(@Header("accessToken") String accessToken);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/checkvin")
    Call<ResponseBody> checkCarVIN(@Header("accessToken") String accessToken,
                                   @FieldMap HashMap<String, String> body);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/getmakebyyear")
    Call<ResponseBody> getMakeList(@Header("accessToken") String accessToken,
                                   @FieldMap HashMap<String, String> body);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/getmodelsbymake")
    Call<ResponseBody> getModelList(@Header("accessToken") String accessToken,
                                    @FieldMap HashMap<String, String> body);


    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/listmycars")
    Call<ResponseBody> listMyCar(@Header("accessToken") String accessToken,
                                 @Field("page") String page);

    @Headers({

            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @POST("wp-json/wpsisapi/v1/updatecarimage")
    Call<ResponseBody> uploadImage(@Header("accessToken") String accessToken,
                                   @Body RequestBody page);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/listyourcar")
    Call<ResponseBody> listYourCar(@Header("accessToken") String accessToken,
                                   @FieldMap HashMap<String, String> body);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/getstatelist")
    Call<ResponseBody> getStateList(@Header("accessToken") String accessToken,
                                    @Field("country") String page);

    @Headers({

            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @POST("wp-json/wpsisapi/v1/updateyourcar")
    Call<ResponseBody> updateCarListing(@Header("accessToken") String accessToken,
                                        @Body RequestBody body);

    @Headers({

            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @POST("wp-json/wpsisapi/v1/setcarprice")
    Call<ResponseBody> setCarPrice(@Header("accessToken") String accessToken,
                                   @Body RequestBody body);

    @Headers({

            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @POST("wp-json/wpsisapi/v1/add_unavailability")
    Call<ResponseBody> addUnavailability(@Header("accessToken") String accessToken,
                                         @Body RequestBody body);

    @Headers({

            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @POST("wp-json/wpsisapi/v1/remove_unavailability")
    Call<ResponseBody> removeUnavailability(@Header("accessToken") String accessToken,
                                            @Body RequestBody body);


    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/extralist")
    Call<ResponseBody> listExtras(@Header("accessToken") String accessToken,
                                  @FieldMap HashMap<String, String> body);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/addextra")
    Call<ResponseBody> addExtras(@Header("accessToken") String accessToken,
                                 @FieldMap HashMap<String, String> body);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/extratypes")
    Call<ResponseBody> extraTypes(@Header("accessToken") String accessToken,
                                  @FieldMap HashMap<String, String> body);

    @Headers({

            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @POST("wp-json/wpsisapi/v1/delete_car")
    Call<ResponseBody> deleteCarListing(@Header("accessToken") String accessToken,
                                        @Body RequestBody body);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/getpickuplist")
    Call<ResponseBody> getNearestAirportList(@Header("accessToken") String accessToken,
                                             @FieldMap HashMap<String, String> body);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "apikey:R^S9(z{(l!cdJ:wCoAX/!eY<~0egMDRUG5_?M&.I>B s(sKmpvY:GLEYAM[8<nHh"
    })
    @FormUrlEncoded
    @POST("wp-json/wpsisapi/v1/select_delivery_location")
    Call<ResponseBody> updateAirportList(@Header("accessToken") String accessToken,
                                         @FieldMap HashMap<String, String> body);
}

