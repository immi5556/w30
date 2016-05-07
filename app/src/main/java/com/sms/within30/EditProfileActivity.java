package com.sms.within30;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sms.within30.dataobjects.BookSlotDO;
import com.sms.within30.dataobjects.CustomerDO;
import com.sms.within30.dataobjects.LocationDO;
import com.sms.within30.dataobjects.ServicesDO;
import com.sms.within30.dataobjects.UserDO;
import com.sms.within30.lib.CleanableEditText;
import com.sms.within30.lib.GPSTracker;
import com.sms.within30.session.SessionManager;
import com.sms.within30.sidemenu.fragment.ContentFragment;
import com.sms.within30.sidemenu.interfaces.Resourceble;
import com.sms.within30.sidemenu.interfaces.ScreenShotable;
import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.CalendarUtils;
import com.sms.within30.utilities.NetworkUtility;
import com.sms.within30.utilities.PreferenceUtils;
import com.sms.within30.utilities.Validation;
import com.sms.within30.utilities.W30Constants;
import com.sms.within30.utilities.W30Database;
import com.sms.within30.utilities.W30Utilities;
import com.sms.within30.webservices.Response;
import com.sms.within30.webservices.businesslayer.CommonBL;
import com.sms.within30.webservices.businesslayer.DataListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.graphics.Color.TRANSPARENT;

public class EditProfileActivity extends BaseActivity  implements  DataListener, View.OnClickListener ,CompoundButton.OnCheckedChangeListener{


   // LinearLayout llbooking;

    LinearLayout homeLayout;
    com.sms.within30.lib.CleanableEditText et_firstname;
    com.sms.within30.lib.CleanableEditText et_lastname;
    TextView et_email;
    com.sms.within30.lib.CleanableEditText et_mobileno;
    Button btsubmit;
    Switch switch_notifications;
    ImageView img_profile;
    private PreferenceUtils preference;
    SessionManager sessionManager  = null;

    ActionBar actionBar;
    String service_id = "";
    String actionbarTitle = "";
    List<ServicesDO> servicesList;
    List<LocationDO> locationList;
    LocationDO locationDO = null;
    W30Utilities utilities = null;
    public void initialize(){
        homeLayout = (LinearLayout) inflater.inflate(R.layout.activity_editprofile, null);
        // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);
        sessionManager = new SessionManager(EditProfileActivity.this);
        actionBar = getSupportActionBar();
        utilities = new W30Utilities();
        getIntentdata();
        intilizeControls();
        setUserInfo();
        setActionBar();
        setListeners();
    }

    private void setListeners() {

        /*
		 * sets the error message the email is not given
		 */
        utilities.setErrorOnEditText(et_firstname, this, "First Name");
        utilities.setErrorOnEditText(et_lastname, this, "Last Name");
        utilities.setErrorOnEditText(et_mobileno, this, "Mobile No");

    }

