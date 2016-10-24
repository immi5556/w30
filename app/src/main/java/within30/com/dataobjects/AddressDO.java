package within30.com.dataobjects;

import java.io.Serializable;

/**
 * Created by SR Lakhsmi on 4/14/2016.
 */
public class AddressDO implements Serializable{

    public String premise = "";
    public String sublocality = "";
    public String city = "";
    public String state = "";

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



    @Override
    public String toString() {
        String str = "";
                if (premise.length()>0){
                    str+=premise;
                }
        if (sublocality.length()>0){
            str+=", "+sublocality;
        }
        if (city.length()>0){
            str+=", "+city;
        }
        if (state.length()>0){
            str+=", "+state;
        }

        return str;
    }
}
