package within30.com.dataobjects;

import java.io.Serializable;

/**
 * Created by SR Lakhsmi on 4/1/2016.
 */
public class GeoDO implements Serializable{
    private String city;
    private String country;

    private double coordinates[];
    private AddressDO address;
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

    public AddressDO getAddress() {
        return address;
    }

    public void setAddress(AddressDO address) {
        this.address = address;
    }
}
