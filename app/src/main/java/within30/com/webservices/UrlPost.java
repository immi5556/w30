package within30.com.webservices;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * class for setting the connection for web services and for getting response as InputStream
 */
public class UrlPost 
{
	// Declaring variables
	private HttpURLConnection connection;
	private InputStream inputStream;
	// Initializing
	public static int TIMEOUT_CONNECT_MILLIS = 150000;
	public static int TIMEOUT_READ_MILLIS 	 = 150000;
	/**
	 * method to soap request
	 * 
	 * @param xmlString
	 * @param url
	 * @param soapUrl
	 * @return
	 * @throws Exception
	 */
	public InputStream soapPost(String xmlString, URL url, String soapUrl)	throws Exception
	{
		try 
		{
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
			connection.setReadTimeout(TIMEOUT_READ_MILLIS);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type","text/xml;charset=UTF-8");
			connection.setRequestProperty("SOAPAction", soapUrl);
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			writer.write(xmlString);
			writer.flush();
		    inputStream = connection.getInputStream();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return inputStream;
	}
	
	public InputStream makeRequest(URL url)	throws Exception
	{
		try 
		{
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type","text/xml;charset=UTF-8");
			inputStream = connection.getInputStream();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return inputStream;
	}
	
	public InputStream surveyPost(URL url)	throws Exception
	{
		try 
		{
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
			connection.setReadTimeout(TIMEOUT_READ_MILLIS);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type","text/xml;charset=UTF-8");
		    inputStream = connection.getInputStream();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return inputStream;
	}
	
}
