package com.sms.within30;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sms.within30.googlemaps.PlaceJSONParser;
import com.sms.within30.lib.GPSTracker;
import com.sms.within30.utilities.NetworkUtility;
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

public class MapsActivity extends BaseActivity  implements  OnMapReadyCallback, LocationListener ,DataListener{

    private GoogleMap mMap;

    double mLatitude=0;
    double mLongitude=0;

    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
    LinearLayout llbooking;
    Animation bottomUp;
    Animation bottomDown;
    Button bt_book;
    RelativeLayout homeLayout;
    String category_type = "";
    String actionbarTitle = "";
    ActionBar actionBar;
    public void initialize(){
        homeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_maps, null);
        // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setTitle("Dentist");

      //  actionBar.setDisplayShowTitleEnabled(false);
        intilizeControls();
    }


    private void intilizeControls() {

        llbooking = (LinearLayout) homeLayout.findViewById(R.id.llbooking);
        bottomUp = AnimationUtils.loadAnimation(this,R.anim.bottom_up);
        bottomDown = AnimationUtils.loadAnimation(this,R.anim.bottom_down);
        bt_book = (Button)homeLayout.findViewById(R.id.bt_book);
        // Getting place reference from the map
        if ( getIntent()!=null) {
            category_type = getIntent().getStringExtra("category_type");
            actionbarTitle = getIntent().getStringExtra("actionbar_title");
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
                showCustomDialog(MapsActivity.this);
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
                // Getting LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Getting Current Location From GPS
                Location location = locationManager.getLastKnownLocation(provider);

                if(location!=null){
                    onLocationChanged(location);
                }

                locationManager.requestLocationUpdates(provider, 20000, 0, this);

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker arg0) {

                        if (llbooking.getVisibility() == View.INVISIBLE || llbooking.getVisibility() == View.GONE) {
                            llbooking.startAnimation(bottomUp);
                            llbooking.setVisibility(View.VISIBLE);
                        }

                    }
                });
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        if (llbooking.getVisibility() == View.INVISIBLE || llbooking.getVisibility() == View.GONE) {
                            llbooking.startAnimation(bottomUp);
                            llbooking.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                });

                loadCategories();
            }
        }
    }
    private  void loadCategories(){


        String browser_key = getResources().getString(R.string.google_maps_browser_key);
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+mLatitude+","+mLongitude);
        sb.append("&radius=5000");
        sb.append("&types="+category_type);
        sb.append("&sensor=true");
        sb.append("&key=");
        sb.append(browser_key);
        System.out.println("url----->"+sb.toString());
        int radius = 500;
        // Creating a new non-ui thread task to download Google place json data
    //    PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
      //  placesTask.execute(sb.toString());

        if (NetworkUtility.isNetworkConnectionAvailable(MapsActivity.this)){
            if(new CommonBL(MapsActivity.this, MapsActivity.this).getMapInfo(mLatitude, mLongitude,radius,browser_key,category_type)){

            }else{
            Toast.makeText(MapsActivity.this,R.string.Unable_to_connect_server_please_try_again,Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(MapsActivity.this,R.string.Unable_to_connect_server_please_try_again,Toast.LENGTH_SHORT).show();
        }

    }
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
       Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
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
        if(data != null && data.data != null) {
            hideLoader();
            switch (data.method) {
                case WS_MAP_INFO:
                    if(data.data!=null && data.data instanceof List<?>)
                    {
                        List<HashMap<String, String>> placesList = (List<HashMap<String, String>>)data.data;
                        // Clears all the existing markers
                        mMap.clear();

                        for (int i = 0; i < placesList.size(); i++) {

                            // Creating a marker
                            MarkerOptions markerOptions = new MarkerOptions();

                            // Getting a place from the places list
                            HashMap<String, String> hmPlace = placesList.get(i);

                            // Getting latitude of the place
                            double lat = Double.parseDouble(hmPlace.get("lat"));

                            // Getting longitude of the place
                            double lng = Double.parseDouble(hmPlace.get("lng"));

                            LatLng latLng = new LatLng(lat, lng);

                            // Setting the position for the marker
                            markerOptions.position(latLng);

                            // Setting the title for the marker.
                            //This will be displayed on taping the marker
                            markerOptions.title("3 OPen Slots");

                            View custom_marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
                            TextView numTxt = (TextView) custom_marker.findViewById(R.id.tvtitle);
                            numTxt.setText("3\nOpen slots");
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, custom_marker)));

                            // Placing a marker on the touched position


                            Marker m = mMap.addMarker(markerOptions);

                            // Linking Marker id and place reference
                            mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
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
                markerOptions.title("3 OPen Slots");

                View custom_marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
                TextView numTxt = (TextView) custom_marker.findViewById(R.id.tvtitle);
                numTxt.setText("3\nOpen slots");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, custom_marker)));

                // Placing a marker on the touched position


                Marker m = mMap.addMarker(markerOptions);

                // Linking Marker id and place reference
                mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

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
        menu.findItem(R.id.menu_filter).setVisible(true);
       // menu.findItem(R.id.menu_search).setVisible(true);

        mSearchCheck = true;
        return true;
    }

}
