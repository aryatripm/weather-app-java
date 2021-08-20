package com.arya.weather.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class WeatherRespone implements Parcelable {

    private Coord coord;
    private ArrayList<Weather> weather = new ArrayList<>();
    private String base;
    private Main main;
    private Integer visibility;
    private Wind wind;
    private Clouds clouds;
    private Integer dt;
    private Sys sys;
    private Integer timezone;
    private Integer id;
    private String name;
    private Integer cod;

    public WeatherRespone() {}
    private WeatherRespone(Parcel in) {
        base = in.readString();
        if (in.readByte() == 0) {
            visibility = null;
        } else {
            visibility = in.readInt();
        }
        if (in.readByte() == 0) {
            dt = null;
        } else {
            dt = in.readInt();
        }
        if (in.readByte() == 0) {
            timezone = null;
        } else {
            timezone = in.readInt();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        if (in.readByte() == 0) {
            cod = null;
        } else {
            cod = in.readInt();
        }
    }

    public static final Creator<WeatherRespone> CREATOR = new Creator<WeatherRespone>() {
        @Override
        public WeatherRespone createFromParcel(Parcel in) {
            return new WeatherRespone(in);
        }

        @Override
        public WeatherRespone[] newArray(int size) {
            return new WeatherRespone[size];
        }
    };


    public Coord getCoord() {
        return coord;
    }
    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public ArrayList<Weather> getWeathers() {
        return weather;
    }
    public void setWeathers(ArrayList<Weather> weathers) {
        this.weather = weathers;
    }

    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }
    public void setMain(Main main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }
    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Integer getDt() {
        return dt;
    }
    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }
    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Integer getTimezone() {
        return timezone;
    }
    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }
    public void setCod(Integer cod) {
        this.cod = cod;
    }

    @Override
    public String toString() {
        return "WeatherRespone{" +
                "coord=" + coord +
                ", weather=" + weather +
                ", base='" + base + '\'' +
                ", main=" + main +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", dt=" + dt +
                ", sys=" + sys +
                ", timezone=" + timezone +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(base);
        if (visibility == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(visibility);
        }
        if (dt == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(dt);
        }
        if (timezone == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(timezone);
        }
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(name);
        if (cod == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(cod);
        }
    }
}
