package within30.com;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import within30.com.bottomsheet.BottomSheetLayout;
import within30.com.dataobjects.BookSlotDO;
import within30.com.dataobjects.CustomerDO;
import within30.com.dataobjects.LocationDO;
import within30.com.dataobjects.ServicesDO;
import within30.com.dataobjects.UserDO;
import within30.com.filters.FilterDistance;
import within30.com.filters.FilterTime;

import within30.com.location.GetAccurateLocationApplication;
import within30.com.location.LocationManagerInterface;
import within30.com.location.MyLocationManager;
import within30.com.session.SessionManager;
import within30.com.sidemenu.fragment.ContentFragment;
import within30.com.sidemenu.interfaces.Resourceble;
import within30.com.sidemenu.interfaces.ScreenShotable;
import within30.com.utilities.CalendarUtils;
import within30.com.utilities.NetworkUtility;
import within30.com.utilities.W30Constants;
import within30.com.utilities.W30Database;
import within30.com.utilities.W30Utilities;
import within30.com.webservices.Response;
import within30.com.webservices.businesslayer.CommonBL;
import within30.com.webservices.businesslayer.DataListener;

import static android.graphics.Color.TRANSPARENT;



public class MapsActivity extends BaseActivity  implements  OnMapReadyCallback, LocationListener ,DataListener, ScreenShotable,View.OnClickListener,SeekBar.OnSeekBarChangeListener ,View.OnTouchListener,LocationManagerInterface {
    public static boolean is_app_running = false;
    private String slotBokedTimePlusDefaultDutaion;
    private GoogleMap mMap;
    CommonBL commonBL = null;


    double mLatitude=0;
    double mLongitude=0;

    HashMap<String, CustomerDO> mMarkerPlaceLink = new HashMap<String, CustomerDO>();
    List<CustomerDO> placesList = new ArrayList<CustomerDO>();
    List<ServicesDO> servicesList;
    Animation bottomUp;
    Animation bottomDown;
    TextView bt_book;
    BottomSheetLayout homeLayout;
    String actionbarTitle = "";
    String service_id = "";
    ActionBar actionBar;
    within30.com.lib.VerticalSeekBar sbfilter_distance;
    within30.com.lib.VerticalSeekBar sbfilter_time;
    TextView tvComapanyName;
    RatingBar ratingbar;
    TextView tvcount;
    TextView tvmiles;
    TextView tvtime;
    TextView tvaddress1;

    private float distanceSelectedRadius = 30;
    private float timeSelectedRadius = 20;
    private boolean isDistanceFilterSelected = false;
    private boolean isTimeFilterSelected = false;


    private static final int LOCATION_REQUEST=3;

    private static  final int CALL_REQUEST1 = 5;

    LocationDO locationDO = null;

    public ActionBarDrawerToggle drawerToggle;

    TextView tvcall,tvsave,tvwebsite,tvcall_show,tv_web_show,tvTimings,tv_floating_distance,tv_floating_time;
    protected BottomSheetLayout bottomSheetLayout;
    public  Marker selectedMarker = null;
    LocationDO selectedLocation = null;
    LocationDO currentLocation = null;
    String pushMessageType = "";

