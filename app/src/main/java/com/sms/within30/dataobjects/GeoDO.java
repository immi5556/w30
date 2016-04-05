package com.sms.within30.dataobjects;

/**
 * Created by SR Lakhsmi on 4/1/2016.
 */
public class GeoDO {
    private String city;
    private String country;

    private double coordinates[];
    public double[] getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
