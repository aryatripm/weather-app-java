package com.arya.weather;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.arya.weather.Preference.WeatherPreference;
import com.arya.weather.db.AppExecutors;
import com.arya.weather.db.WeatherDao;
import com.arya.weather.db.WeatherDatabase;
import com.arya.weather.db.WeatherTable;
import com.arya.weather.entity.WeatherRespone;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.View;
import android.widget.Toast;

import com.arya.weather.databinding.ActivityScrollingBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private WeatherPreference weatherPreference;

    // RecyclerView
    private RecyclerView recyclerView;
    private final ArrayList<WeatherTable> history = new ArrayList<>();
    private RecyclerView.LayoutManager RecyclerViewLayoutManager;
    private Adapter adapter;
    private LinearLayoutManager HorizontalLayout;

    // API
    private Retrofit retrofit;
    private ApiInterface api;
    private WeatherRespone respone;
    public static final String API_KEY = "your-api-key";
    private String city = "Soreang";

    // Reminder
    private WorkManager workManager;
    private PeriodicWorkRequest periodicWorkRequest;
    private Boolean isReminderActive;

    // Database
    private WeatherDatabase db;
    private WeatherDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        workManager = WorkManager.getInstance(this);
        weatherPreference = new WeatherPreference(this);

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
            fetchWeather();
            history.removeAll(history);
            AppExecutors.getInstance().diskIO().execute(() -> {
                history.addAll(dao.getAll());
            });
            adapter.notifyItemChanged(history.size());
            showSnackBar(view, "Berhasil memuat ulang!");
        });

        // Reminder
        isReminderActive = weatherPreference.getWeatherNotif();
        checkNotif();
        binding.toolbarContent.weatherNotif.setOnClickListener(view -> {
            if (!isReminderActive) {
                startReminder();
                weatherPreference.setWeatherNotif(true);
                showSnackBar(binding.getRoot(), "Reminder aktif!");
            } else {
                stopReminder();
                weatherPreference.setWeatherNotif(false);
                showSnackBar(binding.getRoot(), "Reminder berhenti");
            }
            isReminderActive = weatherPreference.getWeatherNotif();
            checkNotif();
        });

        // Search
        SearchView searchView =  binding.appBarSearch;
        searchView.setQueryHint("Cari kota...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                city = query;
                fetchWeather();
                searchView.clearFocus();
                showSnackBar(binding.getRoot(), "Berhasil mengubah kota!");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Database
        db = WeatherDatabase.getDatabase(this);
        dao = db.weatherDao();
        AppExecutors.getInstance().diskIO().execute(() ->
                history.addAll(dao.getAll())
        );

        // API
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiInterface.class);


        // RecyclerView
        recyclerView = binding.content.rvHistory;
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        adapter = new Adapter(history, weather -> {});
        HorizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(adapter);


        if (!weatherPreference.getWeather().getName().equals(city)) {
            respone = weatherPreference.getWeather();
            city = weatherPreference.getWeather().getName();
            setData(respone);
        } else {
            fetchWeather();
        }
    }

    private void fetchWeather() {
        Call<WeatherRespone> call = api.getWeather(city, API_KEY, "metric");
        call.enqueue(new Callback<WeatherRespone>() {
            @Override
            public void onResponse(Call<WeatherRespone> call, Response<WeatherRespone> response) {
                respone = response.body();
                if (response.body() != null) {
                    setData(respone);
                    saveWeather(respone);
                } else {
                    showSnackBar(binding.getRoot(), "Kota tidak ditemukan!");
                }
            }

            @Override
            public void onFailure(Call<WeatherRespone> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startReminder() {
        Data data = new Data.Builder().putString(WeatherWorker.CITY, city).build();

        periodicWorkRequest = new PeriodicWorkRequest.Builder(WeatherWorker.class, 1 , TimeUnit.DAYS)
                .setInputData(data)
                .build();

        workManager.enqueue(periodicWorkRequest);
    }

    private void stopReminder() {
        workManager.cancelWorkById(periodicWorkRequest.getId());
    }

    private void setData(WeatherRespone respone) {
        binding.toolbarContent.weatherCity.setText(respone.getName());
        binding.toolbarContent.weatherDate.setText(formatUnix(respone.getDt()));
        binding.toolbarContent.weatherTitle.setText(respone.getMain().getTemp().toString() + " C");
        binding.toolbarContent.weatherSubtitle.setText("Feels like " + respone.getMain().getFeels_like());
        binding.toolbarContent.weatherDesc.setText("Low : " + respone.getMain().getTemp_min() + " | High : " + respone.getMain().getTemp_max());
        binding.toolbarContent.weatherImg.setImageDrawable(getIcon(respone));
        binding.content.sumPress.setText(respone.getMain().getPressure().toString());
        binding.content.sumHum.setText(respone.getMain().getHumidity().toString());
        binding.content.sumWind.setText(respone.getWind().getSpeed().toString());
        binding.content.sumCloud.setText(respone.getClouds().getAll().toString());
    }

    private void saveWeather(WeatherRespone weather) {
        weatherPreference.setWeather(weather);
    }

    private void checkNotif() {
        if (isReminderActive) {
            binding.toolbarContent.weatherNotif.setImageDrawable(getDrawable(R.drawable.ic_baseline_notifications_24));
        }
        else {
            binding.toolbarContent.weatherNotif.setImageDrawable(getDrawable(R.drawable.ic_baseline_notifications_off_24));
        }
    }

    private Drawable getIcon(WeatherRespone weather) {
        String icon = weather.getWeathers().get(0).getIcon();
        Drawable finalIcon;
        if (icon.equals("01d")) {
            finalIcon = getDrawable(R.drawable.ic_day);
        } else if (icon.equals("01n")) {
            finalIcon = getDrawable(R.drawable.ic_night);
        } else if (icon.equals("02d")) {
            finalIcon = getDrawable(R.drawable.ic_cloudy_day_3);
        } else if (icon.equals("02n")) {
            finalIcon = getDrawable(R.drawable.ic_cloudy_night_3);
        } else if (icon.equals("03n") || icon.equals("03d")) {
            finalIcon = getDrawable(R.drawable.ic_cloudy);
        }else if (icon.equals("04n") || icon.equals("04d")) {
            finalIcon = getDrawable(R.drawable.ic_cloudy);
        }else if (icon.equals("09n") || icon.equals("09d")) {
            finalIcon = getDrawable(R.drawable.ic_rainy_7);
        }else if (icon.equals("10n") || icon.equals("10d")) {
            finalIcon = getDrawable(R.drawable.ic_rainy_6);
        }else if (icon.equals("11n") || icon.equals("11d")) {
            finalIcon = getDrawable(R.drawable.ic_thunder);
        }else if (icon.equals("13n") || icon.equals("13d")) {
            finalIcon = getDrawable(R.drawable.ic_snowy_6);
        }else if (icon.equals("50n") || icon.equals("50d")) {
            finalIcon = getDrawable(R.drawable.ic_weather_sunset);
        } else {
            finalIcon = getDrawable(R.drawable.ic_baseline_refresh_24);
        }
        return finalIcon;
    }

    private String formatUnix(int unix) {
        Date date = new Date(unix*1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("EEE, d MMM yyyy");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return jdf.format(date);
    }

    private void showSnackBar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}