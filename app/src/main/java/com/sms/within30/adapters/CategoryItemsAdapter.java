package com.sms.within30.adapters;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sms.within30.BaseActivity;
import com.sms.within30.CategoryItemDetailsActivity;
import com.sms.within30.R;
import com.sms.within30.dataobjects.CategoriesDO;
import com.sms.within30.utilities.AppConstants;

public class CategoryItemsAdapter extends BaseAdapter
{
	private Context context;

//	private int itemsResources[] = new int[] {
//			R.drawable.parlour_one,R.drawable.parlour_three, R.drawable.parlour_two,
//			R.drawable.parlour_one,R.drawable.parlour_three, R.drawable.parlour_two,
//			R.drawable.parlour_one,R.drawable.parlour_three, R.drawable.parlour_two,
//			R.drawable.parlour_one,R.drawable.parlour_three, R.drawable.parlour_two};
	
	private Vector<CategoriesDO> vecParlours;
	private ImageLoader imgLoader;
	private DisplayImageOptions displayImageOptions;
	private String CategoryName;
	
	public void setCategory(String categoryName)
	{
		this.CategoryName = categoryName;
	}
	
	private int mHeight, mWidth;
	public CategoryItemsAdapter(Context context)
	{
		this.context	= context;
		/*imgLoader = ((BaseActivity)context).initImageLoader(context, R.drawable.noimage);
		displayImageOptions = ((BaseActivity)context).getDisplayOptions(R.drawable.dashboard_noimage);
		Drawable d = context.getResources().getDrawable(R.drawable.dashboard_noimage);
		mHeight = d.getIntrinsicHeight();
		mWidth = d.getIntrinsicWidth();*/
	}
	@Override
	public int getCount() 
	{
		if(vecParlours != null && vecParlours.size()>0)
			return vecParlours.size();
		else
			return 0;
//		return itemsResources.length;
	}

	@Override
	public Object getItem(int position) 
	{
		return vecParlours.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	
	public void refresh(Vector<CategoriesDO> vecCategories)
	{
		this.vecParlours = vecCategories;
        System.out.println("vecParlours->"+vecParlours);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;
		final CategoriesDO parlourDO = vecParlours.get(position);
		if(convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.category_items_cell, null);
			viewHolder = new ViewHolder();
			viewHolder.tvComapanyName 			= 	(TextView)		convertView.findViewById(R.id.tvComapanyName);

			viewHolder.tvmiles 			= 	(TextView)		convertView.findViewById(R.id.tvmiles);
			viewHolder.tvrating 		= 	(TextView)		convertView.findViewById(R.id.tvrating);
			viewHolder.ivCategoryItemPic 	= 	(ImageView)		convertView.findViewById(R.id.ivCategoryItemPic);
			viewHolder.pBar					=	(ProgressBar)	convertView.findViewById(R.id.pBar);

			convertView.setTag(viewHolder);

		}
		else 
		{
			viewHolder = (ViewHolder) convertView.getTag(); 
		}

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent detailsIntent  = new Intent(context, CategoryItemDetailsActivity.class);
				context.startActivity(detailsIntent);
			}
		});
		
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView		tvComapanyName ;
		TextView		tvopenSlots;
		TextView		tvmiles;
		TextView		tvrating;

		ImageView		ivCategoryItemPic;
		ProgressBar		pBar;
	}

}
