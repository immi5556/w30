package com.sms.within30.webservices;

import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.sms.within30.R;
import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.NetworkUtility;
import com.sms.within30.utilities.StringUtils;
import com.sms.within30.webservices.parsers.BaseParser;


public class BaseWA implements HttpListener
{
	private WebAccessListener listener;
	private Context mContext;
	private String empNo;
	
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
	 * @param parameters
	 * @return boolean
	 */
	public boolean startDataDownload(ServiceMethods method, Object parameters)
	{

		if(NetworkUtility.isNetworkConnectionAvailable(mContext))
		{
			HTTPSimulator downloader = new HTTPSimulator(this, method, parameters.toString(),"");
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
		//UserProfileDO userDO = null;
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

				if(parameters == null)
					isResponse = mContext.getResources().openRawResource(rawSource);
				else{
					if (payload.equals("IMAGE_URL")) {
						//isResponse = new RestClient().sendRequest(method, parameters, empNo, userDO);
					}else{
						isResponse = new RestClient().sendRequest(method,parameters,empNo);
					}


				}


				if(isResponse != null)
				{
					String responseStr = StringUtils.convertStreamToString(isResponse);
					if(AppConstants.DEBUG)
						System.out.println("----------------- responseStr->"+responseStr);
					Log.e("response", ""+responseStr);
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
					isResponse = new RestClient().sendRequest(method,parameters,empNo);   
				
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
