package com.sms.within30;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.PreferenceUtils;
import com.sms.within30.utilities.ShimmerFrameLayout;


public class SplashActivity extends Activity {

	private final int SPLASH_TIME = 2000;
	private PreferenceUtils preference;
    private ShimmerFrameLayout mShimmerViewContainer;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		//initializeFonts();
		preference = new PreferenceUtils(this);
        mShimmerViewContainer 	= (ShimmerFrameLayout)findViewById(R.id.shimmer_view_container);

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
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mShimmerViewContainer.stopShimmerAnimation();
            }
        }, AppConstants.SHIMMER_PAUSE_DELAY);

	}

	@Override
	protected void onResume() {
		super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				moveToNextScreen();
			}
		}, SPLASH_TIME);
	}


	public void moveToNextScreen() {
		if("".equalsIgnoreCase(preference.getStringFromPreference(PreferenceUtils.USER_NAME, "")))
		{
			// New User
			Intent splashActivty = new Intent(getApplicationContext(), LandingActivity.class);
			startActivity(splashActivty);
		}
		else
		{
			// Existing user
			/*Intent splashActivty = new Intent(getApplicationContext(), HomeScreenActivty.class);
			startActivity(splashActivty);*/
			/*Intent splashActivty = new Intent(getApplicationContext(), HomeScreenArcActivty.class);
			startActivity(splashActivty);*/
		/*	Intent splashActivty = new Intent(getApplicationContext(), HomeScreenSweetSheetActivty.class);
			startActivity(splashActivty);*/
			Intent splashActivty = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(splashActivty);
		}
		finish();

	}

}
