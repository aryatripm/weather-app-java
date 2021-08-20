package com.arya.weather;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.arya.weather.db.AppExecutors;
import com.arya.weather.db.WeatherDao;
import com.arya.weather.db.WeatherDatabase;
import com.arya.weather.db.WeatherTable;
import com.arya.weather.entity.WeatherRespone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherWorker extends Worker {

    public WeatherWorker (@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static final String CITY = "city";
    private Result resultStatus;

    private WeatherDatabase db;
    private WeatherDao dao;

    @NonNull
    @Override
    public Result doWork() {
        String city = getInputData().getString(CITY);
        return fetchWeather(city);
    }

    private Result fetchWeather(String city) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface api = retrofit.create(ApiInterface.class);

        db = WeatherDatabase.getDatabase(this.getApplicationContext());
        dao = db.weatherDao();


        Call<WeatherRespone> call = api.getWeather(city, ScrollingActivity.API_KEY, "metric");
        call.enqueue(new Callback<WeatherRespone>() {
            @Override
            public void onResponse(Call<WeatherRespone> call, Response<WeatherRespone> response) {

                String code = response.body().getWeathers().get(0).getIcon();
                String date = new SimpleDateFormat("MMM d", Locale.getDefault()).format(new Date());
                WeatherTable result = new WeatherTable(code, date);

                AppExecutors.getInstance().diskIO().execute(() ->
                    dao.insert(result)
                );

                resultStatus = Result.success();
                showNotification(response.body().getName(), response.body().getMain().toString());
            }

            @Override
            public void onFailure(Call<WeatherRespone> call, Throwable t) {
                resultStatus = Result.failure();
            }
        });

        return resultStatus;
    }

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_01";
    private static final String CHANNEL_NAME = "weather_channel";

    private void showNotification(String title, String description) {

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_weather_sunset)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notification.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification.build());
        }
    }
}
