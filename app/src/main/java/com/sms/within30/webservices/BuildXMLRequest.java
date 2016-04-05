package com.sms.within30.webservices;



import android.net.Uri;

import com.sms.within30.dataobjects.BookSlotDO;
import com.sms.within30.dataobjects.CustomerDO;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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

	/**
	 * This method is used to get the requested params as namevalue pairs
	 * @param customerDO
	 * @return List<NameValuePair>
	 */
	public static  JSONObject customerRequest(CustomerDO customerDO){
		JSONObject pairs = new JSONObject();
		try {
			pairs.put("latitude", Double.toString(customerDO.getLatitude()));
			pairs.put("longitude", Double.toString(customerDO.getLongitude()));
			pairs.put("serviceId", customerDO.getServiceId());
			pairs.put("miles", Integer.toString(customerDO.getMiles()));
			pairs.put("minutes", Integer.toString(customerDO.getMinutes()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return pairs;
	}

	/**
	 * This method is used to get the requested params as namevalue pairs
	 * @param bookSlotDO
	 * @return JSONObject
	 */
	public static  JSONObject bookSlotRequest(BookSlotDO bookSlotDO){
		JSONObject pairs = new JSONObject();
		try {
			//{"subDomain":"desaloon","date":"2016-4-4 21:10","email":"","mobile":""}
			pairs.put("subDomain", bookSlotDO.getSubDomain());
			pairs.put("date", bookSlotDO.getDate());
			pairs.put("email", bookSlotDO.getEmail());
			pairs.put("mobile", bookSlotDO.getMobile());

		}catch(Exception e){
			e.printStackTrace();
		}
		return pairs;
	}
}
