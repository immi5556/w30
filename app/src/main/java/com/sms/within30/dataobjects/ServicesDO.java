package com.sms.within30.dataobjects;

/**
 * Created by SR Lakhsmi on 2/4/2016.
 */
public class ServicesDO {
    private String name;
    private String _id;
    private String descr;
    private String image;
    private String mobileDecription;
    private String mobileImage;
    private long createdat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobileDecription() {
        return mobileDecription;
    }

    public void setMobileDecription(String mobileDecription) {
        this.mobileDecription = mobileDecription;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public void setMobileImage(String mobileImage) {
        this.mobileImage = mobileImage;
    }

    public long getCreatedat() {
        return createdat;
    }

    public void setCreatedat(long createdat) {
        this.createdat = createdat;
    }

    @Override
    public String toString() {
        return "ServicesDO{" +
                "name='" + name + '\'' +
                ", _id='" + _id + '\'' +
                ", descr='" + descr + '\'' +
                ", image='" + image + '\'' +
                ", mobileDecription='" + mobileDecription + '\'' +
                ", mobileImage='" + mobileImage + '\'' +
                ", createdat=" + createdat +
                '}';
    }
}
