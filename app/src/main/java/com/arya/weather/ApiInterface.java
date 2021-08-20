package com.arya.weather;

import com.arya.weather.entity.WeatherRespone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather")
    Call<WeatherRespone> getWeather(
            @Query("q") String cityName,
            @Query("appid") String api_key,
            @Query("units") String units
            );
}
