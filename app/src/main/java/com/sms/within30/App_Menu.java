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
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class App_Menu extends BaseActivity implements View.OnClickListener {
    LinearLayout homeLayout;

    public void initialize(){
        homeLayout = (LinearLayout) inflater.inflate(R.layout.app_menu, null);
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

    }




    @Override
    public void onClick(View v) {


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
