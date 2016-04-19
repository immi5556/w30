package com.sms.within30;




import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.sms.within30.dataobjects.ServicesDO;
import com.sms.within30.sidemenu.fragment.ContentFragment;
import com.sms.within30.sidemenu.fragment.MyLInearLayout;
import com.sms.within30.sidemenu.interfaces.Resourceble;
import com.sms.within30.sidemenu.interfaces.ScreenShotable;
import com.sms.within30.sidemenu.model.SlideMenuItem;
import com.sms.within30.sidemenu.util.ViewAnimator;
import com.sms.within30.utilities.AppConstants;

public abstract class BaseActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener{

	public LinearLayout llBaseMenuLeft,llBody;
	public TextView tvTitle;
	public LayoutInflater inflater;
	private Toast toast;

	public boolean mSearchCheck;
	public DrawerLayout mLayoutDrawer;
	//private ActionBarDrawerToggleCompat mDrawerToggle;
	private View bottomBarNullifier;
	public Toolbar mToolbar;
	protected Dialog dialog;
	protected ProgressDialog progressDialog;
	public ProgressDialog pd;

	//side menu
	public ViewAnimator viewAnimator;
	public LinearLayout linearLayout;
	public List<SlideMenuItem> list = new ArrayList<>();
	public ActionBarDrawerToggle drawerToggle;
//	public Spinner navSpinner;
	
	public ImageLoader initImageLoader(Context context, int resId)
	{
		ImageLoader imgLoader = ImageLoader.getInstance();
		DisplayImageOptions defaultOptions = getDisplayOptions(resId);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .defaultDisplayImageOptions(defaultOptions)
        .build();
		imgLoader.init(config);
		return imgLoader;
	}
	
	public DisplayImageOptions getDisplayOptions(int resId)
	{
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .considerExifParams(true)
        .showImageOnLoading(resId)
        .showImageOnFail(resId)
        .showImageForEmptyUri(resId)
        .build();
		return defaultOptions;
	}
	/** Google+ Code */
	protected boolean mIntentInProgress;

	protected static final int RC_SIGN_IN = 0;
	protected int mSignInProgress;
	protected static final String SAVED_PROGRESS = "sign_in_progress";
	protected static final int STATE_DEFAULT = 0;
	protected static final int STATE_SIGN_IN = 1;
	protected static final int STATE_IN_PROGRESS = 2;
	// Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
	protected PendingIntent mSignInIntent;
	// Used to store the error code most recently returned by Google Play services
    // until the user clicks 'sign in'.
	protected int mSignInError;

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		super.onActivityResult(requestCode, requestCode, intent);

	}
	
	
	public static boolean hasSoftKeys(WindowManager windowManager){
	    Display d = windowManager.getDefaultDisplay();

	    DisplayMetrics realDisplayMetrics = new DisplayMetrics();
	    d.getRealMetrics(realDisplayMetrics);

	    int realHeight = realDisplayMetrics.heightPixels;
	    int realWidth = realDisplayMetrics.widthPixels;

	    DisplayMetrics displayMetrics = new DisplayMetrics();
	    d.getMetrics(displayMetrics);

	    int displayHeight = displayMetrics.heightPixels;
	    int displayWidth = displayMetrics.widthPixels;

	    return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
	}
	MyLInearLayout myLInearLayout ;
	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(arg0); 
		if (arg0 != null) {
            mSignInProgress = arg0
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }
		
		setContentView(R.layout.base_activity);
		Resources res 			= getResources();
		DisplayMetrics metrics 	= res.getDisplayMetrics();
		Log.e("Density ", metrics.density+"");

		AppConstants.DEVICE_DENSITY = metrics.density;
		AppConstants.DISPLAY_WIDTH 	= metrics.widthPixels;
		AppConstants.DISPLAY_HEIGHT = metrics.heightPixels;
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.more_options);

        mToolbar.setVisibility(View.VISIBLE);
		
		tvTitle						= (TextView) mToolbar.findViewById(R.id.tvTitle);
