package within30.com.sidemenu.animation;

/**
 * Created by SR Lakhsmi on 2/12/2016.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


public class RevealFrameLayout extends FrameLayout implements RevealAnimator {
    Path mRevealPath;
    boolean mClipOutlines;
    float mCenterX;
    float mCenterY;
    float mRadius;
    View mTarget;

    public RevealFrameLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public RevealFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RevealFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mRevealPath = new Path();
    }

    public void setTarget(View view) {
        this.mTarget = view;
    }

    public void setCenter(float centerX, float centerY) {
        this.mCenterX = centerX;
        this.mCenterY = centerY;
    }

    public void setClipOutlines(boolean clip) {
        this.mClipOutlines = clip;
    }

    public void setRevealRadius(float radius) {
        this.mRadius = radius;
        this.invalidate();
    }

    public float getRevealRadius() {
        return this.mRadius;
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if(!this.mClipOutlines && child != this.mTarget) {
            return super.drawChild(canvas, child, drawingTime);
        } else {
            int state = canvas.save();
            this.mRevealPath.reset();
            this.mRevealPath.addCircle(this.mCenterX, this.mCenterY, this.mRadius, Direction.CW);
            canvas.clipPath(this.mRevealPath);
            boolean isInvalided = super.drawChild(canvas, child, drawingTime);
            canvas.restoreToCount(state);
            return isInvalided;
        }
    }
}