    private LinearLayout mapmainlayout;
    //Location
    public MyLocationManager mLocationManager;
    private static final int REQUEST_FINE_LOCATION = 1;
    private Activity mCurrentActivity;
    private boolean isLocationReadRequest = false;
    public boolean isWebServiceRunning = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String subdomain = intent.getExtras().getString("subdomain");
            pushMessageType =  intent.getExtras().getString("pushMessageType");
            for(CustomerDO customerDO:placesList){
                if(subdomain.equalsIgnoreCase(customerDO.getSubdomain())){
                    //Call getCustomerInfo here
                    if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
                        if (selectedLocation == null || (selectedLocation.getLatitude() == 0 || selectedLocation.getLongitude() == 0)) {

                            if (mLocationManager != null) {
                                isLocationReadRequest = true;
                                mLocationManager.startLocationFetching();

                            }
                            else{
                                initLocationFetching(mCurrentActivity);
                            }
                        }else{
                            // clear data
                            CustomerDO customerDOTemp = new CustomerDO();
                            customerDOTemp.setLatitude(selectedLocation.getLatitude());
                            customerDOTemp.setLongitude(selectedLocation.getLongitude());
                            customerDOTemp.setServiceId(service_id);
                            customerDOTemp.setSubdomain(subdomain);
                            customerDOTemp.setMiles(W30Constants.MILES);
                            customerDOTemp.setMinutes(W30Constants.MINITUS);
                            String userId = new SessionManager(MapsActivity.this).getUserid();
                            customerDOTemp.setUserId(userId);
                            if(commonBL.getCustomerInfo(customerDOTemp)){
                                isWebServiceRunning = true;
                            }
                        }
                    }else{
                        showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                    }
                }
            }
        }

    };


    public void initialize(){
        homeLayout = (BottomSheetLayout) inflater.inflate(R.layout.activity_maps, null);
        llBody.addView(homeLayout);
        mCurrentActivity = MapsActivity.this;
        commonBL = new CommonBL(MapsActivity.this,MapsActivity.this);
        actionBar = getSupportActionBar();
        intilizeControls();
        setActionBar();
        IntentFilter filter = new IntentFilter();
        filter.addAction("within30.com.USER_ACTION");
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


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
        bottomUp = AnimationUtils.loadAnimation(this,R.anim.bottom_up);
        bottomDown = AnimationUtils.loadAnimation(this,R.anim.bottom_down);

        sbfilter_distance = (within30.com.lib.VerticalSeekBar)homeLayout.findViewById(R.id.sbfilter_distance);
        sbfilter_time = (within30.com.lib.VerticalSeekBar) homeLayout.findViewById(R.id.sbfilter_time);

        sbfilter_distance.setOnSeekBarChangeListener(this);
        sbfilter_time.setOnSeekBarChangeListener(this);

        tv_floating_distance = (TextView) homeLayout.findViewById(R.id.tv_floating_distance);
        tv_floating_time = (TextView)homeLayout.findViewById(R.id.tv_floating_time);
        tv_floating_time.setOnTouchListener(new FilterTime(sbfilter_time,sbfilter_distance,placesList));
        tv_floating_distance.setOnTouchListener(new FilterDistance(sbfilter_distance, sbfilter_time, placesList));
        mapmainlayout = (LinearLayout) homeLayout.findViewById(R.id.mapmainlayout);


        try{
            initilizeMap();
        }catch(Exception e){
            e.printStackTrace();
        }

        if (mMap!=null) {
          //  mMap.setMyLocationEnabled(true);

            // create class object
            try{
                if (getIntent().hasExtra("selectedLocation")) { // selected location
                    locationDO = (LocationDO)getIntent().getExtras().getSerializable("selectedLocation");
                    selectedLocation = locationDO;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                if (getIntent().hasExtra("service_id")) {
                    service_id = getIntent().getStringExtra("service_id");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            isLocationReadRequest = true;
            initLocationFetching(mCurrentActivity);
           /* try{
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    gpsTracker = new GPSTracker(MapsActivity.this,MapsActivity.this);
                }else{
                    if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                        Log.d("ACCESS_FINE_LOCATION"," permission requesting...");
                    } else {
                        Log.d("ACCESS_FINE_LOCATION"," permission suceess...");
                        gpsTracker = new GPSTracker(MapsActivity.this,MapsActivity.this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (gpsTracker!=null){
                getGPSTrackerInfo();
            }else{
                Log.d("GPS traker null","unable to get the location coz android latest version ...........");
            }*/
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });
            mapmainlayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // closing menu when opened
                   /* linearLayout.removeAllViews();
                    linearLayout.invalidate();*/
                    return false;
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

                        CustomerDO marker_customerInfo = mMarkerPlaceLink.get(marker.getId());
                        //  Log.d("selected_marker_customer_info",  marker_customerInfo.toString());
                        if (marker_customerInfo != null) {
                            if (marker_customerInfo.getSlotBookedAt()!=null && marker_customerInfo.getSlotBookedAt().length()>0) {
                                selectedMarker = marker;
                                View child = LayoutInflater.from(MapsActivity.this).inflate(R.layout.place_details_and_booking, bottomSheetLayout, false);
                                setCustomerInfo(marker,marker_customerInfo,child,true);
                               // bottomSheetLayout.showWithSheetView(child);
                            }else if (CalendarUtils.getComparisionCurrentTimeWithNextslotAt1(marker_customerInfo)) {
                                selectedMarker = marker;
                                View child = LayoutInflater.from(MapsActivity.this).inflate(R.layout.place_details_and_booking, bottomSheetLayout, false);
                                setCustomerInfo(marker,marker_customerInfo,child,true);
                               // bottomSheetLayout.showWithSheetView(child);
                            }else {
                                selectedMarker = marker;
                                if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
                                    if (selectedLocation == null || (selectedLocation.getLatitude() == 0 || selectedLocation.getLongitude() == 0)) {
                                       // getGPSTrackerInfo();
                                        if (mLocationManager != null) {
                                            isLocationReadRequest = true;
                                            mLocationManager.startLocationFetching();
                                        }
                                        else{
                                            initLocationFetching(mCurrentActivity);
                                        }
                                    }else{
                                        // clear data
                                        CustomerDO customerDO = new CustomerDO();
                                        customerDO.setLatitude(selectedLocation.getLatitude());
                                        customerDO.setLongitude(selectedLocation.getLongitude());
                                        customerDO.setServiceId(marker_customerInfo.getServiceId());
                                        customerDO.setSubdomain(marker_customerInfo.getSubdomain());
                                        customerDO.setMiles(W30Constants.MILES);
                                        customerDO.setMinutes(W30Constants.MINITUS);
                                        String userId = new SessionManager(MapsActivity.this).getUserid();
                                        customerDO.setUserId(userId);
                                        if(commonBL.getCustomerInfo(customerDO)){
                                            isWebServiceRunning = true;
                                            if (pd == null) {
                                                pd =  new ProgressDialog(MapsActivity.this);
                                                pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                                                pd.setMessage("Loading...");
                                                pd.show();
                                            }
                                        }else{
                                            // showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                                        }
                                    }
                                }else{
                                    showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                                }
                            }//end else

                        }
                        marker.hideInfoWindow();
                       // marker.
                        return true;
                    }
                });
            }
        }



        homeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (bottomSheetLayout.isSheetShowing()) {
                    bottomSheetLayout.dismissSheet();
                }
                // closing menu when opened
                linearLayout.removeAllViews();
                linearLayout.invalidate();
                return false;
            }
        });
        getIntentData();
    }

    private void getIntentData() {
        // Getting place reference from the map
        if ( getIntent()!=null) {

            try{
                if (getIntent().hasExtra("actionbar_title")) {
                    actionbarTitle = getIntent().getStringExtra("actionbar_title");
                    tvTitle.setText(actionbarTitle);
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

                    if (servicesList!=null) {
                        createMenuList(servicesList);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }


        }


    }



    public void getGPSTrackerInfo(double lat,double lng){
            isLocationReadRequest = false;
                LocationDO locationDOTemp = new LocationDO();

                currentLocation = locationDOTemp;
                mLatitude = lat;
                mLongitude = lng;
                currentLocation.setLatitude(mLatitude);
                currentLocation.setLongitude(mLongitude);
                currentLocation.setCity(new W30Utilities().getCityName(mLatitude, mLongitude, MapsActivity.this));
                if (selectedLocation != null){
                 //   selectedLocation = currentLocation;
                    if ((placesList == null || placesList.size() == 0) && !isWebServiceRunning) {
                        try{
                            getServices();
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                   /* if(isLocationChanged(currentLocation,selectedLocation)){

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle("Location");
                        String message ="You have selected location as "+ selectedLocation.getCity()+"."+" Do you want to continue or change to your current location?";
                        alertDialog.setMessage(message);
                        alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                selectedLocation = currentLocation;
                                try{
                                    getServices();
                                }catch(Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        alertDialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                                try{
                                    getServices();
                                }catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    }else{
                        selectedLocation = currentLocation;
                        if ((placesList == null || placesList.size() == 0) && !isWebServiceRunning) {
                            try{
                                getServices();
                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } // End else*/
                }else{
                    selectedLocation = currentLocation;
                    if ((placesList == null || placesList.size() == 0) && !isWebServiceRunning) {
                        try{
                            getServices();
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


               // new SessionManager(MapsActivity.this).setLocationChanged(false);

    } // End getGPSTrackerInfo()

    private boolean isLocationChanged(LocationDO currentLocation, LocationDO selectedLocation) {
        try {
            if (!currentLocation.getCity().toString().equalsIgnoreCase(selectedLocation.getCity().toString())) {
                return true;
            } else {
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void animateLatLngZoom(LatLng latlng, float reqZoom, int offsetX, int offsetY) {

        // Save current zoom
        float originalZoom = mMap.getCameraPosition().zoom;

        // Move temporarily camera zoom
        mMap.moveCamera(CameraUpdateFactory.zoomTo(reqZoom));

        Point pointInScreen = mMap.getProjection().toScreenLocation(latlng);

        Point newPoint = new Point();
        newPoint.x = pointInScreen.x + offsetX;
        newPoint.y = pointInScreen.y + offsetY;

        LatLng newCenterLatLng = mMap.getProjection().fromScreenLocation(newPoint);

        // Restore original zoom
        mMap.moveCamera(CameraUpdateFactory.zoomTo(originalZoom));

        // Animate a camera with new latlng center and required zoom.
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newCenterLatLng, reqZoom));

    }

    private void setCustomerInfo(final Marker selectedMarkerTemp1,final CustomerDO customerDO,View child,boolean showBottomShet){
       final Marker selectedMarkerTemp = selectedMarkerTemp1;

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

            tvcall_show  = (TextView) child.findViewById(R.id.tvcall_show);
            tv_web_show  = (TextView) child.findViewById(R.id.tv_web_show);
            tvTimings  = (TextView) child.findViewById(R.id.tvTimings);
            FrameLayout frame_slide_details_view = (FrameLayout) child.findViewById(R.id.frame_slide_details_view);
            final TextView slide_details_view = (TextView)child.findViewById(R.id.slide_details_view);
            String upperCaseString_companyName = customerDO.getFullName().substring(0, 1).toUpperCase() + customerDO.getFullName().substring(1);
            tvComapanyName.setText(upperCaseString_companyName);
            // to set margins to the textview company name
            String address = customerDO.getGeo().getAddress().toString();

            String addressTemp = address.replaceFirst("^,","");
            String addressTemp1 = addressTemp.replaceAll(", $", "");

            tvaddress1.setText(addressTemp1);
            double newKB = Math.round(customerDO.getRating()*10.0)/10.0;
            ratingbar.setRating((float)newKB);
            try{
                tvcount.setText(" "+"("+customerDO.getRatingCount()+")");
            }catch(Exception e) {
                e.printStackTrace();
            }


            if(showBottomShet)
                bottomSheetLayout.showWithSheetView(child);

            child.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("1","child.setOnTouchListener");
                    setselectedMarkerIcon(customerDO, true, selectedMarkerTemp);
                    if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.EXPANDED)) {
                        slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slidedown));

                    } else if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.PREPARING) || bottomSheetLayout.getState().equals(BottomSheetLayout.State.PEEKED)) {
                       slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slideup));
                    }else if(bottomSheetLayout.getState().equals(BottomSheetLayout.State.HIDDEN)){
                        selectedMarker = null;
                    }
                    return true;
                }
            });

            bottomSheetLayout.addOnSheetStateChangeListener(new BottomSheetLayout.OnSheetStateChangeListener() {
                @Override
                public void onSheetStateChanged(BottomSheetLayout.State state) {
                    Log.d("2","bottomSheetLayout.addOnSheetStateChangeListener");
                    if (state.equals(BottomSheetLayout.State.PEEKED)) {
                        Log.d("2.1","frame_slide_details_view.setOnClickListener");
                        slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slideup));
                        double[] coordinates = customerDO.getGeo().getCoordinates();
                        double lng = coordinates[0];
                        double lat = coordinates[1];
                        float currentZoomLevel =  mMap.getCameraPosition().zoom;
                        if (currentZoomLevel >= 11) {
                            animateLatLngZoom(new LatLng(lat, lng), currentZoomLevel, 0, 0);
                        }else {
                            animateLatLngZoom(new LatLng(lat, lng), 11, 0, 0);
                        }
                        setselectedMarkerIcon(customerDO,true,selectedMarkerTemp);


                    } else if (state.equals(BottomSheetLayout.State.EXPANDED)) {
                        Log.d("2.2","frame_slide_details_view.setOnClickListener");
                        slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slidedown));
                        float currentZoomLevel =  mMap.getCameraPosition().zoom;

                        double[] coordinates = customerDO.getGeo().getCoordinates();
                        double lng = coordinates[0];
                        double lat = coordinates[1];
                        if (currentZoomLevel >= 11) {
                            animateLatLngZoom(new LatLng(lat, lng), currentZoomLevel, 50, 200);
                        }else {
                            animateLatLngZoom(new LatLng(lat, lng), 11, 50, 200);
                        }
                        setselectedMarkerIcon(customerDO,true,selectedMarkerTemp);

                    }else if (state.equals(BottomSheetLayout.State.HIDDEN) ){
                        Log.d("2.3","frame_slide_details_view.setOnClickListener");
                        setselectedMarkerIcon(customerDO, false, selectedMarkerTemp);
                        selectedMarker = null;
                    }
                }
            });
            frame_slide_details_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("3","frame_slide_details_view.setOnClickListener");
                    try{
                        setselectedMarkerIcon(customerDO,true,selectedMarkerTemp);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.PEEKED)) {
                        Log.d("3.1","frame_slide_details_view.setOnClickListener");
                        bottomSheetLayout.setState(BottomSheetLayout.State.EXPANDED);
                        bottomSheetLayout.expandSheet();
                        slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slidedown));
                        double[] coordinates = customerDO.getGeo().getCoordinates();
                        double lng = coordinates[0];
                        double lat = coordinates[1];
                        float currentZoomLevel = mMap.getCameraPosition().zoom;
                        if (currentZoomLevel >= 11) {
                            animateLatLngZoom(new LatLng(lat, lng), currentZoomLevel, 50, 200);
                        }else {
                            animateLatLngZoom(new LatLng(lat, lng), 11, 50, 200);
                        }

                    }else if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.EXPANDED)) {
                        Log.d("3.2","frame_slide_details_view.setOnClickListener");

                        bottomSheetLayout.setState(BottomSheetLayout.State.PEEKED);
                        bottomSheetLayout.peekSheet();
                         slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slideup));
                        CameraPosition oldPos = mMap.getCameraPosition();
                        CameraPosition pos = CameraPosition.builder(oldPos).bearing(5).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));

                        double[] coordinates = customerDO.getGeo().getCoordinates();
                        double lng = coordinates[0];
                        double lat = coordinates[1];
                        float currentZoomLevel = mMap.getCameraPosition().zoom;
                        if (currentZoomLevel >= 11) {
                            animateLatLngZoom(new LatLng(lat, lng), currentZoomLevel, 0, 0);
                        }else {
                            animateLatLngZoom(new LatLng(lat, lng), 11, 0, 0);
                        }
                    }else if(bottomSheetLayout.getState().equals(BottomSheetLayout.State.HIDDEN)){
                        Log.d("3.3","frame_slide_details_view.setOnClickListener");
                        selectedMarker = null;
                    }
                }
            });
            //Detect double tap on bottomSheetLayout
            bottomSheetLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("4","bottomSheetLayout.setOnTouchListener");
                    gestureDetector.onTouchEvent(event);
                    return false;
                }
                private GestureDetector gestureDetector = new GestureDetector(MapsActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        Log.d("5","gestureDetector");
                        setselectedMarkerIcon(customerDO,true,selectedMarkerTemp);
                        if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.PEEKED)) {

                            bottomSheetLayout.setState(BottomSheetLayout.State.EXPANDED);
                            bottomSheetLayout.expandSheet();
                            slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slidedown));
                            double[] coordinates = customerDO.getGeo().getCoordinates();
                            double lng = coordinates[0];
                            double lat = coordinates[1];
                            float currentZoomLevel = mMap.getCameraPosition().zoom;
                            if (currentZoomLevel >= 11) {
                                animateLatLngZoom(new LatLng(lat, lng), currentZoomLevel, 50, 200);
                            }else {
                                animateLatLngZoom(new LatLng(lat, lng), 11, 50, 200);
                            }

                        }else if (bottomSheetLayout.getState().equals(BottomSheetLayout.State.EXPANDED)) {
                            bottomSheetLayout.setState(BottomSheetLayout.State.PEEKED);
                            bottomSheetLayout.peekSheet();
                            slide_details_view.setBackground(getResources().getDrawable(R.mipmap.icon_slideup));
                            CameraPosition oldPos = mMap.getCameraPosition();
                            CameraPosition pos = CameraPosition.builder(oldPos).bearing(5).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));

                            double[] coordinates = customerDO.getGeo().getCoordinates();
                            double lng = coordinates[0];
                            double lat = coordinates[1];
                            float currentZoomLevel = mMap.getCameraPosition().zoom;
                            if (currentZoomLevel >= 11) {
                                animateLatLngZoom(new LatLng(lat, lng), currentZoomLevel, 0, 0);
                            }else {
                                animateLatLngZoom(new LatLng(lat, lng), 11, 0, 0);
                            }
                        }else if(bottomSheetLayout.getState().equals(BottomSheetLayout.State.HIDDEN)){
                            selectedMarker = null;
                        }
                        return super.onDoubleTap(e);
                    }

                });
            });
            StringBuilder messageStr = new StringBuilder();
            if (customerDO.getSlotBookedAt() != null && customerDO.getSlotBookedAt().length()>0){

                messageStr.append("Slot Booked For:");
                messageStr.append(" ");
                if (!CalendarUtils.checkSlotDateIsToday(customerDO.getTimeZone(),customerDO.getSlotBookedDate())) {
                    messageStr.append(customerDO.getSlotBookedDate());
                    messageStr.append(" ");
                }
                messageStr.append(customerDO.getSlotBookedAt());
                messageStr.append(" ");
              //  tvtime.setText("Slot Booked For :" + " " + (customerDO.getSlotBookedAt()) + " " + "Mins");
            }else if ((customerDO.getNextSlotAt() !=null && customerDO.getNextSlotAt().length()>0)
                    && (customerDO.getNextSlotDate()!=null && customerDO.getNextSlotDate().length()>0)) {

                messageStr.append("Next Slot At:");
                messageStr.append(" ");
                if (!CalendarUtils.checkSlotDateIsToday(customerDO.getTimeZone(),customerDO.getNextSlotDate())) {
                   // messageStr.append(customerDO.getNextSlotDate());
                    messageStr.append(CalendarUtils.nextSlotDate(customerDO.getNextSlotDate()));
                    messageStr.append(" ");
                }
                messageStr.append(customerDO.getNextSlotAt());
                messageStr.append(" ");
            }
           // messageStr.append("Mins");
            tvtime.setText(messageStr.toString());
            BigDecimal miles;
            miles= W30Utilities.round(customerDO.getDestinationDistance(),2);
            tvmiles.setText(miles + " " + "Miles");
            if(customerDO.getMobile() != null && customerDO.getMobile().length() > 0) {
                // phone number exists.
                tvcall.setTag(customerDO);
                tvcall.setAlpha(1f);
                tvcall.setClickable(true);

            }else { // phone number not exists
                tvcall.setAlpha(0.5f);
                tvcall.setClickable(false);

            }
            if(customerDO.getLogoUrl()!=null && customerDO.getLogoUrl().length()>0){
                tvwebsite.setTag(customerDO.getLogoUrl());

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
               if (customerDO.getSlotBookedAt()!=null && customerDO.getSlotBookedAt().length()>0) {
                       int defaultDuration = customerDO.getDefaultDuration();
                       slotBokedTimePlusDefaultDutaion = CalendarUtils.getTimeAddedDefaultDeutation(customerDO.getSlotBookedAt(),
                               defaultDuration,customerDO.getTimeZone());
                       bt_book.setVisibility(View.VISIBLE);
                       bt_book.setClickable(false);
                       bt_book.setAlpha(0.5f);

            }else { // If user not booked any slot
                if (customerDO.getNextSlotAt() != null && customerDO.getNextSlotAt().length() > 0) {
                    bt_book.setVisibility(View.VISIBLE);
                    bt_book.setClickable(true);
                    bt_book.setTag(customerDO);
                    bt_book.setAlpha(1f);

                }
            }

            bt_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                       final CustomerDO customerDO =(CustomerDO) bt_book.getTag();
                        if (customerDO !=null) {

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
                            Log.d("url->",url);
                            // Tracking Event
                            SessionManager.getInstance().trackEvent("Website", url, customerDO.getBusinessType() + " " + customerDO.toString());
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
    public static boolean slotBooked = false;
    private void setselectedMarkerIcon(CustomerDO customerDO, boolean isMarkerSelected,Marker selectedMarkerTemp1) {
        Log.d("7","setselectedMarkerIcon");
        try {
            if (customerDO !=null) {
                if (customerDO.getSlotBookedAt().length()>0){// checked in
                    //set customer details view height
                    slotBooked = true;
                    if (customerDO.isPremium()) {
                        if (isMarkerSelected) {
                            selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_slot_booked_selected));
                        }else{
                            selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_slot_booked));
                        }
                    }else {
                        if (isMarkerSelected) {
                            selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_slot_booked_selected));
                        }else{
                            selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_slot_booked));
                        }
                    }
                }else{
                    slotBooked = false;
                    if (customerDO.getNextSlotAt()!=null && customerDO.getNextSlotAt().length()>0) {
                        if(CalendarUtils.getComparisionCurrentTimeWithNextslotAt(customerDO,(int)timeSelectedRadius)){
                            if (customerDO.isPremium()) {
                                if (isMarkerSelected) {
                                    selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_slots_avilable_selected));
                                }else {
                                    selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_slots_avilable));
                                }
                            }else {
                                if (isMarkerSelected) {
                                    selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_selected));
                                }else {
                                    selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker));
                                }
                            }
                        }else {
                            if (customerDO.isPremium()) {
                                if (isMarkerSelected) {
                                    selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_check_nextslot_selected));
                                }else {
                                    selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_check_nextslot));
                                }
                            }else {
                                if (isMarkerSelected) {
                                    selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_check_nextslot_selected));
                                }else {
                                    selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_check_nextslot));
                                }
                            }
                        }
                        }else {

                                if (customerDO.isPremium()) {
                                    if (isMarkerSelected) {
                                        selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_check_nextslot_selected));
                                    }else {
                                        selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_check_nextslot));
                                    }
                                }else {
                                    if (isMarkerSelected) {
                                        selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_check_nextslot_selected));
                                    }else {
                                        selectedMarkerTemp1.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_check_nextslot));
                                    }
                                }
                        }

                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bookSlot( CustomerDO customerDO ){

        if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
            BookSlotDO bookSlotDO = new BookSlotDO();
            UserDO userDO = new SessionManager(MapsActivity.this).getUserInfo();
            String userID = new SessionManager(MapsActivity.this).getUserid();
            String date = customerDO.getNextSlotDate()+" "+customerDO.getNextSlotAt();
            bookSlotDO.setDate(date);
            bookSlotDO.setSubDomain(customerDO.getSubdomain());
            bookSlotDO.setEmail(userDO.getEmail());
            String phnoTemp = userDO.getMobile().toString().replaceAll("[()-]","");
            bookSlotDO.setMobile(phnoTemp);
            bookSlotDO.setUserId(userID);
            if(commonBL.bookSlot(bookSlotDO)){
                isWebServiceRunning = true;
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
        if (placesList != null && placesList.size()>0 ) {
            // Clears all the existing markers
            mMap.clear();
            if (selectedLocation!=null) {
                setSelectedLOcationMarker(selectedLocation.getLatitude(),selectedLocation.getLongitude());
            }
            addCircleToMap(30);
            sbfilter_distance.setProgress((int) distanceSelectedRadius);
            sbfilter_time.setProgress((int) timeSelectedRadius);
        }else{
            if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
                if (selectedLocation != null) {

                    // clear data
                    placesList.clear();
                    new SessionManager(MapsActivity.this).saveUserSelectedLocationInfo(selectedLocation);
                    CustomerDO customerDO = new CustomerDO();
                    customerDO.setLatitude(selectedLocation.getLatitude());
                    customerDO.setLongitude(selectedLocation.getLongitude());

                    customerDO.setMiles(W30Constants.MILES);
                    customerDO.setMinutes(W30Constants.MINITUS);
                    try{
                        if (getIntent().hasExtra("service_id")) {
                            service_id = getIntent().getStringExtra("service_id");
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    customerDO.setServiceId(service_id);
                    String userId = new SessionManager(MapsActivity.this).getUserid();
                    customerDO.setUserId(userId);
                    setSelectedLOcationMarker(customerDO.getLatitude(),customerDO.getLongitude());
                    if(commonBL.getCustomers(customerDO)){
                        isWebServiceRunning = true;
                        if (pd == null) {
                            pd =  new ProgressDialog(MapsActivity.this);
                            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                            pd.setMessage("Loading...");
                            pd.show();
                        }
                    }
                }
            }
        }
    }

    private void setSelectedLOcationMarker(double lat,double lng) {


        LatLng latLngTemp = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        // Setting the position for the marker
        markerOptions.position(latLngTemp);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_currentlocation));

        Marker m = mMap.addMarker(markerOptions);
        CustomerDO customerDO = null;
        mMarkerPlaceLink.put(m.getId(), customerDO);
    }

    private void  showCustomDialog(Context context,String timeZone,String selecteddate,String startTime){
       // custom dialog
       final Dialog dialog = new Dialog(context);
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       dialog.setContentView(R.layout.custom_dialog_booking);
       TextView tvEstimatedTime = (TextView) dialog.findViewById(R.id.tvEstimatedTime);
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
       StringBuilder tempStr = new StringBuilder();
       tempStr.append("See you at");
       tempStr.append(" ");
       if(!CalendarUtils.checkSlotDateIsToday(timeZone,selecteddate)) {
           try{
               DateFormat selectedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
               DateFormat showDareFormat = new SimpleDateFormat("MM-dd");
               Date showDate = selectedDateFormat.parse(selecteddate);
               String showDateStr = showDareFormat.format(showDate);
               tempStr.append(showDateStr);
               tempStr.append(" ");
           }catch(Exception e) {
               e.printStackTrace();;
           }
       }
       tempStr.append(startTime);
       tvEstimatedTime.setText(tempStr);

       // if button is clicked, close the custom dialog
       dialogButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
               placesList.clear();
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
      //  mMap = googleMap;

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
       if (mMap == null) {
           mMap = ((MapFragment) getFragmentManager().findFragmentById(
                   R.id.map)).getMap();
           /* SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);*/

           // check if map is created successfully or not
           if (mMap == null) {
               Toast.makeText(getApplicationContext(),
                       "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                       .show();
           }
       }
    }
    boolean isRedirectedWhenNoCustomers = false;

    @Override
    public void dataRetreived(Response data) {
       // llLoader.setVisibility(View.GONE);
        isWebServiceRunning = false;
        try{
            if (pd != null)
                if (pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }
        }catch(Exception e) {
            e.printStackTrace();
        }

        if(data != null && data.data != null) {
            hideLoader();

            switch (data.method) {
                case WS_CUSTOMERS:
                    if(data.data!=null && data.data instanceof List<?>)
                    {
                        isRedirectedWhenNoCustomers = false;
                        if (placesList != null && placesList.size()>0) {
                            placesList.clear();
                        }
                       placesList = (List<CustomerDO>)data.data;
                        // Clears all the existing markers
                        try{
                            mMap.clear();
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }


                        addCircleToMap((int)distanceSelectedRadius);
                        addCircleToMap((int)timeSelectedRadius);
                        sbfilter_distance.setProgress((int) distanceSelectedRadius);
                        sbfilter_time.setProgress((int) timeSelectedRadius);

                    }else if(data.data!=null && data.data instanceof LocationDO ){
                        LocationDO redirectedLocation = (LocationDO) data.data;
                        if(!isRedirectedWhenNoCustomers){
                            isRedirectedWhenNoCustomers = true;
                            selectedLocation = redirectedLocation;
                            new SessionManager(MapsActivity.this).setLocationChanged(true);
                            new SessionManager(MapsActivity.this).saveUserSelectedLocationInfo(selectedLocation);
                            mMap.clear();
                            if (selectedLocation!=null){
                                setSelectedLOcationMarker(selectedLocation.getLatitude(),selectedLocation.getLongitude());
                                if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
                                    if (selectedLocation !=null){
                                        if (selectedLocation.getLatitude() != 0 && selectedLocation.getLongitude() != 0) {

                                            CustomerDO customerDO = new CustomerDO();
                                            customerDO.setLatitude(selectedLocation.getLatitude());
                                            customerDO.setLongitude(selectedLocation.getLongitude());
                                            customerDO.setMiles(W30Constants.MILES);
                                            customerDO.setMinutes(W30Constants.MINITUS);
                                            customerDO.setServiceId(service_id);
                                            String userId = new SessionManager(MapsActivity.this).getUserid();
                                            customerDO.setUserId(userId);
                                            setSelectedLOcationMarker(customerDO.getLatitude(),customerDO.getLongitude());
                                            if(commonBL.getCustomers(customerDO))
                                                isWebServiceRunning = true;
                                        }
                                    }
                                }else{
                                    showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
                                    addCircleToMap((int)distanceSelectedRadius);
                                    addCircleToMap((int)timeSelectedRadius);

                                    sbfilter_distance.setProgress((int) distanceSelectedRadius);
                                    sbfilter_time.setProgress((int) timeSelectedRadius);
                                }
                            }

                            if (!isRefeshEnabled) {
                                showToast(selectedLocation.getMessage());
                                showToast("There seem to be no businesses in your area currently." +
                                        "Redirecting to the "+" "+selectedLocation.getCity()+""+" available businesses.");
                            }

                        }else{
                            addCircleToMap((int)distanceSelectedRadius);
                            addCircleToMap((int)timeSelectedRadius);

                            sbfilter_distance.setProgress((int) distanceSelectedRadius);
                            sbfilter_time.setProgress((int) timeSelectedRadius);
                        }



                    }
                    break;
                case WS_BOOKSLOT:
                    if(data.data!=null && data.data instanceof JSONObject ){
                        JSONObject jsonObject = (JSONObject)data.data;

                        CustomerDO customerDO =(CustomerDO) bt_book.getTag();
                        if (customerDO !=null) {
                            // Tracking Event
                            SessionManager.getInstance().trackEvent("Schedule","Slot Booked", customerDO.getBusinessType()+" "+customerDO.toString());
                            saveBookedInfo(customerDO);
                        }
                        try{
                            String selecteddate = jsonObject.getString("selecteddate");
                            String startTime = jsonObject.getJSONObject("data").getString("startTime");
                            showCustomDialog(MapsActivity.this,customerDO.getTimeZone(),selecteddate,startTime);
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    } else if (data.data!=null && data.data instanceof String){
                        String str = (String) data.data;
                        showToast(str);
                    }
                    break;
                case WS_GETCUSTOMERINFO:
                    if(data.data!=null && data.data instanceof List<?> ){
                        List<CustomerDO> customerDOListTemp = (List<CustomerDO>)data.data;
                        CustomerDO customerDO = null;
                       for (CustomerDO customerDOTemp:customerDOListTemp) {
                           customerDO = customerDOTemp;
                       }
                        if (customerDO!=null) {
                            View child = LayoutInflater.from(MapsActivity.this).inflate(R.layout.place_details_and_booking, bottomSheetLayout, false);
                            try{

                                    if (selectedMarker !=null) {
                                        setCustomerInfo(selectedMarker,customerDO,child,true);
                                        //  setselectedMarkerIcon(customerDO, true, selectedMarker);
                                        //  bottomSheetLayout.dismissSheet();
                                        //  bottomSheetLayout.showWithSheetView(child);
                                    }
                                if(pushMessageType.equalsIgnoreCase("newAppointment")){

                                    pushMessageType = "";
                                }
                                if (placesList.size()>0) {
                                    for (int i =0;i<placesList.size();i++) {
                                        CustomerDO customerDO1 = placesList.get(i);
                                        if(customerDO.getSubdomain().equalsIgnoreCase(customerDO1.getSubdomain())){
                                            mMap.clear();
                                            placesList.set(i,customerDO);
                                            addCircleToMap((int)distanceSelectedRadius);
                                            addCircleToMap((int)timeSelectedRadius);
                                            sbfilter_distance.setProgress((int) distanceSelectedRadius);
                                            sbfilter_time.setProgress((int) timeSelectedRadius);
                                        }
                                    }
                                }


                               /* if (selectedMarker !=null) {
                                    setCustomerInfo(selectedMarker,customerDO,child,true);
                                  //  setselectedMarkerIcon(customerDO, true, selectedMarker);
                                  //  bottomSheetLayout.dismissSheet();
                                  //  bottomSheetLayout.showWithSheetView(child);
                                }else if(pushMessageType.equalsIgnoreCase("newAppointment")){
                                    pushMessageType = "";
                                    if (placesList.size()>0) {
                                        for (int i =0;i<placesList.size();i++) {
                                            CustomerDO customerDO1 = placesList.get(i);
                                            if(customerDO.getSubdomain().equalsIgnoreCase(customerDO1.getSubdomain())){
                                                mMap.clear();
                                                placesList.set(i,customerDO);
                                                addCircleToMap((int)distanceSelectedRadius);
                                                addCircleToMap((int)timeSelectedRadius);
                                                sbfilter_distance.setProgress((int) distanceSelectedRadius);
                                                sbfilter_time.setProgress((int) timeSelectedRadius);
                                            }
                                        }
                                    }

                                }*/

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }



    private void saveBookedInfo(CustomerDO customerDO) {
        String currentDate = CalendarUtils.getCurrentPostDateForBookedSlot((long)customerDO.getDefaultDuration());
        customerDO.setSlotBookedDate(currentDate);
        W30Database w30Database = new W30Database(MapsActivity.this);
        w30Database.addBookedSlotsInfo(customerDO);

    }

    @Override
    public void takeScreenShot() {}

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public void onClick(View v) {}

/**
 * add circle to map
 *
 */
Circle circle;
   private void addCircleToMap(int radius ){
      // mMap.

       if (circle!=null) {
           circle.remove();
           circle = null;

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

                               double miles_server = customerDO.getDestinationDistance();


                               if ((miles_server <= radius)) {
                                   setMarker(customerDO);

                               }
                           } else if (isTimeFilterSelected) {
                               slider_in_meters  = distanceSelectedRadius*W30Constants.MILES_IN_METERS;

                               if ((customerDO.getDestinationDistance() <= distanceSelectedRadius)) {
                                   setMarker(customerDO);

                               }
                           }else{
                               setMarker(customerDO);
                           }
                       }
                   }else {
                   }
               }else {
               }
               if(selectedLocation!=null){
                   setSelectedLOcationMarker(selectedLocation.getLatitude(),selectedLocation.getLongitude());
               }
           }catch(Exception e){
               if(selectedLocation!=null){
                   setSelectedLOcationMarker(selectedLocation.getLatitude(),selectedLocation.getLongitude());
               }
               e.printStackTrace();
           }

           CircleOptions circleOptions = new CircleOptions()
                   .center(new LatLng(selectedLocation.getLatitude(),selectedLocation.getLongitude()))
                   .radius(slider_in_meters)
                   .strokeColor(getResources().getColor(R.color.circle_map))
                   .fillColor(0x95ffffff)
                   .strokeWidth(3f)
                   .visible(true);

           circle =  mMap.addCircle(circleOptions);
           float currentZoomLevel =  mMap.getCameraPosition().zoom;
           if (currentZoomLevel >= 11) {
               animateLatLngZoom(new LatLng(selectedLocation.getLatitude(),selectedLocation.getLongitude()), currentZoomLevel, 0, 0);
           }else {
               mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circleOptions.getCenter(),getZoomLevel(circle,slider_in_meters)));
           }
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
      /*  View custom_marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        TextView numTxt = (TextView) custom_marker.findViewById(R.id.tvtitle);*/

      //  numTxt.setVisibility(View.GONE);


            if (customerDO.getSlotBookedAt().length()>0){
                // checked in
                if (customerDO.isPremium()) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_slot_booked));
                }else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_slot_booked));
                }
            } else{
                if (customerDO.getNextSlotAt() !=null && (customerDO.getNextSlotAt().length() > 0))  {

                        if(CalendarUtils.getComparisionCurrentTimeWithNextslotAt(customerDO,(int)timeSelectedRadius)){
                            if (customerDO.isPremium()) {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_slots_avilable));
                            }else {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker));
                            }

                        }else {
                            if (customerDO.isPremium()) {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_check_nextslot));
                            }else {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_check_nextslot));
                            }
                        }
                } else {
                    if (customerDO.isPremium()) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_premium_check_nextslot));
                    }else {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_check_nextslot));
                    }
                }
           }
        Marker m = mMap.addMarker(markerOptions);
        mMarkerPlaceLink.put(m.getId(), customerDO);
    }

    public int getZoomLevel(Circle circle,double slider_radius) {
        int zoomLevel = 11;
        return zoomLevel;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.sbfilter_distance){
            sbfilter_distance.getThumb().mutate().setAlpha(0);
            int secondaryProgress = (60*sbfilter_time.getProgress())/50;
            sbfilter_distance.setSecondaryProgress(secondaryProgress);
            distanceSelectedRadius = sbfilter_distance.getProgress();
            isDistanceFilterSelected = true;
            isTimeFilterSelected = false;

            if (secondaryProgress >= distanceSelectedRadius) {
                sbfilter_distance.setProgressDrawable(getResources().getDrawable(R.drawable.progress_distance_filter_1));
            }else{
                sbfilter_distance.setProgressDrawable(getResources().getDrawable(R.drawable.progress_distance_filter));
            }
            addCircleToMap((int) distanceSelectedRadius);
            int floatingDistanceWidth = sbfilter_distance.getHeight()
                    - sbfilter_distance.getPaddingTop()
                    - sbfilter_distance.getPaddingBottom();
            int seekbarDistanceThumbPos = sbfilter_distance.getPaddingTop()
                    + floatingDistanceWidth
                    * sbfilter_distance.getProgress()
                    / sbfilter_distance.getMax();
            tv_floating_distance.setText(" "+progress+" \n MI"+" ");
            if (sbfilter_distance.getProgress()<=40) {
                tv_floating_distance.setY((float) ((sbfilter_distance.getHeight() - seekbarDistanceThumbPos))-40);
                if (sbfilter_distance.getProgress()<=10) {
                    tv_floating_distance.setY((float) ((sbfilter_distance.getHeight() - seekbarDistanceThumbPos))-100);
                }else{
                    tv_floating_distance.setY((float) ((sbfilter_distance.getHeight() - seekbarDistanceThumbPos))-50);
                }
            }else {
                if (sbfilter_distance.getProgress()>=50) {
                    tv_floating_distance.setY((float) ((sbfilter_distance.getHeight() - seekbarDistanceThumbPos))-10);
                }else {
                    tv_floating_distance.setY((float) ((sbfilter_distance.getHeight() - seekbarDistanceThumbPos))-30);
                }

            }

        }else if (seekBar.getId() == R.id.sbfilter_time) {
            sbfilter_time.getThumb().mutate().setAlpha(0);

            int secondaryProgress = (50*sbfilter_distance.getProgress())/60;
            sbfilter_time.setSecondaryProgress(secondaryProgress);
            timeSelectedRadius = sbfilter_time.getProgress();
            isDistanceFilterSelected = false;
            isTimeFilterSelected = true;
            if ( secondaryProgress  >= timeSelectedRadius) {
                sbfilter_time.setProgressDrawable(getResources().getDrawable(R.drawable.progress_time_filter_1));
            }else{
                sbfilter_time.setProgressDrawable(getResources().getDrawable(R.drawable.progress_time_filter));
            }
            int floatingTimeWidth = sbfilter_time.getHeight()
                    - sbfilter_time.getPaddingTop()
                    - sbfilter_time.getPaddingBottom();
            int seekbarTimeThumbPos = sbfilter_time.getPaddingTop()
                    + floatingTimeWidth
                    * sbfilter_time.getProgress()
                    / sbfilter_time.getMax();
            tv_floating_time.setText(" " + (progress+10) + "\nMin");
            addCircleToMap((int) timeSelectedRadius);
            if (sbfilter_time.getProgress()<=10){
                tv_floating_time.setY((float)((sbfilter_time.getHeight() - seekbarTimeThumbPos))-100);
            }else if (sbfilter_time.getProgress()>10 && sbfilter_time.getProgress()<=20) {
                tv_floating_time.setY((float)((sbfilter_time.getHeight() - seekbarTimeThumbPos))-70);
            }else if (sbfilter_time.getProgress()>20 && sbfilter_time.getProgress()<=30) {
                tv_floating_time.setY((float)((sbfilter_time.getHeight() - seekbarTimeThumbPos))-35);
            }else if (sbfilter_time.getProgress()>30 && sbfilter_time.getProgress()<=40) {
                tv_floating_time.setY((float)((sbfilter_time.getHeight() - seekbarTimeThumbPos))-30);
            }else if(sbfilter_time.getProgress()>0){
                tv_floating_time.setY((float)((sbfilter_time.getHeight() - seekbarTimeThumbPos))-10);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}


    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void loadData() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {

            case R.id.menu_edit:
                Intent intent = new Intent(this,EditProfileActivity.class);
                intent.putExtra("actionbar_title", tvTitle.getText().toString());
                intent.putExtra("service_id", service_id);
                // here we use GSON to serialize mMyObjectList and pass it throught intent to second Activity
                String listSerializedToJson = new Gson().toJson(servicesList);
                intent.putExtra("service_list", listSerializedToJson);
                intent.putExtra("location",selectedLocation);
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
                intent_landing.putExtra("selectedLocation",selectedLocation);
                startActivity(intent_landing);
                return  screenShotable;

            case ContentFragment.SETTINGS:
                Intent intent = new Intent(this,SearchLocationActivity.class);
                intent.putExtra("actionbar_title", tvTitle.getText().toString());
                intent.putExtra("service_id", service_id);
                // here we use GSON to serialize mMyObjectList and pass it throught intent to second Activity
                String listSerializedToJson = new Gson().toJson(servicesList);
                intent.putExtra("service_list", listSerializedToJson);
                // customers list
                startActivity(intent);
                return screenShotable;
            default:

                List<ServicesDO> servicesDOListTemp = new ArrayList<ServicesDO>();
                service_id =  slideMenuItem.get_id();
                ServicesDO servicesDOTemp = new ServicesDO();
                for (ServicesDO servicesDO:servicesList){
                    if (service_id.equalsIgnoreCase(servicesDO.get_id())){
                        servicesDOTemp = servicesDO;
                        servicesDO.setSelectedService(true);
                        if (servicesDO.isActive()) {
                            tvTitle.setText(servicesDOTemp.getName());
                        }

                        placesList.clear();
                        addCircleToMap(30);

                    }else{
                        servicesDO.setSelectedService(false);
                    }
                    servicesDOListTemp.add(servicesDO);
                }
                createMenuList(servicesDOListTemp);
                if (servicesDOTemp.isActive()) {

                    if (service_id != null) {
                        mMap.clear();
                        getServices();
                    }
                }else{
                    W30Utilities.showCustomNoServiceDialog(MapsActivity.this,new W30Utilities().getServiceImage(
                            W30Utilities.SERVICE_MENU_ACTIVE,servicesDOTemp.getName()),servicesDOTemp.getName());
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
        super.onBackPressed();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (v.getId() == R.id.tvcall) {
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
            }else if (v.getId() == R.id.slide_details_view) { }
        }

        return false;
    }

    private void callPhone() {
        try{
            CustomerDO customerDOTemp = (CustomerDO)tvcall.getTag();
        String phoneNo = (String )customerDOTemp.getMobile();
        if (phoneNo!=null && phoneNo.length()>0){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNo));
            mCurrentActivity.startActivity(callIntent);
            // Tracking Event
            SessionManager.getInstance().trackEvent("Call", phoneNo, customerDOTemp.getBusinessType() + " " + customerDOTemp.toString());
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
                } else {
                    callPhone();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

public boolean isRefeshEnabled = false;

    @Override
    public void onStart() {
        super.onStart();
        if (mLocationManager != null) {
         //   isLocationReadRequest = true;
          //  isLocationReadRequest = false;
            mLocationManager.startLocationFetching();
        }
        else
            initLocationFetching(mCurrentActivity);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mLocationManager != null)
            mLocationManager.abortLocationFetching();
    }

    @Override
    public void onPause() {
        super.onPause();
        //is app out of foucus
        is_app_running = false;
        if (mLocationManager != null)
            mLocationManager.pauseLocationFetching();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if(isAppInActive){
            isAppInActive = false;
            try {
                sbfilter_distance.setProgress((int) distanceSelectedRadius);
                sbfilter_time.setProgress((int) timeSelectedRadius);
            }catch(Exception e){
                e.printStackTrace();
            }

        }
      /*  if(isAppInActive){
            isAppInActive = false;

            if (mLocationManager != null) {
                isLocationReadRequest = true;
                mLocationManager.startLocationFetching();
            }
            else{

                initLocationFetching(mCurrentActivity);
            }
        }else{
            if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
                if (selectedLocation !=null){
                    if (selectedLocation.getLatitude() != 0 && selectedLocation.getLongitude() != 0) {
                        isRefeshEnabled = true;
                        CustomerDO customerDO = new CustomerDO();
                        customerDO.setLatitude(selectedLocation.getLatitude());
                        customerDO.setLongitude(selectedLocation.getLongitude());
                        customerDO.setMiles(W30Constants.MILES);
                        customerDO.setMinutes(W30Constants.MINITUS);
                        customerDO.setServiceId(service_id);
                        String userId = new SessionManager(MapsActivity.this).getUserid();
                        customerDO.setUserId(userId);
                        setSelectedLOcationMarker(customerDO.getLatitude(),customerDO.getLongitude());
                        if(commonBL.getCustomers(customerDO))
                            isWebServiceRunning = true;
                    }
                }
            }
        }*/
    }

    //@Override
    public void onResume() {
        super.onResume();
        //is in foreground
        is_app_running = true;
    } // End onResume
   public static boolean isAppInActive = false;

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        isAppInActive = true;

    }
    public void initLocationFetching(Activity mActivity) {

        isLocationReadRequest = true;
        // ask permission for M

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showLocationPermission();
        } else {

            mLocationManager = new MyLocationManager(getApplicationContext(), mActivity, this, MyLocationManager.ALL_PROVIDERS, LocationRequest.PRIORITY_HIGH_ACCURACY, 10 * 1000, 1 * 1000, MyLocationManager.LOCATION_PROVIDER_RESTRICTION_NONE); // init location manager

        }
    }
    private void showLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(mCurrentActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
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
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationReadRequest = true;
                    mLocationManager = new MyLocationManager(getApplicationContext(), this, this, MyLocationManager.ALL_PROVIDERS, LocationRequest.PRIORITY_HIGH_ACCURACY, 10 * 1000, 1 * 1000, MyLocationManager.LOCATION_PROVIDER_RESTRICTION_NONE);
                    // init location manager
                    mLocationManager.startLocationFetching();
                    //permission granted here
                }
        }
    }
    @Override
    public void locationFetched(Location mLocation, Location oldLocation, String time, String locationProvider) {
        // storing it on application level
        GetAccurateLocationApplication.mCurrentLocation = mLocation;
        GetAccurateLocationApplication.oldLocation = oldLocation;
        GetAccurateLocationApplication.locationProvider = locationProvider;
        GetAccurateLocationApplication.locationTime = time;
        if(isLocationReadRequest)
            getGPSTrackerInfo(mLocation.getLatitude(),mLocation.getLongitude());

    }
}
