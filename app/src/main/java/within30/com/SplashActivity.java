package within30.com;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Window;

import within30.com.session.SessionManager;
import within30.com.utilities.AppConstants;
import within30.com.utilities.PreferenceUtils;
import within30.com.utilities.ShimmerFrameLayout;


public class SplashActivity extends Activity {

	private final int SPLASH_TIME = 2000;
	private PreferenceUtils preference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		preference = new PreferenceUtils(this);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				moveToNextScreen();
			}
		}, SPLASH_TIME);

	}



	@Override
	public void onPause() {
		super.onPause();


	}

	@Override
	protected void onResume() {
		super.onResume();

	}


	public void moveToNextScreen() {
		SessionManager sessionManager = new SessionManager(SplashActivity.this);
		if(sessionManager.checkLoginSession()){
			// New User
			Intent splashActivty = new Intent(getApplicationContext(), LandingActivity.class);
			splashActivty.putExtra("setLocation",true);
			startActivity(splashActivty);

		}else{
			// New User
			Intent splashActivty = new Intent(getApplicationContext(), RegistrationActivity.class);
			startActivity(splashActivty);
		}
		finish();

	}

}
