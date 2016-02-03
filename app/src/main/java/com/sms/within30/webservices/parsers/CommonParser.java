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

import com.sms.within30.R;
import com.sms.within30.googlemaps.PlaceJSONParser;
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
}
