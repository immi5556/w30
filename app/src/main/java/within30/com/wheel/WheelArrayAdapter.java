package within30.com.wheel;

import java.util.List;

public abstract class WheelArrayAdapter<T> implements WheelAdapter {
    private List<T> mItems;

    public WheelArrayAdapter(List<T> items) {
        mItems = items;

    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    /*@Override
    public View getItem(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item, parent, false);
        return convertView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item, parent, false);
        return convertView;
    }*/
}
