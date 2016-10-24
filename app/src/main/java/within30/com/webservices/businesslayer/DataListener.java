package within30.com.webservices.businesslayer;


import within30.com.webservices.Response;

/** interface to recieve the Retreived data  */
public interface DataListener 
{
	/**
	 * Method to respond when data got received from web-service.
	 * @param data
	 */
	public void dataRetreived(Response data);
	
	
}
