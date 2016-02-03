package com.sms.within30.webservices.businesslayer;

import android.app.Activity;
import android.content.Context;

import com.sms.within30.webservices.Response;
import com.sms.within30.webservices.WebAccessListener;


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
//				if(data != null && (data.messageCode == -1 || (data.method == ServiceMethods.WS_UPDATE_AUTHKEY && data.messageCode == 0)))
//				{
//					if(mContext instanceof BaseActivity)
//					{
//						((BaseActivity)mContext).hideloader();
//						((BaseActivity)mContext).showAlertDialog(((Activity)mContext).getString(R.string.message), ((Activity)mContext).getString(R.string.access_token_expiry_msg), "Ok", null, "accessTokenExpired");
//					}
//				}
//				else
//				{
					((Activity)mContext).runOnUiThread(new DataRetreivedRunnable(listener, data));
//				}
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
