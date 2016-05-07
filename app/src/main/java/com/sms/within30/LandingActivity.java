package com.sms.within30;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sms.within30.adapters.LocationSearchAdapter;
import com.sms.within30.adapters.ServicesSearchAdapter;
import com.sms.within30.dataobjects.CustomerDO;
import com.sms.within30.dataobjects.LocationDO;
import com.sms.within30.dataobjects.ServicesDO;
import com.sms.within30.lib.GPSTracker;
import com.sms.within30.lib.SlidingUpPanelLayout;
import com.sms.within30.session.SessionManager;
import com.sms.within30.sidemenu.interfaces.Resourceble;
import com.sms.within30.sidemenu.interfaces.ScreenShotable;
import com.sms.within30.utilities.NetworkUtility;
import com.sms.within30.webservices.Response;
import com.sms.within30.webservices.ServiceURLs;
import com.sms.within30.webservices.businesslayer.CommonBL;
import com.sms.within30.webservices.businesslayer.DataListener;
import com.sms.within30.wheel.MaterialColor;
import com.sms.within30.wheel.TextDrawable;
import com.sms.within30.wheel.WheelArrayAdapter;
import com.sms.within30.wheel.WheelView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.graphics.Color.TRANSPARENT;


public class LandingActivity extends BaseActivity implements View.OnClickListener,DataListener,View.OnTouchListener {
    RelativeLayout homeLayout;
    private static final String KEY_DEMO = "demo";

   // private static final int ITEM_COUNT = 7;
    ImageView btmenu;
    AutoCompleteTextView autoCompleteTextView;
    WheelView wheelView;
   // ImageView img_filter;
    LinearLayout llmenu;
    TextView tvbusinessower;
    AutoCompleteTextView etChangeLocation;
    TextView tvlocation;

   // HashMap<Integer,String> categories = new HashMap<Integer,String>();

    Animation wheelOpen;
    Animation wheelClose;
   // com.ogaclejapan.arclayout.ArcLayout arc_layout;
    int currentlocation = 0;

    List<ServicesDO> servicesList;
    List<LocationDO> locationList;
    ArrayList<String> searchArrayList = new ArrayList<String>();
    ArrayList<String> LocationsArrayList = new ArrayList<String>();
    ServicesSearchAdapter servicesSearchAdapter;
    LocationSearchAdapter locationSearchAdapter;

    double mLatitude=0;
    double mLongitude=0;
    private static final int LOCATION_REQUEST=3;
    private static  final int LOCATION_REQUEST1 = 4;
    GPSTracker gpsTracker = null;
   // private int count = 0;
    CommonBL commonBL = null;

    public void initialize() {
       // moveToNextScreen();
        homeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_landing, null);
       // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);
        commonBL = new CommonBL(LandingActivity.this,LandingActivity.this);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().hide();

