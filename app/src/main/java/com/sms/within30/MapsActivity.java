package com.sms.within30;
import com.nineoldandroids.animation.ValueAnimator;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sms.within30.bottomsheet.BottomSheetLayout;
import com.sms.within30.bottomsheet.MyFragment;
import com.sms.within30.dataobjects.BookSlotDO;
import com.sms.within30.dataobjects.CustomerDO;
import com.sms.within30.dataobjects.LocationDO;
import com.sms.within30.dataobjects.ServicesDO;
import com.sms.within30.dataobjects.UserDO;
import com.sms.within30.filters.FilterDistance;
import com.sms.within30.filters.FilterTime;
import com.sms.within30.googlemaps.PlaceJSONParser;
import com.sms.within30.lib.GPSTracker;
import com.sms.within30.lib.HeightEvaluator;
import com.sms.within30.lib.VerticalSeekBar;
import com.sms.within30.session.SessionManager;
import com.sms.within30.sidemenu.fragment.ContentFragment;
import com.sms.within30.sidemenu.fragment.MyLInearLayout;
import com.sms.within30.sidemenu.interfaces.Resourceble;
import com.sms.within30.sidemenu.interfaces.ScreenShotable;
import com.sms.within30.sidemenu.util.ViewAnimator;
import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.CalendarUtils;
import com.sms.within30.utilities.NetworkUtility;
import com.sms.within30.utilities.OnSwipeTouchListener;
import com.sms.within30.utilities.StringUtils;
import com.sms.within30.utilities.W30Constants;
import com.sms.within30.utilities.W30Database;
import com.sms.within30.utilities.W30Utilities;
import com.sms.within30.webservices.Response;
import com.sms.within30.webservices.businesslayer.CommonBL;
import com.sms.within30.webservices.businesslayer.DataListener;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.graphics.Color.MAGENTA;
import static android.graphics.Color.TRANSPARENT;

public class MapsActivity extends BaseActivity  implements  OnMapReadyCallback, LocationListener ,DataListener, ScreenShotable,View.OnClickListener,SeekBar.OnSeekBarChangeListener ,View.OnTouchListener{

    private GoogleMap mMap;

    double mLatitude=0;
    double mLongitude=0;
   // ImageView slide_details_view;
   // LinearLayout ll_details_view;

    HashMap<String, CustomerDO> mMarkerPlaceLink = new HashMap<String, CustomerDO>();
    List<CustomerDO> placesList = new ArrayList<CustomerDO>();
    List<ServicesDO> servicesList;
    LinearLayout llbooking;
    Animation bottomUp;
    Animation bottomDown;
    TextView bt_book;
    BottomSheetLayout homeLayout;
    String category_type = "";
    String actionbarTitle = "";
    String service_id = "";
    ActionBar actionBar;
    com.sms.within30.lib.VerticalSeekBar sbfilter_distance;
    com.sms.within30.lib.VerticalSeekBar sbfilter_time;
    TextView btfilterdistance;
    TextView btfiltertime;
  //  TextView tv_selected_service;

    TextView tvComapanyName;
    RatingBar ratingbar;
    TextView tvcount;
    TextView tvmiles;
    TextView tvtime;
    TextView tvaddress1;
    TextView tvaddress2;
FrameLayout abcd;

    private float distanceSelectedRadius = 30;
    private float timeSelectedRadius = 30;
    private boolean isDistanceFilterSelected = false;
    private boolean isTimeFilterSelected = false;


    private static final int LOCATION_REQUEST=3;
    private static  final int LOCATION_REQUEST1 = 4;
    private static  final int CALL_REQUEST1 = 5;
    GPSTracker gpsTracker = null;
    LocationDO locationDO = null;
    List<LocationDO> locationList;
    public ActionBarDrawerToggle drawerToggle;

    TextView tvcall,tvsave,tvwebsite,tvcall_show,tv_web_show,tvTimings,tv_floating_distance,tv_floating_time;
    protected BottomSheetLayout bottomSheetLayout;
    /** View that accepts the touch events. */
   // private View handle;
    /** Whole slide down view. */
   // private LinearLayout slideDownView;

