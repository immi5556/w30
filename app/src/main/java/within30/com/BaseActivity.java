package within30.com;




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
import within30.com.dataobjects.ServicesDO;
import within30.com.lib.SlidingUpPanelLayout;
import within30.com.sidemenu.fragment.ContentFragment;
import within30.com.sidemenu.fragment.MyLInearLayout;
import within30.com.sidemenu.interfaces.Resourceble;
import within30.com.sidemenu.interfaces.ScreenShotable;
import within30.com.sidemenu.model.SlideMenuItem;
import within30.com.sidemenu.util.ViewAnimator;
import within30.com.utilities.AppConstants;
import within30.com.utilities.W30Utilities;

public abstract class BaseActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener{

	public LinearLayout llBaseMenuLeft,llBody;
	public TextView tvTitle;
	public LayoutInflater inflater;
	private Toast toast;

	public boolean mSearchCheck;
	public DrawerLayout mLayoutDrawer;

	private View bottomBarNullifier;
	public Toolbar mToolbar;
	protected Dialog dialog;
	protected ProgressDialog progressDialog;
	public ProgressDialog pd;

	//side menu
	public ViewAnimator viewAnimator;
	public LinearLayout linearLayout;
	public List<SlideMenuItem> list = new ArrayList<>();

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
	protected int mSignInProgress;
	protected static final String SAVED_PROGRESS = "sign_in_progress";
	protected static final int STATE_DEFAULT = 0;
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


		AppConstants.DEVICE_DENSITY = metrics.density;
		AppConstants.DISPLAY_WIDTH 	= metrics.widthPixels;
		AppConstants.DISPLAY_HEIGHT = metrics.heightPixels;
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.more_options);

        mToolbar.setVisibility(View.VISIBLE);
		
		tvTitle						= (TextView) mToolbar.findViewById(R.id.tvTitle);
		llBody						= (LinearLayout) findViewById(R.id.llBody);
		bottomBarNullifier			= (View) findViewById(R.id.bottomBarNullifier);

		mLayoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);		inflater = this.getLayoutInflater();

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

		viewAnimator = new ViewAnimator<>(BaseActivity.this, list, myLInearLayout, mLayoutDrawer, this);

	}
	


List<ServicesDO> servicesList;
	public void createMenuList(List<ServicesDO> servicesList) {
		this.servicesList = servicesList;
		if (list !=null && list.size()>0){
			 list.clear();
		}
		try{
			Drawable imgCross = getResources().getDrawable(R.mipmap.cross);
			Drawable imgHome = getResources().getDrawable(R.mipmap.menu_home);
			Drawable imgSettings = getResources().getDrawable(R.mipmap.menu_steetings);
			SlideMenuItem menu_Item_close = new SlideMenuItem(ContentFragment.CLOSE, R.mipmap.cross);
			list.add(menu_Item_close);
			SlideMenuItem menu_Item_home = new SlideMenuItem(ContentFragment.HOME, R.mipmap.menu_home);
			list.add(menu_Item_home);
			if (servicesList !=null) {
				for (ServicesDO servicesDO:servicesList){
					SlideMenuItem menu_Item = null ;
					W30Utilities w30Utilities = new W30Utilities();
					int id =0;
					if (servicesDO.isActive()) {
						id = w30Utilities.getServiceImage(W30Utilities.SERVICE_MENU_ACTIVE,servicesDO.getName());
					}else {

							id = w30Utilities.getServiceImage(W30Utilities.SERVICE_MENU_INACTIVE,servicesDO.getName());

					}
					menu_Item= new SlideMenuItem(servicesDO.getName(), id);
					menu_Item.set_id(servicesDO.get_id());

					if (servicesDO.isSelectedService()) {
						menu_Item.setIsServiceSelected(true);
						id = w30Utilities.getServiceImage(W30Utilities.SERVICE_MENU_ACTIVE,servicesDO.getName());
					}else {
						menu_Item.setIsServiceSelected(false);
					}


					list.add(menu_Item);

				}
				SlideMenuItem menu_Item_settings = new SlideMenuItem(ContentFragment.SETTINGS, R.mipmap.menu_steetings);
				list.add(menu_Item_settings);

			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

	    menu.findItem(R.id.menu_reset).setVisible(false);
		menu.findItem(R.id.menu_edit).setVisible(false);
		mSearchCheck = false;
		return true;
	}
	
	private OnQueryTextListener OnQuerySearchView = new OnQueryTextListener() {
		
		@Override
		public boolean onQueryTextSubmit(String arg0) {

			showToast(arg0);
			return false;
		}
		
		@Override
		public boolean onQueryTextChange(String arg0) {

			if (mSearchCheck){
				// implement your search here
				showToast(arg0);
			}
			return false;
		}
	};
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

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