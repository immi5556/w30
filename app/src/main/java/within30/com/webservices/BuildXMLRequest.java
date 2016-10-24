package within30.com.webservices;



import android.net.Uri;

import within30.com.dataobjects.BookSlotDO;
import within30.com.dataobjects.CustomerDO;
import within30.com.dataobjects.UserDO;
import within30.com.utilities.W30Constants;

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
			/*pairs.put("miles", "30");
			pairs.put("minutes", "30");*/
			pairs.put("userId",customerDO.getUserId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return pairs;
	}
	/**
	 * This method is used to get the requested params as namevalue pairs
	 * @param customerDO
	 * @return List<NameValuePair>
	 */
	public static  JSONObject customerInfoRequest(CustomerDO customerDO){
		JSONObject pairs = new JSONObject();
		try {
			pairs.put("latitude", Double.toString(customerDO.getLatitude()));
			pairs.put("longitude", Double.toString(customerDO.getLongitude()));
			pairs.put("serviceId", customerDO.getServiceId());
			pairs.put("miles", Integer.toString(customerDO.getMiles()));
			pairs.put("minutes", Integer.toString(customerDO.getMinutes()));
			pairs.put("subdomain",customerDO.getSubdomain());
			pairs.put("userId",customerDO.getUserId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return pairs;
	}
	/**
	 * This method is used to get the requested params as namevalue pairs	 *
	 * @param  int rating,String customerId,String email,String mobile
	 * @return List<NameValuePair>
	 */
	public static  JSONObject submitRatingRequest(int rating,String customerId,String email,String mobile,String userId ){
		JSONObject pairs = new JSONObject();
		try {
			pairs.put("rating", Integer.toString(rating));
			pairs.put("customerId", customerId);
			pairs.put("email", email);
			pairs.put("mobile", mobile);
			pairs.put("userId",userId);

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
			pairs.put("userId",bookSlotDO.getUserId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return pairs;
	}
	public static  JSONObject getCitiesRequest(double latitude,double longitude){
		JSONObject pairs = new JSONObject();
		try {

			pairs.put("latitude", latitude);
			pairs.put("longitude",longitude);


		}catch(Exception e){
			e.printStackTrace();
		}
		return pairs;
	}
	public static  JSONObject saveenduser(UserDO userDO){
		JSONObject pairs = new JSONObject();
		try {

					//"icon": ""
			pairs.put("firstName", userDO.getFirstName());
			pairs.put("lastName",userDO.getLastName());
			pairs.put("email", userDO.getEmail());
			pairs.put("mobile",userDO.getMobile());
			pairs.put("deviceToken",userDO.getDeviceToken());
			pairs.put("deviceType","android");
			pairs.put("notifications", W30Constants.NOTIFICATIONS_ON);

		}catch(Exception e){
			e.printStackTrace();
		}
		return pairs;
	}
	public static  JSONObject updateenduser(UserDO userDO){
		JSONObject pairs = new JSONObject();
		try {

			//"icon": ""
			pairs.put("firstName", userDO.getFirstName());
			pairs.put("lastName",userDO.getLastName());
			pairs.put("email", userDO.getEmail());
			pairs.put("mobile",userDO.getMobile());
			pairs.put("_id",userDO.get_id());
			pairs.put("notifications", ""+userDO.getNotications());
			pairs.put("status",userDO.getStatus());


		}catch(Exception e){
			e.printStackTrace();
		}
		return pairs;
	}
}
