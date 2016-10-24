package within30.com.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by SR Lakhsmi on 5/5/2016.
 */
public class MyTextView extends TextView {
float startX,startY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                    if (newY - startY > 0) {
                        // Moving Down

                    } else {
                        // Moving Up

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
        return super.onTouchEvent(event);
    }
    public MyTextView(Context context) {
        super(context);
      //  Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
       // this.setTypeface(face);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
      //  Typeface face=Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
       // this.setTypeface(face);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
      //  Typeface face= Typeface.createFromAsset(context.getAssets(), "Helvetica_Neue.ttf");
      //  this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

}