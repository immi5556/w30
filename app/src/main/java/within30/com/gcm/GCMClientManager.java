package within30.com.gcm;

/**
 * Created by SR Lakhsmi on 6/6/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import within30.com.session.SessionManager;

public class GCMClientManager {
    // Constants
    public static final String TAG = "GCMClientManager";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // Member variables
    private GoogleCloudMessaging gcm;
    private String regid;
    private String projectNumber;
    private Activity activity;
    public GCMClientManager(Activity activity, String projectNumber) {
        this.activity = activity;
        this.projectNumber = projectNumber;
        this.gcm = GoogleCloudMessaging.getInstance(activity);
    }

    // Register if needed or fetch from local store
    public void registerIfNeeded(final RegistrationCompletedHandler handler) {
        if (checkPlayServices()) {
            regid = getRegistrationId(getContext());
            if (regid.isEmpty()) {
                registerInBackground(handler);
            } else { // got id from cache
                Log.i(TAG, regid);
                handler.onSuccess(regid, false);
            }
        } else { // no play services
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }
    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground(final RegistrationCompletedHandler handler) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getContext());
                    }
                    InstanceID instanceID = InstanceID.getInstance(getContext());
                    regid = instanceID.getToken(projectNumber, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.i(TAG, regid);
                    // Persist the regID - no need to register again.
                    storeRegistrationId(getContext(), regid);
                } catch (IOException ex) {
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                    handler.onFailure("Error :" + ex.getMessage());
                }
                return regid;
            }
            @Override
            protected void onPostExecute(String regId) {
                if (regId != null) {
                    handler.onSuccess(regId, true);
                }
            }
        }.execute(null, null, null);
    }
    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *     registration ID.
     */
    private String getRegistrationId(Context context) {
        String registrationId =  new SessionManager(context).getGCMRegId();
         return registrationId;
    }
    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.saveGCMRegId(regId);
    }
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getContext().getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }
    private Context getContext() {
        return activity;
    }
    private Activity getActivity() {
        return activity;
    }
    public static abstract class RegistrationCompletedHandler {
        public abstract void onSuccess(String registrationId, boolean isNewRegistration);
        public void onFailure(String ex) {
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
            Log.e(TAG, ex);
        }
    }
}
