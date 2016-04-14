package com.sms.within30.dataobjects;

/**
 * Created by SR Lakhsmi on 4/14/2016.
 */
public class AddressDO {

    public String premise = "";
    public String sublocality = "";
    public String city = "";
    public String state = "";
    public String postalcode = "";
    public String  country =  "";

    public String getPremise() {
        return premise;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public String getSublocality() {
        return sublocality;
    }

    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        String str = "";
                if (premise.length()>0){
                    str+=premise;
                }
        if (sublocality.length()>0){
            str+=","+sublocality;
        }
        if (city.length()>0){
            str+=","+city;
        }
        if (state.length()>0){
            str+=","+state;
        }
        if (country.length()>0){
            str+=","+country;
        }
        if (postalcode.length()>0){
            str+=","+postalcode;
        }
return str;
    }
}
