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
import within30.com.dataobjects.CustomerDO;
import within30.com.dataobjects.ServicesDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sms on 14/01/16.
 */
public class ServicesSearchAdapter extends ArrayAdapter<ServicesDO> implements
        Filterable {


    private ArrayList<ServicesDO> mOriginalValues = new ArrayList<ServicesDO>();
    private ArrayFilter mFilter;
    List<ServicesDO> servicesList = new ArrayList<ServicesDO>();
    int resource = 0;
    Context context;
    public ServicesSearchAdapter(Context context, int resource,
                                 int textViewResourceId) {

        super(context, resource, textViewResourceId);
        this.resource = resource;
        this.context = context;


    }

    @Override
    public int getCount() {
        return servicesList.size();
    }

    @Override
    public ServicesDO getItem(int position) {
        return servicesList.get(position);
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
        holder.textView.setText(servicesList.get(position).getName());
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
                    mOriginalValues = new ArrayList<ServicesDO>(servicesList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<ServicesDO> list = new ArrayList<ServicesDO>(
                            mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<ServicesDO> values = mOriginalValues;
                int count = values.size();

                ArrayList<ServicesDO> newValues = new ArrayList<ServicesDO>(count);

                for (int i = 0; i < count; i++) {
                    ServicesDO servicesDO = (ServicesDO) values.get(i);
                    String itemDisplay = servicesDO.getName();
                    String itemName = servicesDO.getName();
                    if (itemDisplay.toLowerCase().contains(prefixString)) {
                        newValues.add(servicesDO);
                    }else if (itemName.toLowerCase().contains(prefixString)) {
                        newValues.add(servicesDO);
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
                servicesList = (ArrayList<ServicesDO>) results.values;
            } else {
                servicesList = new ArrayList<ServicesDO>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public void refreshServicesList(List<ServicesDO> servicesList){
        this.servicesList = servicesList;
        mOriginalValues = (ArrayList<ServicesDO>)servicesList;
        notifyDataSetChanged();
    }
    public static class ViewHolder {
        public TextView textView;
    }

}
