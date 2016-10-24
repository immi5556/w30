package within30.com.filters;

import android.view.MotionEvent;
import android.view.View;

import within30.com.dataobjects.CustomerDO;
import within30.com.lib.VerticalSeekBar;

import java.util.List;

/**
 * Created by SR Lakhsmi on 5/5/2016.
 */
public class FilterDistance implements View.OnTouchListener {
    float startX;
    float startY;
    within30.com.lib.VerticalSeekBar sbfilter_distance;
    within30.com.lib.VerticalSeekBar sbfilter_time;
    List<CustomerDO> placesList;

    public  FilterDistance(VerticalSeekBar sbfilter_distance, VerticalSeekBar sbfilter_time, List<CustomerDO> placesList){
        this.sbfilter_distance = sbfilter_distance;
        this.sbfilter_time = sbfilter_time;
        this.placesList = placesList;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        sbfilter_time.setVisibility(View.GONE);
        sbfilter_distance.setVisibility(View.VISIBLE);
       // sbfilter_distance.setSecondaryProgress(sbfilter_time.getProgress()+5);
        int secondaryProgress = (60*sbfilter_time.getProgress())/50;
        sbfilter_distance.setSecondaryProgress(secondaryProgress);

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

                    } else {
                        // Moving Left

                    }
                } else {
                    // Means Vertical Movement.
                   // sbfilter_distance.setSecondaryProgress(sbfilter_time.getProgress());
                    sbfilter_distance.setSecondaryProgress(secondaryProgress);
                    if (newY - startY > 0) {
                        // Moving Down

                        if (placesList !=null && placesList.size()>0) {
                            sbfilter_distance.setProgress(sbfilter_distance.getProgress() - 1);
                        }else {
                            if (newY - startY >2){
                                sbfilter_distance.setProgress(sbfilter_distance.getProgress() - 1);
                            }
                        }


                    } else {
                        // Moving Up

                        if (placesList!=null && placesList.size()>0){
                            sbfilter_distance.setProgress(sbfilter_distance.getProgress() + 1);
                        }else {
                            if (newY - startY < -2) {
                                sbfilter_distance.setProgress(sbfilter_distance.getProgress() + 1);
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