//		navSpinner					= (Spinner) mToolbar.findViewById(R.id.navSpinner);
		llBody						= (LinearLayout) findViewById(R.id.llBody);
		bottomBarNullifier			= (View) findViewById(R.id.bottomBarNullifier);

		mLayoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
	//	mDrawerToggle = new ActionBarDrawerToggleCompat(this, mLayoutDrawer);

		
		inflater = this.getLayoutInflater();

		tvTitle.setTypeface(AppConstants.SIT_HEADLINE);
		

		if(hasSoftKeys(getWindowManager()))
		{
			bottomBarNullifier.setVisibility(View.VISIBLE);
		}
		initialize();

		mLayoutDrawer.setScrimColor(Color.TRANSPARENT);
		linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
		linearLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mLayoutDrawer.closeDrawers();
			}
		});
		myLInearLayout = new MyLInearLayout(BaseActivity.this).newInstance();

		setActionBar();
		//mLayoutDrawer.setDrawerListener(drawerToggle);
		//createMenuList();
		viewAnimator = new ViewAnimator<>(BaseActivity.this, list, myLInearLayout, mLayoutDrawer, this);
	//	this.getWindow().setStatusBarColor(getResources().getColor(R.color.backgroung_actionbar));
	}
	
	protected SearchView searchView;
	private void setActionBar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		drawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mLayoutDrawer,         /* DrawerLayout object */
				toolbar,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				linearLayout.removeAllViews();
				linearLayout.invalidate();
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
					viewAnimator.showMenuContent();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		mLayoutDrawer.setDrawerListener(drawerToggle);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
