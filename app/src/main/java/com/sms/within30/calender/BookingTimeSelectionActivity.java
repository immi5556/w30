package com.sms.within30.calender;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sms.within30.BaseActivity;
import com.sms.within30.R;
import com.sms.within30.utilities.AppConstants;


/**
 * Created by Sri Krishna on 9/20/2015.
 */
public class BookingTimeSelectionActivity extends BaseActivity /*implements DataListener*/{


	public TextView tvCurrentDate, tvTiming, tvConfirm;
	private ViewPager viewPager;
	public ListView lvTiming;
	private PagerAdapter mPagerAdapter;
	private EditText etMobile;
	public int viewPagePosition = 0, viewPagerItemPosition = 0, listItemPosition = 0;
	//private HashMap<BookingMenuItemDO,BookingSubMenuItemDO> arrSelectedMenuItems;
	private View layout;
	//private DealsDO dealsDo;
	private String Time = "", Date = "";
	private int Type = 1;
	private long parlourId;
	private String mobileNo = "";
	String OpeningHours;
	ListAdapter listAdapter;
	@Override
	public void initialize() 
	{

		layout = inflater.inflate(R.layout.parlour_booking_time_activity, null);

		/*if(getIntent()!=null && getIntent().hasExtra("selection"))
			arrSelectedMenuItems = (HashMap<BookingMenuItemDO,BookingSubMenuItemDO>) getIntent().getSerializableExtra("selection");
		if(getIntent()!=null && getIntent().hasExtra("parlourId"))
			parlourId = getIntent().getIntExtra("parlourId", 0);
		if (getIntent()!=null && getIntent().hasExtra("OpeningHours")) {
			OpeningHours = getIntent().getStringExtra("OpeningHours");
		}*/
		tvCurrentDate 	= (TextView) layout.findViewById(R.id.tvCurrentDate);
		//tvTiming 		= (TextView) layout.findViewById(R.id.tvTiming);
		tvConfirm 		= (TextView) layout.findViewById(R.id.tvConfirm);
		viewPager 		= (ViewPager)layout.findViewById(R.id.viewPager);
		etMobile		= (EditText) layout.findViewById(R.id.etMobile);
		lvTiming 		= (ListView) layout.findViewById(R.id.lvTiming);

		tvTitle.setText("Schedule");
		tvTitle.setTypeface(AppConstants.SIT_HEADLINE);
		/*resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);*/

	//	dealsDo = new DealsDO();

		mPagerAdapter = new CalenderViewPagerAdapter(getSupportFragmentManager());
		listAdapter =new ListAdapter(BookingTimeSelectionActivity.this,OpeningHours);
		viewPager.setAdapter(mPagerAdapter);
		//viewPager.
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}

			@Override
			public void onPageSelected(int position) {
				
				CalendarFragment fragment = (CalendarFragment) mPagerAdapter.instantiateItem(viewPager, position);
				if (fragment != null) {
					fragment.fragmentBecameVisible(position);
				}				
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
			
		});


		lvTiming.setAdapter(listAdapter);
		listAdapter.refreshListOnSelectedDay(true);
		tvCurrentDate.setText("Avilable Slots on - "+CalendarUtils.getTitle(0));

		tvConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				/*String message = CalendarUtils.getSelectedDateForServer(viewPagePosition * 7 + viewPagerItemPosition) + " " + ListAdapter.timings.get(listItemPosition);
				//                Toast.makeText(BookingTimeSelectionActivity.this, message, Toast.LENGTH_SHORT).show();

				showLoader("Loading..");
				mobileNo = etMobile.getText().toString();
				if(mobileNo.isEmpty() || mobileNo.equalsIgnoreCase(""))
				{
					hideLoader();
					showToast("Please enter mobile number.");
				}
				else if(!isValidMobile(mobileNo) || mobileNo.length()<10 || mobileNo.length()>13)
				{
					hideLoader();
					showToast("Please enter valid mobile number.");
				}else if(!NetworkUtility.isNetworkConnectionAvailable(BookingTimeSelectionActivity.this)){
					hideLoader();
					showToast(getString(R.string.no_internet_connection));
				}else{
					AppointmentBookingBean appointmentBookingBean = new AppointmentBookingBean();
					appointmentBookingBean.setSplitString(getSplitString().toString());	
					appointmentBookingBean.setDescription(getDescription());
					appointmentBookingBean.setTotalAmount( getTotalAmount());					
					appointmentBookingBean.setAmount(0.0);
					
					appointmentBookingBean.setQuantity(0l);
					appointmentBookingBean.setId(0);
					appointmentBookingBean.setDiscountId(0);
					
					appointmentBookingBean.setDisCountAmount(0.0);
					appointmentBookingBean.setDate("");
					appointmentBookingBean.setTime("");
					appointmentBookingBean.setType(BookingTypeConstants.TYPE_APPOINTMENT);
					appointmentBookingBean.setParlourId(parlourId);	
					appointmentBookingBean.setUserId(preference.getLongInPreference(PreferenceUtils.USER_ID));
					appointmentBookingBean.setMobileNo(mobileNo);
					appointmentBookingBean.setInStatus(1);
					appointmentBookingBean.setInvid(1);					
					appointmentBookingBean.setProductId(0l);	
					new CommonBL(BookingTimeSelectionActivity.this, BookingTimeSelectionActivity.this).addInvoiceItem(appointmentBookingBean);
				}
				*/
			}
		});

		lvTiming.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{

			}
		});

		//        llBody.addView(layout);
		llBody.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
	}

	/*@Override
	public void dataRetreived(Response data) {
		if(data != null)
		{
			switch (data.method) {			
			case WS_GET_ADD_INVOICE_ITEM:
				hideLoader();
				if(data.data !=null && data.data instanceof InvoiceItemResponseDO)
				{
					InvoiceItemResponseDO invoiceDo = (InvoiceItemResponseDO) data.data;
					if(invoiceDo.GatewayURL!=null && !invoiceDo.GatewayURL.equalsIgnoreCase("") && !invoiceDo.GatewayURL.equalsIgnoreCase("null"))
					{
						Intent intent = new Intent(BookingTimeSelectionActivity.this, PaymentGateWayActivity.class);
						intent.putExtra("url", invoiceDo.GatewayURL);
						startActivity(intent);
					}
					else
						showToast(getString(R.string.server_error_please_try_again));

				}
				else if(data.data!=null)
					showToast(data.data.toString());
				break;
			default:
				break;
			}
		}
	}
*/



	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		menu.findItem(R.id.menu_filter).setVisible(false);
		menu.findItem(R.id.menu_search).setVisible(false);
		menu.findItem(R.id.menu_profile).setVisible(false);
		mSearchCheck = false;
		return true;
	}
*/
	@Override
	public void loadData() 
	{

	}
	
	public boolean isValidMobile(CharSequence target) {
		  if (TextUtils.isEmpty(target)) {
		    return false;
		  } else {
		    return android.util.Patterns.PHONE.matcher(target).matches();
		  }
		}
}
