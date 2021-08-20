package com.arya.weather.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather_history")
    List<WeatherTable> getAll();

    @Insert
    void insert(WeatherTable weather);

    @Delete
    void delete(WeatherTable weather);
}
