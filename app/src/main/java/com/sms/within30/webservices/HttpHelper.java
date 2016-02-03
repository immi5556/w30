package com.sms.within30.webservices;

import java.io.InputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.StringEntity;
/*import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;*/
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.sms.within30.utilities.AppConstants;


//This class is used for getting connections and response.
public class HttpHelper 
{
	// Time out for the connection is set with settings CSCClientTimeout in milliseconds 
	private int TIMEOUT_CONNECT_MILLIS = (10*60*1000);
	private int TIMEOUT_READ_MILLIS = TIMEOUT_CONNECT_MILLIS - 5000;
	
	public DefaultHttpClient getHttpClient() 
	{
		// sets up parameters
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		params.setBooleanParameter("http.protocol.expect-continue", false);

		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECT_MILLIS);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT_READ_MILLIS);

		// registers schemes for both http and https
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		// Set verifier
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() 
		{
			@Override
			public boolean verify(String hostname, SSLSession session) 
			{
				return true;
			}
		});

		SocketFactory sslSocketFactory = new EasySSLSocketFactory();

		registry.register(new Scheme("https", sslSocketFactory, 443));

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);

		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(manager, params);
		
		return defaultHttpClient;
	}
	
	/**
	 * 
	 * @param strPostURL
	 * @param strParamToPost
	 * @param headers
	 * @return
	 */
	public InputStream sendPOSTRequest(String strPostURL, 
			String strParamToPost, 
			String userId,
			ServiceMethods methods) 
	{
		strPostURL += methods.getValue() + strParamToPost;
		strPostURL = strPostURL.replace(" ", "%20");
		if(AppConstants.DEBUG)
			Log.e("strGetURL", strPostURL);
		DefaultHttpClient defaultHttpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(strPostURL);
//		httpPost.addHeader("Content-Type", "text/xml");
//		httpPost.addHeader("SOAPAction",ServiceURLs.SOAPAction+methods.getValue());
		InputStream inputStream = null;
		
		try 
		{
			if(strParamToPost != null && strParamToPost.length() > 0)
				httpPost.setEntity(new StringEntity(strParamToPost)); 
		    
		    
			HttpResponse response = defaultHttpClient.execute(httpPost);
			
			
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return inputStream;
	}
	

	
	
	
	public InputStream sendRequestForSurvey(String strPostURL, String strParamToPost) 
	{
		strPostURL = strPostURL.replace(" ", "%20");
		DefaultHttpClient defaultHttpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(strPostURL);
		InputStream inputStream = null;
		
		try 
		{
			HttpResponse response = defaultHttpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();			
		}
		catch (Exception e)
		{
		}
		return inputStream;
	}
	
	/**
	 * 
	 * @param strGetURL
	 * @param headers
	 * @return 
	 */
	public InputStream sendGETRequest(String strGetURL) 
	{
		strGetURL = strGetURL.replace(" ", "%20");
		
		DefaultHttpClient defaultHttpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(strGetURL);
		if(AppConstants.DEBUG)
			Log.e("strGetURL", strGetURL);
//		addRequestHeaders(httpGet, headers);
		
		InputStream inputStream = null;
		
		try 
		{
			HttpResponse response = defaultHttpClient.execute(httpGet);
			
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return inputStream;
	}
	
	
	public InputStream sendGETRequest(String strGetURL, 
			String strParamToGet, 
			String userId,
			ServiceMethods methods) 
	{
		strGetURL += methods.getValue() + strParamToGet;
		strGetURL = strGetURL.replace(" ", "%20");
		if(AppConstants.DEBUG)
			Log.e("strGetURL", strGetURL);
		DefaultHttpClient defaultHttpClient = getHttpClient();
		HttpGet httpGet = new HttpGet(strGetURL);
		
		InputStream inputStream = null;
		
		try 
		{
			HttpResponse response = defaultHttpClient.execute(httpGet);
			
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return inputStream;
	}
	
	public InputStream sendPOSTRequestForSurvey(String strPostURL, String strParamToPost) 
	{
		strPostURL = strPostURL.replace(" ", "%20");
		DefaultHttpClient defaultHttpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(strPostURL);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

		InputStream inputStream = null;
		
		try 
		{
			if(strParamToPost != null)
				httpPost.setEntity(new StringEntity(strParamToPost)); 
			HttpResponse response = defaultHttpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			
			inputStream = entity.getContent();
			
		}
		catch (Exception e)
		{
		}
		return inputStream;
	}
}
