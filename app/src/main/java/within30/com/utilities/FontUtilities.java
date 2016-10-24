package within30.com.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by SR Lakhsmi on 5/10/2016.
 */
public class FontUtilities {
    Context context ;
    AssetManager assetManager;

    public FontUtilities(Context context){
       this. context = context;
        assetManager = context.getApplicationContext().getAssets();
    }

    public Typeface getDroidSerif() {
        return Typeface.createFromAsset(assetManager, "fonts/DroidSerif.ttf");
    }
    public Typeface getDroidSerif_Bold() {
        return Typeface.createFromAsset(assetManager, "fonts/DroidSerif_Bold.ttf");
    }
}
