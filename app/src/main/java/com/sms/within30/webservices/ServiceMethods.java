package com.sms.within30.webservices;

public enum ServiceMethods
{
	WS_MAP_INFO(ServiceURLs.Mapinfo);

	
	private final String value;
    private ServiceMethods(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}