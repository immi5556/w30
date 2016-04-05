package com.sms.within30.webservices.parsers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sms.within30.R;
import com.sms.within30.dataobjects.CustomerDO;
import com.sms.within30.dataobjects.ServicesDO;
import com.sms.within30.googlemaps.PlaceJSONParser;
import com.sms.within30.utilities.AppConstants;
import com.sms.within30.webservices.ServiceMethods;


public class CommonParser extends BaseParser{
	
	Object responseObject;
	String responseMsg;
	int responseCode;
//	PreferenceUtils preferenceUtils;
	protected CommonParser(Context context) {
		super(context);
		//preferenceUtils = new PreferenceUtils(context);
	}

	@Override
	public String getErrorMessage() {
		return responseMsg;
	}

	@Override
	public int getMessageCode() {
		return responseCode;
	}

	@Override
	public Object getData() {
		
		return responseObject;
	}

	@Override
	public void parse(String jsonString, ServiceMethods wsMethod) {
		
		switch (wsMethod) {
			case WS_MAP_INFO:
				parseMapInfo(jsonString);
			/*if(jsonString.contains("status")) parseStatus(jsonString);
			else parseCategories(jsonString);*/
			break;
			case WS_SERVICES:
				parseServies(jsonString);
				break;
			case WS_CUSTOMERS:
				parseCustomers(jsonString);
				break;
			case WS_BOOKSLOT:
				parseBookSlot(jsonString);
				break;
		default:
			break;
		}
	}

	private void parseMapInfo(String jsonString)
	{

		if(jsonString!=null && jsonString.length()>0)
		{
			try {
				try{
					JSONObject jObject = new JSONObject(jsonString);
					PlaceJSONParser placeJsonParser = new PlaceJSONParser();
					/** Getting the parsed data as a List construct */
					List<HashMap<String, String>> places = null;
					places = placeJsonParser.parse(jObject);
					responseObject = places;
				}catch(Exception e){
					Log.d("Exception", e.toString());
					responseObject = context.getString(R.string.server_error_please_try_again);
				}


			} catch (Exception e) {
				e.printStackTrace();
				responseObject = context.getString(R.string.server_error_please_try_again);
			}
		}
	}

	/**
	 * Here parsing the response jsonString(Services data)
	 * @param jsonString
	 */
	private void parseServies(String jsonString)
	{

		if(jsonString!=null && jsonString.length()>0)
		{
			try {
				try{
				//	JSONObject jObject = new JSONObject(jsonString);
					//JSONArray  jsonArray = new JSONArray(jsonString);
					JSONObject jsonObject = new JSONObject(jsonString);
					String stattus = (String)jsonObject.get("Status");
					if (stattus.equals(AppConstants.Failed)) {
						responseObject = context.getResources().getString(R.string.server_error_please_try_again);
					}else if (stattus.equals(AppConstants.OK)) {
						Type listType = new TypeToken<List<ServicesDO>>() {}.getType();
						List<ServicesDO> servicesList = new Gson().fromJson(jsonObject.getJSONArray("Data").toString(), listType);
						responseObject = servicesList;
					}
					/** Getting the parsed data as a List construct */

				}catch(Exception e){
					Log.d("Exception", e.toString());
					responseObject = context.getResources().getString(R.string.server_error_please_try_again);
				}


			} catch (Exception e) {
				e.printStackTrace();
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
		}
	}

	/**
	 * Here parsing response jsonString (Customer data)
	 * @param jsonString
	 */

	private void parseCustomers(String jsonString){
		//CustomerDO
		if(jsonString!=null && jsonString.length()>0)
		{
			try {
				try{
					//	JSONObject jObject = new JSONObject(jsonString);
					JSONObject jsonObject = new JSONObject(jsonString);
					String stattus = (String)jsonObject.get("Status");
					if (stattus.equals(AppConstants.Failed)) {
						if (jsonObject.get("Message").equals("NoCustomersAvailable")) {
							responseObject = jsonObject.get("Message");
						}else{
							responseObject = context.getResources().getString(R.string.server_error_please_try_again);
						}

					}else if (stattus.equals(AppConstants.OK)) {
						Type listType = new TypeToken<List<CustomerDO>>() {}.getType();
						List<CustomerDO> customerList = new Gson().fromJson(jsonObject.getJSONArray("Data").toString(), listType);
						responseObject = customerList;
					}
					/*if (jsonString.contains("NoCustomersAvailable")) {
						responseObject = jsonString;
					}else{
						JSONArray  jsonArray = new JSONArray(jsonString);
						*//** Getting the parsed data as a List construct *//*
						if (jsonArray.length()>0) {

						}else{
							responseObject = jsonString;
						}
					}*/


				}catch(Exception e) {
					Log.d("Exception", e.toString());
					responseObject = context.getResources().getString(R.string.server_error_please_try_again);
				}


			} catch (Exception e) {
				e.printStackTrace();
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
		}
	}
	/**
	 * Here parsing the response jsonString(Bookslot data)
	 * @param jsonString
	 */
	private void parseBookSlot(String jsonString)
	{

		if(jsonString!=null && jsonString.length()>0)
		{
			try {
				try{
					//	JSONObject jObject = new JSONObject(jsonString);
					//JSONArray  jsonArray = new JSONArray(jsonString);
					JSONObject jsonObject = new JSONObject(jsonString);
					String stattus = (String)jsonObject.get("Status");
					if (stattus.equals(AppConstants.Failed)) {
						responseObject = (String)jsonObject.get("Message");
					}else if (stattus.equals(AppConstants.OK)) {

						responseObject = stattus;
					}
					/** Getting the parsed data as a List construct */

				}catch(Exception e){
					Log.d("Exception", e.toString());
					responseObject = context.getResources().getString(R.string.server_error_please_try_again);
				}


			} catch (Exception e) {
				e.printStackTrace();
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
		}
	}

}
