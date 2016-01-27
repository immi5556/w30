package com.sms.within30.calender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sms.within30.R;
import com.sms.within30.utilities.TimeUtils;


/**
 * Created by Sri Krishna on 9/20/2015.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    String OpeningHours;
    boolean currentDay = false;
    String str1="";
	String str2="";
    public static ArrayList<String> timings;

    public ListAdapter(Context context,String OpeningHours)
    {
        this.context = context;
        this.OpeningHours = OpeningHours;
        OpeningHours = "10:00AM-5:00PM";
        String[] splitOpeningHours = OpeningHours.split("-");
        if (splitOpeningHours.length == 2){
        	// timings =  getSlots(splitOpeningHours[0],splitOpeningHours[1]);
        	
        	if (splitOpeningHours[0].contains("AM")){
        		str1 = splitOpeningHours[0].replace("AM", " AM");
        	}else if (splitOpeningHours[0].contains("PM")){
        		str1 = splitOpeningHours[0].replace("PM", " PM");
        	}
        	if (splitOpeningHours[1].contains("AM")){
        		str2 = splitOpeningHours[1].replace("AM", " AM");
        	}else if (splitOpeningHours[1].contains("PM")){
        		str2 = splitOpeningHours[1].replace("PM", " PM");
        	}
        	
        	System.out.println("str1->"+str1+"\n"+"str2->"+str2);
        	 timings = TimeUtils.getSlots(str1, str2, currentDay);
        }       
    }
   
    @Override
    public int getCount() {
        return timings.size();
    }

    @Override
    public Object getItem(int i) {
        return timings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);

        TextView tv = (TextView) view.findViewById(R.id.tvItem);
        CheckBox cb = (CheckBox) view.findViewById(R.id.cbItem);
        cb.setTag(R.string.app_name, i);
        tv.setText(timings.get(i));

        if(i == ((BookingTimeSelectionActivity)context).listItemPosition)
        {
            cb.setChecked(true);
        }

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int position = (int) compoundButton.getTag(R.string.app_name);
                if (isChecked) {
                	System.out.println();
                    for (int i = 0; i < ((BookingTimeSelectionActivity) context).lvTiming.getChildCount(); i++) {
                        if (position != i) {
                        	LinearLayout linearLayout = (LinearLayout) ((BookingTimeSelectionActivity) context).lvTiming.getChildAt(i);
                            ((CheckBox) linearLayout.getChildAt(1)).setChecked(false);
                        }
                    }
                    ((BookingTimeSelectionActivity) context).listItemPosition = position;
                }
            }
        });

        return view;
    }
    
    public void refreshListOnSelectedDay(boolean currentDay){
    	 timings =TimeUtils.getSlots(str1,str2,currentDay);
    	 notifyDataSetChanged();
    	
    }
  

}
