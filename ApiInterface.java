package com.example.ine5424.smartufsc;

import java.util.List;

import javax.xml.transform.Result;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by anna on 6/16/17.
 */

public interface ApiInterface {

    @GET("api/lines/")
    Call<List<BusLine>> getLines();

    @GET("api/lines/{id}")
    Call<BusLine> getLineById(@Path("id") int id);

    @GET("api/linestops/{id}")
    Call<LineStop> getLineStop(@Path("id") int id);

    @GET("api/busstops/{id}")
    Call<Stop> getStop(@Path("id") int id);

    @GET("api/buses/{id}")
    Call<Bus> getBus(@Path("id") int id);

    @POST("api/passengerstops/")
    @FormUrlEncoded
    Call<PassengerStops> savePassengerStop(@Field("bus") int bus,
                                   @Field("busstop") int busstop);

    @DELETE("api/passengerstops/{id}/")
    Call<ResponseBody> deletePassengerStop(@Path("id") int id);



}

