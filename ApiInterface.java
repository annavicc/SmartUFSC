package com.example.ine5424.smartufsc;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by anna on 6/16/17.
 */

public interface ApiInterface {

    @GET("api/lines/")
    Call<List<Line>> getLines();

    @GET("api/linestops/{id}")
    Call<LineStop> getLineStop(@Path("id") int id);

    @GET("api/busstops/{id}")
    Call<Stop> getStop(@Path("id") int id);
}
