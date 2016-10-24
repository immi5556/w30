package within30.com.dataobjects;

import java.io.Serializable;

/**
 * Created by SR Lakhsmi on 4/21/2016.
 */
public class LocationDO implements Serializable{

    private String country = "";
    private String country_code = "";
    private String region_code = "";
    private String city="";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double distance = 0.0;
    private String Message = "";

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "LocationDO{" +
                "country='" + country + '\'' +
                ", country_code='" + country_code + '\'' +
                ", region_code='" + region_code + '\'' +
                ", city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", distance=" + distance +
                '}';
    }
}
