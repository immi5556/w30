package within30.com.location;

import android.location.Location;

/**
 * Created by SR Lakhsmi on 9/28/2016.
 */
public  interface LocationManagerInterface {
    String TAG = LocationManagerInterface.class.getSimpleName();

     void locationFetched(Location mLocation, Location oldLocation, String time, String locationProvider);
}
