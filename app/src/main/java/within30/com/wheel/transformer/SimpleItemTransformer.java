package within30.com.wheel.transformer;

import android.graphics.Rect;

import within30.com.wheel.Circle;
import within30.com.wheel.WheelView;

import within30.com.wheel.transformer.WheelItemTransformer;


public class SimpleItemTransformer implements WheelItemTransformer {
    @Override
    public void transform(WheelView.ItemState itemState, Rect itemBounds) {
        Circle bounds = itemState.getBounds();
        float radius = bounds.getRadius();
        float x = bounds.getCenterX();
        float y = bounds.getCenterY();
        itemBounds.set(Math.round(x - radius), Math.round(y - radius), Math.round(x + radius), Math.round(y + radius));
    }
}
