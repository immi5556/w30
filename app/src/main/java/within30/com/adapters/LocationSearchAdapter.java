package within30.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import within30.com.R;
import within30.com.dataobjects.LocationDO;
import within30.com.dataobjects.ServicesDO;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sms on 14/01/16.
 */
public class LocationSearchAdapter extends ArrayAdapter<LocationDO> implements
        Filterable {


    private ArrayList<LocationDO> mOriginalValues = new ArrayList<LocationDO>();
    private ArrayFilter mFilter;
    List<LocationDO> locationList = new ArrayList<LocationDO>();
    int resource = 0;
    Context context;
    public LocationSearchAdapter(Context context, int resource,
                                 int textViewResourceId) {

        super(context, resource, textViewResourceId);
        this.resource = resource;
        this.context = context;


    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public LocationDO getItem(int position) {
        return locationList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater mInflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
            convertView = mInflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.tv_category);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(locationList.get(position).getCity());
        return convertView;

    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<LocationDO>(locationList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<LocationDO> list = new ArrayList<LocationDO>(
                            mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<LocationDO> values = mOriginalValues;
                int count = values.size();

                ArrayList<LocationDO> newValues = new ArrayList<LocationDO>(count);

                for (int i = 0; i < count; i++) {
                    LocationDO locationDO = (LocationDO) values.get(i);
                    String itemDisplay = locationDO.getCity();
                    String itemName = locationDO.getCity();
                    if (itemDisplay.toLowerCase().contains(prefixString)) {
                        newValues.add(locationDO);
                    }else if (itemName.toLowerCase().contains(prefixString)) {
                        newValues.add(locationDO);
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.values != null) {
                locationList = (ArrayList<LocationDO>) results.values;
            } else {
                locationList = new ArrayList<LocationDO>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public void refreshServicesList(List<LocationDO> locationList){
        this.locationList = locationList;
        mOriginalValues = (ArrayList<LocationDO>)locationList;
        notifyDataSetChanged();
    }
    public static class ViewHolder {
        public TextView textView;
    }

}
