package within30.com.wheel.transformer;

import android.graphics.drawable.Drawable;

import within30.com.wheel.WheelView;


public interface WheelSelectionTransformer {
    void transform(Drawable drawable, WheelView.ItemState itemState);
}
