package com.sms.within30;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sms.within30.adapters.LocationHistoryAdapter;
import com.sms.within30.dataobjects.LocationDO;
import com.sms.within30.dataobjects.ServicesDO;
import com.sms.within30.dataobjects.UserDO;
import com.sms.within30.lib.GPSTracker;
import com.sms.within30.session.SessionManager;
import com.sms.within30.utilities.AppConstants;

import com.sms.within30.utilities.PreferenceUtils;
import com.sms.within30.utilities.W30Database;
import com.sms.within30.webservices.Response;
import com.sms.within30.webservices.businesslayer.DataListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchLocationActivity extends BaseActivity  implements   DataListener, View.OnClickListener{

    LinearLayout homeLayout;
    SessionManager sessionManager  = null;
    List<LocationDO> locationList;
    ActionBar actionBar;
    private EditText etSearch;
    TextView tvusegps;
    TextView tv_currentLocation;
    private MenuItem mSearchMenu;
    private SearchView searchView;
    String service_id = "";
    String actionbarTitle = "";
    List<ServicesDO> servicesList;
    LocationDO locationDO = null;
    ListView lv_locationhistory;
    LocationHistoryAdapter locationHistoryAdapter;

    double mLatitude=0;
    double mLongitude=0;
    private static final int LOCATION_REQUEST=3;
    private static  final int LOCATION_REQUEST1 = 4;
    GPSTracker gpsTracker = null;
   // public ActionBarDrawerToggle drawerToggle;
    public void initialize(){
        homeLayout = (LinearLayout) inflater.inflate(R.layout.activity_serachlocation, null);
        // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);
        sessionManager = new SessionManager(SearchLocationActivity.this);
        actionBar = getSupportActionBar();
        getIntentdata();
        intilizeControls();
        setActionBar();
        getHistoryFromDB();
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
    private void setIntentData(LocationDO locationDO){

        Intent mapsIntent = new Intent(SearchLocationActivity.this, MapsActivity.class);
        String str = getSupportActionBar().getTitle().toString();
        mapsIntent.putExtra("actionbar_title", locationDO.getCity());
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

    private void getHistoryFromDB() {
        W30Database w30Database = new W30Database(SearchLocationActivity.this);
        List<LocationDO> locationDOList = w30Database.getLocationHistory();
      try {
          if (locationDOList!=null & locationDOList.size()>0) {
              locationHistoryAdapter   = new LocationHistoryAdapter(SearchLocationActivity.this, R.layout.history_list_item, R.id.tv_category,locationDOList);
              lv_locationhistory.setAdapter(locationHistoryAdapter);
              for (LocationDO locationDO : locationDOList) {
                Log.d("Location History",locationDO.toString());
              }
          }
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    public void setActionBar(){
        tvTitle.setVisibility(View.GONE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public void loadData() {

    }

    private void intilizeControls() {

        lv_locationhistory = (ListView) homeLayout.findViewById(R.id.lv_locationhistory);
        tvusegps = (TextView)homeLayout.findViewById(R.id.tvusegps);
        tv_currentLocation = (TextView)homeLayout.findViewById(R.id.tv_currentLocation);
        // Getting place reference from the map
        h = new Handler(getMainLooper());
        tvusegps.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getGPSTrackerInfo();
                return false;
            }
        });


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


    }
    public void hideSearch()
	{
	     MenuItemCompat.collapseActionView(mSearchMenu);
	     getSupportActionBar().setNavigationMode( ActionBar.NAVIGATION_MODE_LIST );
	     getSupportActionBar().setDisplayShowTitleEnabled( false );
         mSearchMenu.expandActionView();
	     mSearchMenu.collapseActionView();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        /**
         * Get the search close button image view
         *
         */
        mSearchCheck = true;
        mSearchMenu = menu.findItem(R.id.menu_search);
        searchView = (SearchView) mSearchMenu.getActionView();
        searchView.setOnQueryTextListener(OnQuerySearchView);
        etSearch = ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        etSearch.setHint("Search");
        etSearch.setHintTextColor(Color.WHITE);
        etSearch.setTypeface(AppConstants.SIT_TEXT);
       ((AutoCompleteTextView)etSearch).setThreshold(1);
        ((AutoCompleteTextView)etSearch).setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                hideKeyBoard(searchView);
                setProgressBarIndeterminateVisibility(true);
                mSearchCheck = false;
                etSearch.setText("");
                searchView.setQuery("", true);
                searchView.onActionViewCollapsed();
                mSearchMenu.collapseActionView();
                LocationDO locationDO = (LocationDO) view.getTag(R.string.app_name);
                Log.d("locationDO",locationDO.toString());
                if (locationDO != null) {
                    W30Database w30Database = new W30Database(SearchLocationActivity.this);
                    w30Database.addLocationHistory(locationDO);
//                	showLoader(getString(R.string.loading));
                   setIntentData(locationDO);
                }
            }
        });

       // MenuInflater inflater = getMenuInflater();
      //  inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_reset).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(false);
        menu.findItem(R.id.menu_edit).setVisible(false);
        menu.findItem(R.id.menu_search).setVisible(true);
        menu.findItem(R.id.menu_home).setVisible(false);
		mSearchCheck = false;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      /*  if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
              //  setIntentData();
               /* Intent mapsIntent = new Intent(SearchLocationActivity.this, MapsActivity.class);
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
                startActivity(mapsIntent);*/
                return true;

            case R.id.menu_search:
                mSearchCheck = true;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private SearchView.OnQueryTextListener OnQuerySearchView = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            mSearchCheck = false;
            etSearch.setText("");
            searchView.setQuery("", true);
            searchView.onActionViewCollapsed();
            mSearchMenu.collapseActionView();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            if (mSearchCheck || !query.equalsIgnoreCase("")){
                loadHistory(query);
                if("".equalsIgnoreCase(query))
                    h.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            ((AutoCompleteTextView)etSearch).showDropDown();
                        }
                    }, 20);
                else
                    ((AutoCompleteTextView)etSearch).showDropDown();
            }

            return true;
        }
    };
    private Handler h;

    private void loadHistory(String query) {

        if (searchView == null)
            return;

        if (locationList == null)
            return;

        if (etSearch == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            String[] columnNames = {"_id", "text"};
            MatrixCursor cursor = new MatrixCursor(columnNames);
            String[] temp = new String[2];
            int id = 0;


            List<LocationDO> locationTemp = new ArrayList<LocationDO>();
            for(LocationDO locationDO :locationList){
                if ((query.length() <= locationDO.getCity().length() && query.toLowerCase(Locale.US).
                        equalsIgnoreCase((String) locationDO.getCity().toLowerCase(Locale.US).subSequence(0, query.length())))
                        || (query.length() <= locationDO.getCity().length() && query.toLowerCase(Locale.US).
                        equalsIgnoreCase((String) locationDO.getCity().toLowerCase(Locale.US).subSequence(0, query.length())))) {
                    temp[0] = "1" + "";
                    temp[1] = locationDO.getCity();
                    cursor.addRow(temp);
                    locationTemp.add(locationDO);
                }
            }



            if (cursor.getCount() == 0) {
                temp[0] = Integer.toString(id++);
                temp[1] = "No items found";
                cursor.addRow(temp);
            }
            String[] from = {"text"};
            int[] to = {R.id.tvItem};
            if (locationTemp.size() == 0) {
                SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(SearchLocationActivity.this, R.layout.listentry, cursor, from, to);
                searchView.setSuggestionsAdapter(simpleCursorAdapter);
            } else {
                CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(SearchLocationActivity.this, cursor, 0, locationTemp);
                searchView.setSuggestionsAdapter(customCursorAdapter);
            }
        }
    }
         class CustomCursorAdapter extends CursorAdapter {

            private LayoutInflater mInflater;
            private List<LocationDO> vecParlourDO;

            public CustomCursorAdapter(Context context, Cursor c, int flags, List<LocationDO> vecParlourDO) {
                super(context, c, flags);
                mInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.vecParlourDO = vecParlourDO;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                int position = cursor.getPosition();
//			if (position % 2 == 1) {
//				view.setBackgroundColor(Color.parseColor("#FFEB3B")/*context.getResources().getColor(
//						R.color.light_blue)*/);
//			} else {
//				view.setBackgroundColor(Color.parseColor("#FFFFFF")/*context.getResources().getColor(
//						R.color.white)*/);
//			}

                TextView content = (TextView) view.findViewById(R.id.tvItem);
                TextView tvItemLocation = (TextView) view.findViewById(R.id.tvItemLocation);

                content.setTypeface(AppConstants.SIT_TEXT);
                view.setTag(R.string.app_name, vecParlourDO.get(position));
                content.setText(cursor.getString(cursor.getColumnIndex("text")));
                tvItemLocation.setText(vecParlourDO.get(position).getCity());

            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return mInflater.inflate(R.layout.listentry, parent, false);
            }
        }
    private void getGPSTrackerInfo() {
        try{
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                gpsTracker = new GPSTracker(SearchLocationActivity.this);
            }else{

                if (ContextCompat.checkSelfPermission(SearchLocationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SearchLocationActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
                    Log.d("ACCESS_COARSE_LOCATION"," permission requesting...");
                } else {
                    Log.d("ACCESS_COARSE_LOCATION"," permission suceess...");
                    gpsTracker = new GPSTracker(SearchLocationActivity.this);
                }
            }

        } catch (Exception e) {
            Log.d("location exception","exception occured ...........");
            e.printStackTrace();

        }
        getLatLng();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case LOCATION_REQUEST:
                if (ContextCompat.checkSelfPermission(SearchLocationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_COARSE_LOCATION success");
                    try{
                        gpsTracker = new GPSTracker(SearchLocationActivity.this);
                        getLatLng();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                } else  if (ContextCompat.checkSelfPermission(SearchLocationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_COARSE_LOCATION failed");
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SearchLocationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        //Show permission explanation dialog...
                        Log.d("in onRequestPermissionsResult", "Show permission explanation dialog..ACCESS_COARSE_LOCATION.");
                        try{
                            gpsTracker = new GPSTracker(SearchLocationActivity.this);
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
                        if (ContextCompat.checkSelfPermission(SearchLocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SearchLocationActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST1);
                            Log.d("ACCESS_FINE_LOCATION"," permission requesting...");
                        } else {
                            Log.d("ACCESS_FINE_LOCATION"," permission suceess...");
                            try{
                                gpsTracker = new GPSTracker(SearchLocationActivity.this);
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
                if (ContextCompat.checkSelfPermission(SearchLocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_FINE_LOCATION success");
                    try{
                        gpsTracker = new GPSTracker(SearchLocationActivity.this);
                        getLatLng();
                    }catch(Exception e){
                        e.printStackTrace();
                    }


                }else  if (ContextCompat.checkSelfPermission(SearchLocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                {
                    Log.d("in onRequestPermissionsResult", "ACCESS_FINE_LOCATION failed");
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SearchLocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        //Show permission explanation dialog...
                        Log.d("in onRequestPermissionsResult", "Show permission explanation dialog...ACCESS_FINE_LOCATION");
                        try{
                            gpsTracker = new GPSTracker(SearchLocationActivity.this);
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
                LocationDO current_locationDO = new LocationDO();
                current_locationDO.setLatitude(mLatitude);
                current_locationDO.setLongitude(mLongitude);
                current_locationDO.setCity(currentCity);
                setIntentData(current_locationDO);

              //  tv_currentLocation.setText(current_locationDO.getCity());

              //  tv_currentLocation.setTag(current_locationDO);

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
        Geocoder gcd = new Geocoder(SearchLocationActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
            if (addresses.size() > 0){
                // System.out.println(addresses.get(0).getAddressLine(0));
                System.out.println(addresses.get(0).getSubLocality());
                String city = addresses.get(0).getSubLocality();
                return city;
            }


        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }








    @Override
    public void onBackPressed()
    {
        super.onBackPressed();


    }


}
