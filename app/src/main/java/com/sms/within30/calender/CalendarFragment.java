package com.sms.within30.calender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sms.within30.R;


/**
 * Created by Sri Krishna on 9/20/2015.
 */
public class CalendarFragment extends Fragment implements CalendarInterface{

    int position;
    private TextView tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven;
    private RadioButton rbOne, rbTwo, rbThree, rbFour, rbFive, rbSix, rbSeven;
    private RadioGroup rgCalendar;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static CalendarFragment newInstance(int position) {
        CalendarFragment f = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt("position") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.calendar_section, container, false);

        ArrayList<CalendarUtils.CalendarDO> arrCalendarDOs = CalendarUtils.populateCalendarDOs();

        tvOne = (TextView)rootView.findViewById(R.id.tvOne);
        tvTwo = (TextView)rootView.findViewById(R.id.tvTwo);
        tvThree = (TextView)rootView.findViewById(R.id.tvThree);
        tvFour = (TextView)rootView.findViewById(R.id.tvFour);
        tvFive = (TextView)rootView.findViewById(R.id.tvFive);
        tvSix = (TextView)rootView.findViewById(R.id.tvSix);
        tvSeven = (TextView)rootView.findViewById(R.id.tvSeven);

        rbOne = (RadioButton)rootView.findViewById(R.id.rbOne);
        rbTwo = (RadioButton)rootView.findViewById(R.id.rbTwo);
        rbThree = (RadioButton)rootView.findViewById(R.id.rbThree);
        rbFour = (RadioButton)rootView.findViewById(R.id.rbFour);
        rbFive = (RadioButton)rootView.findViewById(R.id.rbFive);
        rbSix = (RadioButton)rootView.findViewById(R.id.rbSix);
        rbSeven = (RadioButton)rootView.findViewById(R.id.rbSeven);

        rgCalendar = (RadioGroup) rootView.findViewById(R.id.rgCalendar);

        for(int i = position * 7; i < (position + 1) * 7; i++)
        {
            TextView tv = null;
            RadioButton rb = null;
            String dayString = arrCalendarDOs.get(i).dayString;
            int dayInt = arrCalendarDOs.get(i).dayInt;
            if(i == position * 7){tv = tvOne; rb = rbOne;}
            else if(i == position * 7 + 1){tv = tvTwo; rb = rbTwo;}
            else if(i == position * 7 + 2){tv = tvThree; rb = rbThree;}
            else if(i == position * 7 + 3){tv = tvFour; rb = rbFour;}
            else if(i == position * 7 + 4){tv = tvFive; rb = rbFive;}
            else if(i == position * 7 + 5){tv = tvSix; rb = rbSix;}
            else if(i == position * 7 + 6){tv = tvSeven; rb = rbSeven;}
            if(tv != null)
                tv.setText(dayString);
            if(rb != null){
                rb.setText(String.valueOf(dayInt));
            }
        }
        rgCalendar.setOnCheckedChangeListener(null);

        refreshCalendarItems(position);

        rgCalendar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
System.out.println("setOnCheckedChangeListener--------------------");
                int itemPosition = -1;
                RadioButton rButton = null;
                if(checkedId == R.id.rbOne) {itemPosition = 0; rButton = rbOne;}
                else if(checkedId == R.id.rbTwo) {itemPosition = 1; rButton = rbTwo;}
                else if(checkedId == R.id.rbThree) {itemPosition = 2; rButton = rbThree;}
                else if(checkedId == R.id.rbFour) {itemPosition = 3; rButton = rbFour;}
                else if(checkedId == R.id.rbFive) {itemPosition = 4; rButton = rbFive;}
                else if(checkedId == R.id.rbSix) {itemPosition = 5; rButton = rbSix;}
                else if(checkedId == R.id.rbSeven) {itemPosition = 6; rButton = rbSeven;}

                if(checkedId != -1 && itemPosition != -1 && rButton != null && rButton.isChecked()){
                    ((BookingTimeSelectionActivity)getActivity()).viewPagerItemPosition = itemPosition;
                    ((BookingTimeSelectionActivity)getActivity()).viewPagePosition = position;
                    ((BookingTimeSelectionActivity)getActivity()).tvCurrentDate.setText("Avilable Slots on - "+CalendarUtils.getTitle(7 * position + itemPosition));
                    String calenderDateAsString = CalendarUtils.getTitle(7 * position + itemPosition);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE LLL d");
                    try{
                    	 Date CalendarDate = sdf.parse(calenderDateAsString);
                    	 Date currentDate =  sdf.parse(sdf.format(new Date()));
                    	 if(CalendarDate.after(currentDate)){
                    		 ((BookingTimeSelectionActivity)getActivity()).listAdapter.refreshListOnSelectedDay(false);
                    	 }else  if(CalendarDate.equals(currentDate) ){
                    		 ((BookingTimeSelectionActivity)getActivity()).listAdapter.refreshListOnSelectedDay(true); 
                    	 }
                    }catch(Exception e){
                    	e.printStackTrace();
                    }
                   
                    
                    
                }
            }
        });
        return rootView;
    }

    public void refreshCalendarItems(int position)
    {
        if(position == ((BookingTimeSelectionActivity)getActivity()).viewPagePosition)
        {
            int selectedItem = ((BookingTimeSelectionActivity)getActivity()).viewPagerItemPosition;
            if(selectedItem == 0){rbOne.setChecked(true);}
            else if(selectedItem == 1){rbTwo.setChecked(true);}
            else if(selectedItem == 2){rbThree.setChecked(true);}
            else if(selectedItem == 3){rbFour.setChecked(true);}
            else if(selectedItem == 4){rbFive.setChecked(true);}
            else if(selectedItem == 5){rbSix.setChecked(true);}
            else if(selectedItem == 6){rbSeven.setChecked(true);}
        }
        else
        {
            rgCalendar.clearCheck();
        }
    }

    @Override
    public void fragmentBecameVisible(int position) {
        refreshCalendarItems(position);
    }

}
