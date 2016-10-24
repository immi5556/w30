package within30.com;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.text.SpannableStringBuilder;
import android.text.Spanned;
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

import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import within30.com.adapters.LocationHistoryAdapter;
import within30.com.dataobjects.LocationDO;
import within30.com.dataobjects.ServicesDO;

import within30.com.location.GetAccurateLocationApplication;
import within30.com.location.LocationManagerInterface;
import within30.com.location.MyLocationManager;
import within30.com.session.SessionManager;
import within30.com.utilities.AppConstants;

import within30.com.utilities.FontUtilities;

import within30.com.utilities.W30Database;
import within30.com.webservices.Response;
import within30.com.webservices.businesslayer.CommonBL;
import within30.com.webservices.businesslayer.DataListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchLocationActivity extends BaseActivity  implements   DataListener, View.OnClickListener,LocationManagerInterface {

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
    LocationDO currentLocation = null;
    LocationDO locationDO = null;
    ListView lv_locationhistory;
    LocationHistoryAdapter locationHistoryAdapter;

    double mLatitude=0;
    double mLongitude=0;

    TextView tv_norecentsearches;
    List<LocationDO> locationDOListFromDB;
    //Location
    public MyLocationManager mLocationManager;
    private static final int REQUEST_FINE_LOCATION = 1;
    private Activity mCurrentActivity;
    private boolean isLocationReadRequest = false;

    private boolean isgetCities = false;

    public void initialize(){
        homeLayout = (LinearLayout) inflater.inflate(R.layout.activity_serachlocation, null);
        homeLayout.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);
        mCurrentActivity = SearchLocationActivity.this;
        sessionManager = new SessionManager(SearchLocationActivity.this);
        actionBar = getSupportActionBar();
        getIntentdata();
        intilizeControls();
        setActionBar();
        getHistoryFromDB();
        initLocationFetching(mCurrentActivity);
        isgetCities = true;

    }

    private void getIntentdata() {
        if (getIntent() != null) {
            try{
                if (getIntent().hasExtra("actionbar_title")) {
                    actionbarTitle = getIntent().getStringExtra("actionbar_title");

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

                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
    private void setIntentData(LocationDO locationDO){

        Intent mapsIntent = new Intent(SearchLocationActivity.this, MapsActivity.class);
        String str = getSupportActionBar().getTitle().toString();
        mapsIntent.putExtra("actionbar_title", actionbarTitle);
        mapsIntent.putExtra("service_id", service_id);
        // here we use GSON to serialize mMyObjectList and pass it throught intent to second Activity
        String listSerializedToJson = new Gson().toJson(servicesList);
        mapsIntent.putExtra("service_list", listSerializedToJson);
        if (locationDO!=null){
            mapsIntent.putExtra("selectedLocation", locationDO);
        }
        startActivity(mapsIntent);
    }

    private void getHistoryFromDB() {
        W30Database w30Database = new W30Database(SearchLocationActivity.this);
        locationDOListFromDB = w30Database.getLocationHistory();
      try {
          if (locationDOListFromDB!=null & locationDOListFromDB.size()>0) {
              locationHistoryAdapter   = new LocationHistoryAdapter(SearchLocationActivity.this, R.layout.history_list_item, R.id.tv_category,locationDOListFromDB);
              lv_locationhistory.setAdapter(locationHistoryAdapter);

              tv_norecentsearches.setVisibility(View.GONE);
              lv_locationhistory.setVisibility(View.VISIBLE);
          }else {
              tv_norecentsearches.setVisibility(View.VISIBLE);
              lv_locationhistory.setVisibility(View.GONE);
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

        SpannableStringBuilder SS = new SpannableStringBuilder("Change Location");
        SS.setSpan(new FontUtilities(SearchLocationActivity.this).getDroidSerif(), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        actionBar.setTitle(SS);
    }

    @Override
    public void loadData() {

    }

    private void intilizeControls() {

        lv_locationhistory = (ListView) homeLayout.findViewById(R.id.lv_locationhistory);
        tvusegps = (TextView)homeLayout.findViewById(R.id.tvusegps);
        tv_currentLocation = (TextView)homeLayout.findViewById(R.id.tv_currentLocation);
        tv_norecentsearches = (TextView)homeLayout.findViewById(R.id.tv_norecentsearches);
        // Getting place reference from the map
        h = new Handler(getMainLooper());
        tvusegps.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setIntentData(null);
                return false;
            }
        });

        lv_locationhistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationDO locationDO = (LocationDO)locationDOListFromDB.get(position);
                if (locationDO != null) {
                    new SessionManager(SearchLocationActivity.this).setLocationChanged(true);
                    setIntentData(locationDO);
                }
            }
        });
    }


    @Override
    public void dataRetreived(Response data) {

        if (pd != null)
            if (pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
        if(data != null && data.data != null) {
            hideLoader();
            switch (data.method) {

                case WS_GETCITIES:
                    if(data.data!=null && data.data instanceof List<?>)
                    {
                        locationList = (List<LocationDO>)data.data;
                    }
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    public void onClick(View v) { }

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
        etSearch.setHint("Change Location");

        etSearch.setHintTextColor(getResources().getColor(R.color.serach_action_bar));
        etSearch.setTypeface(AppConstants.SIT_TEXT);
       ((AutoCompleteTextView)etSearch).setThreshold(0);
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

                if (locationDO != null) {
                    new SessionManager(SearchLocationActivity.this).setLocationChanged(true);
                    SpannableStringBuilder SS = new SpannableStringBuilder(locationDO.getCity());
                    SS.setSpan(new FontUtilities(SearchLocationActivity.this).getDroidSerif(), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    actionBar.setTitle(SS);
                    W30Database w30Database = new W30Database(SearchLocationActivity.this);
                    w30Database.addLocationHistory(locationDO);

                   setIntentData(locationDO);
                }
            }
        });
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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
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
            if (locationList == null || locationList.size() == 0) {
                isgetCities = true;
                if (mLocationManager != null) {
                    isLocationReadRequest = true;
                    mLocationManager.startLocationFetching();
                }
                else{
                    initLocationFetching(mCurrentActivity);
                }
            }else {
                for (LocationDO locationDO : locationList) {
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
    private void getLatLng(double lat,double lng){
        isLocationReadRequest = false;
                mLatitude = lat;
                mLongitude = lng;
                String currentCity = getCityName(mLatitude,mLongitude);
                LocationDO current_locationDO = new LocationDO();
                current_locationDO.setLatitude(mLatitude);
                current_locationDO.setLongitude(mLongitude);
                current_locationDO.setCity(currentCity);
                if (mLatitude != 0 && mLongitude !=0) {
                    if (isgetCities) {
                        if (pd == null) {
                            pd =  new ProgressDialog(SearchLocationActivity.this);
                            pd.setProgressStyle(android.R.attr.progressBarStyleSmall);
                            pd.setMessage("Loading...");
                            pd.show();
                        }
                        if ( !new CommonBL(SearchLocationActivity.this,SearchLocationActivity.this).getCities(current_locationDO.getLatitude(),current_locationDO.getLongitude())){

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
                        setIntentData(current_locationDO);
                    }
                }


    }

    private String  getCityName(double mLatitude, double mLongitude) {
        Geocoder gcd = new Geocoder(SearchLocationActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
            if (addresses.size() > 0){

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
                    isgetCities = false;

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
            getLatLng(mLocation.getLatitude(),mLocation.getLongitude());

    }
}
