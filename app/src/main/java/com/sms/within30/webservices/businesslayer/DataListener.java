package com.sms.within30.webservices.businesslayer;


import com.sms.within30.webservices.Response;

/** interface to recieve the Retreived data  */
public interface DataListener 
{
	/**
	 * Method to respond when data got received from web-service.
	 * @param data
	 */
	public void dataRetreived(Response data);
	
	
}
