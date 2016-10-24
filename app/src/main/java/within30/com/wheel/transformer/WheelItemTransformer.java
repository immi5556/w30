package within30.com.wheel.transformer;

import android.graphics.Rect;

import within30.com.wheel.WheelView;


public interface WheelItemTransformer {
    /**
     * You have control over the Items draw bounds. By supplying your own WheelItemTransformer
     * you must call set bounds on the itemBounds.
     */
    void transform(WheelView.ItemState itemState, Rect itemBounds);
}
