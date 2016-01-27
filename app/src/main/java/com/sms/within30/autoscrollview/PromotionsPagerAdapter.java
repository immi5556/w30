package com.sms.within30.autoscrollview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.sms.within30.R;
import com.squareup.picasso.Picasso;


public class PromotionsPagerAdapter extends PagerAdapter {
	private Context context;
	private LayoutInflater inflater;
	private int[] promtionImages = {R.mipmap.restaurent_one,R.mipmap.restaurent_two,R.mipmap.restaurent_three,R.mipmap.restaurent_four};

	public PromotionsPagerAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() 
	{
		return promtionImages.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView ivPromotion;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout itemView = (LinearLayout) inflater.inflate(R.layout.promotions_pager_item, container, false);
		ivPromotion = (ImageView) itemView.findViewById(R.id.ivPromotion);
		
		Picasso.with(context).load(promtionImages[position]).into(ivPromotion);
		((ViewPager) container).addView(itemView);
		
		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				
			}
			
			
			
		});
		
		
		
		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((LinearLayout) object);
	}
}