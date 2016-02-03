package com.sms.within30.webservices;

public class ServiceURLs 
{
	//=============Service URL's=========================================================================
	//Main URL

	public static String MAIN_URL 	 	 		= 	"https://maps.googleapis.com/maps/api/place/";

	public static final String SOAPAction 		= 	"http://tempuri.org/";

//	 public static String MAIN_URL       		=  "http://103.231.43.83:90/SaloonUser.svc/";
//	 public static String IMAGE_URL       		=  "http://salonintown.com";
//	 public static final String SOAPAction   	=  "http://tempuri.org/";

	//Soap method names
	public static String Mapinfo 				= "nearbysearch/json?";

	
//	http://103.231.43.83:90/SaloonUser.svc/SocialReg?SocialId=1232321&EmailID=dasdsa@asd.com&FirstName=null&Gender=0	
	public static String getServiceMethod(ServiceMethods serviceMethods)
	{
		String method = "GET";
		switch (serviceMethods) {
			case WS_MAP_INFO:

			method = "GET";
				break;



		default:
			break;
		}
		
		return method;
	}
	
}
