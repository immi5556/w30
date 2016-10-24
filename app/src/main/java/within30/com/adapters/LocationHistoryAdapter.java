package within30.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import within30.com.R;
import within30.com.dataobjects.LocationDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sms on 14/01/16.
 */
public class LocationHistoryAdapter extends BaseAdapter
         {




    List<LocationDO> locationList = new ArrayList<LocationDO>();
    int resource = 0;
    Context context;
    public LocationHistoryAdapter(Context context, int resource,
                                  int textViewResourceId,List<LocationDO> locationList) {


        this.resource = resource;
        this.context = context;
        this.locationList = locationList;


    }

    @Override
    public int getCount() {
        return locationList.size();
    }
     public long getItemId(int position) {
         return position;
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





    public void refreshServicesList(List<LocationDO> locationList){
        this.locationList = locationList;

        notifyDataSetChanged();
    }
    public static class ViewHolder {
        public TextView textView;
    }

}
