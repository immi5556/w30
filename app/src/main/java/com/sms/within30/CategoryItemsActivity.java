package com.sms.within30;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ogaclejapan.arclayout.ArcLayout;
import com.sms.within30.adapters.CategoryItemsAdapter;
import com.sms.within30.dataobjects.CategoriesDO;
import com.sms.within30.utilities.AnimatorUtils;
import com.sms.within30.utilities.AppConstants;
import com.sms.within30.utilities.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class CategoryItemsActivity extends BaseActivity implements View.OnClickListener{


	private PreferenceUtils preference;
    LinearLayout homeLayout;
    ListView lvCategoriesItems;
    Vector<CategoriesDO> vecCatgories;
    View fab;
    View menuLayout;
    ArcLayout arcLayout;
    private CategoryItemsAdapter categoryItemsAdapter;
    public void initialize(){
        homeLayout = (LinearLayout) inflater.inflate(R.layout.activity_category_items, null);
        // llParlours.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(homeLayout);
        vecCatgories = new Vector<CategoriesDO>();
        ActionBar actionBar = getSupportActionBar();


        intilizeControls();

    }
    private  void intilizeControls() {

        fab = homeLayout.findViewById(R.id.fab);
        menuLayout = homeLayout.findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout)homeLayout. findViewById(R.id.arc_layout);

        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(CategoryItemsActivity.this);
        }

        fab.setOnClickListener(this);
        fab.setSelected(true);
        createCategoryItems();
        lvCategoriesItems = (ListView)homeLayout.findViewById(R.id.lvCategoriesItems);
      //  Context context = CategoryItemDetailsActivity.this;
        categoryItemsAdapter = new CategoryItemsAdapter(CategoryItemsActivity.this);
        lvCategoriesItems.setAdapter(categoryItemsAdapter);
        categoryItemsAdapter.refresh(vecCatgories);
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
		AppConstants.SIT_HEADLINE = Typeface.createFromAsset(getApplicationContext().getAssets(), "Gotham-Bold.ttf");;
		AppConstants.SIT_SUB_HEADLINE = Typeface.createFromAsset(getApplicationContext().getAssets(), "Gotham-Medium.ttf");;
		AppConstants.SIT_TEXT = Typeface.createFromAsset(getApplicationContext().getAssets(), "Gotham-Book.ttf");;
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
        if (v.getId() == R.id.fab) {
            onFabClick(v);
            return;
        }

        if (v instanceof LinearLayout) {
            //showToast((Button) v);
            Intent categoriesiIntent = new Intent(CategoryItemsActivity.this,CategoryItemsActivity.class);
            startActivity(categoriesiIntent);
        }




    }
    private void onFabClick(View v) {
        if (v.isSelected()) {
            hideMenu();
        } else {
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }
    @SuppressWarnings("NewApi")
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }
    private Animator createShowItemAnimator(View item) {

        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }
}
