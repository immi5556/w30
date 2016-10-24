package within30.com.sidemenu.model;


import android.graphics.drawable.Drawable;

import within30.com.sidemenu.interfaces.Resourceble;

/**
 * Created by Konstantin on 23.12.2014.
 */

public class SlideMenuItem implements Resourceble {
    private String name;
   // private int imageRes;
   private int imageRes;
    private String _id;
    private boolean isServiceSelected = false;

    public SlideMenuItem(String name, int imageRes) {
        this.name = name;
        this.imageRes = imageRes;
    }

    public boolean isServiceSelected() {
        return isServiceSelected;
    }

    public void setIsServiceSelected(boolean isServiceSelected) {
        this.isServiceSelected = isServiceSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
