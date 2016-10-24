package within30.com.filters;

import android.view.MotionEvent;
import android.view.View;

import within30.com.dataobjects.CustomerDO;
import within30.com.lib.VerticalSeekBar;

import java.util.List;

/**
 * Created by SR Lakhsmi on 5/5/2016.
 */
public class FilterTime implements View.OnTouchListener {
    float startX;
    float startY;
    VerticalSeekBar sbfilter_time;
    VerticalSeekBar sbfilter_distance;
    List<CustomerDO> placesList;

    public FilterTime(VerticalSeekBar sbfilter_time,VerticalSeekBar sbfilter_distance, List<CustomerDO> placesList){
        this.sbfilter_time = sbfilter_time;
        this.sbfilter_distance = sbfilter_distance;
        this.placesList = placesList;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        sbfilter_distance.setVisibility(View.GONE);
        sbfilter_time.setVisibility(View.VISIBLE);
        //sbfilter_time.setSecondaryProgress(sbfilter_distance.getProgress());
        int secondaryProgress = (50*sbfilter_distance.getProgress())/60;
        sbfilter_time.setSecondaryProgress(secondaryProgress);
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

                    } else {
                        // Moving Left

                    }
                } else {
                   // sbfilter_time.setSecondaryProgress(sbfilter_distance.getProgress());
                    sbfilter_time.setSecondaryProgress(secondaryProgress);
                    // Means Vertical Movement.
                    if (newY - startY > 0) {
                        // Moving Down

                        if (placesList!=null && placesList.size()>0) {
                            sbfilter_time.setProgress(sbfilter_time.getProgress() - 1);
                        }else{
                            if (newY - startY > 2){
                                sbfilter_time.setProgress(sbfilter_time.getProgress() - 1);
                            }
                        }

                    } else {
                        // Moving Up
                        if (placesList !=null && placesList.size()>0) {
                            sbfilter_time.setProgress(sbfilter_time.getProgress() + 1);
                        }else {
                            if (newY - startY < -2) {
                                sbfilter_time.setProgress(sbfilter_time.getProgress() + 1);
                            }
                        }

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
