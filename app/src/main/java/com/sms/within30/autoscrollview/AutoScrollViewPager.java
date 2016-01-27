package com.sms.within30.autoscrollview;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class AutoScrollViewPager extends ViewPager
{
	
	private int SCROLL_DURATION = 1500;
	private int TRANSITION_DELAY = 1500;
	private int scrolledPageCount=0;
	private int maxPagesCount = 0;
	private boolean isForward = true;

	public AutoScrollViewPager(Context context) {
		super(context);
		changePagerScroller(context);
	}

	public AutoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		changePagerScroller(context);
	}

	private void changePagerScroller(Context mContext) {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			ViewPagerScroller scroller = new ViewPagerScroller(mContext);
			mScroller.set(this, scroller);
		} catch (Exception e) {
			Log.i("Scroller : ", "error of change scroller ");
		}
	}

	/**This method is used to start the auto scrolling of pages in the pager*/
	public void startAutoScrollPager(final AutoScrollViewPager pager)
	{
		maxPagesCount = this.getAdapter().getCount();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(scrolledPageCount < maxPagesCount  && isForward)
				{
					scrolledPageCount++;
					if(scrolledPageCount == maxPagesCount)
					{
						isForward = false;
						scrolledPageCount--;
					}
				}
				else if(!isForward)
				{
					scrolledPageCount--;
					if(scrolledPageCount == 0)
						isForward = true;
				}
				pager.setCurrentItem(scrolledPageCount, true);
				startAutoScrollPager(pager);
			}
		}, TRANSITION_DELAY);
	}

	class ViewPagerScroller extends Scroller {

		public ViewPagerScroller(Context context) {
			super(context);
		}

		public ViewPagerScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			super.startScroll(startX, startY, dx, dy, SCROLL_DURATION);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			super.startScroll(startX, startY, dx, dy, SCROLL_DURATION);
		}
	}
}