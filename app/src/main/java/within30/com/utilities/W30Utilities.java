package within30.com.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import within30.com.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by SR Lakhsmi on 4/24/2016.
 */
public class W30Utilities {
    public boolean edittextValidation(HashMap hm,Activity activity) {
        boolean valid = true;
        // hashmap iteration and printing
        Iterator<EditText> keyIterator = hm.keySet().iterator();
        while(keyIterator.hasNext()) {

            EditText key = keyIterator.next();
            if (key.getText().toString().length() == 0) {
                setValidationEditText(key, activity, (String) hm.get(key));
                valid = false;
            }

        }
        return valid;

    }
    /**
     * This method validates to the given edit text
     * should call when  validation requires
     * @param e
     * @param activity
     * @param error to show
     */
    public void setValidationEditText(final EditText e, final Activity activity, final String error) {

        e.setHint(error);
       // e.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.exclamation5, 0, 0, 0);
        e.setHintTextColor(activity.getResources().getColor(R.color.red));
        e.setCompoundDrawablePadding(5);
    }
    /**
     * This methos sets the error message to the given edit text
     * Shouold call at oncreate method of activity
     * @param e Edittext
     * @param activity
     * @param hintText
     */
    public void  setErrorOnEditText(final EditText e,final Activity activity, final String hintText) {

        e.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                e.setHint(hintText);
                e.setHintTextColor(activity.getResources().getColor(R.color.gray_light));

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    /**
     * This methos validates the given phonen number by usig matcher from Android util
     * @param number
     * @return boolean if the given number is valid
     */
    public boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
    public static void  showCustomNoServiceDialog(Context context,int id,String serviceName){
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_noservice_dialog);
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
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout llmain = (LinearLayout)dialog.findViewById(R.id.llmain);
        TextView tvservicename = (TextView)dialog.findViewById(R.id.tvservicename);
        TextView img_service = (TextView)dialog.findViewById(R.id.img_service);
        TextView tverrortxt = (TextView)dialog.findViewById(R.id.tverrortxt);
        tvservicename.setText(serviceName);
        img_service.setBackgroundResource(id);
        /*String tempStr  ="Currently not Available.<br/><br/><b><font color = #39425f>Launching Soon!</font></b>";
        tverrortxt.setText(Html.fromHtml(tempStr));*/
        llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public final static String SERVICE_HAIRSALON = "Hair Salon";
    public final static String SERVICE_SPAS = "Spas";
    public final static String SERVICE_DENTISTS = "Dentists";
    public final static String SERVICE_PHYSICIANS= "Physicians";
    public final static String SERVICE_CAR_SERVICES = "Car Services";
    public final static String SERVICES_LEGAL = "Attorneys";

    public final static int SERVICE_LANDING_ACTIVE= 1;
    public final static int SERVICE_LANDING_INACTIVE = 2;
    public final static int SERVICE_MENU_ACTIVE = 3;
    public final static int SERVICE_MENU_INACTIVE = 4;
    public final static int SERVICE_POPUP = 5;

    HashMap<String,Integer>  servicesDrawablesActive = new HashMap<String,Integer>();
    HashMap<String,Integer>  servicesDrawablesInative = new HashMap<String,Integer>();
    HashMap<String,Integer>  servicesDrawablesMenuActive = new HashMap<String,Integer>();
    HashMap<String,Integer>  servicesDrawablesmenuInactive = new HashMap<String,Integer>();
    HashMap<String,Integer>  servicesDrawablesPopup = new HashMap<String,Integer>();

    public void addDrawables(){

        servicesDrawablesActive.put(SERVICE_HAIRSALON,R.mipmap.salon);
        servicesDrawablesActive.put(SERVICE_SPAS,R.mipmap.spa_and_massage);
        servicesDrawablesActive.put(SERVICE_DENTISTS,R.mipmap.dentist);
        servicesDrawablesActive.put(SERVICE_PHYSICIANS,R.mipmap.diagntcs);
        servicesDrawablesActive.put(SERVICE_CAR_SERVICES,R.mipmap.autoservices);
        servicesDrawablesActive.put(SERVICES_LEGAL,R.mipmap.law);

        servicesDrawablesInative.put(SERVICE_HAIRSALON,R.mipmap.noservice_salon);
        servicesDrawablesInative.put(SERVICE_SPAS,R.mipmap.noservice_spa_and_massage);
        servicesDrawablesInative.put(SERVICE_DENTISTS,R.mipmap.dentist);
        servicesDrawablesInative.put(SERVICE_PHYSICIANS,R.mipmap.noservice_diagntcs);
        servicesDrawablesInative.put(SERVICE_CAR_SERVICES,R.mipmap.noservice_autoservices);
        servicesDrawablesInative.put(SERVICES_LEGAL,R.mipmap.noservice_law);

        servicesDrawablesMenuActive.put(SERVICE_HAIRSALON,R.mipmap.menu_salon);
        servicesDrawablesMenuActive.put(SERVICE_SPAS,R.mipmap.menu_spa);
        servicesDrawablesMenuActive.put(SERVICE_DENTISTS,R.mipmap.menu_dentist);
        servicesDrawablesMenuActive.put(SERVICE_PHYSICIANS,R.mipmap.menu_diagnostics);
        servicesDrawablesMenuActive.put(SERVICE_CAR_SERVICES,R.mipmap.menu_car_maintainence);
        servicesDrawablesMenuActive.put(SERVICES_LEGAL,R.mipmap.menu_law);

        servicesDrawablesmenuInactive.put(SERVICE_HAIRSALON,R.mipmap.menu_salon_noservice);
        servicesDrawablesmenuInactive.put(SERVICE_SPAS,R.mipmap.menu_spa_noservice);
        servicesDrawablesmenuInactive.put(SERVICE_DENTISTS,R.mipmap.menu_dentist);
        servicesDrawablesmenuInactive.put(SERVICE_PHYSICIANS,R.mipmap.menu_diagnostics_noservice);
        servicesDrawablesmenuInactive.put(SERVICE_CAR_SERVICES,R.mipmap.menu_car_maintainence_noservice);
        servicesDrawablesmenuInactive.put(SERVICES_LEGAL,R.mipmap.menu_law_noservice);

        servicesDrawablesPopup.put(SERVICE_HAIRSALON,R.mipmap.icon_noservices_salon);
        servicesDrawablesPopup.put(SERVICE_SPAS,R.mipmap.icon_noservices_spa);
        servicesDrawablesPopup.put(SERVICE_DENTISTS,R.mipmap.dentist);
        servicesDrawablesPopup.put(SERVICE_PHYSICIANS,R.mipmap.icon_noservices_diagnostics);
        servicesDrawablesPopup.put(SERVICE_CAR_SERVICES,R.mipmap.icon_noservices_carservice);
        servicesDrawablesPopup.put(SERVICES_LEGAL,R.mipmap.icon_noservices_law);

    }
    public int getServiceImage(int type,String serviceName){
        addDrawables();
        int id = 0;
        switch (type){
            case SERVICE_LANDING_ACTIVE:
                id = servicesDrawablesActive.get(serviceName);
                break;
            case SERVICE_LANDING_INACTIVE:
                id = servicesDrawablesInative.get(serviceName);
                break;
            case SERVICE_MENU_ACTIVE:
                id = servicesDrawablesMenuActive.get(serviceName);
                break;
            case SERVICE_MENU_INACTIVE:
                id = servicesDrawablesmenuInactive.get(serviceName);
                break;
            case SERVICE_POPUP:
                id = servicesDrawablesPopup.get(serviceName);
                break;
            default:break;
        }
        return id ;
    }

    public String  getCityName(double mLatitude, double mLongitude,Context context) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
            if (addresses.size() > 0){
                // System.out.println("------------address->"+addresses.toString());
                String city ="";
                city = addresses.get(0).getSubLocality();
                if (city == null || city.length() == 0) {
                    city =  addresses.get(0).getLocality();//.getAddressLine(0);
                }
                return city;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
