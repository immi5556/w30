package within30.com.dataobjects;

import java.io.Serializable;

/**
 * Created by SR Lakhsmi on 4/1/2016.
 */
public class CustomerDO implements Serializable {
    private int ratingId;
    private String _id;
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
    private float expectedTime;
    public String subdomain;
    public float destinationDistance;
    public String startHour;
    public String  endHour;
    public int defaultDuration = 0;
    public String  slotBookedDate = "";
    public float rating = 0;
    public boolean premium = false;
    private int ratingCount = 0;
    private String timeZone = "";
    private String nextSlotDate = "";

    public String getNextSlotDate() {
        return nextSlotDate;
    }

    public void setNextSlotDate(String nextSlotDate) {
        this.nextSlotDate = nextSlotDate;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSlotBookedDate() {
        return slotBookedDate;
    }

    public void setSlotBookedDate(String slotBookedDate) {
        this.slotBookedDate = slotBookedDate;
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }

    public String getNextSlotAt() {
        return nextSlotAt;
    }

    public void setNextSlotAt(String nextSlotAt) {
        this.nextSlotAt = nextSlotAt;
    }

    public String nextSlotAt = "";



    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String userId;


    public void setExpectedTime(float expectedTime) {
        this.expectedTime = expectedTime;
    }

    public void setDestinationDistance(float destinationDistance) {
        this.destinationDistance = destinationDistance;
    }

    public String getSlotBookedAt() {
        return slotBookedAt;
    }

    public void setSlotBookedAt(String slotBookedAt) {
        this.slotBookedAt = slotBookedAt;
    }

    private  String slotBookedAt;




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



    public String getFullName() {
        return fullName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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


    public float getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(int expectedTime) {
        this.expectedTime = expectedTime;
    }

    public float getDestinationDistance() {
        return destinationDistance;
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
                "ratingId=" + ratingId +
                ", _id='" + _id + '\'' +
                ", _clientid='" + _clientid + '\'' +
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
               /* ", slotsAvailable=" + slotsAvailable +*/
                ", expectedTime=" + expectedTime +
                ", subdomain='" + subdomain + '\'' +
                ", destinationDistance=" + destinationDistance +
                ", startHour='" + startHour + '\'' +
                ", endHour='" + endHour + '\'' +
                ", defaultDuration=" + defaultDuration +
                ", slotBookedDate='" + slotBookedDate + '\'' +
                ", rating=" + rating +
                ", nextSlotAt='" + nextSlotAt + '\'' +
                ", userId='" + userId + '\'' +
                ", slotBookedAt='" + slotBookedAt + '\'' +
                '}';
    }
}
