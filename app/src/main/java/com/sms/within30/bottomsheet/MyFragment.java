package com.sms.within30.bottomsheet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sms.within30.R;


public class MyFragment extends BottomSheetFragment {
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.place_details_and_booking, container, false);
    }
}
