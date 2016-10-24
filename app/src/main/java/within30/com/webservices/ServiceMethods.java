package within30.com.webservices;

public enum ServiceMethods
{
	WS_MAP_INFO(ServiceConfig.Mapinfo),
    WS_SERVICES(ServiceConfig.GETMYSERVICES),
    WS_CUSTOMERS(ServiceConfig.GETMYCUSTOMERS),
    WS_GETCUSTOMERINFO(ServiceConfig.GETCUSTOMERINFO),
    WS_BOOKSLOT(ServiceConfig.BOOKSLOT),
    WS_GETCITIES(ServiceConfig.GETCITIES),
    WS_GETENDUSER(ServiceConfig.GETeNDUSER),
    WS_SAVEENDYUSER(ServiceConfig.SAVEENDUSER),
    WS_UPDATEENDUSER(ServiceConfig.UPDATEENDUSER),
    WS_SUBMITRATING(ServiceConfig.SUBMITRATING);
	
	private final String value;
    private ServiceMethods(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
