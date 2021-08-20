package com.arya.weather.entity;

public class Wind {

    private Double speed;
    private Integer deg;
    private Double gust;

    public Double getSpeed() {
        return speed;
    }
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getDeg() {
        return deg;
    }
    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    public Double getGust() {
        return gust;
    }
    public void setGust(Double gust) {
        this.gust = gust;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", deg=" + deg +
                ", gust=" + gust +
                '}';
    }
}
