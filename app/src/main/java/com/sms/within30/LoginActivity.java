package com.sms.within30;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.widget.RelativeLayout;

import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.PreferenceUtils;


public class LoginActivity extends BaseActivity {

	private final int SPLASH_TIME = 2000;
	private PreferenceUtils preference;
    RelativeLayout homeLayout;

    public void initialize(){
        homeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_login, null);
        // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().hide();

      //  intilizeControls();

    }
    private  void intilizeControls() {}

    @Override
    public void loadData() {

    }
	private void initializeFonts()
	{
		AppConstants.SIT_HEADLINE = Typeface.createFromAsset(getApplicationContext().getAssets(), "Gotham-Bold.ttf");;
		AppConstants.SIT_SUB_HEADLINE = Typeface.createFromAsset(getApplicationContext().getAssets(), "Gotham-Medium.ttf");;
		AppConstants.SIT_TEXT = Typeface.createFromAsset(getApplicationContext().getAssets(), "Gotham-Book.ttf");;
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
