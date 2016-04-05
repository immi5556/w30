package com.sms.within30.webservices;

public class ServiceURLs 
{
	//=============Service URL's=========================================================================
	//Main URL

//	public static String MAIN_URL 	 	 		= 	"http://services.que.one";

	//public static String MAIN_URL 	 	 		= 	"http://win-a37f:kMEKlE0Ujovo@49.206.64.209:9012/endpoint";
	public static String MAIN_URL 	 	 		= 	"http://49.206.64.209:9012/endpoint";
	//Soap method names
	public static String Mapinfo 				= "nearbysearch/json?";
	public static String GETMYSERVICES 			= "/api/getmyservices";
	public static String GETMYCUSTOMERS 		= "/api/getmycustomers";
	public static String BOOKSLOT 				= "/api/bookslot";


	

	public static String getServiceMethod(ServiceMethods serviceMethods)
	{
		String method = "POST";
		switch (serviceMethods) {
			case WS_MAP_INFO:
			case WS_SERVICES:
			case WS_CUSTOMERS:
			case WS_BOOKSLOT:
			method = "POST";
				break;



		default:
			break;
		}
		
		return method;
	}
	
}
