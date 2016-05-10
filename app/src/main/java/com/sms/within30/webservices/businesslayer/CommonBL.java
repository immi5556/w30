package com.sms.within30.webservices.businesslayer;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.sms.within30.dataobjects.BookSlotDO;
import com.sms.within30.dataobjects.CustomerDO;
import com.sms.within30.dataobjects.UserDO;
import com.sms.within30.webservices.BaseWA;
import com.sms.within30.webservices.BuildXMLRequest;
import com.sms.within30.webservices.ServiceMethods;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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
    
	/*public boolean getMapInfo(double mLatitude, double mLongitude,int radius,String browser_key,String category_type) {
		return new BaseWA(mContext, this, empNo).startDataDownload(
				ServiceMethods.WS_MAP_INFO,
				BuildXMLRequest.mapRequest(mLatitude,mLongitude,radius,browser_key,category_type));
	}
*/
	/**
	 * getService call
	 * @return boolean
	 */
	JSONObject pairs = new JSONObject();
	public boolean getServices() {

		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_SERVICES,pairs);
	}

	/**
	 * getCustomers call
	 * @return boolean
	 */
	public boolean getCustomers(CustomerDO customerDO) {
		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_CUSTOMERS,BuildXMLRequest.customerRequest(customerDO));
	}
	/**
	 * getCustomers call
	 * @return boolean
	 */
	public boolean submitRating(int rating,String customerId,String email,String mobile) {
		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_SUBMITRATING,BuildXMLRequest.submitRatingRequest(rating,customerId,email,mobile));
	}
	/**
	 * getCustomers call
	 * @return boolean
	 */
	public boolean bookSlot(BookSlotDO bookSlotDO) {
		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_BOOKSLOT,BuildXMLRequest.bookSlotRequest(bookSlotDO));
	}
	/**
	 * getCustomers call
	 * @return boolean
	 */
	public boolean getCities(double latitude,double longitude) {
		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_GETCITIES,BuildXMLRequest.getCitiesRequest(latitude, longitude));
	}

	/**
	 * getEndUser call
	 * @return boolean
	 */
	public boolean getEndUser() {
		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_GETENDUSER,pairs);
	}
	/**
	 * saveenduser call
	 * @return boolean
	 */
	public boolean saveenduser(UserDO userDO) {
		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_SAVEENDYUSER,BuildXMLRequest.saveenduser(userDO));
	}
	/**
	 * updateenduser call
	 * @return boolean
	 */
	public boolean updateenduser(UserDO userDO) {
		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_UPDATEENDUSER,BuildXMLRequest.updateenduser(userDO));
	}
}