package com.arya.weather.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_history")
public class WeatherTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String code_weather;
    private String date;

    public WeatherTable(String code_weather, String date) {
        this.code_weather = code_weather;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCode_weather() {
        return code_weather;
    }
    public void setCode_weather(String code_weather) {
        this.code_weather = code_weather;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
