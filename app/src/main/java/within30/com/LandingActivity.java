package within30.com;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import within30.com.adapters.LocationSearchAdapter;
import within30.com.adapters.ServicesSearchAdapter;
import within30.com.dataobjects.CustomerDO;
import within30.com.dataobjects.LocationDO;
import within30.com.dataobjects.ServicesDO;
import within30.com.dataobjects.UserDO;
import within30.com.location.GetAccurateLocationApplication;
import within30.com.location.LocationManagerInterface;
import within30.com.location.MyLocationManager;
import within30.com.session.SessionManager;
import within30.com.sidemenu.interfaces.Resourceble;
import within30.com.sidemenu.interfaces.ScreenShotable;
import within30.com.utilities.AppConstants;
import within30.com.utilities.CalendarUtils;
import within30.com.utilities.FontUtilities;
import within30.com.utilities.NetworkUtility;
import within30.com.utilities.W30Database;
import within30.com.utilities.W30Utilities;
import within30.com.webservices.Response;
import within30.com.webservices.businesslayer.CommonBL;
import within30.com.webservices.businesslayer.DataListener;
import within30.com.wheel.MaterialColor;
import within30.com.wheel.TextDrawable;
import within30.com.wheel.WheelArrayAdapter;
import within30.com.wheel.WheelView;

import static android.R.attr.permission;
import static android.R.attr.targetSdkVersion;
import static android.graphics.Color.TRANSPARENT;


public class LandingActivity extends BaseActivity implements View.OnClickListener,DataListener,View.OnTouchListener, LocationManagerInterface {

    RelativeLayout homeLayout;

    ImageView btmenu;
    AutoCompleteTextView autoCompleteTextView;
    WheelView wheelView;
    LinearLayout llmenu;
    TextView tvbusinessower;
    AutoCompleteTextView etChangeLocation;
    TextView tvlocation;

    Animation wheelOpen;
    Animation wheelClose;
    Animation rating_smiley;
    List<ServicesDO> servicesList;
    List<LocationDO> locationList;
    ServicesSearchAdapter servicesSearchAdapter;
    LocationSearchAdapter locationSearchAdapter;
    LocationDO locationDO = null;

    double mLatitude=0;
    double mLongitude=0;

    CommonBL commonBL = null;
    int ratingId = 0;
    W30Database w30Database;
    FontUtilities fontUtilities;

    //Location
    public MyLocationManager mLocationManager;
    private static final int REQUEST_FINE_LOCATION = 1;
    private Activity mCurrentActivity;
    public boolean isWebServiceRunning = false;
    private boolean isLocationReadRequest = false;
    public void initialize() {

        moveToNextScreen();
        homeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_landing, null);
        llBody.addView(homeLayout);
        mCurrentActivity = LandingActivity.this;
        commonBL = new CommonBL(LandingActivity.this,LandingActivity.this);
        w30Database = new W30Database(LandingActivity.this);
        fontUtilities = new FontUtilities(LandingActivity.this);
        getSupportActionBar().hide();
        intilizeControls();
        getUnRatedCustomer();
        callServices();
        setTypeface();
        if (selectedLocation == null) {

            isLocationReadRequest = true;
            initLocationFetching(LandingActivity.this);
        }else {
            tvlocation.setText(selectedLocation.getCity());
            tvlocation.setTag(selectedLocation);
        }
    }
    private void setTypeface() {

        etChangeLocation.setTypeface(fontUtilities.getDroidSerif());
        autoCompleteTextView.setTypeface(fontUtilities.getDroidSerif());
    }

    private void getUnRatedCustomer(){

        List<CustomerDO> unratedCustomerList = w30Database.getUnRatedCustomer();
        if (unratedCustomerList.size()>0) {
            for(CustomerDO customerDO:unratedCustomerList){
                showCustomDialog(LandingActivity.this,customerDO);
            }
        }
    }

    private void  showCustomDialog(Context context, final CustomerDO customerDO){
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_rating_dialog);
        // set the custom dialog components - text, image and button
//Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        final SeekBar seek_rating = (SeekBar)dialog.findViewById(R.id.seek_rating);
        final Button bt_submit = (Button)dialog.findViewById(R.id.bt_submit);
        TextView tvComapanyName = (TextView) dialog.findViewById(R.id.tvComapanyName);
        final TextView tv_floating_rating = (TextView) dialog.findViewById(R.id.tv_floating_rating);
        final Button bt_later = (Button)dialog.findViewById(R.id.bt_later);
        //Would you like to rate your experience at Matrix Dental Services on 05/10/2016
        Log.d("11111",customerDO.getSlotBookedDate());
        String temp = CalendarUtils.nextSlotDate1(customerDO.getSlotBookedDate());
        String tempStr  ="<font>Rate your experience at</font> <br/>"
                +" "+
                "<b><font color = #cb2027>"
                +""+customerDO.getCompanyName()+"</font></b><font size = 10>"
                +" "+"on"+" "+"</font>"
                +"<b><font color = #cb2027>"+temp
                +"</b></font>";
        tvComapanyName.setText(Html.fromHtml(tempStr));
        tvComapanyName.setTypeface(fontUtilities.getDroidSerif());
        bt_submit.setTypeface(fontUtilities.getDroidSerif_Bold());
        bt_later.setTypeface(fontUtilities.getDroidSerif_Bold());
        bt_submit.setTag(customerDO.get_id());

        bt_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtility.isNetworkConnectionAvailable(LandingActivity.this)) {
                    dialog.dismiss();
                    if (pd == null) {
                        pd =  new ProgressDialog(LandingActivity.this);
                        pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                        pd.setMessage("Loading...");
                        pd.show();
                    }
                    String customerId =(String) bt_submit.getTag();
                    UserDO userDO = new SessionManager(LandingActivity.this).getUserInfo();
                    String userId = new SessionManager(LandingActivity.this).getUserid();
                    ratingId = customerDO.getRatingId();

                    if(commonBL.submitRating(seek_rating.getProgress()+1,customerId,userDO.getEmail(),userDO.getMobile(),userId))
                    {
                        isWebServiceRunning = true;
                        if (pd == null) {
                            pd =  new ProgressDialog(LandingActivity.this);
                            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                            pd.setMessage("Loading...");
                            pd.show();
                        }

                    }else{
                        showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                        if (pd != null)
                            if (pd.isShowing()) {
                                pd.dismiss();
                                pd = null;
                            }
                    }
                }else{
                    showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                }
            }
        });
        seek_rating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int progress = seek_rating.getProgress();
                tv_floating_rating.startAnimation(rating_smiley);

                tv_floating_rating.setVisibility(View.VISIBLE);

                seek_rating.setAlpha(1);
                if (progress ==0 ){
                    tv_floating_rating.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_rating_smiley_1));
                }else if( progress ==1){
                    tv_floating_rating.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_rating_smiley_2));
                }else if ( progress ==2) {
                    tv_floating_rating.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_rating_smiley_3));
                }else if ( progress ==3){
                    tv_floating_rating.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_rating_smiley_4));
                }else if ( progress==4){
                    tv_floating_rating.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_rating_smiley_5));
                }
                int max= seek_rating.getMax();
                int offset = seek_rating.getThumbOffset();
                float percent = ((float)progress)/(float)max;
                int width = seek_rating.getWidth() - 2*offset;
                int answer =((int)(width*percent +offset - tv_floating_rating.getWidth()/3));
                tv_floating_rating.setX(answer);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tv_floating_rating
                        .getLayoutParams();
                if (progress == 0){
                    tv_floating_rating.setX(answer+12);
                }else if (progress == 1) {
                    tv_floating_rating.setX(answer + 5);
                }else if (progress == 3) {
                    tv_floating_rating.setX(answer-5);
                }else if (progress == 4) {
                    tv_floating_rating.setX(answer-12);
                }

             return false;
          }
        });


        dialog.show();
    }
    private void callServices(){
        try{
            if (NetworkUtility.isNetworkConnectionAvailable(LandingActivity.this)) {


                if(commonBL.getServices())
                {
                    isWebServiceRunning = true;
                    if (pd == null) {
                        pd =  new ProgressDialog(LandingActivity.this);
                        pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                        pd.setMessage("Loading...");
                        pd.show();
                    }
                }
            }else{
                showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void loadData() {

    }



    private  void intilizeControls() {
        tvbusinessower = (TextView) homeLayout.findViewById(R.id.tvbusinessower);
        btmenu = (ImageView) findViewById(R.id.btmenu);
        llmenu = (LinearLayout) homeLayout.findViewById(R.id.llmenu);
        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setActivity(this);
        wheelClose = AnimationUtils.loadAnimation(this, R.anim.wheel_close);
        wheelOpen = AnimationUtils.loadAnimation(this,R.anim.wheel_open);
        rating_smiley = AnimationUtils.loadAnimation(this,R.anim.rating_smiley);
        tvlocation = (TextView)homeLayout.findViewById(R.id.tvlocation);
        tvlocation.setOnTouchListener(this);
        servicesSearchAdapter  = new ServicesSearchAdapter(LandingActivity.this, R.layout.category_list_item, R.id.tv_category);
        locationSearchAdapter  = new LocationSearchAdapter(LandingActivity.this, R.layout.category_list_item, R.id.tv_category);

        autoCompleteTextView = (AutoCompleteTextView) homeLayout.findViewById(R.id.autoCompleted_categorysearch);
        autoCompleteTextView.setAdapter(servicesSearchAdapter);

        etChangeLocation = (AutoCompleteTextView) homeLayout.findViewById(R.id.etChangeLocation);
        etChangeLocation.setAdapter(locationSearchAdapter);
        etChangeLocation.setOnTouchListener(this);

        getIntentData();
        if (selectedLocation !=null) {
            tvlocation.setText(selectedLocation.getCity());
            tvlocation.setTag(selectedLocation);
        }


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

               final ServicesDO servicesDO = (ServicesDO) adapterView.getItemAtPosition(position);
                    autoCompleteTextView.setText(servicesDO.getName());
                    if (servicesDO.isActive()) {
                        if (locationDO != null) {
                            if(new SessionManager(LandingActivity.this).isLocationChanged()){
                                W30Database w30Database = new W30Database(LandingActivity.this);
                                w30Database.addLocationHistory(locationDO);
                            }

                            callMapsActivity(servicesDO, (LocationDO) tvlocation.getTag());

                        }else{
                            showToast("Please select location.");
                        }

                    }else {
                        W30Utilities.showCustomNoServiceDialog(LandingActivity.this, new W30Utilities().getServiceImage(
                                W30Utilities.SERVICE_MENU_ACTIVE, servicesDO.getName()), servicesDO.getName());
                    }

            }
        });

        etChangeLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                tvlocation.setVisibility(View.VISIBLE);
                etChangeLocation.setVisibility(View.GONE);
                LocationDO locationDO = (LocationDO) adapterView.getItemAtPosition(position);
                selectedLocation = locationDO;
                tvlocation.setText(locationDO.getCity());
                tvlocation.setTag(locationDO);

                new SessionManager(LandingActivity.this).setLocationChanged(true);
                new SessionManager(LandingActivity.this).saveUserSelectedLocationInfo(selectedLocation);
                InputMethodManager imm = (InputMethodManager) LandingActivity.this.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etChangeLocation.getWindowToken(), 0);

            }
        });
        tvbusinessower.setOnClickListener(this);
        //a listener for receiving a callback for when the item closest to the selection angle changes
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, Drawable itemDrawable, int position) {
                //get the item at this position
                Map.Entry<String, Integer> selectedEntry = ((MaterialColorAdapter) parent.getAdapter()).getItem(position);
                parent.setSelectionColor(getContrastColor(selectedEntry));
            }
        });

        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                final LocationDO locationDO = (LocationDO) tvlocation.getTag();
                final ServicesDO servicesDO = (ServicesDO) servicesList.get(position);
                if (servicesDO.isActive()) {
                    if (locationDO != null) {
                        if(new SessionManager(LandingActivity.this).isLocationChanged()){
                            W30Database w30Database = new W30Database(LandingActivity.this);
                            w30Database.addLocationHistory(locationDO);
                        }
                        callMapsActivity(servicesDO, (LocationDO) tvlocation.getTag());

                    }else {
                        showToast("Please select location.");
                    }
                } else {
                    W30Utilities.showCustomNoServiceDialog(LandingActivity.this, new W30Utilities().getServiceImage(
                            W30Utilities.SERVICE_MENU_ACTIVE, servicesDO.getName()), servicesDO.getName());
                }
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private  void callMapsActivity(ServicesDO servicesDO,LocationDO locationDO) {
        // Tracking Event
        SessionManager.getInstance().trackEvent("click",servicesDO.getName(), servicesDO.getName()+" Mapping Page");

        Intent mapsIntent = new Intent(LandingActivity.this, MapsActivity.class);
        mapsIntent.putExtra("actionbar_title", servicesDO.getName());
        mapsIntent.putExtra("service_id", servicesDO.get_id());
        // here we use GSON to serialize mMyObjectList and pass it throught intent to second Activity
        List<ServicesDO> servicesDOListTemp = new ArrayList<ServicesDO>();
        for (ServicesDO servicesDO1 : servicesList) {
            if (servicesDO1.get_id().equals(servicesDO.get_id())) {
                servicesDO1.setSelectedService(true);
            } else {
                servicesDO1.setSelectedService(false);
            }
            servicesDOListTemp.add(servicesDO1);
        }
        String listSerializedToJson = new Gson().toJson(servicesDOListTemp);
        mapsIntent.putExtra("service_list", listSerializedToJson);
        mapsIntent.putExtra("selectedLocation", locationDO);// user selected location
        new SessionManager(LandingActivity.this).saveUserSelectedLocationInfo(selectedLocation);
        startActivity(mapsIntent);
    }


    LocationDO selectedLocation;
    private void getIntentData() {
        // Getting place reference from the map
        if ( getIntent()!=null) {
            try {
                if (getIntent().hasExtra("selectedLocation")) { // selected location
                    selectedLocation = (LocationDO) getIntent().getExtras().getSerializable("selectedLocation");
                }else if(getIntent().hasExtra("setLocation")){
                   LocationDO locationDOTemp =  new SessionManager(LandingActivity.this).getUserSelectedLocation();
                   try{
                       if(locationDOTemp!=null){
                           selectedLocation = locationDOTemp;
                       }else {
                           selectedLocation = null;
                       }
                   }catch(Exception e){
                       e.printStackTrace();
                   }
                }else{
                    selectedLocation = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getContrastColor(Map.Entry<String, Integer> entry) {
        String colorName = MaterialColor.getColorName(entry);
        return MaterialColor.getContrastColor(colorName);
    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.llmenu) {
            if (wheelView.getVisibility() == View.VISIBLE) {
                wheelView.startAnimation(wheelClose);
                wheelView.setVisibility(View.INVISIBLE);
                btmenu.setImageResource(R.mipmap.menu_open);
            } else {
                wheelView.startAnimation(wheelOpen);
                wheelView.setVisibility(View.VISIBLE);
                btmenu.setImageResource(R.mipmap.menu_open);
            }
        } else if (v.getId() == R.id.tvbusinessower) {

        }
    }
    public void moveToNextScreen() {
        SessionManager sessionManager = new SessionManager(LandingActivity.this);
        if(!sessionManager.checkLoginSession()){
            // New User
            Intent splashActivty = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(splashActivty);
            finish();
        }
    }

    public void getLocationList() {
        if (locationList == null || locationList.size() == 0) {
            if (mLatitude != 0 && mLongitude != 0) {

                if ( commonBL.getCities(mLatitude,mLongitude)){
                    isWebServiceRunning = true;
                    if (pd == null) {
                        pd =  new ProgressDialog(LandingActivity.this);
                        pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                        pd.setMessage("Loading...");
                        pd.show();
                    }
                }else{
                    try{
                        if (pd != null)
                            if (pd.isShowing()) {
                                pd.dismiss();
                                pd = null;
                            }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else {
                isLocationReadRequest = true;
                mLocationManager = new MyLocationManager(getApplicationContext(), this, this, MyLocationManager.ALL_PROVIDERS, LocationRequest.PRIORITY_HIGH_ACCURACY, 10 * 1000, 1 * 1000, MyLocationManager.LOCATION_PROVIDER_RESTRICTION_NONE);
                // init location manager
                mLocationManager.startLocationFetching();

            }

        }
    }
    @Override
    public void onBackPressed() {
        Intent deviceHome = new Intent(Intent.ACTION_MAIN);
        deviceHome.addCategory(Intent.CATEGORY_HOME);
        startActivity(deviceHome);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.tvlocation){
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getRawX() >= (tvlocation.getRight() - tvlocation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    getLocationList();
                    tvlocation.setVisibility(View.GONE);
                    etChangeLocation.setVisibility(View.VISIBLE);
                    etChangeLocation.setFocusable(true);
                    etChangeLocation.setText("");
                    etChangeLocation.requestFocus();
                    InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etChangeLocation, 0);
                    return true;
                }
            }
        }else if (v.getId() == R.id.etChangeLocation) {
            final int DRAWABLE_RIGHT = 2;
            if(event.getRawX() >= (etChangeLocation.getRight() - etChangeLocation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here
                tvlocation.setVisibility(View.VISIBLE);
                etChangeLocation.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etChangeLocation.getWindowToken(), 0);
                return true;
            }
        }
        return false;
    }


    static class MaterialColorAdapter extends WheelArrayAdapter<Map.Entry<String, Integer>> {
        Activity activity;
        MaterialColorAdapter(List<Map.Entry<String, Integer>> entries,Activity activity) {
            super(entries);
            this.activity = activity;
        }

        @Override
        public Drawable getDrawable(int position) {
            Drawable[] drawable = new Drawable[] {
                    createOvalDrawable(getItem(position).getValue()),
                    new TextDrawable(String.valueOf(position))
            };
            return new LayerDrawable(drawable);
        }

        private Drawable createOvalDrawable(int color) {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
        }

        @Override
        public View getItem(int position, View convertView, ViewGroup parent) {
            return convertView;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return convertView;
        }
    }



    @Override
    public void dataRetreived(Response data) {
        isWebServiceRunning = false;
        if(data != null && data.data != null) {
            hideLoader();
            switch (data.method) {
                case WS_SERVICES:
                    if(data.data!=null && data.data instanceof List<?>)
                    {

                        servicesList = (List<ServicesDO>)data.data;
                        if (servicesList!=null) {

                            servicesSearchAdapter.refreshServicesList(servicesList);
                            wheelView.setWheelItemCount(servicesList.size());
                            //create data for the adapter
                            List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(servicesList.size());

                            for (int i = 0; i < servicesList.size(); i++) {
                                Map.Entry<String, Integer> entry = MaterialColor.random(this, "\\D*_500$");
                                entries.add(entry);
                            }
                            //populate the adapter, that knows how to draw each item (as you would do with a ListAdapter)
                            wheelView.setAdapter(new MaterialColorAdapter(entries, LandingActivity.this));
                            wheelView.setSetvices(servicesList);
                            wheelView.refreshDrawableState();

                        }
                    }else if(data.data instanceof String){
                        String str = (String)data.data;
                        showToast(str);
                        try{
                            if (pd != null)
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                    pd = null;
                                }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                case WS_GETCITIES:
                    if(data.data!=null && data.data instanceof List<?>)
                    {
                        locationList = (List<LocationDO>)data.data;
                        if (locationList!=null) {
                            locationSearchAdapter.refreshServicesList(locationList);
                        }
                    }else{
                    }

                    try{
                        if (pd != null)
                            if (pd.isShowing()) {
                                pd.dismiss();
                                pd = null;
                            }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case WS_SUBMITRATING:
                    if(data.data!=null && data.data instanceof String)
                    {
                        String  status = (String)data.data;
                       if (status.equalsIgnoreCase(AppConstants.SUCCESS)){
                           showToast("Updated successfully");
                           w30Database.removeRatedCustomer(ratingId);
                       }else{
                           showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                       }
                    }else{
                        showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                    }
                    break;
                default:
                    break;
            }
            try{
                if (pd != null)
                    if (pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
            }catch(Exception e){
                e.printStackTrace();
            }

        }


    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        return null;
    }

    @Override
    public void disableHomeButton() {

    }

    @Override
    public void enableHomeButton() {

    }

    @Override
    public void addViewToContainer(View view) {

    }


   public  static  class InternetConnectionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Make sure it's an event we're listening for ...
            if (!intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION) &&
                    !intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) &&
                    !intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
            {
                return;
            }

            ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

            if (cm == null) {
                return;
            }

            // Now to check if we're actually connected
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                // Start the service to do our thing

            }
        }
    }

    boolean isAppActive = false;

    @Override
    public void onStop() {
        super.onStop();
        isAppActive = false;
        if (mLocationManager != null)
            mLocationManager.abortLocationFetching();
    }

    @Override
    public void onResume() {
        super.onResume(); //is in foreground
        isAppActive = true;
    }
   @Override
   protected void onPause() {
        super.onPause();
       if (mLocationManager != null)
           mLocationManager.pauseLocationFetching();

   }

    @Override
    public void onStart() {
        super.onStart();
        if (mLocationManager != null) {
            isLocationReadRequest = true;
            mLocationManager.startLocationFetching();
        }
        else{
            initLocationFetching(LandingActivity.this);
        }

    }
    @Override
    public void locationFetched(Location mLocation, Location oldLocation, String time, String locationProvider) {
        // storing it on application level4

        GetAccurateLocationApplication.mCurrentLocation = mLocation;
        GetAccurateLocationApplication.oldLocation = oldLocation;
        GetAccurateLocationApplication.locationProvider = locationProvider;
        GetAccurateLocationApplication.locationTime = time;
        Log.d("1--","locationFetched");
        Log.d("latitude--",""+mLocation.getLatitude());
        Log.d("longitude--",""+mLocation.getLongitude());
        if(isLocationReadRequest){
            isLocationReadRequest = false;
            if(mLocation != null){
                double latitude = mLocation.getLatitude();
                double longitude = mLocation.getLongitude();
                mLatitude = latitude;
                mLongitude = longitude;

                String currentCity = new W30Utilities().getCityName(mLatitude,mLongitude,LandingActivity.this);
                locationDO = new LocationDO();
                locationDO.setLatitude(mLatitude);
                locationDO.setLongitude(mLongitude);
                locationDO.setCity(currentCity);
                if (selectedLocation == null){
                    new SessionManager(LandingActivity.this).setLocationChanged(false);
                    tvlocation.setText(locationDO.getCity());
                    tvlocation.setTag(locationDO);
                    new SessionManager(LandingActivity.this).saveUserSelectedLocationInfo(locationDO);
                }
                if(locationList == null  || locationList.size() == 0){
                    try{
                        if (mLongitude !=0 && mLongitude !=0 && !isWebServiceRunning) {

                            if (NetworkUtility.isNetworkConnectionAvailable(LandingActivity.this)) {
                                if (commonBL.getCities(mLatitude, mLongitude)) {
                                    isWebServiceRunning = true;
                                    if (pd == null) {
                                        pd = new ProgressDialog(LandingActivity.this);
                                        pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                                        pd.setMessage("Loading...");
                                        pd.show();
                                    }
                                }
                            }else {
                                showToast(getResources().getString(R.string.server_error_please_try_again));
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }

        }
    }



    public void initLocationFetching(Activity mActivity) {
        isLocationReadRequest = true;
        mCurrentActivity = mActivity;
        // ask permission for M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showLocationPermission();
        } else {
            mLocationManager = new MyLocationManager(getApplicationContext(), mActivity, this, MyLocationManager.ALL_PROVIDERS, LocationRequest.PRIORITY_HIGH_ACCURACY, 10 * 1000, 1 * 1000, MyLocationManager.LOCATION_PROVIDER_RESTRICTION_NONE); // init location manager

        }
    }
    private void showLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(mCurrentActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                 requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
               } else {
                    requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
               }
        } else {
            mLocationManager = new MyLocationManager(getApplicationContext(), mCurrentActivity, this, MyLocationManager.ALL_PROVIDERS, LocationRequest.PRIORITY_HIGH_ACCURACY, 10 * 1000, 1 * 1000, MyLocationManager.LOCATION_PROVIDER_RESTRICTION_NONE); // init location manager
        }
    }



    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(mCurrentActivity, new String[]{permissionName}, permissionRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
       /* Log.d("onRequestPermissionsResult","onRequestPermissionsResult  Landing activity "+requestCode);
        for(String temp:permissions){
            Log.d("permissions",temp );
        }
        for(int temp:grantResults){
            Log.d("grantresults",""+temp );
        }

        Log.d("",permissions.toString());
        Log.d("",grantResults.toString());*/

        isLocationReadRequest = true;
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
              //  Log.d("grantResults.length",""+grantResults.length );
             //   Log.d("grantResults.length",""+grantResults[0]);
             //   Log.d("grantResults.length > 0 && grantResults[0]",""+(grantResults.length > 0 && grantResults[0]));
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck== PackageManager.PERMISSION_GRANTED)
                {
                //if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //    Log.d("QQQQQQQQQQq","QQQQQQQQQQq");
                    mLocationManager = new MyLocationManager(getApplicationContext(), this, this, MyLocationManager.ALL_PROVIDERS, LocationRequest.PRIORITY_HIGH_ACCURACY, 10 * 1000, 1 * 1000, MyLocationManager.LOCATION_PROVIDER_RESTRICTION_NONE);
                    // init location manager
                    mLocationManager.startLocationFetching();
                    //permission granted here
                }
        }
    }

}
