package com.arya.weather;

import static androidx.core.content.ContextCompat.getDrawable;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arya.weather.databinding.HistoryItemBinding;
import com.arya.weather.db.WeatherTable;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final List<WeatherTable> weathers;
    private final UserItemListener listener;
    public Adapter(List<WeatherTable> weathers, UserItemListener listener) {
        this.weathers = weathers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        holder.setBinding(weathers.get(position));
        holder.itemView.setOnClickListener(view -> {listener.userClicked(weathers.get(position));});
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final HistoryItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HistoryItemBinding.bind(itemView);
        }

        public void setBinding(WeatherTable weather) {
            binding.imageItem.setImageDrawable(this.getIcon(weather.getCode_weather()));
            binding.titleItem.setText(weather.getDate());
        }

        private Drawable getIcon(String icon) {
            Drawable finalIcon;
            if (icon.equals("01d")) {
                finalIcon = getDrawable(this.itemView.getContext(), R.drawable.ic_day);
            } else if (icon.equals("01n")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_night);
            } else if (icon.equals("02d")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_cloudy_day_3);
            } else if (icon.equals("02n")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_cloudy_night_3);
            } else if (icon.equals("03n") || icon.equals("03d")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_cloudy);
            }else if (icon.equals("04n") || icon.equals("04d")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_cloudy);
            }else if (icon.equals("09n") || icon.equals("09d")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_rainy_7);
            }else if (icon.equals("10n") || icon.equals("10d")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_rainy_6);
            }else if (icon.equals("11n") || icon.equals("11d")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_thunder);
            }else if (icon.equals("13n") || icon.equals("13d")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_snowy_6);
            }else if (icon.equals("50n") || icon.equals("50d")) {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_weather_sunset);
            } else {
                finalIcon = getDrawable(this.itemView.getContext(),R.drawable.ic_baseline_refresh_24);
            }
            return finalIcon;
        }
    }

    public interface UserItemListener{
        void userClicked(WeatherTable weather);
    }
}