        intilizeControls();
        getGPSTrackerInfo();
       /* mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
        mSlidingUpPanelLayout.setEnableDragViewTouchEvents(false);
       // mSlidingUpPanelLayout.collapsePane();
        mSlidingUpPanelLayout.showPane();*/
       // mSlidingUpPanelLayout.

    }

    private void getGPSTrackerInfo() {
        try{
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                gpsTracker = new GPSTracker(LandingActivity.this);
            }else{

                if (ContextCompat.checkSelfPermission(LandingActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LandingActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
                    Log.d("ACCESS_COARSE_LOCATION"," permission requesting...");
                } else {
                    Log.d("ACCESS_COARSE_LOCATION"," permission suceess...");
                    gpsTracker = new GPSTracker(LandingActivity.this);
                }
            }

        } catch (Exception e) {
            Log.d("location exception","exception occured ...........");
            e.printStackTrace();

        }
        getLatLng();
        callServices();


    }
    private void callServices(){
        if (NetworkUtility.isNetworkConnectionAvailable(LandingActivity.this)) {

            if (pd == null) {
                pd =  new ProgressDialog(LandingActivity.this);
                pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                pd.setMessage("Loading...");
                pd.show();
            }
          //  count++;

            if(!commonBL.getServices())
            {
                showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                if (pd != null)
                    if (pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
            }
          //  count++;
           /* if ( commonBL.getCities(mLatitude,mLongitude)){
                showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                if (pd != null)
                    if (pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
            }*/
        }else{
            showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case LOCATION_REQUEST:
                if (ContextCompat.checkSelfPermission(LandingActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_COARSE_LOCATION success");
                    try{
                        gpsTracker = new GPSTracker(LandingActivity.this);
                        getLatLng();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                } else  if (ContextCompat.checkSelfPermission(LandingActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_COARSE_LOCATION failed");
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        //Show permission explanation dialog...
                        Log.d("in onRequestPermissionsResult", "Show permission explanation dialog..ACCESS_COARSE_LOCATION.");
                        try{
                            gpsTracker = new GPSTracker(LandingActivity.this);
                            getLatLng();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        //So, disable that feature, or fall back to another situation...
                        Log.d("in onRequestPermissionsResult", "Never ask again selected...ACCESS_COARSE_LOCATION");
                        // Toast.makeText(this, "Go to settings and enable permissions...1", Toast.LENGTH_LONG)
                        //        .show();
                        if (ContextCompat.checkSelfPermission(LandingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(LandingActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST1);
                            Log.d("ACCESS_FINE_LOCATION"," permission requesting...");
                        } else {
                            Log.d("ACCESS_FINE_LOCATION"," permission suceess...");
                            try{
                                gpsTracker = new GPSTracker(LandingActivity.this);
                                getLatLng();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case LOCATION_REQUEST1:
                //Do the stuff that requires permission...
                if (ContextCompat.checkSelfPermission(LandingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_FINE_LOCATION success");
                    try{
                        gpsTracker = new GPSTracker(LandingActivity.this);
                        getLatLng();
                    }catch(Exception e){
                        e.printStackTrace();
                    }


                }else  if (ContextCompat.checkSelfPermission(LandingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_FINE_LOCATION failed");
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        //Show permission explanation dialog...
                        Log.d("in onRequestPermissionsResult", "Show permission explanation dialog...ACCESS_FINE_LOCATION");
                        try{
                            gpsTracker = new GPSTracker(LandingActivity.this);
                            getLatLng();
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        //So, disable that feature, or fall back to another situation...
                        Log.d("in onRequestPermissionsResult", "Never ask again selected...ACCESS_FINE_LOCATION");
                        Toast.makeText(this, "Go to settings and enable permissions.", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                break;



        }
    }
   private void getLatLng(){
       if (gpsTracker!=null){

           // check if GPS enabled
           if(gpsTracker.canGetLocation()){

               double latitude = gpsTracker.getLatitude();
               double longitude = gpsTracker.getLongitude();
               mLatitude = latitude;
               mLongitude = longitude;
               String currentCity = getCityName(mLatitude,mLongitude);
               LocationDO locationDO = new LocationDO();
               locationDO.setLatitude(mLatitude);
               locationDO.setLongitude(mLongitude);
               locationDO.setCity(currentCity);
               tvlocation.setText(locationDO.getCity());
               etChangeLocation.setText(locationDO.getCity());
               tvlocation.setTag(locationDO);

           }else{
               // can't get location
               // GPS or Network is not enabled
               // Ask user to enable GPS/network in settings
               try{
                   gpsTracker.showSettingsAlert();
               }catch(Exception e){
                   e.printStackTrace();
               }

           }
       }else{
           Log.d("GPS traker null","unable to get the location coz android latest version ...........");

       }
   }

    private String  getCityName(double mLatitude, double mLongitude) {
        Geocoder gcd = new Geocoder(LandingActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
            if (addresses.size() > 0){
               // System.out.println(addresses.get(0).getAddressLine(0));
                String city ="";
                city = addresses.get(0).getSubLocality();
                if (city.length() == 0) {
                    city =  addresses.get(0).getAddressLine(0);
                }

                System.out.println("city->"+city);

                return city;
            }


        }catch(Exception e){
            e.printStackTrace();
        }
       return "";
    }

    @Override
    public void loadData() {

    }



    private  void intilizeControls() {

       // placesList = new ArrayList<ServicesDO>();
        tvbusinessower = (TextView) homeLayout.findViewById(R.id.tvbusinessower);
        btmenu = (ImageView) findViewById(R.id.btmenu);
        llmenu = (LinearLayout) homeLayout.findViewById(R.id.llmenu);
      //  llmenu.setOnClickListener(this);
        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setActivity(this);
        wheelClose = AnimationUtils.loadAnimation(this, R.anim.wheel_close);
        wheelOpen = AnimationUtils.loadAnimation(this,R.anim.wheel_open);
        tvlocation = (TextView)homeLayout.findViewById(R.id.tvlocation);
        tvlocation.setOnTouchListener(this);
        servicesSearchAdapter  = new ServicesSearchAdapter(LandingActivity.this, R.layout.category_list_item, R.id.tv_category);
        locationSearchAdapter  = new LocationSearchAdapter(LandingActivity.this, R.layout.category_list_item, R.id.tv_category);

        autoCompleteTextView = (AutoCompleteTextView) homeLayout.findViewById(R.id.autoCompleted_categorysearch);
        autoCompleteTextView.setAdapter(servicesSearchAdapter);

        etChangeLocation = (AutoCompleteTextView) homeLayout.findViewById(R.id.etChangeLocation);
        etChangeLocation.setAdapter(locationSearchAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                ServicesDO servicesDO = (ServicesDO) adapterView.getItemAtPosition(position);
                autoCompleteTextView.setText(servicesDO.getName());
               // if (servicesDO.isActive()) {
                    Intent mapsIntent = new Intent(LandingActivity.this,MapsActivity.class);
                    mapsIntent.putExtra("actionbar_title", servicesDO.getName());
                    mapsIntent.putExtra("service_id", servicesDO.get_id());
                    // here we use GSON to serialize mMyObjectList and pass it throught intent to second Activity
                    String listSerializedToJson = new Gson().toJson(servicesList);
                    mapsIntent.putExtra("service_list", listSerializedToJson);
                    LocationDO locationDO = (LocationDO)tvlocation.getTag();
                    mapsIntent.putExtra("location",locationDO);
                    String locationListSerializedToJson = new Gson().toJson(locationList);
                    mapsIntent.putExtra("locationList",locationListSerializedToJson);
                    startActivity(mapsIntent);
               /* }else{
                    showToast("Coming Soon");
                }*/
            }
        });

        etChangeLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                tvlocation.setVisibility(View.VISIBLE);
                etChangeLocation.setVisibility(View.GONE);
                LocationDO locationDO = (LocationDO)adapterView.getItemAtPosition(position);
                etChangeLocation.setText(locationDO.getCity());
                tvlocation.setText(locationDO.getCity());
                tvlocation.setTag(locationDO);

            }
        });
      //  img_filter = (ImageView) homeLayout.findViewById(R.id.img_filter);

      //  img_filter.setOnClickListener(this);
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
                LocationDO locationDO = (LocationDO)tvlocation.getTag();
                String msg = String.valueOf(position) + " " + isSelected;
                Log.i("wheel position", "" + position);
                ServicesDO servicesDO = (ServicesDO)servicesList.get(position);
                //if (servicesDO.isActive()) {
                    Intent mapsIntent = new Intent(LandingActivity.this, MapsActivity.class);
                    mapsIntent.putExtra("actionbar_title", servicesDO.getName());
                    mapsIntent.putExtra("service_id", servicesDO.get_id());
                    // here we use GSON to serialize mMyObjectList and pass it throught intent to second Activity
                    String listSerializedToJson = new Gson().toJson(servicesList);
                    mapsIntent.putExtra("service_list", listSerializedToJson);
                    mapsIntent.putExtra("location",locationDO);
                    String locationListSerializedToJson = new Gson().toJson(locationList);
                    mapsIntent.putExtra("locationList",locationListSerializedToJson);
                    startActivity(mapsIntent);
              /*  }else{
                    showToast("Coming Soon");
                }*/



            }
        });


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.tvlocation){
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getRawX() >= (tvlocation.getRight() - tvlocation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    tvlocation.setVisibility(View.GONE);
                    etChangeLocation.setVisibility(View.VISIBLE);

                    return true;
                }
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

            // return getDrawable(R.drawable.salon);
        }

        private Drawable createOvalDrawable(int color) {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
            //  return getDrawable(R.drawable.salon);
        }

        @Override
        public View getItem(int position, View convertView, ViewGroup parent) {
          /*  LayoutInflater inflater = (LayoutInflater)activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);*/
            return convertView;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

           /* LayoutInflater inflater = (LayoutInflater)activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);*/
            return convertView;
        }
    }



    @Override
    public void dataRetreived(Response data) {
       /* if (pd != null)
            if (pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }*/
        //count--;
        if(data != null && data.data != null) {
            hideLoader();
            switch (data.method) {
                case WS_SERVICES:
                    if(data.data!=null && data.data instanceof List<?>)
                    {
                        List<ServicesDO> servicesDOListTemp =  (List<ServicesDO>)data.data;
                        servicesList = (List<ServicesDO>)data.data;
                       /* System.out.println("servicesDOListTemp->"+servicesDOListTemp.size());
                        if (servicesDOListTemp!=null) {
                            servicesList = new ArrayList<ServicesDO>();
                            for (final ServicesDO servicesDO:servicesDOListTemp) {
                                final ServicesDO servicesDO1 = new ServicesDO();
                                servicesDO1.setName(servicesDO.getName());
                                servicesDO1.set_id(servicesDO.get_id());
                                String imageURL = ServiceURLs.IMAGE_URL+servicesDO.getMobileImage();
                                String menuImageURL = ServiceURLs.IMAGE_URL+servicesDO.getMobileMenuImage();
                                Log.d("imageURL", imageURL);
                                final ImageView imageView = new ImageView(LandingActivity.this);
                                final ImageView menuimageView = new ImageView(LandingActivity.this);
                                Picasso.with(LandingActivity.this).load(imageURL).into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {//You will get your bitmap here
                                                servicesDO1.setImageView(imageView);
                                                Bitmap innerBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                                servicesDO1.setDrawable(new BitmapDrawable(LandingActivity.this.getResources(), innerBitmap));
                                                Log.d("services image", "completed download image");
                                                if (servicesDO1.getDrawable() != null *//*&& servicesDO1.getMenuImageDrawable()!=null*//*) {
                                                    servicesList.add(servicesDO1);
                                                }
                                            }
                                        }, 100);
                                    }

                                    @Override
                                    public void onError() {
                                        Log.d("Error","error while downloading  image");
                                    }
                                });
                             }
                        }*/
                        Log.d("servicesList", ""+servicesList.size());
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
                    if ( !commonBL.getCities(mLatitude,mLongitude)){
                        showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
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
                        System.out.println("locationList->"+locationList.size());
                        if (locationList!=null) {
                            locationSearchAdapter.refreshServicesList(locationList);
                        }
                    }else{
                        showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
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
}
