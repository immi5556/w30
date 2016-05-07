package com.sms.within30.filters;

import android.view.MotionEvent;
import android.view.View;

import com.sms.within30.lib.VerticalSeekBar;

/**
 * Created by SR Lakhsmi on 5/5/2016.
 */
public class FilterDistance implements View.OnTouchListener {
    float startX;
    float startY;
    com.sms.within30.lib.VerticalSeekBar sbfilter_distance;
    com.sms.within30.lib.VerticalSeekBar sbfilter_time;
    public  FilterDistance(VerticalSeekBar sbfilter_distance,VerticalSeekBar sbfilter_time){
        this.sbfilter_distance = sbfilter_distance;
        this.sbfilter_time = sbfilter_time;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        sbfilter_time.setVisibility(View.GONE);
        sbfilter_distance.setVisibility(View.VISIBLE);
        sbfilter_distance.setSecondaryProgress(sbfilter_time.getProgress());

       // sbfilter_distance.set
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float newX = event.getRawX();
                float newY = event.getRawY();

                if (Math.abs(newX - startX) > Math.abs(newY - startY)) {
                    // Means Horizontal Movement.
                    if (newX - startX > 0) {
                        // Moving Right
                        System.out.println("moving right.............");
                    } else {
                        // Moving Left
                        System.out.println("moving left.............");
                    }
                } else {
                    // Means Vertical Movement.
                    if (newY - startY > 0) {
                        // Moving Down
                        System.out.println("moving down.............");
                        sbfilter_distance.setProgress(sbfilter_distance.getProgress() - 1);
                    } else {
                        // Moving Up
                        System.out.println("moving top.............");
                        sbfilter_distance.setProgress(sbfilter_distance.getProgress() + 1);
                    }
                }

                startX = newX;
                startY = newY;
                break;

            case MotionEvent.ACTION_UP:
                // Finger Up and the motion is complete
                startX = 0;
                startY = 0;
                break;
        }
        return true;
    }
}
