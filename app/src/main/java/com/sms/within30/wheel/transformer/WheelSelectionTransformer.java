package com.sms.within30.wheel.transformer;

import android.graphics.drawable.Drawable;

import com.sms.within30.wheel.WheelView;


public interface WheelSelectionTransformer {
    void transform(Drawable drawable, WheelView.ItemState itemState);
}
