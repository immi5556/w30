package within30.com.location;

import android.app.Application;
import android.content.Context;
import android.location.Location;

/**
 * Created by SR Lakhsmi on 9/28/2016.
 */
public class GetAccurateLocationApplication extends Application {

    public static Location mCurrentLocation;
    public static String locationProvider;
    public static Location oldLocation;
    public static String locationTime;

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        initApplication();
    }

    private void initApplication() {
        mContext = getApplicationContext();
    }
}
