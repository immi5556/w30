package within30.com.webservices;

public class ServiceConfig
{
	//=============Service URL's=========================================================================
	//Main URL
	public static String MAIN_URL 	 	 		= "https://services.schejule.com:9095/endpoint";//test
	//public static String MAIN_URL 	 	 	="https://services.within30.com/endpoint";//play store

	public static String USER_NAME 				= "win-HQGQ";
	public static String PASSWORD 				= "zxosxtR76Z80";

	//Soap method names
	public static String Mapinfo 				= "nearbysearch/json?";
	public static String GETMYSERVICES 			= "/api/getmyservices";
	public static String GETMYCUSTOMERS 		= "/api/getmycustomers";
	public static String GETCUSTOMERINFO 		= "/api/getcustomerinfo";
	public static String BOOKSLOT 				= "/api/bookslot";
	//public static String GETCITIES 				= "/api/getcities";
	public static String GETCITIES 				= "/api/getindiacities";
	public static String GETeNDUSER 			= "/api/getenduser";
	public static String SAVEENDUSER 			= "/api/saveenduser";
	public static String UPDATEENDUSER 			= "/api/updateenduser";
	public static String SUBMITRATING 			= "/api/submitrating";
	public static String PROJECT_NUMBER			= "841587965302";

	public static String getServiceMethod(ServiceMethods serviceMethods)
	{
		String method = "POST";
		switch (serviceMethods) {
			case WS_MAP_INFO:
			case WS_SERVICES:
			case WS_CUSTOMERS:
			case WS_BOOKSLOT:
			case WS_GETCITIES:
			case WS_GETENDUSER:
			case WS_SAVEENDYUSER:
			case WS_UPDATEENDUSER:
			case WS_SUBMITRATING:
			case WS_GETCUSTOMERINFO:
			method = "POST";
				break;
		default:
			break;
		}
		
		return method;
	}
	
}
