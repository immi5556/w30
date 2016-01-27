package com.sms.within30.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sms.within30.R;
import com.sms.within30.dataobjects.CitiesDO;

import java.util.Vector;

/**
 * Created by sms on 07/01/16.
 */
public class CityAdapter  extends BaseAdapter
{
    private Context context;
    private Vector<CitiesDO> vecCities;
    Activity activity;

    public CityAdapter(Context context, Vector<CitiesDO> vecCities,Activity activity)
    {
        this.context = context;
        this.vecCities = vecCities;
        this.activity = activity;
    }
    @Override


    public int getCount()
    {
        if(vecCities!=null && vecCities.size()>0)
            return vecCities.size();
        else
            return 0;
    }

    public void refreshAdapter(Vector<CitiesDO> vecCities)
    {
        this.vecCities = vecCities;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getDropDownView(final int position, View view, ViewGroup parent)
    {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = activity.getLayoutInflater().inflate(R.layout.
                    toolbar_spinner_item_actionbar, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));



        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < vecCities.size() ? vecCities.get(position).CityName : "";
    }

}
