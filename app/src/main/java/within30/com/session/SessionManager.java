package within30.com.session;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import within30.com.dataobjects.LocationDO;
import within30.com.dataobjects.UserDO;
import within30.com.googleanalytics.AnalyticsTrackers;
import within30.com.utilities.StringUtils;

import java.util.HashMap;
import java.util.List;

public class SessionManager extends Application {

	public static final String TAG = SessionManager.class.getSimpleName();
	public static SessionManager mInstance;


	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "W30Pref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
    public static final String GCM_REG_ID = "gcm_reg_ifd";
	public static final String IS_LOCATION_CHANGED = "isLocationChanged";
	

	
	public static final String USERID = "userid";
	public static final String USER_INFO = "user_info";
	//Shared Preferences File Name
	public static String file_Name = "W30_PREFS";
	//Applciation Context
	public static Context appContext;
	// Constructor
	public static final String SELECTED_LOCATION = "selected_location";

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = getApplicationContext();
		mInstance = this;
		AnalyticsTrackers.initialize(this);
		AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
	}
	public static synchronized SessionManager getInstance() {
		return mInstance;
	}
	public synchronized Tracker getGoogleAnalyticsTracker() {
		AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
		return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
	}

	/***
	 * Tracking screen view
	 *
	 * @param screenName screen name to be displayed on GA dashboard
	 */
	public void trackScreenView(String screenName) {
		Tracker t = getGoogleAnalyticsTracker();

		// Set screen name.
		t.setScreenName(screenName);

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		GoogleAnalytics.getInstance(this).dispatchLocalHits();
	}

	/***
	 * Tracking exception
	 *
	 * @param e exception to be tracked
	 */
	public void trackException(Exception e) {
		if (e != null) {
			Tracker t = getGoogleAnalyticsTracker();

			t.send(new HitBuilders.ExceptionBuilder()
							.setDescription(
									new StandardExceptionParser(this, null)
											.getDescription(Thread.currentThread().getName(), e))
							.setFatal(false)
							.build()
			);
		}
	}

	/***
	 * Tracking event
	 *
	 * @param category event category
	 * @param action   action of the event
	 * @param label    label
	 */
	public void trackEvent(String category, String action, String label) {
		Tracker t = getGoogleAnalyticsTracker();

		// Build and send an Event.
		t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
	}


	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public SessionManager(){}

	public void saveGCMRegId(String regId) {
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		Editor editor = pref.edit();
		editor.putString(GCM_REG_ID, regId);
		editor.commit();
	}
	public String getGCMRegId()
	{
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		return pref.getString(GCM_REG_ID, "");
	}
	public void saveUserId(String _id) {
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		Editor editor = pref.edit();
		editor.putString(USERID, _id);
		editor.commit();
	}
	public String getUserid()
	{
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		return pref.getString(USERID, "");
	}
	



	/**
	 * Create login session
	 *
	 * */
	public void createLoginSession() {
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		Editor editor = pref.edit();
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		// commit changes
		editor.commit();
	}

	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public boolean checkLoginSession() {
		// Check login status
		if(this.isLoggedIn()) {
            return  true;

		}else{
            return false;
        }

	}
	public void setLocationChanged(boolean isLocationChaged) {
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		Editor editor = pref.edit();
		// Storing login value as TRUE
		editor.putBoolean(IS_LOCATION_CHANGED, isLocationChaged);
		// commit changes
		editor.commit();
	}


	/**
	 * Quick check for login
	 * **/

	public boolean isLoggedIn() {
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		return pref.getBoolean(IS_LOGIN, false);
	}

	public boolean isLocationChanged() {
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		return pref.getBoolean(IS_LOCATION_CHANGED, false);
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		Editor editor = pref.edit();
		editor.remove(IS_LOGIN);//clear();
		editor.remove(USERID);//clear();

		editor.commit();		
	}

    public void clearSessions() {
        this.logoutUser();;

    }

	public void saveUserInfo(UserDO userDO) {
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		Gson gson = new Gson();
		String str = gson.toJson(userDO);
		Editor editor = pref.edit();
		editor.putString(USER_INFO, str);
		editor.commit();

	}
	public UserDO getUserInfo()
	{
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		String str = pref.getString(USER_INFO, "");
		Gson gson = new Gson();
		UserDO userDO = gson.fromJson(str,UserDO.class);
		return userDO;
	}
	public void saveUserSelectedLocationInfo(LocationDO locationDO) {
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		Gson gson = new Gson();
		String str = gson.toJson(locationDO);
		Editor editor = pref.edit();
		editor.putString(SELECTED_LOCATION, str);
		editor.commit();

	}
	public LocationDO getUserSelectedLocation()
	{
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		String str = pref.getString(SELECTED_LOCATION, "");
		Gson gson = new Gson();
		LocationDO locationDO = gson.fromJson(str,LocationDO.class);
		return locationDO;
	}
}
