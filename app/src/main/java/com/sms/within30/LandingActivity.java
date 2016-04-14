package com.sms.within30;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sms.within30.adapters.ServicesSearchAdapter;
import com.sms.within30.dataobjects.CustomerDO;
import com.sms.within30.dataobjects.ServicesDO;
import com.sms.within30.sidemenu.interfaces.Resourceble;
import com.sms.within30.sidemenu.interfaces.ScreenShotable;
import com.sms.within30.utilities.NetworkUtility;
import com.sms.within30.webservices.Response;
import com.sms.within30.webservices.businesslayer.CommonBL;
import com.sms.within30.webservices.businesslayer.DataListener;
import com.sms.within30.wheel.MaterialColor;
import com.sms.within30.wheel.TextDrawable;
import com.sms.within30.wheel.WheelArrayAdapter;
import com.sms.within30.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.TRANSPARENT;


public class LandingActivity extends BaseActivity implements View.OnClickListener,DataListener {
    RelativeLayout homeLayout;
    private static final String KEY_DEMO = "demo";

   // private static final int ITEM_COUNT = 7;
    ImageView btmenu;
    AutoCompleteTextView autoCompleteTextView;
    WheelView wheelView;
    ImageView img_filter;
    LinearLayout llmenu;
    TextView tvbusinessower;

   // HashMap<Integer,String> categories = new HashMap<Integer,String>();

    Animation wheelOpen;
    Animation wheelClose;
   // com.ogaclejapan.arclayout.ArcLayout arc_layout;
    int currentlocation = 0;

    List<ServicesDO> servicesList;
    ArrayList<String> searchArrayList = new ArrayList<String>();
    ServicesSearchAdapter servicesSearchAdapter;
    public void initialize(){
        homeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_landing, null);
       // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().hide();

