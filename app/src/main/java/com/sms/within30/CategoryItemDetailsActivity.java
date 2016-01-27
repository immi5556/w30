package com.sms.within30;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ogaclejapan.arclayout.ArcLayout;
import com.sms.within30.adapters.CategoriesSearchAdapter;
import com.sms.within30.adapters.CategoritySubItemsSearchAdapter;
import com.sms.within30.adapters.CategoryItemDetailsAdapter;
import com.sms.within30.adapters.CategoryItemsAdapter;
import com.sms.within30.dataobjects.CategoriesDO;
import com.sms.within30.utilities.AnimatorUtils;
import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class CategoryItemDetailsActivity extends BaseActivity implements View.OnClickListener{


	private PreferenceUtils preference;
    LinearLayout homeLayout;
    GridView lvCategoriesItems;
    Vector<CategoriesDO> vecCatgories;
    View fab;
    View menuLayout;
    ArcLayout arcLayout;

     CategoryItemDetailsAdapter categoryItemDetailsAdapter;
    public void initialize(){
        homeLayout = (LinearLayout) inflater.inflate(R.layout.activity_category_items_details, null);
        // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);
        vecCatgories = new Vector<CategoriesDO>();
        ActionBar actionBar = getSupportActionBar();


        intilizeControls();

    }
    private  void intilizeControls()
    {
        createCategoryItems();
        lvCategoriesItems = (GridView)homeLayout.findViewById(R.id.gvCategoriesItems);
        categoryItemDetailsAdapter = new CategoryItemDetailsAdapter(CategoryItemDetailsActivity.this);

        lvCategoriesItems.setAdapter(categoryItemDetailsAdapter);
        categoryItemDetailsAdapter.refresh(vecCatgories);

    }
    private void  createCategoryItems(){
        for(int i =0;i<25;i++){
            CategoriesDO categoriesDO = new CategoriesDO();
            vecCatgories.add(categoriesDO);
        }

    }

    @Override
    public void loadData() {

    }
	private void initializeFonts()
	{
	//	AppConstants.SIT_HEADLINE = Typeface.createFromAsset(getApplicationContext().getAssets(), "Gotham-Bold.ttf");;
	//	AppConstants.SIT_SUB_HEADLINE = Typeface.createFromAsset(getApplicationContext().getAssets(), "Gotham-Medium.ttf");;
	//	AppConstants.SIT_TEXT = Typeface.createFromAsset(getApplicationContext().getAssets(), "Gotham-Book.ttf");;
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
        super.onResume();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public void onClick(View v) {




    }


}
