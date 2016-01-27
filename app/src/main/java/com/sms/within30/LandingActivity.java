package com.sms.within30;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.arclayout.ArcLayout;
import com.sms.within30.adapters.CategoriesSearchAdapter;
import com.sms.within30.utilities.AnimatorUtils;
import com.sms.within30.utilities.OnSwipeTouchListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LandingActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout homeLayout;
    private static final String KEY_DEMO = "demo";
    Toast toast = null;
    View fab;
    View menuLayout;
    ArcLayout arcLayout;
    TextView login;
    TextView register;
    ImageView img_category_1,img_category_2,img_category_3,img_category_4,img_category_5;
    TextView tv_category_1,tv_category_2,tv_category_3,tv_category_4,tv_category_5;

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
        setCategories();
        intilizeControls();

    }


    @Override
    public void loadData() {

    }
private void setCategories(){
    categories.put(R.drawable.salon,"Salon");
    categories.put(R.drawable.spa_and_massage,"Spa Massages");
    categories.put(R.drawable.counselling_therapy,"Counselling");
    categories.put(R.drawable.law,"Law");
    categories.put(R.drawable.autoservices,"Auto Services");
    categories.put(R.drawable.chiropractor,"Chiropractor");
    categories.put(R.drawable.dentist,"Dentist");
    categories.put(R.drawable.finance_and_tax,"Finance and Tax");
}


    private  void intilizeControls() {
        login = (TextView) homeLayout.findViewById(R.id.login);
        register = (TextView) homeLayout.findViewById(R.id.register);
        fab = (View) homeLayout.findViewById(R.id.fab);
        menuLayout = homeLayout.findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout)homeLayout. findViewById(R.id.arc_layout);

        tv_category_1 = (TextView) homeLayout.findViewById(R.id.tv_category_1);
        tv_category_2 = (TextView) homeLayout.findViewById(R.id.tv_category_2);
        tv_category_3 = (TextView) homeLayout.findViewById(R.id.tv_category_3);
        tv_category_4 = (TextView) homeLayout.findViewById(R.id.tv_category_4);
        tv_category_5 = (TextView) homeLayout.findViewById(R.id.tv_category_5);

        img_category_1 = (ImageView) homeLayout.findViewById(R.id.img_category_1);
        img_category_2 = (ImageView) homeLayout.findViewById(R.id.img_category_2);
        img_category_3 = (ImageView) homeLayout.findViewById(R.id.img_category_3);
        img_category_4 = (ImageView) homeLayout.findViewById(R.id.img_category_4);
        img_category_5 = (ImageView) homeLayout.findViewById(R.id.img_category_5);


        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(LandingActivity.this);
        }

        fab.setOnClickListener(this);
        fab.setSelected(true);
        showMenu();
        setCategoriesList();
        CategoriesSearchAdapter adapter = new CategoriesSearchAdapter(this,
                R.layout.category_list_item, R.id.tv_category, searchArrayList);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) homeLayout.findViewById(R.id.autoCompleted_categorysearch);
        autoCompleteTextView.setAdapter(adapter);

        login.setOnClickListener(this);
        register.setOnClickListener(this);



        menuLayout.setOnTouchListener(new OnSwipeTouchListener(LandingActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(LandingActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(LandingActivity.this, "right", Toast.LENGTH_SHORT).show();

            }
            public void onSwipeLeft() {
                Toast.makeText(LandingActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(LandingActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

       // setCircleViews();

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
        if (v.getId() == R.id.fab) {
            onFabClick(v);
            return;
        }

        if (v instanceof LinearLayout) {
            //showToast((Button) v);
            Intent categoriesiIntent = new Intent(LandingActivity.this,CategoryItemsActivity.class);
            startActivity(categoriesiIntent);
        }

        if (v.getId() == R.id.login) {
          Intent loginIntent = new Intent(LandingActivity.this,LoginActivity.class);
          startActivity(loginIntent);
        }else if (v.getId() == R.id.register) {
            Intent registerIntent = new Intent(LandingActivity.this,RegisterActivity.class);
            startActivity(registerIntent);
        }

    }
    private void showToast(Button btn) {
        if (toast != null) {
            toast.cancel();
        }

        String text = "Clicked: " + btn.getText();
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();

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
        fab.setBackgroundResource(R.drawable.cross_mark);
        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
            currentlocation++;
            System.out.println("currentlocation->"+currentlocation);
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu() {
        fab.setBackgroundResource(R.drawable.plus_button);
        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
            currentlocation--;
            System.out.println("currentlocation->"+currentlocation);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