        intilizeControls();

    }



    @Override
    public void loadData() {

    }



    private  void intilizeControls() {

       // placesList = new ArrayList<ServicesDO>();
        tvbusinessower = (TextView) homeLayout.findViewById(R.id.tvbusinessower);
        btmenu = (ImageView) findViewById(R.id.btmenu);
        llmenu = (LinearLayout) homeLayout.findViewById(R.id.llmenu);
        llmenu.setOnClickListener(this);
        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setActivity(this);
        wheelClose = AnimationUtils.loadAnimation(this, R.anim.wheel_close);
        wheelOpen = AnimationUtils.loadAnimation(this,R.anim.wheel_open);
        servicesSearchAdapter  = new ServicesSearchAdapter(LandingActivity.this, R.layout.category_list_item, R.id.tv_category);

        autoCompleteTextView = (AutoCompleteTextView) homeLayout.findViewById(R.id.autoCompleted_categorysearch);
        autoCompleteTextView.setAdapter(servicesSearchAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ServicesDO servicesDO = (ServicesDO) adapterView.getItemAtPosition(position);
                autoCompleteTextView.setText(servicesDO.getName());

                Intent mapsIntent = new Intent(LandingActivity.this,MapsActivity.class);
                mapsIntent.putExtra("actionbar_title",servicesDO.getName());
             //   mapsIntent.putExtra("category_type","hospitals");
                mapsIntent.putExtra("service_id",servicesDO.get_id());
                startActivity(mapsIntent);
            }
        });
        img_filter = (ImageView) homeLayout.findViewById(R.id.img_filter);

        img_filter.setOnClickListener(this);
        tvbusinessower.setOnClickListener(this);
        //a listener for receiving a callback for when the item closest to the selection angle changes
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, Drawable itemDrawable, int position) {
                //get the item at this position
                Map.Entry<String, Integer> selectedEntry = ((MaterialColorAdapter) parent.getAdapter()).getItem(position);
                parent.setSelectionColor(getContrastColor(selectedEntry));
            }
        });

        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                String msg = String.valueOf(position) + " " + isSelected;
                Log.i("wheel position", "" + position);
                ServicesDO servicesDO = (ServicesDO)servicesList.get(position);
                Intent mapsIntent = new Intent(LandingActivity.this, MapsActivity.class);
                mapsIntent.putExtra("actionbar_title", servicesDO.getName());
                mapsIntent.putExtra("service_id",servicesDO.get_id());
               // mapsIntent.putExtra("service_type", "hospitals");
                startActivity(mapsIntent);
            }
        });

     //  setMenuItems();
        if (NetworkUtility.isNetworkConnectionAvailable(LandingActivity.this)) {
            if(new CommonBL(LandingActivity.this, LandingActivity.this).getServices()){
                if (pd == null) {

                    pd =  new ProgressDialog(LandingActivity.this);
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

    private int getContrastColor(Map.Entry<String, Integer> entry) {
        String colorName = MaterialColor.getColorName(entry);
        return MaterialColor.getContrastColor(colorName);
    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.llmenu) {
            if (wheelView.getVisibility() == View.VISIBLE) {
                wheelView.startAnimation(wheelClose);
                wheelView.setVisibility(View.INVISIBLE);
                btmenu.setImageResource(R.mipmap.menu_open);
            }else{
                wheelView.startAnimation(wheelOpen);
                wheelView.setVisibility(View.VISIBLE);
                btmenu.setImageResource(R.mipmap.menu_open);
            }
        }else if (v.getId() == R.id.img_filter) {
            showCustomDialog(LandingActivity.this);
        }else if (v.getId() == R.id.tvbusinessower) {
          /**  if (NetworkUtility.isNetworkConnectionAvailable(LandingActivity.this)) {
                Intent mapsIntent = new Intent(LandingActivity.this,MapsActivity.class);
                mapsIntent.putExtra("actionbar_title","Dentist");
                mapsIntent.putExtra("category_type","hospitals");
                startActivity(mapsIntent);
            }else{
                showToast(getResources().getString(R.string.Unable_to_connect_server_please_try_again));
            }*/
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    static class MaterialColorAdapter extends WheelArrayAdapter<Map.Entry<String, Integer>> {
        Activity activity;
        MaterialColorAdapter(List<Map.Entry<String, Integer>> entries,Activity activity) {
            super(entries);
            this.activity = activity;
        }

        @Override
        public Drawable getDrawable(int position) {
            Drawable[] drawable = new Drawable[] {
                    createOvalDrawable(getItem(position).getValue()),
                    new TextDrawable(String.valueOf(position))
            };
            return new LayerDrawable(drawable);

            // return getDrawable(R.drawable.salon);
        }

        private Drawable createOvalDrawable(int color) {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
            //  return getDrawable(R.drawable.salon);
        }

        @Override
        public View getItem(int position, View convertView, ViewGroup parent) {
          /*  LayoutInflater inflater = (LayoutInflater)activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);*/
            return convertView;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

           /* LayoutInflater inflater = (LayoutInflater)activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);*/
            return convertView;
        }
    }

    private void  showCustomDialog(Context context){
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

   /* public void setMenuItems(){

        try {

            List<ServicesDO> tempList = new ArrayList<ServicesDO>();
            ServicesDO  servicesDO = new ServicesDO();
            servicesDO.setName("Hair Saloons");
            servicesDO.setName("Hair Saloons");
            tempList.add(servicesDO);

            ServicesDO  servicesDO1 = new ServicesDO();
            servicesDO1.setName("Spas");
            servicesDO1.setName("Spas");
            tempList.add(servicesDO1);

            ServicesDO  servicesDO2 = new ServicesDO();
            servicesDO2.setName("Dentist");
            servicesDO2.setName("Dentist");
            tempList.add(servicesDO2);

            ServicesDO  servicesDO3 = new ServicesDO();
            servicesDO3.setName("Diagnostics");
            servicesDO3.setName("Diagnostics");
            tempList.add(servicesDO3);

            ServicesDO  servicesDO4 = new ServicesDO();
            servicesDO4.setName("Car Maintenance");
            servicesDO4.setName("Car Maintenance");
            tempList.add(servicesDO4);

            ServicesDO  servicesDO5 = new ServicesDO();
            servicesDO5.setName("Legal Services");
            servicesDO5.setName("Legal Services");
            tempList.add(servicesDO5);

            ServicesDO  servicesDO6 = new ServicesDO();
            servicesDO6.setName("Photographers");
            servicesDO6.setName("Photographers");
            tempList.add(servicesDO6);

            servicesList = tempList;
            System.out.println("servicesList->"+servicesList.size());
            if (servicesList!=null) {
                servicesSearchAdapter.refreshServicesList(servicesList);
                wheelView.setWheelItemCount(servicesList.size());
                //create data for the adapter
                List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(servicesList.size());

                for (int i = 0; i < servicesList.size(); i++) {
                    Map.Entry<String, Integer> entry = MaterialColor.random(this, "\\D*_500$");
                    entries.add(entry);
                }

                //populate the adapter, that knows how to draw each item (as you would do with a ListAdapter)
                wheelView.setAdapter(new MaterialColorAdapter(entries, LandingActivity.this));
                wheelView.setSetvices(servicesList);

            }
        }catch(Exception e){
              e.printStackTrace();;
        }

    }
*/

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
                case WS_SERVICES:
                    if(data.data!=null && data.data instanceof List<?>)
                    {
                        servicesList = (List<ServicesDO>)data.data;
                        System.out.println("servicesList->"+servicesList.size());
                        if (servicesList!=null) {
                            servicesSearchAdapter.refreshServicesList(servicesList);
                            wheelView.setWheelItemCount(servicesList.size());
                            //create data for the adapter
                            List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(servicesList.size());

                            for (int i = 0; i < servicesList.size(); i++) {
                                Map.Entry<String, Integer> entry = MaterialColor.random(this, "\\D*_500$");
                                entries.add(entry);
                            }

                            //populate the adapter, that knows how to draw each item (as you would do with a ListAdapter)
                            wheelView.setAdapter(new MaterialColorAdapter(entries, LandingActivity.this));
                            wheelView.setSetvices(servicesList);
                            wheelView.refreshDrawableState();
                        }
                    }else if(data.data instanceof String){
                        String str = (String)data.data;
                        showToast(str);
                    }
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        return null;
    }

    @Override
    public void disableHomeButton() {

    }

    @Override
    public void enableHomeButton() {

    }

    @Override
    public void addViewToContainer(View view) {

    }
}
