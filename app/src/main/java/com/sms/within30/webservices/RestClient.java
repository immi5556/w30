package com.sms.within30.webservices;

import java.io.IOException;
import java.io.InputStream;



public class RestClient 
{
	/**
	 * Method to send the request to the server.
	 * @param method
	 * @param parameters
	 * @return InputStream
	 * @throws IOException
	 */
	public InputStream sendRequest(ServiceMethods method,String parameters, String userId) throws IOException
	{
		if("GET".equalsIgnoreCase(ServiceURLs.getServiceMethod(method)))
		{

			return new HttpHelper().sendGETRequest(ServiceURLs.MAIN_URL,parameters, userId,method);

		}

		return null;
		
	}
	

	
}
