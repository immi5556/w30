package com.sms.within30;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.sms.within30.dataobjects.BookSlotDO;
import com.sms.within30.dataobjects.CustomerDO;
import com.sms.within30.googlemaps.PlaceJSONParser;
import com.sms.within30.lib.GPSTracker;
import com.sms.within30.sidemenu.fragment.ContentFragment;
import com.sms.within30.sidemenu.fragment.MyLInearLayout;
import com.sms.within30.sidemenu.interfaces.Resourceble;
import com.sms.within30.sidemenu.interfaces.ScreenShotable;
import com.sms.within30.sidemenu.util.ViewAnimator;
import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.CalendarUtils;
import com.sms.within30.utilities.NetworkUtility;
import com.sms.within30.utilities.W30Constants;
import com.sms.within30.webservices.Response;
import com.sms.within30.webservices.businesslayer.CommonBL;
import com.sms.within30.webservices.businesslayer.DataListener;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static android.graphics.Color.TRANSPARENT;

public class MapsActivity extends BaseActivity  implements  OnMapReadyCallback, LocationListener ,DataListener, ScreenShotable,View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    private GoogleMap mMap;

    double mLatitude=0;
    double mLongitude=0;

    HashMap<String, CustomerDO> mMarkerPlaceLink = new HashMap<String, CustomerDO>();
    LinearLayout llbooking;
    Animation bottomUp;
    Animation bottomDown;
    Button bt_book;
    LinearLayout homeLayout;
    String category_type = "";
    String actionbarTitle = "";
    String service_id = "";
    ActionBar actionBar;
    com.sms.within30.lib.VerticalSeekBar sbfilter;
    Button btfilterdistance;
    Button btfiltertime;
    TextView tvComapanyName;
    RatingBar ratingbar;
    TextView tvcount;
    TextView tvmiles;
    TextView tvtime;
    TextView tvaddress1;
    TextView tvaddress2;

    public void initialize(){
        homeLayout = (LinearLayout) inflater.inflate(R.layout.activity_maps, null);
        // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);

        actionBar = getSupportActionBar();
        intilizeControls();
    }

    private void intilizeControls() {

        llbooking = (LinearLayout) homeLayout.findViewById(R.id.llbooking);
        bottomUp = AnimationUtils.loadAnimation(this,R.anim.bottom_up);
        bottomDown = AnimationUtils.loadAnimation(this,R.anim.bottom_down);
        bt_book = (Button)homeLayout.findViewById(R.id.bt_book);
        btfiltertime = (Button)homeLayout.findViewById(R.id.btfiltertime);
        btfilterdistance = (Button)homeLayout.findViewById(R.id.btfilterdistance);
        sbfilter = (com.sms.within30.lib.VerticalSeekBar)homeLayout.findViewById(R.id.sbfilter);
        btfilterdistance.setOnClickListener(this);
        btfiltertime.setOnClickListener(this);
        sbfilter.setOnSeekBarChangeListener(this);
        sbfilter.setVisibility(View.GONE);
        tvComapanyName  = (TextView)homeLayout.findViewById(R.id.tvComapanyName);
        ratingbar = (RatingBar)homeLayout.findViewById(R.id.ratingbar);
        tvcount = (TextView)homeLayout.findViewById(R.id.tvcount);
        tvmiles = (TextView)homeLayout.findViewById(R.id.tvmiles);
        tvtime = (TextView) homeLayout.findViewById(R.id.tvtime);
        tvaddress1 = (TextView) homeLayout.findViewById(R.id.tvaddress1);
        tvaddress2 = (TextView)homeLayout.findViewById(R.id.tvaddress2);
       // myLInearLayout = new MyLInearLayout(MapsActivity.this).newInstance();
      //  viewAnimator = new ViewAnimator<>(MapsActivity.this, list, myLInearLayout, mLayoutDrawer, this);
        // Getting place reference from the map
        if ( getIntent()!=null) {
           // category_type = getIntent().getStringExtra("category_type");
            actionbarTitle = getIntent().getStringExtra("actionbar_title");
            service_id = getIntent().getStringExtra("service_id");
            tvTitle.setText(actionbarTitle);
        }

        try{
            initilizeMap();
        }catch(Exception e){
            e.printStackTrace();
        }
        bt_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerDO customerDO =(CustomerDO) bt_book.getTag();
                bookSlot(customerDO);


            }
        });
        if (mMap!=null) {
            //   mMap.setMyLocationEnabled(true);
            // create class object
            GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);

            // check if GPS enabled
            if(gpsTracker.canGetLocation()){

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                mLatitude = latitude;
                mLongitude = longitude;
                // create marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");

                // adding marker
                mMap.addMarker(marker);
                }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gpsTracker.showSettingsAlert();
            }
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    System.out.println("Clicked on map...");
                    sbfilter.setVisibility(View.GONE);
                    if(llbooking.getVisibility() == View.VISIBLE){
                        llbooking.startAnimation(bottomDown);
                        llbooking.setVisibility(View.INVISIBLE);
                    }
                }
            });


            // Getting Google Play availability status
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

            if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                dialog.show();

            }else {
              /*  // Getting LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Getting Current Location From GPS
                Location location = locationManager.getLastKnownLocation(provider);

                if(location!=null){
                    onLocationChanged(location);
                }*/

// check if GPS enabled

             //   locationManager.requestLocationUpdates(provider, 20000, 0, this);

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker arg0) {

                       /* if (llbooking.getVisibility() == View.INVISIBLE || llbooking.getVisibility() == View.GONE) {
                            llbooking.startAnimation(bottomUp);
                            llbooking.setVisibility(View.VISIBLE);
                        }*/

                    }
                });
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        sbfilter.setVisibility(View.GONE);
                       // marker..getId();
                       ;
                        CustomerDO marker_customerInfo = mMarkerPlaceLink.get(marker.getId());
                      //  Log.d("selected_marker_customer_info",  marker_customerInfo.toString());
                        setCustomerInfo(marker_customerInfo);
                        if (llbooking.getVisibility() == View.INVISIBLE || llbooking.getVisibility() == View.GONE) {
                            llbooking.startAnimation(bottomUp);
                            llbooking.setVisibility(View.VISIBLE);
                        }
                        marker.hideInfoWindow();
                        return true;
                    }
                });

               // loadCategories();
                getServices();
            }
        }
    }
    private void setCustomerInfo(CustomerDO customerDO){
      /*  TextView tvComapanyName;
        RatingBar ratingbar;
        TextView tvcount;
        TextView tvmiles;
        TextView tvtime;
        TextView tvaddress1;
        TextView tvaddress2;*/
        try{

            tvComapanyName.setText(customerDO.getCompanyName());
            tvaddress1.setText(customerDO.getGeo().getCity()+" "+customerDO.getGeo().getCountry());
            tvtime.setText("Estimated time:"+" "+customerDO.getExpectedTime()+" "+"MIN");
            tvmiles.setText(customerDO.getDestinationDistance()+" "+"Miles");
            bt_book.setTag(customerDO);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void bookSlot( CustomerDO customerDO ){

        if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
            BookSlotDO bookSlotDO = new BookSlotDO();
            //TODO: construct current data here
            String currentDate = CalendarUtils.getCurrentPostDate((long)customerDO.getExpectedTime());
            bookSlotDO.setDate(currentDate);
            bookSlotDO.setSubDomain(customerDO.getSubdomain());
            bookSlotDO.setEmail("");
            bookSlotDO.setMobile("");
            Log.d("Book slot toString()",bookSlotDO.toString());
            if(new CommonBL(MapsActivity.this, MapsActivity.this).bookSlot(bookSlotDO)){
                if (pd == null) {
                    pd =  new ProgressDialog(MapsActivity.this);
                    pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                    pd.setMessage("Loading...");
                    pd.show();
                }
            }else{
                showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
            }
        }else{
            showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
        }
    }

    private void getServices(){
        if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)) {
            CustomerDO customerDO = new CustomerDO();
            customerDO.setLatitude(mLatitude);
            customerDO.setLongitude(mLongitude);
            customerDO.setMiles(W30Constants.MILES);
            customerDO.setMinutes(W30Constants.MINITUS);
            customerDO.setServiceId(service_id);
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
        }else{
            showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
        }
    }


  /*  private  void loadCategories(){


        String browser_key = getResources().getString(R.string.google_maps_browser_key);
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+mLatitude+","+mLongitude);
        sb.append("&radius=5000");
        sb.append("&types="+category_type);
        sb.append("&sensor=true");
        sb.append("&key=");
        sb.append(browser_key);
        System.out.println("url----->" + sb.toString());
        int radius = 500;
        // Creating a new non-ui thread task to download Google place json data
        PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());

       *//* if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)){
            if(new CommonBL(MapsActivity.this, MapsActivity.this).getMapInfo(mLatitude, mLongitude,radius,browser_key,category_type)){

            }else{
            Toast.makeText(MapsActivity.this,R.string.Unable_to_connect_server_please_try_again,Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(MapsActivity.this,R.string.Unable_to_connect_server_please_try_again,Toast.LENGTH_SHORT).show();
        }*//*

    }*/
    private void  showCustomFilterDialog(Context context){
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_filter_dialog);
        // dialog.setTitle("Title...");

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
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        // if button is clicked, close the custom dialog
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
       // if button is clicked, close the custom dialog
       dialogButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
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
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            e.printStackTrace();
           // Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
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
                        List<CustomerDO> placesList = (List<CustomerDO>)data.data;
                        // Clears all the existing markers
                        mMap.clear();
                        Log.d("places list.size",""+placesList.size());
                        for (int i = 0; i < placesList.size(); i++) {

                            // Creating a marker
                            MarkerOptions markerOptions = new MarkerOptions();

                            // Getting a place from the places list
                            CustomerDO  customerDO= (CustomerDO) placesList.get(i);
                            Log.d("customerDO",customerDO.toString());
                            // Getting latitude of the place
                            double[] coordinates = customerDO.getGeo().getCoordinates();
                            double lat = coordinates[0];
                            double lng = coordinates[1];
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
                            Log.d("open slots",""+customerDO.getSlotsAvailable());
                            if (customerDO.getSlotsAvailable()!=null) {
                                if (customerDO.getSlotsAvailable() == 0) {
                                    markerOptions.title("Fully Booked");
                                    numTxt.setText("Fully Booked");
                                }else{
                                    markerOptions.title(customerDO.getSlotsAvailable()+" "+"OPen Slots");
                                    numTxt.setText(customerDO.getSlotsAvailable()+"\nOpen slots");
                                }
                            }

                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, custom_marker)));

                            // Placing a marker on the touched position


                            Marker m = mMap.addMarker(markerOptions);

                            // Linking Marker id and place reference

                          //  mMarkerPlaceLink.put(m.getId(), customerDO.getServiceId()); mMarkerPlaceLink.put("_clientid",customerDO.get_clientid());
                            /*HashMap<String,String> marker_customerInfo = new HashMap<String,String>();
                            marker_customerInfo.put("subDomain",customerDO.getSubDomain());
                            marker_customerInfo.put("companyName",customerDO.getCompanyName());
                            marker_customerInfo.put("city",customerDO.getGeo().getCity());
                            marker_customerInfo.put("country",customerDO.getGeo().getCountry());
                            marker_customerInfo.put("expectedTime",Double.toString(customerDO.getExpectedTime()));*/
                            mMarkerPlaceLink.put(m.getId(),customerDO);

                            //TODO: put all values here

                        }
                    }else if(data.data!=null && data.data instanceof String ){
                        String str = (String)data.data;
                        showToast(str);
                    }
                    break;
                case WS_BOOKSLOT:
                    if(data.data!=null && data.data instanceof String ){
                        String str = (String)data.data;
                        if (str.equals(AppConstants.OK)) {
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

    @Override
    public void takeScreenShot() {


    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btfiltertime) {
            System.out.println("clicked on btfiltertime");
            sbfilter.setVisibility(View.VISIBLE);

            btfilterdistance.setAlpha(0.5f);
            btfiltertime.setAlpha(1.0f);
            if(llbooking.getVisibility() == View.VISIBLE){
                llbooking.startAnimation(bottomDown);
                llbooking.setVisibility(View.INVISIBLE);
            }
        }else if (v.getId() == R.id.btfilterdistance) {
            System.out.println("clicked on btfilterdistance");
            sbfilter.setVisibility(View.VISIBLE);

            btfilterdistance.setAlpha(1.0f);
            btfiltertime.setAlpha(0.5f);
            if(llbooking.getVisibility() == View.VISIBLE){
                llbooking.startAnimation(bottomDown);
                llbooking.setVisibility(View.INVISIBLE);
            }
        }

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
         //  circle = null;
       }
       CircleOptions circleOptions = new CircleOptions()
               .center(new LatLng(17.4119848, 78.4200735))
               .radius(radius)
               .strokeColor(getResources().getColor(R.color.w30_blue))
              // .fillColor(getResources().getColor(R.color.map_circle_fill))
               .fillColor(0x55ffffff)
               .strokeWidth(3f)
               .visible(true);

       circle =  mMap.addCircle(circleOptions);
       //circle.al
      //
      // mMap.an
       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circleOptions.getCenter(),getZoomLevel(circle)));

   }


    public int getZoomLevel(Circle circle) {
        int zoomLevel = 11;
        if (circle != null) {
            double radius = circle.getRadius() + circle.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        System.out.println("seek bar on progress changed....."+progress);
        addCircleToMap(progress);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {


    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String>{

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
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
    }
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
        switch (item.getItemId()) {
            /*case android.R.id.home:
               *//*Intent menuIntent = new Intent(this,AppMenuActivity.class);
                startActivity(menuIntent);
                overridePendingTransition(R.anim.app_menu_in, 0);*//*
                return true;*/

            case R.id.menu_filter:
                showCustomFilterDialog(MapsActivity.this);
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
        menu.findItem(R.id.menu_filter).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(false);

        mSearchCheck = true;
        return true;
    }
    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case ContentFragment.CLOSE:
                return screenShotable;
            default:
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


}
