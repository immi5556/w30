package within30.com.sidemenu.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import within30.com.R;
import within30.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by SR Lakhsmi on 2/12/2016.
 */
public class MyLInearLayout  extends View implements ScreenShotable {

    Context context;
    View view;
    private Bitmap bitmap;
    public MyLInearLayout(Context context) {
        super(context);
        this.context = context;
       /* view =  LayoutInflater.from(context).inflate(
                R.layout.fragment_main_temp, null);*/
    }


    public  MyLInearLayout newInstance(/*int resId*/) {
        MyLInearLayout contentFragment = new MyLInearLayout(context);
        Bundle bundle = new Bundle();
     //   bundle.putInt(Integer.class.getName(), resId);



        return contentFragment;
    }
    @Override
    public void takeScreenShot() {
        /*Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                        view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
                MyLInearLayout.this.bitmap = bitmap;
            }
        };*/

       // thread.start();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
