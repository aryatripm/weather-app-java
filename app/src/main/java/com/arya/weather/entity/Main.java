package com.arya.weather.entity;

public class Main {

    private Double temp;
    private Double feels_like;
    private Double temp_min;
    private Double temp_max;
    private Integer pressure;
    private Integer humidity;
    private Integer sea_level;
    private Integer grnd_level;

    public Double getTemp() {
        return temp;
    }
    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getFeels_like() {
        return feels_like;
    }
    public void setFeels_like(Double feels_like) {
        this.feels_like = feels_like;
    }

    public Double getTemp_min() {
        return temp_min;
    }
    public void setTemp_min(Double temp_min) {
        this.temp_min = temp_min;
    }

    public Double getTemp_max() {
        return temp_max;
    }
    public void setTemp_max(Double temp_max) {
        this.temp_max = temp_max;
    }

    public Integer getPressure() {
        return pressure;
    }
    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getSea_level() {
        return sea_level;
    }
    public void setSea_level(Integer sea_level) {
        this.sea_level = sea_level;
    }

    public Integer getGrnd_level() {
        return grnd_level;
    }
    public void setGrnd_level(Integer grnd_level) {
        this.grnd_level = grnd_level;
    }

    @Override
    public String toString() {
        return "Main{" +
                "temp=" + temp +
                ", feels_like=" + feels_like +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", sea_level=" + sea_level +
                ", grnd_level=" + grnd_level +
                '}';
    }
}
