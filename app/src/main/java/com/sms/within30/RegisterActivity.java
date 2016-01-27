package com.sms.within30;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.PreferenceUtils;


public class RegisterActivity extends BaseActivity implements View.OnClickListener{

	private final int SPLASH_TIME = 2000;
	private PreferenceUtils preference;
    RelativeLayout homeLayout;
    Button btuser;
    Button btregister;
    TextView tvpagenumber;
    LinearLayout ll_register_business1;
    LinearLayout ll_register_business2;

    public void initialize(){
        homeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_register, null);
        // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().hide();

        intilizeControls();

    }
    private  void intilizeControls() {
        btuser = (Button) homeLayout.findViewById(R.id.btuser);
        btuser.setOnClickListener(this);

        btregister = (Button)homeLayout.findViewById(R.id.btregister);
        btregister.setOnClickListener(this);

        tvpagenumber = (TextView) homeLayout.findViewById(R.id.tvpagenumber);
        ll_register_business1 = (LinearLayout)homeLayout.findViewById(R.id.ll_register_business1);
        ll_register_business2  =(LinearLayout)homeLayout.findViewById(R.id.ll_register_business2);
    }

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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btuser) {
            btregister.setBackgroundColor(getResources().getColor(R.color.light_blue));
            btuser.setBackgroundColor(getResources().getColor(R.color.orange));
            ll_register_business1.setVisibility(View.VISIBLE);
            ll_register_business2.setVisibility(View.GONE);

            tvpagenumber.setVisibility(View.GONE);

        }else if (view.getId() == R.id.btregister) {
            btregister.setBackgroundColor(getResources().getColor(R.color.orange));
            btuser.setBackgroundColor(getResources().getColor(R.color.light_blue));

            ll_register_business1.setVisibility(View.VISIBLE);
            ll_register_business2.setVisibility(View.GONE);
            tvpagenumber.setVisibility(View.VISIBLE);
            tvpagenumber.setText(getResources().getString(R.string.onebyfour));



        }

    }
}
