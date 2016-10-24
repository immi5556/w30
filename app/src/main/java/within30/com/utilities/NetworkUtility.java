package within30.com.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Description Class : Checking Network Connections
 */
public class NetworkUtility 
{
	/**
	 * Method to check Network Connections 
	 * @param context
	 * @return boolean value
	 */
	public static boolean isNetworkConnectionAvailable(Context context)
	{
		boolean isNetworkConnectionAvailable = false;
		
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		
		if(activeNetworkInfo != null) 
		{
		    isNetworkConnectionAvailable = activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
		}
		return isNetworkConnectionAvailable;
	}
	
	public static boolean isWifiConnected(Context context)
	{
		boolean isNetworkConnectionAvailable = false;
		ConnectivityManager connManager = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
			isNetworkConnectionAvailable = true;
		}
		return isNetworkConnectionAvailable;
	}
	
	
	
}
