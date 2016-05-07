package com.sms.within30;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sms.within30.dataobjects.CustomerDO;
import com.sms.within30.dataobjects.UserDO;
import com.sms.within30.lib.CleanableEditText;
import com.sms.within30.session.SessionManager;
import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.NetworkUtility;
import com.sms.within30.utilities.PreferenceUtils;
import com.sms.within30.utilities.ShimmerFrameLayout;
import com.sms.within30.utilities.Validation;
import com.sms.within30.utilities.W30Constants;
import com.sms.within30.utilities.W30Utilities;
import com.sms.within30.webservices.Response;
import com.sms.within30.webservices.businesslayer.CommonBL;
import com.sms.within30.webservices.businesslayer.DataListener;

import java.util.HashMap;


public class RegistrationActivity extends BaseActivity implements View.OnClickListener,DataListener {

	private PreferenceUtils preference;

	com.sms.within30.lib.CleanableEditText et_firstname;
	com.sms.within30.lib.CleanableEditText et_lastname;
	com.sms.within30.lib.CleanableEditText et_email;
	com.sms.within30.lib.CleanableEditText et_mobileno;
	TextView btsubmit;
	LinearLayout homeLayout;
	ActionBar actionBar;
	W30Utilities utilities = null;
	private static final int READPHONE_REQUEST=3;

	@Override
	public void dataRetreived(Response data) {
		if (pd != null)
			if (pd.isShowing()) {
				pd.dismiss();
				pd = null;
			}
		if(data != null && data.data != null) {
			hideLoader();
			switch (data.method) {
				case WS_SAVEENDYUSER:
					if (data.data != null && data.data instanceof UserDO) {
						UserDO userDO = (UserDO)data.data;
						userDO.setFirstName(et_firstname.getText().toString());
						userDO.setLastName(et_lastname.getText().toString());
						userDO.setEmail(et_email.getText().toString());
						userDO.setMobile(et_mobileno.getText().toString());
						SessionManager sessionManager = new SessionManager(RegistrationActivity.this);
						sessionManager.saveUserId(userDO.get_id());
						sessionManager.saveUserInfo(userDO);
						sessionManager.createLoginSession();
						Intent splashActivty = new Intent(getApplicationContext(), LandingActivity.class);
						startActivity(splashActivty);
					}else if (data.data != null && data.data instanceof String ){
						String str = (String )data.data;
					showToast(str);
					}
					break;

				default:
					break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		try{
			if (v.getId() == R.id.btsubmit) {
				saveEndUser();

			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void setListeners() {

        /*
		 * sets the error message the email is not given
		 */
		utilities.setErrorOnEditText(et_firstname, this, "First Name");
		utilities.setErrorOnEditText(et_lastname, this, "Last Name");
		utilities.setErrorOnEditText(et_mobileno, this, "Mobile No");
		utilities.setErrorOnEditText(et_email, this, "E-mail");

	}
	private void saveEndUser() {
		HashMap<EditText,String> hm = new HashMap<EditText,String>();
		hm.put(et_firstname, W30Constants.ERROR_FIRSTNAME);
		hm.put(et_lastname, W30Constants.ERROR_LASTNAME);
		hm.put(et_mobileno, W30Constants.ERROR_Mobile);
		hm.put(et_email, W30Constants.ERROR_EMAIL);

		if (utilities.edittextValidation(hm, RegistrationActivity.this)) {
			String emailPattern = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+";

			if (!et_email.getText().toString().matches(emailPattern)) {

				showToast("Invalid E-Mail");

			}else if (!Validation.isPhoneNumber(et_mobileno, false)) {
				showToast("Invalid Phone Number");
				// Toast.makeText(AmpowerWelcome.this, AmpowerConstants.ERROR_INVALID_PHONE_NUMBER, Toast.LENGTH_LONG).show();

			} else if (!utilities.validCellPhone(et_mobileno.getText().toString())) {
				showToast("Invalid Phone Number");
			} else  if (et_mobileno.getText().toString().length()<13){
				showToast("Invalid Phone Number");
			}else {
				if (NetworkUtility.isNetworkConnectionAvailable(RegistrationActivity.this)) {
					UserDO userDO = new UserDO();
					userDO.setFirstName(et_firstname.getText().toString());
					userDO.setLastName(et_lastname.getText().toString());
					userDO.setEmail(et_email.getText().toString());
					userDO.setMobile(et_mobileno.getText().toString());
					if (pd == null) {
						pd =  new ProgressDialog(RegistrationActivity.this);
						pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
						pd.setMessage("Loading...");
						pd.show();
					}
					if(new CommonBL(RegistrationActivity.this, RegistrationActivity.this).saveenduser(userDO)){
						if (pd == null) {
							pd =  new ProgressDialog(RegistrationActivity.this);
							pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
							pd.setMessage("Loading...");
							pd.show();
						}
					}else{
						showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
					}
				}

			}
		}




	}

	public void moveToNextScreen() {
		SessionManager sessionManager = new SessionManager(RegistrationActivity.this);
		if(sessionManager.checkLoginSession()){
			// New User
			Intent splashActivty = new Intent(getApplicationContext(), LandingActivity.class);
			startActivity(splashActivty);
			finish();

		}
	}
	public void initialize(){
		//moveToNextScreen();
		homeLayout = (LinearLayout) inflater.inflate(R.layout.activity_registration, null);
		// llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
		llBody.addView(homeLayout);

		actionBar = getSupportActionBar();
		actionBar.hide();
		utilities = new W30Utilities();
		intilizeControls();
	//	preference = new PreferenceUtils(this);
		setListeners();
		checkReadPhoneStatePermissions();

	}
	private void setPhoneNumber() {
		TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		//TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		String phoneNumber = tm.getLine1Number();
		Log.d("phoneNumber",phoneNumber);
		if (phoneNumber != null && phoneNumber.length() > 0) {
			int digits = 10;
			int country_digits = phoneNumber.length() - digits;
			phoneNumber = phoneNumber.substring(country_digits);
			et_mobileno.setText(phoneNumber);
			//return  number;
		}
	}

    private void checkReadPhoneStatePermissions() {
		try {

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
				setPhoneNumber();
			else {
				if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READPHONE_REQUEST);
					Log.d("READ_PHONE_STATE", " permission requesting...");
				} else {
					Log.d("READ_PHONE_STATE", " permission suceess...");
					setPhoneNumber();
				}
			}
		} catch (Exception e) {
			Log.d("location exception", "exception occured ...........");
			e.printStackTrace();

		}

	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch(requestCode) {
			case READPHONE_REQUEST:
				if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
				{
					Log.d("in onRequestPermissionsResult", "ACCESS_COARSE_LOCATION success");
					try{
						setPhoneNumber();
					}catch(Exception e){
						e.printStackTrace();
					}

				}
				break;
		}
	}

	private void intilizeControls()
	{
		et_firstname = (CleanableEditText) findViewById(R.id.et_firstname);
		et_lastname = (CleanableEditText)findViewById(R.id.et_lastname);
		et_email = (CleanableEditText)findViewById(R.id.et_email);
		et_mobileno = (CleanableEditText)findViewById(R.id.et_mobileno);
		btsubmit = (TextView)findViewById(R.id.btsubmit);
		btsubmit.setOnClickListener(this);
		et_mobileno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					saveEndUser();
				}
				return false;
			}
		});