List<ServicesDO> servicesList;
	public void createMenuList(List<ServicesDO> servicesList) {
		this.servicesList = servicesList;
		/*SlideMenuItem menuItem0 = new SlideMenuItem(ContentFragment.CLOSE, R.mipmap.cross);
		list.add(menuItem0);
		SlideMenuItem menuItem = new SlideMenuItem(ContentFragment.SALON, R.mipmap.menu_salon);
		list.add(menuItem);
		SlideMenuItem menuItem2 = new SlideMenuItem(ContentFragment.SPA, R.mipmap.menu_spa);
		list.add(menuItem2);
		SlideMenuItem menuItem3 = new SlideMenuItem(ContentFragment.DENTIST, R.mipmap.menu_dentist);
		list.add(menuItem3);
		SlideMenuItem menuItem4 = new SlideMenuItem(ContentFragment.LEGAL_SERVICES, R.mipmap.menu_law);
		list.add(menuItem4);
		SlideMenuItem menuItem5 = new SlideMenuItem(ContentFragment.CAR_MAINTENANCE, R.mipmap.menu_car_maintainence);
		list.add(menuItem5);
		SlideMenuItem menuItem6 = new SlideMenuItem(ContentFragment.DIAGNOSTICKS, R.mipmap.menu_diagnostics);
		list.add(menuItem6);
		SlideMenuItem menuItem7 = new SlideMenuItem(ContentFragment.PHOTOGRAPHERS, R.mipmap.menu_photographers);
		list.add(menuItem7);*/

		try{
			SlideMenuItem menu_Item_close = new SlideMenuItem(ContentFragment.CLOSE, R.mipmap.cross);
			list.add(menu_Item_close);
			if (servicesList !=null) {
				for (ServicesDO servicesDO:servicesList){
					SlideMenuItem menu_Item = null ;
					if (servicesDO.getName().equalsIgnoreCase("Hair Salon")) {
						menu_Item= new SlideMenuItem(servicesDO.getName(), R.mipmap.menu_salon);

					}else if (servicesDO.getName().equalsIgnoreCase("Massage & Spa")) {
						menu_Item= new SlideMenuItem(servicesDO.getName(), R.mipmap.menu_spa);
					}else if (servicesDO.getName().equalsIgnoreCase("Diagnostics")) {
						menu_Item= new SlideMenuItem(servicesDO.getName(), R.mipmap.menu_diagnostics);
					}else if (servicesDO.getName().equalsIgnoreCase("Legal")) {
						menu_Item= new SlideMenuItem(servicesDO.getName(), R.mipmap.menu_law);
					}else if (servicesDO.getName().equalsIgnoreCase("Automobile Maintenance")) {
						menu_Item= new SlideMenuItem(servicesDO.getName(), R.mipmap.menu_car_maintainence);
					}else if (servicesDO.getName().equalsIgnoreCase("Dental")) {
						menu_Item= new SlideMenuItem(servicesDO.getName(), R.mipmap.menu_dentist);
					}else if (servicesDO.getName().equalsIgnoreCase("Photography")) {
						menu_Item= new SlideMenuItem(servicesDO.getName(), R.mipmap.menu_photographers);
					}
					menu_Item.set_id(servicesDO.get_id());
					list.add(menu_Item);
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		/*searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
	    searchView.setQueryHint(this.getString(R.string.search));
	    
	    ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
        .setHintTextColor(getResources().getColor(R.color.white));	    
	    searchView.setOnQueryTextListener(OnQuerySearchView);*/

	    menu.findItem(R.id.menu_filter).setVisible(false);

		mSearchCheck = false;
		return true;
	}
	
	private OnQueryTextListener OnQuerySearchView = new OnQueryTextListener() {
		
		@Override
		public boolean onQueryTextSubmit(String arg0) {
			// TODO Auto-generated method stub
			showToast(arg0);
			return false;
		}
		
		@Override
		public boolean onQueryTextChange(String arg0) {
			// TODO Auto-generated method stub
			if (mSearchCheck){
				// implement your search here
				showToast(arg0);
			}
			return false;
		}
	};
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		/*switch (item.getItemId()) {
		case android.R.id.home:
			resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
		case R.id.menu_search:
			mSearchCheck = true;
			break;
		case R.id.menu_filter:
			resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
			break;
		case R.id.menu_profile:
			Intent intent = new Intent(BaseActivity.this,EditProfileActivity.class);
			startActivity(intent);
			break;
		}*/
		return true;
	}	



	@Override
	public void onBackPressed() 
	{
        super.onBackPressed();
	}




	


	
	public abstract void initialize();





	/**
	 * method is declared to load the data in respective activities
	 */
	public abstract void loadData();

	/**
	 * This method is to show the loading progress dialog when some other
	 * functionality is taking place.
	 **/
	
	public void showLoader(String str) 
	{
		//runOnUiThread(new RunShowLoader(getResources().getString(R.string.loading)));
	}


	/**
	 * Name:         RunShowLoader
	   Description:  This is to show the loading progress dialog when some other functionality is taking place.**/
	class RunShowLoader implements Runnable
	{
		private String strMsg;

		public RunShowLoader(String strMsg) 
		{
			this.strMsg = strMsg;
		}
		public void run() {
			try {

				dialog = new Dialog(BaseActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setCancelable(false);
				dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				//dialog.setContentView (R.layout.loader_custom);
				dialog.show ();
			} catch (Exception e) {
				progressDialog = null;
			}
		}
	}
	/**
	 * Name:         RunShowLoader
	   Description:  For hiding progress dialog (Loader ).**/
	public void hideLoader() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() 
			{
				try {

					if(dialog!=null && dialog.isShowing())
					{
						dialog.dismiss();
						dialog.hide();
					}
					dialog = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
//	public void showLoader(String msg) {
//		runOnUiThread(new RunShowLoader(msg, ""));
//	}
//
//	public void showLoader(String msg, String title) {
//		runOnUiThread(new RunShowLoader(msg, title));
//	}
	public void hideKeyBoard(View v) 
	{
		if(v != null)
		{
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}





	@Override
	protected void onDestroy() {
		super.onDestroy();
		hideLoader();
	}

	public void changeHintDrawable(final EditText editText, final int hintIcon, final int hintStr)
	{
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean gotfocus) {
				if(gotfocus){
					editText.setHint("");
					editText.setCompoundDrawables(null, null, null, null);
				}
				else if(!gotfocus){
					if(editText.getText().length()==0)
					{
						editText.setHint(hintStr);
						editText.setCompoundDrawablesWithIntrinsicBounds(hintIcon, 0, 0, 0);
					}
				}
			}
		});
	}
	public void showToast(String message)
	{
		if(toast != null)
			toast.cancel();

		toast = Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}

	public void setSpecificTypeFace(ViewGroup group, Typeface typeface) 
	{
		int count = group.getChildCount();
		View v;
		for(int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if(v instanceof TextView || v instanceof Button || v instanceof EditText/*etc.*/)
				((TextView)v).setTypeface(typeface);
			else if(v instanceof ViewGroup)
				setSpecificTypeFace((ViewGroup)v, typeface);
		}
	}

	public void setTypeFaceRobotoNormal(ViewGroup group) 
	{
		int count = group.getChildCount();
		View v;
		for(int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if(v instanceof TextView || v instanceof Button || v instanceof EditText/*etc.*/)
				((TextView)v).setTypeface(AppConstants.SIT_SUB_HEADLINE);
			else if(v instanceof ViewGroup)
				setTypeFaceRobotoNormal((ViewGroup)v);
		}
	}
	
	public void setColor(ViewGroup group, int color) 
	{
		int count = group.getChildCount();
		View v;
		for(int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if(v instanceof TextView || v instanceof Button || v instanceof EditText/*etc.*/)
				((TextView)v).setTextColor(color);
			else if(v instanceof ViewGroup)
				setTypeFaceRobotoNormal((ViewGroup)v);
		}
	}

	/**
	 * created to make search easy
	 * @author Shyam
	 * @param <T>
	 */
	public interface Predicate<T> {
		boolean apply(T type);
	}
	public static <T> Collection<T> filter(Collection<T> col, Predicate<T> predicate) {
		Collection<T> result = new ArrayList<T>();
		if(col!=null)
		{
			for (T element : col) {
				if (predicate.apply(element)) {
					result.add(element);
				}
			}
		}
		return result;
	}
	
	public static <T> Collection<T> filterByVector(Collection<T> col, Predicate<T> predicate) {
		Collection<T> result = new Vector<T>();
		if(col!=null)
		{
			for (T element : col) {
				if (predicate.apply(element)) {
					result.add(element);
				}
			}
		}
		return result;
	}

	public void showSettingsAlert()
	{
		/*showCustomDialog(BaseActivity.this, "GPS Settings", "GPS is not enabled.So please enable GPS for better Location.",
				"Settings", null, "Settings");*/
	}

	public void onSoftKeyboardClosed() {

	}
	
	
	public void Camera(int requestCode)
	{
		/*File imageFile = new File(CameraUtils.filePathBuilder(AppConstants.SALON_TEMP_FOLDER, "Temp"));
		Camera(requestCode, imageFile);*/
	}
	
	public void Camera(int requestCode, File f)
	{
		Uri imageFileUri = Uri.fromFile(f); // convert path to Uri
		new File(f.getParent()).mkdirs();
		Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri); 
		startActivityForResult(it, requestCode);
	}
	public void Gallery(int requestCode, File f)
	{
		Uri imageFileUri = Uri.fromFile(f); // convert path to Uri
		new File(f.getParent()).mkdirs();
		Intent it = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
		//it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri); 
		startActivityForResult(it, requestCode);
		
		// Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       //  startActivityForResult(intent, 2);
	}
	
	public void Camera(int requestCode, Uri uri)
	{
		Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri); 
		startActivityForResult(it, requestCode);
	}
	
/*	@SuppressWarnings("deprecation")
	private class ActionBarDrawerToggleCompat extends ActionBarDrawerToggle {

		public ActionBarDrawerToggleCompat(Activity mActivity, DrawerLayout mDrawerLayout){
            super(mActivity,mDrawerLayout,R.string.open,R.string.close);
           // super(mActivity, mDrawerLayout, R.drawable.ic_action_navigation_drawer, R.drawable.ic_action_navigation_drawer);
			//super(mActivity, mDrawerLayout, "open","close"*//*R.mipmap.ic_action_navigation_drawer, R.mipmap.ic_action_navigation_drawer*//*);
//			//super(
//			    mActivity,
//			    mDrawerLayout,
//  			    R.drawable.ic_action_navigation_drawer,
//				R.string.app_name,
//				R.string.app_name);
		}
		
		@Override
		public void onDrawerClosed(View view) {			
			supportInvalidateOptionsMenu();				
		}

		@Override
		public void onDrawerOpened(View drawerView) {	
			supportInvalidateOptionsMenu();			
		}		
	}*/
	
	/*protected void showNoInternetDialog(Context context)
	{
		final Dialog dialog = new Dialog(context);
		View view = LayoutInflater.from(context).inflate(R.layout.no_internet_screen, null);
		ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.addContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		dialog.show();
	}*/
	

	
	public void setLayoutParams(View v, int drawableId)
	{
		int [] dDimens = getDrawableDimensions(this, drawableId);
		v.getLayoutParams().width = dDimens[0];
		v.getLayoutParams().height = dDimens[1];
	}
	
	public int[] getDrawableDimensions(Context context, int id)
	{
		int[] dimensions = new int[2];
		if(context == null)
			return dimensions;
		Drawable d = getResources().getDrawable(id);
		if(d != null)
		{
			dimensions[0] = d.getIntrinsicWidth();
			dimensions[1] = d.getIntrinsicHeight();
		}
		return dimensions;
	}
	
	@Override
	 public void onStart() 
	 {
      super.onStart();

	 }
	
	  @Override
	  public void onStop() 
	  {
      super.onStop();

	  }
	 
	  @Override
   protected void onPause() 
	 {
      super.onPause();

      
   }

  @Override
  protected void onResume() 
  {
      super.onResume();

  }

	public int getActionBarHeight() {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		{
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	public ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition) {
		/*this.res = this.res == R.drawable.content_music ? R.drawable.content_films : R.drawable.content_music;
		View view = findViewById(R.id.content_frame);
		int finalRadius = Math.max(view.getWidth(), view.getHeight());
		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
		animator.setInterpolator(new AccelerateInterpolator());
		animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

		findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
		animator.start();
		ContentFragment contentFragment = ContentFragment.newInstance(this.res);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, contentFragment).commit();*/
		MyLInearLayout myLInearLayout = new MyLInearLayout(BaseActivity.this);
		return myLInearLayout;
	}



	@Override
	public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
		switch (slideMenuItem.getName()) {
			case ContentFragment.CLOSE:
				return screenShotable;
			default:
				return replaceFragment(screenShotable, position);
		}
	}

	@Override
	public void disableHomeButton() {
		getSupportActionBar().setHomeButtonEnabled(false);

	}

	@Override
	public void enableHomeButton() {
		getSupportActionBar().setHomeButtonEnabled(true);
		mLayoutDrawer.closeDrawers();

	}

	@Override
	public void addViewToContainer(View view) {
		linearLayout.addView(view);
	}
}