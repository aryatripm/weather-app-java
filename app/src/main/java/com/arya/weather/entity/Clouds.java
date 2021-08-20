package com.arya.weather.entity;

public class Clouds {

    private Integer all;

    public Integer getAll() {
        return all;
    }
    public void setAll(Integer all) {
        this.all = all;
    }

    @Override
    public String toString() {
        return "Clouds{" +
                "all=" + all +
                '}';
    }
}
