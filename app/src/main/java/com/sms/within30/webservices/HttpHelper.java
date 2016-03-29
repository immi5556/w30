package com.sms.within30.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
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
	

	
	public InputStream sendGETRequest(String strGetURL, 
			String strParamToGet, 
			String userId,
			ServiceMethods methods) 
	{
		strGetURL += methods.getValue() + strParamToGet;
		strGetURL = strGetURL.replace(" ", "%20");
		if(AppConstants.DEBUG)
			Log.e("sendGETRequest strGetURL", strGetURL);

		InputStream inputStream = null;
		
		try 
		{

			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpGet httpGetRequest = new HttpGet(strGetURL.toString());
			  /* headers */

			httpGetRequest.setHeader("Accept", "application/json/text/plain");
			httpGetRequest.setHeader("Content-Type", "application/json/text/plain");
			httpGetRequest.setHeader(HTTP.CONTENT_TYPE,
					"application/x-www-form-urlencoded;charset=UTF-8");
			// Add your data
		//	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//httpPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

			HttpResponse response = (HttpResponse) httpClient.execute(httpGetRequest);

			StatusLine statusLine = response.getStatusLine();
			System.out.println("Status code->"+statusLine.getStatusCode());
			HttpEntity entity1 = response.getEntity();

			if (entity1 != null) {

				// Read the content stream
				inputStream = entity1.getContent();


			}
		} 
		catch (Exception e)
		{

			e.printStackTrace();
		}
		
		return inputStream;
	}



}
