package within30.com.webservices.businesslayer;

import android.app.Activity;
import android.content.Context;

import within30.com.webservices.Response;
import within30.com.webservices.WebAccessListener;


/** this class contains the control all over the Business Layer Classes **/
public class BaseBL implements WebAccessListener
{
	DataListener listener;
	public Context mContext;

	public BaseBL(Context mContext,DataListener listener)
	{
		this.mContext = mContext;
		this.listener = listener;
	}

	@Override
	public void dataDownloaded(Response data)
	{
		if(listener != null)
		{
			if(mContext instanceof Activity)
			{

					((Activity)mContext).runOnUiThread(new DataRetreivedRunnable(listener, data));

			}
			else
			{
				new Thread(new DataRetreivedRunnable(listener, data)).start();
			}
		}
	}

	/**
	 * Class to respond when data has received successfully.
	 */
	class DataRetreivedRunnable implements Runnable
	{
		DataListener listener;
		Response data;

		DataRetreivedRunnable(DataListener listener, Response data)
		{
			this.listener = listener;
			this.data = data;
		}
		
		@Override
		public void run() 
		{
			if(listener!=null)
				listener.dataRetreived(data);
		}
	}
}
