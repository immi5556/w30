package com.sms.within30.webservices;



import android.net.Uri;


public class BuildXMLRequest {
/*	private final static String SOAP_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
			+ "<soap:Body>";

	private final static String SOAP_FOOTER = "</soap:Body>"
			+ "</soap:Envelope>";*/

	private final static String AMPERSAND = "&";
	private final static String QUESTION = "?";
	private final static String EQUALS = "=";

	public static String mapRequest(double mLatitude, double mLongitude,int radius,String browser_key,String category_type) {
		StringBuilder sb = new StringBuilder();
		sb.append("location="+mLatitude+","+mLongitude);
		sb.append("&radius="+radius);
		sb.append("&types="+category_type);
		sb.append("&sensor=true");
		sb.append("&key=");
		sb.append(browser_key);
		return sb.toString();
	}
	

}
