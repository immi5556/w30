package com.sms.within30.filters;

import android.view.MotionEvent;
import android.view.View;

import com.sms.within30.lib.VerticalSeekBar;

/**
 * Created by SR Lakhsmi on 5/5/2016.
 */
public class FilterTime implements View.OnTouchListener {
    float startX;
    float startY;
    VerticalSeekBar sbfilter_time;
    VerticalSeekBar sbfilter_distance;

    public FilterTime(VerticalSeekBar sbfilter_time,VerticalSeekBar sbfilter_distance){
        this.sbfilter_time = sbfilter_time;
        this.sbfilter_distance = sbfilter_distance;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        sbfilter_distance.setVisibility(View.GONE);
        sbfilter_time.setVisibility(View.VISIBLE);
        sbfilter_time.setSecondaryProgress(sbfilter_distance.getProgress());
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
                    sbfilter_time.setSecondaryProgress(sbfilter_distance.getProgress());
                    // Means Vertical Movement.
                    if (newY - startY > 0) {
                        // Moving Down
                        System.out.println("moving down.............");
                        sbfilter_time.setProgress(sbfilter_time.getProgress() - 1);
                    } else {
                        // Moving Up
                        System.out.println("moving top.............");
                        sbfilter_time.setProgress(sbfilter_time.getProgress() + 1);
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
