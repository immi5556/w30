package within30.com.dataobjects;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by SR Lakhsmi on 2/4/2016.
 */
public class ServicesDO {
    private String name;
    private String _id;
    private String descr;
    private String image;
    private String mobileDecription;
    private String mobileImage;
    private String mobileMenuImage;
    private long createdat;
    private Drawable drawable;
    private Drawable menuImageDrawable;
    public boolean active = false;
    private ImageView imageView;
    private ImageView menuImageView;
    private boolean selectedService = false;

    public boolean isSelectedService() {
        return selectedService;
    }

    public void setSelectedService(boolean selectedService) {
        this.selectedService = selectedService;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getMenuImageView() {
        return menuImageView;
    }

    public void setMenuImageView(ImageView menuImageView) {
        this.menuImageView = menuImageView;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public Drawable getMenuImageDrawable() {
        return menuImageDrawable;
    }

    public void setMenuImageDrawable(Drawable menuImageDrawable) {
        this.menuImageDrawable = menuImageDrawable;
    }

    public String getMobileMenuImage() {
        return mobileMenuImage;
    }

    public void setMobileMenuImage(String mobileMenuImage) {
        this.mobileMenuImage = mobileMenuImage;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobileDecription() {
        return mobileDecription;
    }

    public void setMobileDecription(String mobileDecription) {
        this.mobileDecription = mobileDecription;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public void setMobileImage(String mobileImage) {
        this.mobileImage = mobileImage;
    }

    public long getCreatedat() {
        return createdat;
    }

    public void setCreatedat(long createdat) {
        this.createdat = createdat;
    }

    @Override
    public String toString() {
        return "ServicesDO{" +
                "name='" + name + '\'' +
                ", _id='" + _id + '\'' +
                ", descr='" + descr + '\'' +
                ", image='" + image + '\'' +
                ", mobileDecription='" + mobileDecription + '\'' +
                ", mobileImage='" + mobileImage + '\'' +
                ", createdat=" + createdat +
                '}';
    }
}
