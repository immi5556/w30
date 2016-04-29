package com.sms.within30.webservices;

public enum ServiceMethods
{
	WS_MAP_INFO(ServiceURLs.Mapinfo),
    WS_SERVICES(ServiceURLs.GETMYSERVICES),
    WS_CUSTOMERS(ServiceURLs.GETMYCUSTOMERS),
    WS_BOOKSLOT(ServiceURLs.BOOKSLOT),
    WS_GETCITIES(ServiceURLs.GETCITIES),
    WS_GETENDUSER(ServiceURLs.GETeNDUSER),
    WS_SAVEENDYUSER(ServiceURLs.SAVEENDUSER),
    WS_UPDATEENDUSER(ServiceURLs.UPDATEENDUSER);
	
	private final String value;
    private ServiceMethods(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
