package com.sms.within30.session;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.google.gson.Gson;
import com.sms.within30.dataobjects.UserDO;
import com.sms.within30.utilities.StringUtils;

import java.util.HashMap;
import java.util.List;

public class SessionManager extends Application {
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
  //public static final String USERID = "user_id";
	

	
	public static final String USERID = "userid";
	public static final String USER_INFO = "user_info";
	//Shared Preferences File Name
	public static String file_Name = "W30_PREFS";
	//Applciation Context
	public static Context appContext;
	// Constructor

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		appContext = getApplicationContext();
	}
	
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

public SessionManager(){}


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

	/**
	 * Get stored session data
	 * */
	/*public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		// user name

		user.put(USERID, Integer.toString(pref.getInt(USERID, 0)));

		// return user
		return user;

	}*/

	/**
	 * Quick check for login
	 * **/

	public boolean isLoggedIn() {
		if (pref == null) {
			pref = appContext.getSharedPreferences(file_Name,Context.MODE_PRIVATE);
		}
		return pref.getBoolean(IS_LOGIN, false);
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





}
