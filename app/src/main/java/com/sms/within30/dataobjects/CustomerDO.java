package com.sms.within30.dataobjects;

/**
 * Created by SR Lakhsmi on 4/1/2016.
 */
public class CustomerDO {
    private String _clientid;
    private double latitude;
    private double longitude;
    private String serviceId;
    private int miles;
    private int minutes;
    private String companyName;
    private String logoPath;
    private String logoUrl;
    private String fullName;
    private String businessType;
    private String mobile;
    private String companyEmail;
    private String companyCity;
    private GeoDO geo;
    Integer slotsAvailable = 0;
    private int expectedTime;
    public String subdomain;
    public int destinationDistance;



    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public GeoDO getGeo() {
        return geo;
    }

    public void setGeo(GeoDO geo) {
        this.geo = geo;
    }

    public Integer getSlotsAvailable() {
        return slotsAvailable;
    }

    public void setSlotsAvailable(Integer slotsAvailable) {
        this.slotsAvailable = slotsAvailable;
    }

    public int getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(int expectedTime) {
        this.expectedTime = expectedTime;
    }

    public int getDestinationDistance() {
        return destinationDistance;
    }

    public void setDestinationDistance(int destinationDistance) {
        this.destinationDistance = destinationDistance;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }

    public int getMinutes() {
        return minutes;
    }

    public String get_clientid() {
        return _clientid;
    }

    public void set_clientid(String _clientid) {
        this._clientid = _clientid;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;

    }

    @Override
    public String toString() {
        return "CustomerDO{" +
                "_clientid='" + _clientid + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", serviceId='" + serviceId + '\'' +
                ", miles=" + miles +
                ", minutes=" + minutes +
                ", companyName='" + companyName + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", fullName='" + fullName + '\'' +
                ", businessType='" + businessType + '\'' +
                ", mobile='" + mobile + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", companyCity='" + companyCity + '\'' +
                ", geo=" + geo +
                ", slotsAvailable=" + slotsAvailable +
                ", expectedTime=" + expectedTime +
                ", subDomain='" + subdomain + '\'' +
                '}';
    }
}
