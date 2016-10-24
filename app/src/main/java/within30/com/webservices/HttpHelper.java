package within30.com.webservices;

import android.util.Log;

import within30.com.lib.W30SSLSocketFactory;
import within30.com.utilities.AppConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/*import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;*/


//This class is used for getting connections and response.
public class HttpHelper 
{
	// Time out for the connection is set with settings CSCClientTimeout in milliseconds 
	private int TIMEOUT_CONNECT_MILLIS = (10*60*1000);
	private int TIMEOUT_READ_MILLIS = TIMEOUT_CONNECT_MILLIS - 5000;
	
//	public DefaultHttpClient getHttpClient()
//	{
//		// sets up parameters
//		HttpParams params = new BasicHttpParams();
//		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//		params.setBooleanParameter("http.protocol.expect-continue", false);
//
//		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECT_MILLIS);
//		HttpConnectionParams.setSoTimeout(params, TIMEOUT_READ_MILLIS);
//
//		// registers schemes for both http and https
//		SchemeRegistry registry = new SchemeRegistry();
//		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//
//		// Set verifier
//		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
//		{
//			@Override
//			public boolean verify(String hostname, SSLSession session)
//			{
//				return true;
//			}
//		});
//
//		SocketFactory sslSocketFactory = new EasySSLSocketFactory();
//
//		registry.register(new Scheme("https", sslSocketFactory, 443));
//
//		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
//
//		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(manager, params);
//
//		return defaultHttpClient;
//	}


	public InputStream sendGETRequest(String strGetURL,
									  String strParamToGet,
			JSONObject pairs,
			ServiceMethods methods)
	{
		strGetURL += methods.getValue();// + strParamToGet;
		strGetURL = strGetURL.replace(" ", "%20");
		if(AppConstants.DEBUG)
			Log.e("sendGETRequest strGetURL", strGetURL);
		Log.i("Request params",pairs.toString());
		InputStream inputStream = null;

		try
		{
			//HttpClient client = new DefaultHttpClient();
			HttpClient client = getHttpsClient(new DefaultHttpClient());
			HttpPost post = new HttpPost(strGetURL.toString());
			post.addHeader(BasicScheme.authenticate(
					new UsernamePasswordCredentials(ServiceConfig.USER_NAME, ServiceConfig.PASSWORD), "UTF-8", false));
			StringEntity se = new StringEntity( pairs.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(se);
			HttpResponse response = client.execute(post);

			StatusLine statusLine = response.getStatusLine();

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

	public static HttpClient getHttpsClient(HttpClient client) {

		try {

			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params,60000);
			HttpConnectionParams.setSoTimeout(params,60000);

			X509TrustManager x509TrustManager = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
			SSLSocketFactory sslSocketFactory = new W30SSLSocketFactory(sslContext);
			sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager clientConnectionManager = client.getConnectionManager();
			SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			return new DefaultHttpClient(clientConnectionManager, client.getParams());
		} catch (Exception ex) {
			return null;
		}
	}



}
