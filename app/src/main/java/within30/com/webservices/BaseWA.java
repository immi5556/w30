package within30.com.webservices;

import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.util.Log;

import within30.com.R;
import within30.com.utilities.AppConstants;
import within30.com.utilities.NetworkUtility;
import within30.com.utilities.StringUtils;
import within30.com.webservices.parsers.BaseParser;

import org.apache.http.NameValuePair;
import org.json.JSONObject;


public class BaseWA implements HttpListener
{
	private WebAccessListener listener;
	private Context mContext;
	private String empNo;
	JSONObject  pairs;


	public BaseWA(Context mContext, WebAccessListener webAccessListener,String empNo)
	{
		this.mContext = mContext;
		this.listener = webAccessListener;
		this.empNo=empNo;
	}
	


	/**
	 * Method to uploadData.
	 * this method is not using any thread
	 * @param method
	 * @param parameters
	 * @return boolean
	 */
	public boolean uploadData(ServiceMethods method, Object parameters)
	{
		
		if(NetworkUtility.isNetworkConnectionAvailable(mContext))
		{
			UploadData uploadData = new UploadData(this, method, parameters.toString(),"");
			uploadData.start();
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Method to start downloading the data.
	 * @param method
	 *
	 * @return boolean
	 */
	public boolean startDataDownload(ServiceMethods method, JSONObject pairs)
	{

		if(NetworkUtility.isNetworkConnectionAvailable(mContext))
		{
			HTTPSimulator downloader = new HTTPSimulator(this, method,pairs);
			downloader.start();
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean startDataDownload(ServiceMethods method, int rawSource)
	{
		if(NetworkUtility.isNetworkConnectionAvailable(mContext))
		{
			HTTPSimulator downloader = new HTTPSimulator(this, method, rawSource,"");
			downloader.start();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	class HTTPSimulator extends Thread
	{
		HttpListener listener;
		ServiceMethods method;
		String parameters;
		String payload;
		int rawSource;
		JSONObject pairs;
		//UserProfileDO userDO = null;
		public HTTPSimulator(HttpListener listener, ServiceMethods method, JSONObject pairs)
		{
			this.listener = listener;
			this.method = method;
			this.pairs = pairs;
			//this.payload = key;
		}
		public HTTPSimulator(HttpListener listener, ServiceMethods method, String parameters,String key)
		{
			this.listener = listener;
			this.method = method;
			this.parameters = parameters;
			this.payload = key;
		}

		
		public HTTPSimulator(HttpListener listener, ServiceMethods method, int rawSource,String key)
		{
			this.listener = listener;
			this.method = method;
			this.rawSource = rawSource;
			this.payload = key;
		}

		@Override
		public void run()
		{
			Response response = new Response(method, true, mContext.getString(R.string.Unable_to_connect_server_please_try_again),0);
			InputStream isResponse = null;

			try
			{
				isResponse = new RestClient().sendRequest(method,parameters,pairs);
				/*if(parameters == null)

					isResponse = mContext.getResources().openRawResource(rawSource);
				else{
					if (payload.equals("IMAGE_URL")) {
						//isResponse = new RestClient().sendRequest(method, parameters, empNo, userDO);
					}else{
						System.out.println("----------------------------------------  -1");
						//isResponse = new RestClient().sendRequest(method,parameters,pairs);
						System.out.println("----------------------------------------6");
					}
*/

				//}


				if(isResponse != null)
				{
					String responseStr = StringUtils.convertStreamToString(isResponse);

					BaseParser handler = BaseParser.getParser(method, mContext);
					handler.parse(responseStr, method);
					if(handler != null)
					{
						response.messageCode=handler.getMessageCode();
						response.data = handler.getData();
						response.isError = false;
						response.jsonResponse = responseStr;
					}
				}
				else{
//					LogUtils.info(LogUtils.SERVICE_LOG_TAG, "InputStream null");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(isResponse != null)
					{
						isResponse.close();
						isResponse = null;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(response.messageCode != -1 )
			{
				listener.onResponseReceived(response);
			}
		}
	}
	class UploadData
	{
		HttpListener listener;
		ServiceMethods method;
		String parameters;
		String payload;
		int rawSource;
		public UploadData(HttpListener listener, ServiceMethods method, String parameters,String key)
		{
			this.listener = listener;
			this.method = method;
			this.parameters = parameters;
			this.payload = key;
		}
		public UploadData(HttpListener listener, ServiceMethods method, int rawSource,String key)
		{
			this.listener = listener;
			this.method = method;
			this.rawSource = rawSource;
			this.payload = key;
		}
		
		public void start()
		{
			Response response = new Response(method, true, mContext.getString(R.string.Unable_to_connect_server_please_try_again),0);
			InputStream isResponse = null;
			
			try 
			{
				if(parameters == null)
					isResponse = mContext.getResources().openRawResource(rawSource);
				else
					isResponse = new RestClient().sendRequest(method,parameters,pairs);
				
				if(isResponse != null)
				{
					String responseStr = StringUtils.convertStreamToString(isResponse);
					BaseParser handler = BaseParser.getParser(method,mContext);
					if(handler != null)
					{
//						SAXParserFactory spf	=	SAXParserFactory.newInstance();
//						SAXParser sp			=	spf.newSAXParser();
//						XMLReader xr			=	sp.getXMLReader();
//						xr.setContentHandler(handler);
						handler.parse(responseStr, method);
						response.messageCode=handler.getMessageCode();
						response.data = handler.getData();

					}
					
				}
				else{
//					LogUtils.info(LogUtils.SERVICE_LOG_TAG, "InputStream null");
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally
			{
		    	try
		    	{
			    	if(isResponse != null)
			    	{
			    		isResponse.close();
			    		isResponse = null;
			    	}
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
			}
			if(response.messageCode != -1 )
			{
				listener.onResponseReceived(response);
			}
		}
	}
	/**
	 * Method to perform operation after receiving the response
	 */
	@Override
	public void onResponseReceived(Response response) 
	{
		this.respondWithData(response);
	}
	
	/**
	 * Method to respond for the data
	 * @param data
	 */
	protected void respondWithData(Response data)
	{
	    listener.dataDownloaded(data);
	}
}
