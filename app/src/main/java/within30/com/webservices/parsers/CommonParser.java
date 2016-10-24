package within30.com.webservices.parsers;

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
import within30.com.R;
import within30.com.dataobjects.CustomerDO;
import within30.com.dataobjects.LocationDO;
import within30.com.dataobjects.ServicesDO;
import within30.com.dataobjects.UserDO;
import within30.com.googlemaps.PlaceJSONParser;
import within30.com.utilities.AppConstants;
import within30.com.webservices.ServiceMethods;


public class CommonParser extends BaseParser{
	
	Object responseObject;
	String responseMsg;
	int responseCode;
	protected CommonParser(Context context) {
		super(context);
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
			case WS_GETCITIES:
				parseGetCities(jsonString);
				break;
			case WS_GETENDUSER:
				parseGetEndUser(jsonString);
				break;
			case WS_SAVEENDYUSER:
				parseSaveEndUser(jsonString);
				break;
			case WS_UPDATEENDUSER:
				parseUpdateEndUser(jsonString);
				break;
			case WS_SUBMITRATING:
				praseSubmitRating(jsonString);
				break;
			case WS_GETCUSTOMERINFO:
				parseGetCustomerInfo(jsonString);
				break;
		default:
			break;
		}
	}

	private void parseGetCustomerInfo(String jsonString) {
		//CustomerDO
		if(jsonString!=null && jsonString.length()>0)
		{
			try {
				try{
					JSONObject jsonObject = new JSONObject(jsonString);
					String stattus = (String)jsonObject.get("Status");
					if (stattus.equals(AppConstants.Failed)) {
						if (jsonObject.get("Message").equals("No customers available")) {
							responseObject = jsonObject.get("Message");
						}else{
							responseObject = context.getResources().getString(R.string.server_error_please_try_again);
						}
					}else if (stattus.equals(AppConstants.OK)) {
						Type listType = new TypeToken<List<CustomerDO>>() {}.getType();
						List<CustomerDO> customerList = new Gson().fromJson(jsonObject.getJSONArray("Data").toString(), listType);
						responseObject = customerList;
					}
				}catch(Exception e) {
					responseObject = context.getResources().getString(R.string.server_error_please_try_again);
				}
			} catch (Exception e) {
				e.printStackTrace();
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
		}


	}

	private void praseSubmitRating(String jsonString) {
		if(jsonString!=null && jsonString.length()>0) {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				String stattus = (String) jsonObject.get("status");
				responseObject = stattus;

			} catch (Exception e) {
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
		}
	}

	private void parseUpdateEndUser(String jsonString) {
		if(jsonString!=null && jsonString.length()>0) {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				String stattus = (String)jsonObject.get("Status");
				if (stattus.equalsIgnoreCase(AppConstants.Failed)) {
					responseObject = context.getResources().getString(R.string.server_error_please_try_again);
				}else if (stattus.equalsIgnoreCase(AppConstants.OK)) {

					UserDO userDO  = new UserDO();
					userDO.setMessage(jsonObject.getString("Message"));
					responseObject = userDO;
				}
			} catch (Exception e) {
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
		}
	}

	private void parseSaveEndUser(String jsonString) {
		if(jsonString!=null && jsonString.length()>0) {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				String stattus = (String)jsonObject.get("Status");
				if (stattus.equalsIgnoreCase(AppConstants.Failed)) {
					responseObject = context.getResources().getString(R.string.server_error_please_try_again);
				}else if (stattus.equalsIgnoreCase(AppConstants.OK)) {

					UserDO userDO = new Gson().fromJson(jsonObject.toString(), UserDO.class);

					responseObject = userDO;
				}
			} catch (Exception e) {
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
		}
	}

	private void parseGetEndUser(String jsonString) {
		if(jsonString!=null && jsonString.length()>0) {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				String stattus = (String)jsonObject.get("Status");
				if (stattus.equalsIgnoreCase(AppConstants.Failed)) {
					responseObject = context.getResources().getString(R.string.server_error_please_try_again);
				}else if (stattus.equalsIgnoreCase(AppConstants.OK)) {

					UserDO userDO = new Gson().fromJson(jsonObject.getJSONObject("Data").toString(), UserDO.class);
					responseObject = userDO;
				}
			} catch (Exception e) {
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
		}
	}

	private void parseGetCities(String jsonString) {
		if(jsonString!=null && jsonString.length()>0) {
			try {
				try {
					//	JSONObject jObject = new JSONObject(jsonString);
					JSONArray  jsonArray = new JSONArray(jsonString);
					Type listType = new TypeToken<List<LocationDO>>() {
					}.getType();
					List<LocationDO> LocationsList = new Gson().fromJson(jsonArray.toString(), listType);
					responseObject = LocationsList;

					/** Getting the parsed data as a List construct */

				} catch (Exception e) {
					responseObject = context.getResources().getString(R.string.server_error_please_try_again);
				}


			} catch (Exception e) {
				e.printStackTrace();
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
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
						if (jsonObject.get("Message").equals("No customers available")) {
							//responseObject = jsonObject.get("Message");
							LocationDO locationDO = new Gson().fromJson(jsonObject.toString(),LocationDO.class);


							responseObject = locationDO;
						}else{
							responseObject = context.getResources().getString(R.string.server_error_please_try_again);
						}

					}else if (stattus.equals(AppConstants.OK)) {
						Type listType = new TypeToken<List<CustomerDO>>() {}.getType();
						List<CustomerDO> customerList = new Gson().fromJson(jsonObject.getJSONArray("Data").toString(), listType);
						responseObject = customerList;
					}

				}catch(Exception e) {
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
					JSONObject jsonObject = new JSONObject(jsonString);
					if (jsonObject.has("action")) {
						String stattus = (String)jsonObject.get("action");
						if (stattus.equals(AppConstants.INSERT)) {

							responseObject = (JSONObject)jsonObject.get("Data");
						}
					}else if (jsonObject.has("Status")) {
						String stattus = (String)jsonObject.get("Status");
						if (stattus.equals(AppConstants.Failed)) {
							responseObject = jsonObject.get("Message");
						}
					}
					/** Getting the parsed data as a List construct */

				} catch (Exception e) {

					responseObject = context.getResources().getString(R.string.server_error_please_try_again);
				}


			} catch (Exception e) {
				e.printStackTrace();
				responseObject = context.getResources().getString(R.string.server_error_please_try_again);
			}
		}
	}

}