		et_mobileno.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
			//we need to know if the user is erasing or inputing some new character
			private boolean backspacingFlag = false;
			//we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
			private boolean editedFlag = false;
			//we need to mark the cursor position and restore it after the edition
			private int cursorComplement;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//we store the cursor local relative to the end of the string in the EditText before the edition
				cursorComplement = s.length()-et_mobileno.getSelectionStart();
				//we check if the user ir inputing or erasing a character
				if (count > after) {
					backspacingFlag = true;
				} else {
					backspacingFlag = false;
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// nothing to do here =D
			}

			@Override
			public void afterTextChanged(Editable s) {
				String string = s.toString();
				//what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
				String phone = string.replaceAll("[^\\d]", "");

				//if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
				//if the flag is false, this is a original user-typed entry. so we go on and do some magic
				if (!editedFlag) {

					//we start verifying the worst case, many characters mask need to be added
					//example: 999999999 <- 6+ digits already typed
					// masked: (999) 999-999
					if (phone.length() >= 6 && !backspacingFlag) {
						//we will edit. next call on this textWatcher will be ignored
						editedFlag = true;
						//here is the core. we substring the raw digits and add the mask as convenient
						String ans = "(" + phone.substring(0, 3) + ")" + phone.substring(3,6) + "-" + phone.substring(6);
						et_mobileno.setText(ans);
						//we deliver the cursor to its original position relative to the end of the string
						et_mobileno.setSelection(et_mobileno.getText().length()-cursorComplement);

						//we end at the most simple case, when just one character mask is needed
						//example: 99999 <- 3+ digits already typed
						// masked: (999) 99
					} else if (phone.length() >= 3 && !backspacingFlag) {
						editedFlag = true;
						String ans = "(" +phone.substring(0, 3) + ") " + phone.substring(3);
						et_mobileno.setText(ans);
						et_mobileno.setSelection(et_mobileno.getText().length()-cursorComplement);
					}
					// We just edited the field, ignoring this cicle of the watcher and getting ready for the next
				} else {
					editedFlag = false;
				}
			}
		});

	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}



}
