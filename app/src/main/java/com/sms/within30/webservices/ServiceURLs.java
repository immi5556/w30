package com.sms.within30.webservices;

public class ServiceURLs 
{
	//=============Service URL's=========================================================================
	//Main URL

//	public static String MAIN_URL 	 	 		= 	"http://services.que.one";

	//public static String MAIN_URL 	 	 		= 	"http://win-a37f:kMEKlE0Ujovo@49.206.64.209:9012/endpoint";
	public static String MAIN_URL 	 	 		= "http://49.206.64.209:9012/endpoint";
	public static String IMAGE_URL				= "http://win-a37f:kMEKlE0Ujovo@49.206.64.209:9012";

	//Soap method names
	public static String Mapinfo 				= "nearbysearch/json?";
	public static String GETMYSERVICES 			= "/api/getmyservices";
	public static String GETMYCUSTOMERS 		= "/api/getmycustomers";
	public static String BOOKSLOT 				= "/api/bookslot";
//	public static String GETCITIES 				= "/api/getcities";
	public static String GETCITIES 				= "/api/getindiacities";
	public static String GETeNDUSER 			= "/api/getenduser";
	public static String SAVEENDUSER 			= "/api/saveenduser";
	public static String UPDATEENDUSER 			= "/api/updateenduser";
	public static String SUBMITRATING 			= "/api/submitrating";



	//http://win-a37f:kMEKlE0Ujovo@49.206.64.209:9012/uploaded/si/all-sizes-for-android_82.png
	

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
			method = "POST";
				break;
		default:
			break;
		}
		
		return method;
	}
	
}
