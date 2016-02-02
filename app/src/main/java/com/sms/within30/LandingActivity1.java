package com.sms.within30;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sms.within30.adapters.CategoriesSearchAdapter;
import com.sms.within30.wheel.MaterialColor;
import com.sms.within30.wheel.TextDrawable;
import com.sms.within30.wheel.WheelArrayAdapter;
import com.sms.within30.wheel.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.TRANSPARENT;


public class LandingActivity1 extends BaseActivity implements View.OnClickListener {
    RelativeLayout homeLayout;
    private static final String KEY_DEMO = "demo";

    private static final int ITEM_COUNT = 7;
    ImageView btmenu;
    WheelView wheelView;
    ImageView img_filter;
    HashMap<Integer,String> categories = new HashMap<Integer,String>();
   // com.ogaclejapan.arclayout.ArcLayout arc_layout;
    int currentlocation = 0;
    ArrayList<String> searchArrayList = new ArrayList<String>();
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
        btmenu = (ImageView) findViewById(R.id.btmenu);
        wheelView = (WheelView) findViewById(R.id.wheelview);
        wheelView.setActivity(this);


        setCategoriesList();
        CategoriesSearchAdapter adapter = new CategoriesSearchAdapter(this,
                R.layout.category_list_item, R.id.tv_category, searchArrayList);

       final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) homeLayout.findViewById(R.id.autoCompleted_categorysearch);
        autoCompleteTextView.setAdapter(adapter);


        img_filter = (ImageView) homeLayout.findViewById(R.id.img_filter);

        //create data for the adapter
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(ITEM_COUNT);


        for (int i = 0; i < ITEM_COUNT; i++) {
            Map.Entry<String, Integer> entry = MaterialColor.random(this, "\\D*_500$");
            entries.add(entry);
        }

        //populate the adapter, that knows how to draw each item (as you would do with a ListAdapter)
        wheelView.setAdapter(new MaterialColorAdapter(entries,LandingActivity1.this));

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
             //   Toast.makeText(LandingActivity1.this, msg, Toast.LENGTH_SHORT).show();
                Intent mapsIntent = new Intent(LandingActivity1.this,MapsActivity.class);
                mapsIntent.putExtra("actionbar_title","Dentist");
                startActivity(mapsIntent);
            }
        });

        //initialise the selection drawable with the first contrast color
        wheelView.setSelectionColor(getContrastColor(entries.get(0)));
        btmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* wheelView.animate()

                        .rotation(100.0f)
                        .translationX(50)
                        .translationY(100)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                wheelView.setVisibility(View.INVISIBLE);
                            }
                        });*/
            }
        });

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(LandingActivity1.this);
            }
        });
    }

    private int getContrastColor(Map.Entry<String, Integer> entry) {
        String colorName = MaterialColor.getColorName(entry);
        return MaterialColor.getContrastColor(colorName);
    }





    private void setCategoriesList(){
        searchArrayList.add("Dentist");
        searchArrayList.add("France");
        searchArrayList.add("Spa Massage");
        searchArrayList.add("Salon");
        searchArrayList.add("Counselling");
        searchArrayList.add("Law");
        searchArrayList.add("Auto Services");
    }


    @Override
    public void onClick(View v) {


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
}
