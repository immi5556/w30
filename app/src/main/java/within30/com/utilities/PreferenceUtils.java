package within30.com.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

@SuppressLint("CommitPrefEdits")
public class PreferenceUtils {

	private SharedPreferences preferences;
	private SharedPreferences.Editor edit;
	public static String IS_INSTALLED = "isInstalled";
	public static String SYNC_STATUS = "SYNCSTATUS";
	public static String USER_ROLE = "user_role";
	public static String USER_ID = "user_id";
	public static String VEHICLE_CODE = "vehicle_code";
	public static String LAST_USER_ID = "last_user_id";
	public static String LAST_PASSWORD = "last_password";
	public static String REMEMBER_ME = "REMEMBER_ME";
	public static final String SQLITE_DATE = "SQLITE_DATE";
	public static final String ORG_CODE = "ORG_CODE";
	public static String DEVICE_DISPLAY_WIDTH = "DEVICE_DISPLAY_WIDTH";
	public static String DEVICE_DISPLAY_HEIGHT = "DEVICE_DISPLAY_HEIGHT";
	public static String LAST_JOURNEY_DATE = "LAST_JOURNEY_DATE";
	public static final String gcmId = "gcmId";
	
	public static String USER_NAME = "USERNAME";
	public static String FIRST_NAME = "FIRSTNAME";
	public static String LAST_NAME = "LASTNAME";
	public static String EMAIL_ID = "EMAIL_ID";
	public static String PASSWORD = "PASSWORD";
	public static String GENDER = "GENDER";
	public static String CITY = "CITY";
	public static String DOB = "DOB";
	public static String PHONE_NUMBER = "PHONE_NUMBER";
	
/** Profile Image Url as obtained by Social Media APIs*/
	public static String PROFILE_PIC_URL = "PROFILE_PIC_URL";

	
	
	public static final String BUILD_INSTALLATIONDATE = "BUILD_INSTALLATION_DATE";
	
	public static String INT_USER_ID 		= "int_user_id";
	public static String SALESMANCODE 		= "SALESMANCODE";

	public static String IS_APP_FIRSTTIME_LAUNCH 		= "IS_APP_FIRSTTIME_LAUNCH";
	

	public static String CUSTOMER_SITE_ID		 		=	"CUSTOMER_SITE_ID";
	public static final String DefaultLoad_Quantity 	= 	"DefaultLoad_Quantity";
	public static final String IS_EOT_DONE              = "IS_EOT_DONE";
	public static final String EMP_NO 			    	= 	"EMP_NO";
	

	public static final String SALON_MENU_COUNT		= 	"Salon_Menu_Count";
	public static final String SALON_PHOTO_COUNT	= 	"Salon_Photo_Count";
	public static final String SALON_MENU_IMG_BASE		= 	"Salon_Menu_Img_Base";
	public static final String SALON_PHOTO_IMG_BASE	= 	"Salon_Photo_Img_Base";
	
	
	public PreferenceUtils(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		edit = preferences.edit();
	}

	public void saveStringInPreference(String strKey, String strValue) {
		edit.putString(strKey, strValue);
	}

	public void saveIntInPreference(String strKey, int value) {
		edit.putInt(strKey, value);
	}

	public void saveBooleanInPreference(String strKey, boolean value) {
		edit.putBoolean(strKey, value);
	}

	public void saveLongInPreference(String strKey, Long value) {
		edit.putLong(strKey, value);
	}

	public void saveDoubleInPreference(String strKey, String value) {
		edit.putString(strKey, value);
	}

	public void removeFromPreference(String strKey) {
		edit.remove(strKey);
	}

	public void commitPreference() {
		edit.commit();
	}

	public String getStringFromPreference(String strKey, String defaultValue) {
		return preferences.getString(strKey, defaultValue);
	}

	public boolean getbooleanFromPreference(String strKey, boolean defaultValue) {
		return preferences.getBoolean(strKey, defaultValue);
	}

	public int getIntFromPreference(String strKey, int defaultValue) {
		return preferences.getInt(strKey, defaultValue);
	}

	public double getDoubleFromPreference(String strKey, double defaultValue) {
		return Double.parseDouble(preferences.getString(strKey, ""
				+ defaultValue));
	}

	public long getLongInPreference(String strKey) {
		return preferences.getLong(strKey, 0);
	}

	public void clearPreferences() {
		edit.clear();
		edit.commit();
	}
}
