package com.lyp.magicweather.model.db;

import org.litepal.crud.DataSupport;

import java.util.Objects;

public class City extends DataSupport {

    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return getId() == city.getId() &&
                getCityCode() == city.getCityCode() &&
                getProvinceId() == city.getProvinceId() &&
                Objects.equals(getCityName(), city.getCityName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getCityName(), getCityCode(), getProvinceId());
    }
}
