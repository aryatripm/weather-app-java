package com.arya.weather.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.arya.weather.entity.Clouds;
import com.arya.weather.entity.Main;
import com.arya.weather.entity.Weather;
import com.arya.weather.entity.WeatherRespone;
import com.arya.weather.entity.Wind;

import java.util.ArrayList;

public class WeatherPreference {

    private final SharedPreferences preferences;

    private static final String PREFS_WEATHER = "weather_pref";
    private static final String DATE = "date";
    private static final String ICON = "icon";
    private static final String TEMP = "temp";
    private static final String FEELS_LIKE = "feels_like";
    private static final String LOW = "low_high";
    private static final String HIGH = "low_high";
    private static final String CITY = "city";
    private static final String HUMIDITY = "humidity";
    private static final String PRESSURE = "pressure";
    private static final String WIND = "wind";
    private static final String CLOUDS = "clouds";

    private static final String NOTIF = "notif";

    public WeatherPreference(Context context) {
        this.preferences = context.getSharedPreferences(PREFS_WEATHER, Context.MODE_PRIVATE);
    }

    public void setWeather(WeatherRespone value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(DATE, value.getDt());
        editor.putString(ICON, value.getWeathers().get(0).getIcon());
        editor.putString(TEMP, value.getMain().getTemp().toString());
        editor.putString(FEELS_LIKE, value.getMain().getFeels_like().toString());
        editor.putString(LOW, value.getMain().getTemp_min().toString());
        editor.putString(HIGH, value.getMain().getTemp_min().toString());
        editor.putString(CITY, value.getName());
        editor.putString(HUMIDITY, value.getMain().getHumidity().toString());
        editor.putString(PRESSURE, value.getMain().getPressure().toString());
        editor.putString(WIND, value.getWind().getSpeed().toString());
        editor.putString(CLOUDS, value.getClouds().getAll().toString());
        editor.apply();
    }

    public WeatherRespone getWeather() {
        WeatherRespone weather = new WeatherRespone();

        weather.setDt(preferences.getInt(DATE, 0));

        Weather icon = new Weather();
        ArrayList<Weather> icons = new ArrayList<>();
        icon.setIcon(preferences.getString(ICON, "123"));
        icons.add(icon);
        weather.setWeathers(icons);

        Main main = new Main();
        main.setTemp(Double.parseDouble(preferences.getString(TEMP, "0.0")));
        main.setFeels_like(Double.parseDouble(preferences.getString(FEELS_LIKE, "0.0")));
        main.setTemp_min(Double.parseDouble(preferences.getString(LOW, "0.0")));
        main.setTemp_max(Double.parseDouble(preferences.getString(HIGH, "0.0")));
        main.setHumidity(Integer.parseInt(preferences.getString(HUMIDITY, "0")));
        main.setPressure(Integer.parseInt(preferences.getString(PRESSURE, "0")));
        weather.setMain(main);

        weather.setName(preferences.getString(CITY, "Soreang"));

        Wind wind = new Wind();
        wind.setSpeed(Double.parseDouble(preferences.getString(WIND, "0.0")));
        weather.setWind(wind);

        Clouds clouds = new Clouds();
        clouds.setAll(Integer.parseInt(preferences.getString(CLOUDS, "0")));
        weather.setClouds(clouds);

        return weather;
    }

    public void setWeatherNotif(Boolean status) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(NOTIF, status);
        editor.apply();
    }

    public Boolean getWeatherNotif() {
        return preferences.getBoolean(NOTIF, false);
    }
}
