package com.sms.within30.webservices;

public class ServiceURLs 
{
	//=============Service URL's=========================================================================
	//Main URL

//	public static String MAIN_URL 	 	 		= 	"http://services.que.one";

	public static String MAIN_URL 	 	 		= 	"http://49.206.64.209:9098";

	//Soap method names
	public static String Mapinfo 				= "nearbysearch/json?";
	public static String Services 				= "/api/services";

	

	public static String getServiceMethod(ServiceMethods serviceMethods)
	{
		String method = "GET";
		switch (serviceMethods) {
			case WS_MAP_INFO:
			case WS_SERVICES:

			method = "GET";
				break;



		default:
			break;
		}
		
		return method;
	}
	
}
