package com.sms.within30;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.sms.within30.adapters.CityAdapter;
import com.sms.within30.autoscrollview.AutoScrollViewPager;
import com.sms.within30.autoscrollview.PromotionsPagerAdapter;
import com.sms.within30.dataobjects.CitiesDO;


import java.util.Vector;


public class MainActivity extends BaseActivity {
 RelativeLayout homeLayout;
    private Spinner spinner;
    private CityAdapter adapter;
    public Vector<CitiesDO> vecCities;
    private AutoScrollViewPager autoScrollPager;

    public CitiesDO citiesDO;
@Override
public void initialize(){
    homeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_main, null);
   // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
    llBody.addView(homeLayout);
    ActionBar actionBar = getSupportActionBar();
    getSupportActionBar().setTitle("");
    tvTitle.setVisibility(View.GONE);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    /*View spinnerContainer = LayoutInflater.from(this).inflate(R.layout.toolbar_spinner, mToolbar, false);
    ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    mToolbar.addView(spinnerContainer, lp);*/

    intilizeControls();

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        menu.findItem(R.id.menu_filter).setVisible(true);
        menu.findItem(R.id.menu_search).setVisible(true);

        mSearchCheck = false;
        return true;
    }

    @Override
    public void loadData() {

    }


    private  void intilizeControls() {



        View spinnerContainer = LayoutInflater.from(this).inflate(R.layout.toolbar_spinner, mToolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mToolbar.addView(spinnerContainer, lp);

        spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        initCityVector();
        adapter = new CityAdapter(MainActivity.this, vecCities,MainActivity.this);
        System.out.print("cities->"+vecCities.size());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                citiesDO = vecCities.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initCityVector() {
        vecCities = new Vector<CitiesDO>();

        CitiesDO citiesDo1 = new CitiesDO();
        citiesDo1.CityId = 1;
        citiesDo1.CityName = "Hyderabad";
        vecCities.add(citiesDo1);
        CitiesDO citiesDo2 = new CitiesDO();
        citiesDo2.CityId = 1;
        citiesDo2.CityName = "Vijayawada";
        vecCities.add(citiesDo2);
        CitiesDO citiesDo3 = new CitiesDO();
        citiesDo3.CityId = 1;
        citiesDo3.CityName = "Khammam";
        vecCities.add(citiesDo3);
        CitiesDO citiesDo4 = new CitiesDO();
        citiesDo4.CityId = 1;
        citiesDo4.CityName = "Tirupati";
        vecCities.add(citiesDo4);
        CitiesDO citiesDo5 = new CitiesDO();
        citiesDo5.CityId = 1;
        citiesDo5.CityName = "Vizag";
        vecCities.add(citiesDo5);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
