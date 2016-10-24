package within30.com.wheel.transformer;

import android.graphics.drawable.Drawable;

import within30.com.wheel.WheelView;


public class FadingSelectionTransformer implements WheelSelectionTransformer  {

    @Override
    public void transform(Drawable drawable, WheelView.ItemState itemState) {
        float relativePosition = Math.abs(itemState.getRelativePosition());
        int alpha = (int) ((1f - Math.pow(relativePosition, 2.5f)) * 255f);

        //clamp to between 0 and 255
        if (alpha > 255) alpha = 255;
        else if (alpha < 0) alpha = 0;

        drawable.setAlpha(alpha);
    }
}
