package com.sms.within30.webservices.businesslayer;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.sms.within30.webservices.BaseWA;
import com.sms.within30.webservices.BuildXMLRequest;
import com.sms.within30.webservices.ServiceMethods;


public class CommonBL extends BaseBL {
	private String deviceId = "";
	private String GCMKey = "";
	private String ApiVersion = "";
	private String deviceType = "Android";
	private String empNo;

	public CommonBL(Context mContext, DataListener listener) {
		super(mContext, listener);
		this.empNo = empNo;
		/*deviceId = ((TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();*/
	}
    
	public boolean getMapInfo(double mLatitude, double mLongitude,int radius,String browser_key,String category_type) {
		return new BaseWA(mContext, this, empNo).startDataDownload(
				ServiceMethods.WS_MAP_INFO,
				BuildXMLRequest.mapRequest(mLatitude,mLongitude,radius,browser_key,category_type));
	}
	public boolean getServices() {
		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_SERVICES,"");
	}

}