    public void initialize(){
        homeLayout = (BottomSheetLayout) inflater.inflate(R.layout.activity_maps, null);
        // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);

        actionBar = getSupportActionBar();
        intilizeControls();
        setActionBar();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    float startX=0,startY=0;
    public void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mLayoutDrawer,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mLayoutDrawer.setDrawerListener(drawerToggle);
    }
    private void intilizeControls() {
        bottomSheetLayout  = (BottomSheetLayout)homeLayout.findViewById(R.id.bottomsheet);
       // llbooking = (LinearLayout) homeLayout.findViewById(R.id.llbooking);

        bottomUp = AnimationUtils.loadAnimation(this,R.anim.bottom_up);
        bottomDown = AnimationUtils.loadAnimation(this,R.anim.bottom_down);
      //  bt_book = (TextView)homeLayout.findViewById(R.id.bt_book);
      //  btfiltertime = (TextView)homeLayout.findViewById(R.id.btfiltertime);
       // btfilterdistance = (TextView)homeLayout.findViewById(R.id.btfilterdistance);
      //  tv_selected_service = (TextView)homeLayout.findViewById(R.id.tv_selected_service);
        sbfilter_distance = (com.sms.within30.lib.VerticalSeekBar)homeLayout.findViewById(R.id.sbfilter_distance);
        sbfilter_time = (com.sms.within30.lib.VerticalSeekBar) homeLayout.findViewById(R.id.sbfilter_time);
      //  slide_details_view = (ImageView)homeLayout.findViewById(R.id.slide_details_view);
      //  ll_details_view = (LinearLayout)homeLayout.findViewById(R.id.ll_details_view);
      //  slideDownView = (LinearLayout)homeLayout. findViewById(R.id.slide_down_view);
      //  handle =homeLayout. findViewById(R.id.handle);
     //   slide_details_view.setOnTouchListener(this);
      //  slide_details_view.bringToFront();
       // btfilterdistance.setOnClickListener(this);
      //  btfiltertime.setOnClickListener(this);
        sbfilter_distance.setOnSeekBarChangeListener(this);
        sbfilter_time.setOnSeekBarChangeListener(this);

       // sbfilter_distance.setVisibility(View.GONE);
       /* tvComapanyName  = (TextView)homeLayout.findViewById(R.id.tvComapanyName);
        ratingbar = (RatingBar)homeLayout.findViewById(R.id.ratingbar);
        tvcount = (TextView)homeLayout.findViewById(R.id.tvcount);
        tvmiles = (TextView)homeLayout.findViewById(R.id.tvmiles);
        tvtime = (TextView) homeLayout.findViewById(R.id.tvtime);
        tvaddress1 = (TextView) homeLayout.findViewById(R.id.tvaddress1);
        tvcall  = (TextView) homeLayout.findViewById(R.id.tvcall);
        tvsave  = (TextView) homeLayout.findViewById(R.id.tvsave);
        tvwebsite  = (TextView) homeLayout.findViewById(R.id.tvwebsite);
       // tvwebsite.setMovementMethod(LinkMovementMethod.getInstance());
     //   tvcall_show  = (TextView) homeLayout.findViewById(R.id.tvcall_show);
        tv_web_show  = (TextView) homeLayout.findViewById(R.id.tv_web_show);
        tvTimings  = (TextView) homeLayout.findViewById(R.id.tvTimings);*/
        tv_floating_distance = (TextView) homeLayout.findViewById(R.id.tv_floating_distance);
        tv_floating_time = (TextView)homeLayout.findViewById(R.id.tv_floating_time);
        tv_floating_time.setOnTouchListener(new FilterTime(sbfilter_time,sbfilter_distance));
       // tv_floating_distance.setOnTouchListener(this);
        tv_floating_distance.setOnTouchListener(new FilterDistance(sbfilter_distance,sbfilter_time));




        // Getting place reference from the map
        if ( getIntent()!=null) {

            try{
                if (getIntent().hasExtra("actionbar_title")) {
                    actionbarTitle = getIntent().getStringExtra("actionbar_title");

                   // tv_selected_service.setText(actionbarTitle);
                    tvTitle.setText(actionbarTitle);
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
                    servicesList =new ArrayList<ServicesDO>(Arrays.asList(new Gson().fromJson(listSerializedToJson, ServicesDO[].class)));
                    for(ServicesDO servicesDO:servicesList){
                        Log.d("services:-",servicesDO.toString());
                    }
                    if (servicesList!=null) {
                        createMenuList(servicesList);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                if (getIntent().hasExtra("locationList")) {
                    String locationsListSerializedToJson = getIntent().getExtras().getString("locationList");
                    locationList = new ArrayList<LocationDO>(Arrays.asList(new Gson().fromJson(locationsListSerializedToJson,LocationDO[].class)));

                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                if (getIntent().hasExtra("location")) {
                    locationDO = (LocationDO)getIntent().getExtras().getSerializable("location");
                  //  tvTitle.setText(locationDO.getCity());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        try{
            initilizeMap();
        }catch(Exception e){
            e.printStackTrace();
        }
      /*  bt_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    CustomerDO customerDO =(CustomerDO) bt_book.getTag();
                    if (customerDO !=null) {
                        if(llbooking.getVisibility() == View.VISIBLE){
                            llbooking.startAnimation(bottomDown);
                            llbooking.setVisibility(View.INVISIBLE);
                        }
                        bookSlot(customerDO);
                    }

                }catch(Exception e) {
                    e.printStackTrace();
                }

            }
        });*/
        if (mMap!=null) {
            //   mMap.setMyLocationEnabled(true);
            // create class object

            try{
                if (locationDO ==null) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        gpsTracker = new GPSTracker(MapsActivity.this);
                    }else{

                        if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
                            Log.d("ACCESS_COARSE_LOCATION"," permission requesting...");
                        } else {
                            Log.d("ACCESS_COARSE_LOCATION"," permission suceess...");
                            gpsTracker = new GPSTracker(MapsActivity.this);
                        }
                    }

                }

            } catch (Exception e) {
                Log.d("location exception","exception occured ...........");
                e.printStackTrace();

            }
            if (locationDO == null){
                if (gpsTracker!=null){
                    getGPSTrackerInfo();

                }else{
                    Log.d("GPS traker null","unable to get the location coz android latest version ...........");

                }

            }else{
                getGPSTrackerInfo();
            }


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    System.out.println("Clicked on map...");
                  //  sbfilter.setVisibility(View.GONE);
                  /*  if(llbooking.getVisibility() == View.VISIBLE){
                        llbooking.startAnimation(bottomDown);
                        llbooking.setVisibility(View.INVISIBLE);
                    }*/
                }
            });


            // Getting Google Play availability status
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

            if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                dialog.show();

            }else {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //sbfilter.setVisibility(View.GONE);

                        CustomerDO marker_customerInfo = mMarkerPlaceLink.get(marker.getId());
                        //  Log.d("selected_marker_customer_info",  marker_customerInfo.toString());
                        if (marker_customerInfo != null) {
                            double[] coordinates = marker_customerInfo.getGeo().getCoordinates();
                            double lng = coordinates[0];
                            double lat = coordinates[1];
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                           // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(, 12.0f));
                            CircleOptions circleOptions = new CircleOptions()
                                    //  .center(new LatLng(17.4119767, 78.4200375))
                                    .center(new LatLng(marker_customerInfo.getLatitude(),marker_customerInfo.getLongitude()))
                                    .radius(1000)
                                    .strokeColor(getResources().getColor(R.color.circle_map))
                                            // .fillColor(getResources().getColor(R.color.map_circle_fill))
                                    .fillColor(0x55ffffff)
                                    .strokeWidth(3f)
                                    .visible(true);

                            circle =  mMap.addCircle(circleOptions);
                          //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circleOptions.getCenter(),getZoomLevel(circle,1000)));


                           /* if (llbooking.getVisibility() == View.INVISIBLE || llbooking.getVisibility() == View.GONE) {
                                llbooking.startAnimation(bottomUp);
                                llbooking.setVisibility(View.VISIBLE);
                            }*/
                          ///  new MyFragment().show(getSupportFragmentManager(), R.id.bottomsheet);
                           // RelativeLayout item = (RelativeLayout)findViewById(R.id.llbooking);
                            View child = LayoutInflater.from(MapsActivity.this).inflate(R.layout.place_details_and_booking, bottomSheetLayout, false);
                          //  bottomSheetLayout.showWithSheetView(child);//.addView(child);
                            setCustomerInfo(marker_customerInfo,child);
                            bottomSheetLayout.showWithSheetView(child);
                        }
                        marker.hideInfoWindow();
                       // marker.
                        return true;
                    }
                });

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      switch(requestCode) {
       case LOCATION_REQUEST:
                if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_COARSE_LOCATION success");
                    try{
                        gpsTracker = new GPSTracker(MapsActivity.this);
                        getGPSTrackerInfo();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                } else  if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_COARSE_LOCATION failed");
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        //Show permission explanation dialog...
                        Log.d("in onRequestPermissionsResult", "Show permission explanation dialog..ACCESS_COARSE_LOCATION.");
                        try{
                            gpsTracker = new GPSTracker(MapsActivity.this);
                            getGPSTrackerInfo();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        //So, disable that feature, or fall back to another situation...
                        Log.d("in onRequestPermissionsResult", "Never ask again selected...ACCESS_COARSE_LOCATION");
                       // Toast.makeText(this, "Go to settings and enable permissions...1", Toast.LENGTH_LONG)
                        //        .show();
                        if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST1);
                            Log.d("ACCESS_FINE_LOCATION"," permission requesting...");
                        } else {
                            Log.d("ACCESS_FINE_LOCATION"," permission suceess...");
                            try{
                                gpsTracker = new GPSTracker(MapsActivity.this);
                                getGPSTrackerInfo();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case LOCATION_REQUEST1:
                    //Do the stuff that requires permission...
                  if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                 {
                    Log.d("in onRequestPermissionsResult", "ACCESS_FINE_LOCATION success");
                    try{
                        gpsTracker = new GPSTracker(MapsActivity.this);
                        getGPSTrackerInfo();
                    }catch(Exception e){
                        e.printStackTrace();
                    }


                 }else  if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                  {
                    Log.d("in onRequestPermissionsResult", "ACCESS_FINE_LOCATION failed");
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        //Show permission explanation dialog...
                        Log.d("in onRequestPermissionsResult", "Show permission explanation dialog...ACCESS_FINE_LOCATION");
                        try{
                            gpsTracker = new GPSTracker(MapsActivity.this);
                            getGPSTrackerInfo();
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
          case CALL_REQUEST1:
              if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
              {
                  Log.d("in onRequestPermissionsResult", "ACCESS_COARSE_LOCATION success");
                  try{
                      callPhone();
                  }catch(Exception e){
                      e.printStackTrace();
                  }

              }
              break;


        }
    }

    public void getGPSTrackerInfo(){
        if (locationDO == null) {
            if (gpsTracker.canGetLocation()) {

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                locationDO = new LocationDO();
                mLatitude = latitude;
                mLongitude = longitude;
                locationDO.setLatitude(mLatitude);
                locationDO.setLongitude(mLongitude);
                LatLng latLng = new LatLng(locationDO.getLatitude(), locationDO.getLongitude());
                // create marker
                MarkerOptions marker = new MarkerOptions().position(latLng);

                // adding marker
                // mMap.addMarker(marker);

                Marker m = mMap.addMarker(marker);
                //  mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                CustomerDO customerDO = null;
                mMarkerPlaceLink.put(m.getId(), customerDO);
                getServices();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                try {
                    gpsTracker.showSettingsAlert();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }else{
                LatLng latLng = new LatLng(locationDO.getLatitude(), locationDO.getLongitude());
                // create marker
                MarkerOptions marker = new MarkerOptions().position(latLng);

                // adding marker
                // mMap.addMarker(marker);

                Marker m = mMap.addMarker(marker);
                //  mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                CustomerDO customerDO = null;
                mMarkerPlaceLink.put(m.getId(), customerDO);
                getServices();
            }
        }



    private void setCustomerInfo(CustomerDO customerDO,View child){

        try{
            bt_book = (TextView)child.findViewById(R.id.bt_book);
            tvComapanyName  = (TextView)child.findViewById(R.id.tvComapanyName);
            ratingbar = (RatingBar)child.findViewById(R.id.ratingbar);
            tvcount = (TextView)child.findViewById(R.id.tvcount);
            tvmiles = (TextView)child.findViewById(R.id.tvmiles);
            tvtime = (TextView) child.findViewById(R.id.tvtime);
            tvaddress1 = (TextView) child.findViewById(R.id.tvaddress1);
            tvcall  = (TextView) child.findViewById(R.id.tvcall);
            tvsave  = (TextView) child.findViewById(R.id.tvsave);
            tvwebsite  = (TextView) child.findViewById(R.id.tvwebsite);
            // tvwebsite.setMovementMethod(LinkMovementMethod.getInstance());
            tvcall_show  = (TextView) child.findViewById(R.id.tvcall_show);
            tv_web_show  = (TextView) child.findViewById(R.id.tv_web_show);
            tvTimings  = (TextView) child.findViewById(R.id.tvTimings);
            FrameLayout frame_slide_details_view = (FrameLayout) child.findViewById(R.id.frame_slide_details_view);
            final TextView slide_details_view = (TextView)child.findViewById(R.id.slide_details_view);
            String upperCaseString_companyName = customerDO.getFullName().substring(0, 1).toUpperCase() + customerDO.getFullName().substring(1);
            tvComapanyName.setText(upperCaseString_companyName);
            tvaddress1.setText(customerDO.getGeo().getAddress().toString());
            ratingbar.setRating(customerDO.getRating());
            child.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("child sheelt state", bottomSheetLayout.getState()+"");
                  //  Log.d("child getTransitionName state", bottomSheetLayout.getTransitionName()+"");
                 //   Log.d("child getHeight", bottomSheetLayout.getHeight()+"");
                  //  Log.d("child getMeasuredHeight", bottomSheetLayout.getMeasuredHeight()+"");

                    if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.EXPANDED)) {


                        slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slidedown));
                    } else if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.PREPARING) ||bottomSheetLayout.getState().equals(BottomSheetLayout.State.PEEKED) ) {
                       // Log.d("child sheelt state", "PEEKED..............");

                        slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slideup));
                    }
                    return true;
                }
            });
            frame_slide_details_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("bottom sheelt state",""+bottomSheetLayout.getState());

                            if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.PEEKED)) {
                                Log.d("bottom sheelt state","expanding..............");
                                bottomSheetLayout.setState(BottomSheetLayout.State.EXPANDED);
                                bottomSheetLayout.expandSheet();
                                slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slidedown));
                            }else if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.EXPANDED)) {
                                Log.d("bottom sheelt state","PEEKED..............");
                                bottomSheetLayout.setState(BottomSheetLayout.State.PEEKED);
                                bottomSheetLayout.peekSheet();
                                 slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slideup));
                            }
                }
            });
            if (customerDO.getSlotsAvailable()!=null) {

                if (customerDO.getSlotsAvailable() == 0) {
                    if (customerDO.getMessage().length()>0){
                        tvtime.setText(customerDO.getMessage());
                    }else{
                        tvtime.setText("Next slot available in :" + " " + customerDO.getNextSlotAt() + " " + "Min");
                    }
                   // BigDecimal result;
                   // result= W30Utilities.round(customerDO.getNextSlotAt(),2);
                  //  tvtime.setText("Next slot available in :" + " " + result + " " + "Min");

                }else{
                   // BigDecimal result;
                   // result= W30Utilities.round(customerDO.getExpectedTime(),2);
                    tvtime.setText("Estimated Time:" + " " +(int)customerDO.getExpectedTime() + " " + "Min");
                }
            }
            BigDecimal miles;
            miles= W30Utilities.round(customerDO.getDestinationDistance(),2);
            tvmiles.setText(miles + " " + "Miles");
            if(customerDO.getMobile() != null && customerDO.getMobile().length() > 0) {
                // phone number exists.
                tvcall.setTag(customerDO.getMobile());
                tvcall_show.setText(customerDO.getMobile());
                tvcall.setAlpha(1f);
                tvcall.setClickable(true);
                tvcall_show.setClickable(true);
                tvcall_show.setVisibility(View.VISIBLE);
            }else { // phone number not exists
                tvcall.setAlpha(0.5f);
                tvcall.setClickable(false);
                tvcall_show.setClickable(false);
                tvcall_show.setVisibility(View.GONE);
            }
            if(customerDO.getLogoUrl()!=null && customerDO.getLogoUrl().length()>0){
                tvwebsite.setTag(customerDO.getLogoUrl());
                tv_web_show.setText(customerDO.getLogoUrl());
            }
            String timings = "";
            if(customerDO.getStartHour()!=null && customerDO.getStartHour().length()>0){
                timings+=customerDO.getStartHour();
            }
            if(customerDO.getEndHour()!=null && customerDO.getEndHour().length()>0){
                timings+=" "+"-"+" "+customerDO.getEndHour();
            }
            if (timings.length()>0){
                tvTimings.setText("Business Hours:"+" "+timings);
            }
           // Business Hours: 09:15 - 18:45

            if (customerDO.getSlotsAvailable() == 0) {
                bt_book.setClickable(false);
                bt_book.setAlpha(0.5f);
            }else{
                bt_book.setClickable(true);
                bt_book.setTag(customerDO);
                bt_book.setAlpha(1f);
                float expectedtime = 0;
                expectedtime = customerDO.getExpectedTime();
                tvtime.setTag(expectedtime);
            }
            bt_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        CustomerDO customerDO =(CustomerDO) bt_book.getTag();
                        if (customerDO !=null) {
                          /*  if(llbooking.getVisibility() == View.VISIBLE){
                                llbooking.startAnimation(bottomDown);
                                llbooking.setVisibility(View.INVISIBLE);
                            }*/
                            bookSlot(customerDO);
                            bottomSheetLayout.dismissSheet();
                        }

                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            tvcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkReadPhoneStatePermissions();
                }
            });
            tvcall_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkReadPhoneStatePermissions();
                }
            });
            tvsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            tvwebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = (String )tvwebsite.getTag();
                    try{
                        if (url!=null && url.length()>0){

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            if(url.contains("http")){
                                i.setData(Uri.parse(url));
                            }else{
                                i.setData(Uri.parse("http://" + url));
                            }

                            startActivity(i);


                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        showToast("Please check URL...");
                    }
                }
            });
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void bookSlot( CustomerDO customerDO ){

        if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
            BookSlotDO bookSlotDO = new BookSlotDO();
            UserDO userDO = new SessionManager(MapsActivity.this).getUserInfo();

            String currentDate = CalendarUtils.getCurrentPostDate((long)customerDO.getExpectedTime());
            bookSlotDO.setDate(currentDate);
            bookSlotDO.setSubDomain(customerDO.getSubdomain());
            bookSlotDO.setEmail(userDO.getEmail());
            bookSlotDO.setMobile(userDO.getMobile());
            bookSlotDO.setUserId(userDO.get_id());

            Log.d("Book slot toString()",bookSlotDO.toString());
            if(new CommonBL(MapsActivity.this, MapsActivity.this).bookSlot(bookSlotDO)){
                if (pd == null) {
                    pd =  new ProgressDialog(MapsActivity.this);
                    pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                    pd.setMessage("Loading...");
                    pd.show();
                }
            } else{
                showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
            }
        }else{
            showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
        }
    }

    private void getServices(){

        if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
            if (locationDO.getLatitude() == 0 || locationDO.getLongitude() == 0){
                showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
            }else{
                // clear data
                placesList.clear();
                CustomerDO customerDO = new CustomerDO();
                customerDO.setLatitude(locationDO.getLatitude());
                customerDO.setLongitude(locationDO.getLongitude());

                // customerDO.setLatitude(17.4119767);
                // customerDO.setLongitude(78.4200375);
                customerDO.setMiles(W30Constants.MILES);
                customerDO.setMinutes(W30Constants.MINITUS);
                customerDO.setServiceId(service_id);
                String userId = new SessionManager(MapsActivity.this).getUserid();
                customerDO.setUserId(userId);
                if(new CommonBL(MapsActivity.this, MapsActivity.this).getCustomers(customerDO)){
                    if (pd == null) {
                        pd =  new ProgressDialog(MapsActivity.this);
                        pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                        pd.setMessage("Loading...");
                        pd.show();
                    }
                }else{
                    showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                }
            }

        }else{
            showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
        }
    }



   private void  showCustomDialog(Context context){
       // custom dialog
       final Dialog dialog = new Dialog(context);
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       dialog.setContentView(R.layout.custom_dialog_booking);

      // dialog.setTitle("Title...");

       // set the custom dialog components - text, image and button
//Grab the window of the dialog, and change the width
       WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
       Window window = dialog.getWindow();
       lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
       lp.width = WindowManager.LayoutParams.MATCH_PARENT;
       lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
       window.setAttributes(lp);
       window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
     //  window.setsty
       TextView dialogButton = (TextView) dialog.findViewById(R.id.dialogButtonOK);
     //  TextView tvEstimatedTime = (TextView) dialog.findViewById(R.id.tvEstimatedTime);
     //  float expectedTime = 0;
     /*  if (tvtime.getTag() !=null) {
           expectedTime = (Float)tvtime.getTag();
       }*/
       UserDO userDO = new SessionManager(MapsActivity.this).getUserInfo();

      // String str = "See you in "+expectedTime+" min";
     //  String str = "See you in within 30 min";
     //  tvEstimatedTime.setText(str);
       // if button is clicked, close the custom dialog
       dialogButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
               getServices();
           }
       });

       dialog.show();
   }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();


            // check if map is created successfully or not
            if (mMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
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
                case WS_CUSTOMERS:
                    if(data.data!=null && data.data instanceof List<?>)
                    {
                       placesList = (List<CustomerDO>)data.data;
                        // Clears all the existing markers
                        mMap.clear();

                        Log.d("places list.size", "" + placesList.size());
                        addCircleToMap(30);
                        sbfilter_distance.setProgress((int) distanceSelectedRadius);
                        sbfilter_time.setProgress((int) timeSelectedRadius);

                        /*sbfilter_distance.setSecondaryProgress(sbfilter_time.getProgress());
                        sbfilter_time.setSecondaryProgress(sbfilter_distance.getProgress());*/

                       // for (int i = 0; i < placesList.size(); i++) {

                           /* // Creating a marker
                            MarkerOptions markerOptions = new MarkerOptions();

                            // Getting a place from the places list
                            CustomerDO  customerDO= (CustomerDO) placesList.get(i);
                            Log.d("customerDO",customerDO.toString());
                            // Getting latitude of the place
                            double[] coordinates = customerDO.getGeo().getCoordinates();

                            double lng = coordinates[0];
                            double lat = coordinates[1];
                           // double lat = Double.parseDouble(hmPlace.get("lat"));
                            Log.d("lat",""+lat);
                            Log.d("lng",""+lng);
                            // Getting longitude of the place
                           // double lng = Double.parseDouble(hmPlace.get("lng"));

                            LatLng latLng = new LatLng(lat, lng);

                            // Setting the position for the marker
                            markerOptions.position(latLng);

                            // Setting the title for the marker.
                            //This will be displayed on taping the marker

                            View custom_marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
                            TextView numTxt = (TextView) custom_marker.findViewById(R.id.tvtitle);
                            TextView img_marker = (TextView) custom_marker.findViewById(R.id.img_marker);
                            Log.d("open slots",""+customerDO.getSlotsAvailable());

                          //  if (customerDO.getSlotBookedAt() !=null) {
                                if (customerDO.getSlotBookedAt().length()>0){
                                    numTxt.setText("Checked In");
                                    numTxt.setBackground(getResources().getDrawable(R.mipmap.onclick_info_window));
                                    img_marker.setBackground(getResources().getDrawable(R.mipmap.on_click_map_marker));
                                }else{
                                    if (customerDO.getSlotsAvailable()!=null) {
                                        if (customerDO.getSlotsAvailable() == 0) {
                                            markerOptions.title("Check Next Slot");
                                            numTxt.setText("Check Next Slot");
                                            numTxt.setBackground(getResources().getDrawable(R.mipmap.check_nextslot_info_window));
                                            img_marker.setBackground(getResources().getDrawable(R.mipmap.map_marker_check_nextslot));
                                        }else{
                                            numTxt.setBackground(getResources().getDrawable(R.mipmap.info_window));
                                            img_marker.setBackground(getResources().getDrawable(R.mipmap.map_marker));
                                            if (customerDO.getSlotsAvailable() == 1) {
                                                markerOptions.title(customerDO.getSlotsAvailable()+" "+"OPen Slot");
                                                numTxt.setText(customerDO.getSlotsAvailable()+"\nOpen slot");
                                            }else{
                                                markerOptions.title(customerDO.getSlotsAvailable()+" "+"OPen Slots");
                                                numTxt.setText(customerDO.getSlotsAvailable()+"\nOpen slots");
                                            }

                                        }
                                    }
                                }
                         //   }

                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, custom_marker)));

                            // Placing a marker on the touched position

                            Marker m = mMap.addMarker(markerOptions);
                            mMarkerPlaceLink.put(m.getId(),customerDO);
*/
                       // }
                    }else if(data.data!=null && data.data instanceof String ){
                        String str = (String)data.data;
                        showToast(str);
                    }
                    break;
                case WS_BOOKSLOT:
                    if(data.data!=null && data.data instanceof String ){
                        String str = (String)data.data;
                        if (str.equals(AppConstants.OK)) {
                           // getServices();
                            CustomerDO customerDO =(CustomerDO) bt_book.getTag();
                            if (customerDO !=null) {
                          /*  if(llbooking.getVisibility() == View.VISIBLE){
                                llbooking.startAnimation(bottomDown);
                                llbooking.setVisibility(View.INVISIBLE);
                            }*/
                                saveBookedInfo(customerDO);

                            }

                            showCustomDialog(MapsActivity.this);
                        }else{
                            showToast(str);
                        }

                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void saveBookedInfo(CustomerDO customerDO) {
        String currentDate = CalendarUtils.getCurrentPostDate(/*(long)customerDO.getDefaultDuration()*/(long)0);
        String tempDate = CalendarUtils.getCurrentPostDate("yyyy-MM-dd HH:mm:ss");
        customerDO.setSlotBookedDate(tempDate);
        W30Database w30Database = new W30Database(MapsActivity.this);
        w30Database.addBookedSlotsInfo(customerDO);

    }

    @Override
    public void takeScreenShot() {


    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public void onClick(View v) {
       // if (v.getId() == R.id.btfiltertime) {
        //    System.out.println("clicked on btfiltertime");
           /* sbfilter.setVisibility(View.VISIBLE);

            btfilterdistance.setAlpha(0.5f);
            btfilterdistance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_distance_filter_disabled, 0);
            btfiltertime.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_time_filter_selected,0);

            btfiltertime.setAlpha(1.0f);
            if(llbooking.getVisibility() == View.VISIBLE){
                llbooking.startAnimation(bottomDown);
                llbooking.setVisibility(View.INVISIBLE);
            }
            isTimeFilterSelected = true;
            isDistanceFilterSelected = false;
            sbfilter.setProgress((int)timeSelectedRadius);
            addCircleToMap((int) timeSelectedRadius);
            btfiltertime.setText("" + timeSelectedRadius + "MIN");*/
      //  }/*else if (v.getId() == R.id.btfilterdistance) {
          //  System.out.println("clicked on btfilterdistance");
           // sbfilter.setVisibility(View.VISIBLE);

           // btfilterdistance.setAlpha(1.0f);
           // btfiltertime.setAlpha(0.5f);
          //  btfilterdistance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_distance_filter_selected, 0);
          //  btfiltertime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_time_filter_disabled, 0);
           // *//*if(llbooking.getVisibility() == View.VISIBLE){
            //    llbooking.startAnimation(bottomDown);
            //    llbooking.setVisibility(View.INVISIBLE);
         //   }*//*

          // *//* isDistanceFilterSelected = true;
          //  isTimeFilterSelected = false;
          // // sbfilter.setProgress((int)distanceSelectedRadius);
         //   addCircleToMap((int) distanceSelectedRadius);
         //   btfilterdistance.setText(""+distanceSelectedRadius+"MI");*//*
       // }*/

    }

/**
 * add circle to map
 *
 */
Circle circle;
   private void addCircleToMap(int radius){
      // mMap.

       System.out.println("adding circle to map..........................." + mLatitude + " " + mLongitude);
       if (circle!=null) {
           circle.remove();
       }
       try{
           double slider_in_meters = 0.0;
           slider_in_meters = radius*W30Constants.MILES_IN_METERS;
           try{
               if (placesList !=null){
                   if (placesList.size()>0) {
                       mMap.clear();
                       for (CustomerDO customerDO:placesList){
                           if (isDistanceFilterSelected){
                               double miles_slider =( radius * 1)/W30Constants.MILES_IN_METERS;
                               Log.d("miles_slider ",""+miles_slider);
                               double miles_server = customerDO.getDestinationDistance();//(customerDO.getDestinationDistance() *1)/W30Constants.MILES_IN_METERS;
                               Log.d("miles_server ",""+miles_server);
                               if ((miles_server <= radius) &&(customerDO.getExpectedTime() <= timeSelectedRadius)) {
                                   setMarker(customerDO);
                               }
                           } else if (isTimeFilterSelected) {

                               if ((customerDO.getDestinationDistance() <= distanceSelectedRadius) &&(customerDO.getExpectedTime() <= radius)) {
                                   setMarker(customerDO);
                               }
                           }else{
                               setMarker(customerDO);
                           }
                       }
                   }
               }
           }catch(Exception e){
               e.printStackTrace();
           }

           CircleOptions circleOptions = new CircleOptions()
                  //  .center(new LatLng(17.4119767, 78.4200375))
                   .center(new LatLng(locationDO.getLatitude(),locationDO.getLongitude()))
                   .radius(slider_in_meters)
                   .strokeColor(getResources().getColor(R.color.circle_map))
                           // .fillColor(getResources().getColor(R.color.map_circle_fill))
                   .fillColor(0x55ffffff)
                   .strokeWidth(3f)
                   .visible(true);

           circle =  mMap.addCircle(circleOptions);
           mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circleOptions.getCenter(),getZoomLevel(circle,slider_in_meters)));

       }catch(Exception e) {
           e.printStackTrace();
       }
   }

    private void setMarker(CustomerDO customerDO){
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();
        double[] coordinates = customerDO.getGeo().getCoordinates();
        double lng = coordinates[0];
        double lat = coordinates[1];
        LatLng latLng = new LatLng(lat, lng);

        // Setting the position for the marker
        markerOptions.position(latLng);
        View custom_marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        TextView numTxt = (TextView) custom_marker.findViewById(R.id.tvtitle);
        TextView img_marker = (TextView) custom_marker.findViewById(R.id.img_marker);
        Log.d("open slots",""+customerDO.getSlotsAvailable());
        numTxt.setVisibility(View.GONE);

       // if (customerDO.getSlotBookedAt() !=null) {
            if (customerDO.getSlotBookedAt().length()>0){
                // checked in
                numTxt.setText("Checked In");
                numTxt.setBackground(getResources().getDrawable(R.mipmap.onclick_info_window));
                img_marker.setBackground(getResources().getDrawable(R.mipmap.on_click_map_marker));
            }else{
                if (customerDO.getSlotsAvailable()!=null) {
                    if (customerDO.getSlotsAvailable() == 0) {
                        //check next slot
                        markerOptions.title("Check\nNext Slot");
                        numTxt.setText("Check\n Next Slot");
                        numTxt.setBackground(getResources().getDrawable(R.mipmap.check_nextslot_info_window));
                        img_marker.setBackground(getResources().getDrawable(R.mipmap.map_marker_check_nextslot));

                    }else{
                        //slots available
                        numTxt.setBackground(getResources().getDrawable(R.mipmap.info_window));
                        img_marker.setBackground(getResources().getDrawable(R.mipmap.map_marker));
                        if (customerDO.getSlotsAvailable() == 1) {
                            markerOptions.title(customerDO.getSlotsAvailable()+" "+"OPen Slot");
                            numTxt.setText(customerDO.getSlotsAvailable()+"\nOpen slot");
                        }else {
                            markerOptions.title(customerDO.getSlotsAvailable()+" "+"OPen Slots");
                            numTxt.setText(customerDO.getSlotsAvailable()+"\nOpen slots");
                        }
                    }
                }
            }
     //   }
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, custom_marker)));
        Marker m = mMap.addMarker(markerOptions);
        mMarkerPlaceLink.put(m.getId(), customerDO);
    }

    public int getZoomLevel(Circle circle,double slider_radius) {
        int zoomLevel = 11;
        if (circle != null) {
            double radius = circle.getRadius() + circle.getRadius() / 2;
            double scale = slider_radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        System.out.println("seek bar on progress changed....." + progress);

        if (seekBar.getId() == R.id.sbfilter_distance){



            Log.d("seek bar", "clicked on distance seek bar");
          //  sbfilter_time.setSecondaryProgress(sbfilter_distance.getProgress());
            sbfilter_distance.setSecondaryProgress(sbfilter_time.getProgress());
           // sbfilter_distance.setProgress(progress);
            distanceSelectedRadius = sbfilter_distance.getProgress();
            isDistanceFilterSelected = true;
            isTimeFilterSelected = false;

            // sbfilter.setProgress((int)distanceSelectedRadius);
            addCircleToMap((int) distanceSelectedRadius);
            int floatingDistanceWidth = sbfilter_distance.getHeight()
                    - sbfilter_distance.getPaddingTop()
                    - sbfilter_distance.getPaddingBottom();
            int seekbarDistanceThumbPos = sbfilter_distance.getPaddingTop()
                    + floatingDistanceWidth
                    * sbfilter_distance.getProgress()
                    / sbfilter_distance.getMax();
            Log.d("thumbPos ", (sbfilter_distance.getHeight() - seekbarDistanceThumbPos) + "----------------------------------------");
            tv_floating_distance.setY((float) (sbfilter_distance.getHeight() - seekbarDistanceThumbPos));
            tv_floating_distance.setText(" "+progress+" \n MI"+" ");


        }else if (seekBar.getId() == R.id.sbfilter_time) {


            Log.d("seek bar", "clicked on time seek bar");
           // sbfilter_distance.setSecondaryProgress(sbfilter_time.getProgress());
            sbfilter_time.setSecondaryProgress(sbfilter_distance.getProgress());
            timeSelectedRadius = sbfilter_time.getProgress();
            isDistanceFilterSelected = false;
            isTimeFilterSelected = true;
            // sbfilter.setProgress((int)distanceSelectedRadius);
            int floatingTimeWidth = sbfilter_time.getHeight()
                    - sbfilter_time.getPaddingTop()
                    - sbfilter_time.getPaddingBottom();
            int seekbarTimeThumbPos = sbfilter_time.getPaddingTop()
                    + floatingTimeWidth
                    * sbfilter_time.getProgress()
                    / sbfilter_time.getMax();
            Log.d("thumbPos ", (sbfilter_time.getHeight() - seekbarTimeThumbPos) + "----------------------------------------");
            tv_floating_time.setY((float)(sbfilter_time.getHeight() - seekbarTimeThumbPos));
            tv_floating_time.setText(" "+progress+"\nMin");
            addCircleToMap((int) timeSelectedRadius);


        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //tv_floating_distance

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }




   /* *//** A class to parse the Google Places in JSON format *//*
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                *//** Getting the parsed data as a List construct *//*
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list) {

            // Clears all the existing markers
            mMap.clear();
        if (list !=null  && list.size()>0) {


            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                //This will be displayed on taping the marker
             //   markerOptions.title("3 OPen Slots");

                View custom_marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
                TextView numTxt = (TextView) custom_marker.findViewById(R.id.tvtitle);
                numTxt.setText("3\nOpen slots");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, custom_marker)));

                // Placing a marker on the touched position


                Marker m = mMap.addMarker(markerOptions);
                m.hideInfoWindow();
                // Linking Marker id and place reference
             //   mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
            }
        }
        }
    }*/
    // Convert a view to bitmap
    public  Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void loadData() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      /*  if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            /*case android.R.id.home:
               *//*Intent menuIntent = new Intent(this,AppMenuActivity.class);
                startActivity(menuIntent);
                overridePendingTransition(R.anim.app_menu_in, 0);*//*
                return true;*/

            case R.id.menu_edit:
                Intent intent = new Intent(this,EditProfileActivity.class);
                intent.putExtra("actionbar_title", tvTitle.getText().toString());
                //   mapsIntent.putExtra("category_type","hospitals");
                intent.putExtra("service_id", service_id);
                // here we use GSON to serialize mMyObjectList and pass it throught intent to second Activity
                String listSerializedToJson = new Gson().toJson(servicesList);
                intent.putExtra("service_list", listSerializedToJson);

                intent.putExtra("location",locationDO);
                String locationListSerializedToJson = new Gson().toJson(locationList);
                intent.putExtra("locationList",locationListSerializedToJson);
                startActivity(intent);
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
        menu.findItem(R.id.menu_reset).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(false);
        menu.findItem(R.id.menu_edit).setVisible(true);
        menu.findItem(R.id.menu_search).setVisible(false);

        mSearchCheck = true;
        return true;
    }
    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {

        switch (slideMenuItem.getName()) {
            case ContentFragment.CLOSE:
                return screenShotable;
            case ContentFragment.HOME:
                Intent intent_landing = new Intent(this,LandingActivity.class);
                startActivity(intent_landing);
                return  screenShotable;

            case ContentFragment.SETTINGS:
                Intent intent = new Intent(this,SearchLocationActivity.class);
                // String locationListSerializedToJson = new Gson().toJson(locationList);
                // intent.putExtra("locationList",locationListSerializedToJson);
                //intent.putExtra("location",locationDO);

                intent.putExtra("actionbar_title", tvTitle.getText().toString());
                //   mapsIntent.putExtra("category_type","hospitals");
                intent.putExtra("service_id", service_id);
                // here we use GSON to serialize mMyObjectList and pass it throught intent to second Activity
                String listSerializedToJson = new Gson().toJson(servicesList);
                intent.putExtra("service_list", listSerializedToJson);

                intent.putExtra("location",locationDO);
                String locationListSerializedToJson = new Gson().toJson(locationList);
                intent.putExtra("locationList",locationListSerializedToJson);
                startActivity(intent);
                return screenShotable;
            default:
                service_id =  slideMenuItem.get_id();
                ServicesDO servicesDOTemp = new ServicesDO();
                for (ServicesDO servicesDO:servicesList){
                    if (service_id.equalsIgnoreCase(servicesDO.get_id())){
                        servicesDOTemp = servicesDO;
                       // tv_selected_service.setText(servicesDO.getName());
                        tvTitle.setText(servicesDO.getName());
                    }
                }
              /*  if (servicesDOTemp.isActive()) {
                    tv_selected_service.setText(servicesDOTemp.getName());
                    if (service_id !=null) {
                        mMap.clear();
                        getServices();
                    }
                }else{
                    showToast("Coming Soon");
                }*/
                Log.d("side menu", "service id from side menu " + service_id);
                if (service_id !=null) {
                    mMap.clear();
                    getServices();
                }

                return replaceFragment(screenShotable, position);
        }
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        mLayoutDrawer.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }
    @Override
    public void onBackPressed()
    {
        /*if(llbooking.getVisibility() == View.VISIBLE){
            llbooking.startAnimation(bottomDown);
            llbooking.setVisibility(View.INVISIBLE);
        }else{
            super.onBackPressed();
        }*/
        super.onBackPressed();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (v.getId() == R.id.tvcall) {
               /* String phoneNo = (String )tvcall.getTag();
                if (phoneNo!=null && phoneNo.length()>0){
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNo));
                    MapsActivity.this.startActivity(callIntent);
                }*/

                checkReadPhoneStatePermissions();
            }else if (v.getId() == R.id.tvsave) {

            }else if (v.getId() == R.id.tvwebsite) {

                String url = (String )tvwebsite.getTag();
                try{
                    if (url!=null && url.length()>0){

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        if(url.contains("http")){
                            i.setData(Uri.parse(url));
                        }else{
                            i.setData(Uri.parse("http://" + url));
                        }

                        startActivity(i);


                    }

                }catch(Exception e){
                    e.printStackTrace();
                    showToast("Please check URL...");
                }


            }else if (v.getId() == R.id.slide_details_view) {
                /*if(ll_details_view.getVisibility() == View.VISIBLE){
                    ll_details_view.startAnimation(bottomDown);
                    ll_details_view.setVisibility(View.GONE);
                }else*/ //llbooking.setVisibility(View.GONE);
               /* if (ll_details_view.getVisibility() == View.INVISIBLE || ll_details_view.getVisibility() == View.GONE) {
                    ll_details_view.startAnimation(bottomUp);
                    ll_details_view.setVisibility(View.VISIBLE);
                }*/

            }/*else if (v.getId() == R.id.tv_floating_distance){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX();
                        float newY = event.getRawY();

                        if (Math.abs(newX - startX) > Math.abs(newY - startY)) {
                            // Means Horizontal Movement.
                            if (newX - startX > 0) {
                                // Moving Right
                                System.out.println("moving right.............");
                            } else {
                                // Moving Left
                                System.out.println("moving left.............");
                            }
                        } else {
                            // Means Vertical Movement.
                            if (newY - startY > 0) {
                                // Moving Down
                                System.out.println("moving down.............");
                                sbfilter_distance.setProgress(sbfilter_distance.getProgress() - 1);
                            } else {
                                // Moving Up
                                System.out.println("moving top.............");
                                sbfilter_distance.setProgress(sbfilter_distance.getProgress() + 1);
                            }
                        }

                        startX = newX;
                        startY = newY;
                        break;

                    case MotionEvent.ACTION_UP:
                        // Finger Up and the motion is complete
                        startX = 0;
                        startY = 0;
                        break;
                }

            }*//*else if (v.getId() == R.id.tv_floating_distance) {
                Log.d("OnTouch ..","tv_floating_distance.......");
                int action = event.getAction();
               System.out.println("action:-"+action);
                System.out.println("getActionMasked:-"+event.getActionMasked());
                System.out.println("getActionIndex:-"+event.getActionIndex());
                switch (action){
                    case MotionEvent.ACTION_DOWN:

                        Log.d("OnTouch ..", "tv_floating_distance.......ACTION_DOWN");
                         action = event.getActionMasked();
                        int actionIndex = event.getActionIndex();
                        System.out.println("action:-" + action);
                        System.out.println("actionIndex:-" + actionIndex);

                        //  sbfilter_distance.setProgress(sbfilter_distance.getProgress() - 1);
                            if (action ==  MotionEvent.ACTION_UP ){
                                Log.d("OnTouch ..", "tv_floating_distance......down.ACTION_UP");
                                sbfilter_distance.setProgress(sbfilter_distance.getProgress() + 1);
                            }else{
                                Log.d("OnTouch ..", "tv_floating_distance......down.down..");
                                sbfilter_distance.setProgress(sbfilter_distance.getProgress() - 1);
                            }
                        // return true;
                        break;
                    case MotionEvent.ACTION_UP :
                        Log.d("OnTouch ..", "tv_floating_distance.......ACTION_UP");
                        sbfilter_distance.setProgress(sbfilter_distance.getProgress() + 1);
                       // return true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("OnTouch ..", "tv_floating_distance.......ACTION_MOVE");
                        break;


                }
               *//* int action = event.getAction() ;//& MotionEvent.ACTION_MASK;

                if ((action ==MotionEvent.ACTION_UP )||(action ==  MotionEvent.ACTION_CANCEL)||(action ==MotionEvent.ACTION_POINTER_UP)) {
                        Log.d("OnTouch ..",".......ACTION_UP..cancel..action pointer up");
                        sbfilter_distance.setProgress(sbfilter_distance.getProgress() + 1);
                    return false;
                }else if ((action ==MotionEvent.ACTION_POINTER_DOWN)||(action ==MotionEvent.ACTION_DOWN)) {

                    Log.d("OnTouch ..", "tv_floating_distance.......ACTION_DOWN");
                    sbfilter_distance.setProgress(sbfilter_distance.getProgress() - 1);

                    return true;
                }*//*

            }*/
        }

        return false;
    }

    private void callPhone() {
        try{
        String phoneNo = (String )tvcall.getTag();
        if (phoneNo!=null && phoneNo.length()>0){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNo));
            MapsActivity.this.startActivity(callIntent);
        }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private void checkReadPhoneStatePermissions() {
        try {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                callPhone();
            }
            //do call
            else {

                if (ContextCompat.checkSelfPermission(MapsActivity.this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_REQUEST1);
                    Log.d("CALL_PHONE", " permission requesting...");
                } else {
                    Log.d("CALL_PHONE", " permission suceess...");
                    callPhone();

                }

            }


        } catch (Exception e) {
            Log.d("CALL_PHONE exception", "exception occured ...........");
            e.printStackTrace();

        }

    }

   /* protected void onResume() {
        super.onResume();

        handle.setOnTouchListener(new View.OnTouchListener() {
            *//* Starting Y point (where touch started). *//*
            float yStart = 0;

            *//* Default height when in the open state. *//*
            float closedHeight = 300;

            *//* Default height when in the closed state. *//*
            float openHeight = 600;

            *//* The height during the transition (changed on ACTION_MOVE). *//*
            float currentHeight;

            *//* The last y touch that occurred. This is used to determine if the view should snap up or down on release.
             * Used in conjunction with directionDown boolean. *//*
            float lastY = 0;
            boolean directionDown = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {

					*//* User tapped down on screen. *//*
                    case MotionEvent.ACTION_DOWN:
                        // User has tapped the screen
                        yStart = event.getRawY();
                        lastY = event.getRawY();
                        currentHeight = slideDownView.getHeight();
                        break;

					*//* User is dragging finger. *//*
                    case MotionEvent.ACTION_MOVE:

                        // Calculate the total height change thus far.
                        float totalHeightDiff = event.getRawY() - yStart;

                        // Adjust the slide down height immediately with touch movements.
                        ViewGroup.LayoutParams params = slideDownView.getLayoutParams();
                        params.height = (int)(currentHeight + totalHeightDiff);
                        slideDownView.setLayoutParams(params);

                        // Check and set which direction drag is moving.
                        if (event.getRawY() > lastY) {
                            directionDown = true;
                        } else {
                            directionDown = false;
                        }

                        // Set the lastY for comparison in the next ACTION_MOVE event.
                        lastY = event.getRawY();
                        break;

					*//* User lifted up finger. *//*
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:

						*//*
						 * Need to snap either up or down. Using ValueAnimator to "finish" the action.
						 * HeightEvaluator is a custom class.
						 *
						 * NOTE: I'm using the nineoldandroids library for
						 *//*
                        if (directionDown) {

                            // Open the sliding view.
                            int startHeight = slideDownView.getHeight();

                            ValueAnimator animation = ValueAnimator.ofObject(
                                    new HeightEvaluator(slideDownView),
                                    startHeight,
                                    (int) openHeight).setDuration(300);

                            // See Table 3 for other interpolator options
                            // - http://developer.android.com/guide/topics/graphics/prop-animation.html
                            animation.setInterpolator(new OvershootInterpolator(1));
                            animation.start();

                        } else {

                            // Close the sliding view.
                            int startHeight = slideDownView.getHeight();
                            ValueAnimator animation = ValueAnimator.ofObject(
                                    new HeightEvaluator(slideDownView),
                                    startHeight,
                                    (int) closedHeight).setDuration(300);
                            animation.setInterpolator(new OvershootInterpolator(1));
                            animation.start();
                        }
                        break;

                }
                return true;
            }
        });
    }
*/

   }
