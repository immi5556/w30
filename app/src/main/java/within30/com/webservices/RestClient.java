package within30.com.webservices;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class RestClient
{
	/**
	 * Method to send the request to the server.
	 * @param method
	 * @param parameters
	 * @return InputStream
	 * @throws IOException
	 */
	public InputStream sendRequest(ServiceMethods method,String parameters, JSONObject pairs) throws IOException
	{
		if("GET".equalsIgnoreCase(ServiceConfig.getServiceMethod(method)))
		{

			return new HttpHelper().sendGETRequest(ServiceConfig.MAIN_URL,"",pairs,method);

		}else if ("POST".equalsIgnoreCase(ServiceConfig.getServiceMethod(method))){
			return new HttpHelper().sendGETRequest(ServiceConfig.MAIN_URL,"",pairs,method);

		}

		return null;
		
	}
	

	
}