    public void setActionBar(){
        tvTitle.setVisibility(View.GONE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Edit Profile");
    }
    private void setUserInfo() {
        UserDO userDO = sessionManager.getUserInfo();
        try{
            if (userDO !=null){
                et_firstname.setText(userDO.getFirstName());
                et_lastname.setText(userDO.getLastName());
                et_email.setText(userDO.getEmail());
                et_mobileno.setText(userDO.getMobile());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {

    }
    private void getIntentdata() {
        if (getIntent() != null) {
            try{
                if (getIntent().hasExtra("actionbar_title")) {
                    actionbarTitle = getIntent().getStringExtra("actionbar_title");
                    actionBar.setTitle(actionbarTitle);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                if (getIntent().hasExtra("service_id")) {
                    service_id = getIntent().getStringExtra("service_id");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                if (getIntent().hasExtra("service_list")) {
                    // in second Activity we get intent and retrieve the string value (listSerializedToJson) back to list
                    String listSerializedToJson = getIntent().getExtras().getString("service_list");
                    // in this example we have array but you can easy convert it to list - new ArrayList<MyObject>(Arrays.asList(mMyObjectList));
                    servicesList = new ArrayList<ServicesDO>(Arrays.asList(new Gson().fromJson(listSerializedToJson, ServicesDO[].class)));
                    for (ServicesDO servicesDO : servicesList) {
                        Log.d("services:-", servicesDO.toString());
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                if (getIntent().hasExtra("locationList")) {
                    String locationsListSerializedToJson = getIntent().getExtras().getString("locationList");
                    locationList = new ArrayList<LocationDO>(Arrays.asList(new Gson().fromJson(locationsListSerializedToJson, LocationDO[].class)));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                if (getIntent().hasExtra("location")) {
                    LocationDO locationDO = (LocationDO) getIntent().getExtras().getSerializable("location");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private void setIntentData(){

        Intent mapsIntent = new Intent(EditProfileActivity.this, MapsActivity.class);
        String str = getSupportActionBar().getTitle().toString();
        mapsIntent.putExtra("actionbar_title", str);
        //   mapsIntent.putExtra("category_type","hospitals");
        mapsIntent.putExtra("service_id", service_id);
        // here we use GSON to serialize mMyObjectList and pass it throught intent to second Activity
        String listSerializedToJson = new Gson().toJson(servicesList);
        mapsIntent.putExtra("service_list", listSerializedToJson);

        mapsIntent.putExtra("location", locationDO);
        String locationListSerializedToJson = new Gson().toJson(locationList);
        mapsIntent.putExtra("locationList", locationListSerializedToJson);
        startActivity(mapsIntent);
    }

    private void intilizeControls() {

       // llbooking = (LinearLayout) homeLayout.findViewById(R.id.llbooking);
        et_firstname = (CleanableEditText) homeLayout.findViewById(R.id.et_firstname);
        et_lastname = (CleanableEditText) homeLayout.findViewById(R.id.et_lastname);
        et_email = (TextView) homeLayout.findViewById(R.id.et_email);
        et_mobileno = (CleanableEditText) homeLayout.findViewById(R.id.et_mobileno);
        btsubmit = (Button) homeLayout.findViewById(R.id.btupdateprofile);
        btsubmit.setOnClickListener(this);
        img_profile = (ImageView)homeLayout.findViewById(R.id.img_profile);
        switch_notifications = (Switch)homeLayout.findViewById(R.id.switch_notifications);
        switch_notifications.setOnCheckedChangeListener(this);



        if(switch_notifications.isChecked()){
            System.out.println("Switch is currently ON");
        }
        else {
            System.out.println("Switch is currently OFF");
        }
        et_mobileno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updateUserInfo();
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
    private void updateUserInfo(){
        HashMap<EditText,String> hm = new HashMap<EditText,String>();
        hm.put(et_firstname, W30Constants.ERROR_FIRSTNAME);
        hm.put(et_lastname, W30Constants.ERROR_LASTNAME);
        hm.put(et_mobileno, W30Constants.ERROR_Mobile);

        if (utilities.edittextValidation(hm,EditProfileActivity.this)) {
            if (!Validation.isPhoneNumber(et_mobileno, false)) {
                showToast("Invalid Phone Number");
                // Toast.makeText(AmpowerWelcome.this, AmpowerConstants.ERROR_INVALID_PHONE_NUMBER, Toast.LENGTH_LONG).show();

            } else if (!utilities.validCellPhone(et_mobileno.getText().toString())) {
                showToast("Invalid Phone Number");
            }else  if (et_mobileno.getText().toString().length()<13){
                showToast("Invalid Phone Number");
            }else {
                if (NetworkUtility.isNetworkConnectionAvailable(EditProfileActivity.this)) {
                    UserDO userDO = new UserDO();
                    userDO.setFirstName(et_firstname.getText().toString());
                    userDO.setLastName(et_lastname.getText().toString());
                    userDO.setEmail(et_email.getText().toString());
                    userDO.setMobile(et_mobileno.getText().toString());
                    userDO.set_id(sessionManager.getUserid());
                    if (pd == null) {
                        pd = new ProgressDialog(EditProfileActivity.this);
                        pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                        pd.setMessage("Loading...");
                        pd.show();
                    }
                    if (new CommonBL(EditProfileActivity.this, EditProfileActivity.this).updateenduser(userDO)) {
                        if (pd == null) {
                            pd = new ProgressDialog(EditProfileActivity.this);
                            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                            pd.setMessage("Loading...");
                            pd.show();
                        }
                    } else {
                        showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                    }
                } else {
                    showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                }
            }
        }
    }
    @Override
    public void dataRetreived(Response data) {
       // llLoader.setVisibility(View.GONE);
        if (pd != null)
            if (pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
        if(data != null && data.data != null) {
            hideLoader();
            switch (data.method) {
                case WS_UPDATEENDUSER:
                    if (data.data != null && data.data instanceof UserDO) {
                        UserDO userDO = (UserDO) data.data;
                        userDO.setFirstName(et_firstname.getText().toString());
                        userDO.setLastName(et_lastname.getText().toString());
                        userDO.setEmail(et_mobileno.getText().toString());
                        userDO.setMobile(et_mobileno.getText().toString());
                        showToast(userDO.getMessage());
                        SessionManager sessionManager = new SessionManager(EditProfileActivity.this);
                        sessionManager.saveUserInfo(userDO);
                       // setIntentData();
                        this.finish();
                    } else if (data.data != null && data.data instanceof String) {
                        String str = (String) data.data;
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
        if (v.getId() == R.id.btupdateprofile) {
            System.out.println("clicked on btupdateprofile");
            updateUserInfo();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_reset:
                et_firstname.setText("");
                et_lastname.setText("");
                et_mobileno.setText("");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_reset).setVisible(true);
        menu.findItem(R.id.menu_home).setVisible(false);
        menu.findItem(R.id.menu_edit).setVisible(false);
        menu.findItem(R.id.menu_search).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(false);
        return true;
    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
          //  switch_notifications.setText("Switch is currently ON");
        }else{
           // switch_notifications.setText("Switch is currently OFF");
        }
    }

}
