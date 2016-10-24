package within30.com.webservices.businesslayer;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import within30.com.dataobjects.BookSlotDO;
import within30.com.dataobjects.CustomerDO;
import within30.com.dataobjects.UserDO;
import within30.com.webservices.BaseWA;
import within30.com.webservices.BuildXMLRequest;
import within30.com.webservices.ServiceMethods;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CommonBL extends BaseBL {

	private String empNo;

	public CommonBL(Context mContext, DataListener listener) {
		super(mContext, listener);

	}


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
	 * getCustomerInfo call
	 * @return boolean
	 */
	public boolean getCustomerInfo(CustomerDO customerDO) {
		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_GETCUSTOMERINFO,BuildXMLRequest.customerInfoRequest(customerDO));
	}
	/**
	 * getCustomers call
	 * @return boolean
	 */
	public boolean submitRating(int rating,String customerId,String email,String mobile,String userId) {

		return new BaseWA(mContext, this, empNo).startDataDownload(ServiceMethods.WS_SUBMITRATING,BuildXMLRequest.submitRatingRequest(rating,customerId,email,mobile,userId));
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