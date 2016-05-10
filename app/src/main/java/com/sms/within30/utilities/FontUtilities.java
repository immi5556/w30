package com.sms.within30.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by SR Lakhsmi on 5/10/2016.
 */
public class FontUtilities {
    Context context ;
    AssetManager assetManager;
    Typeface LATO_BLACK
            ,LATO_BLACKITALIC
            ,LATO_BOLD
            ,LATO_BOLDITALIC
            ,LATO_HAIRLINE
            ,LATO_HAIRLINEITALIC
            ,LATO_HEAVY
            ,LATO_HEAVYITALIC
            ,LATO_ITALIC
            ,LATO_LIGHT
            ,LATO_LIGHTITALIC
            ,LATO_MEDIUM
            ,LATO_MEDIUMITALIC
            ,LATO_REGULAR
            ,LATO_SEMIBOLD
            ,LATO_SEMIBOLDITALIC
            ,LATO_THIN
            ,LATO_THINITALIC;
    FontUtilities(Context context){
       this. context = context;
        assetManager = context.getApplicationContext().getAssets();
    }

    public Typeface getLATO_BLACK() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_BLACK.ttf");
    }
    public Typeface getLATO_BLACKITALIC() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_BLACKITALIC.ttf");
    }
    public Typeface getLATO_BOLD() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_BOLD.ttf");
    }
    public Typeface getLATO_BOLDITALIC() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_BOLDITALIC.ttf");
    }
    public Typeface getLATO_HAIRLINE() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_HAIRLINE.ttf");
    }
    public Typeface getLATO_HAIRLINEITALIC() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_HAIRLINEITALIC.ttf");
    }
    public Typeface getLATO_HEAVY() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_HEAVY.ttf");
    }
    public Typeface getLATO_HEAVYITALIC() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_HEAVYITALIC.ttf");
    }
    public Typeface getLATO_ITALIC() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_ITALIC.ttf");
    }
    public Typeface getLATO_LIGHT() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_LIGHT.ttf");
    }
    public Typeface getLATO_LIGHTITALIC() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_LIGHTITALIC.ttf");
    }
    public Typeface getLATO_MEDIUM() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_MEDIUM.ttf");
    }
    public Typeface getLATO_MEDIUMITALIC() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_MEDIUMITALIC.ttf");
    }
    public Typeface getLATO_REGULAR() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_REGULAR.ttf");
    }
    public Typeface getLATO_SEMIBOLD() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_SEMIBOLD.ttf");
    }
    public Typeface getLATO_SEMIBOLDITALIC() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_SEMIBOLDITALIC.ttf");
    }
    public Typeface getLATO_THIN() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_THIN.ttf");
    }
    public Typeface getLATO_THINITALIC() {
        return Typeface.createFromAsset(assetManager, "fonts/LATO_THINITALIC.ttf");
    }

}
