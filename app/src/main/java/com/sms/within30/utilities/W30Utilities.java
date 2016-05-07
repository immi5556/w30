package com.sms.within30.utilities;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.sms.within30.R;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by SR Lakhsmi on 4/24/2016.
 */
public class W30Utilities {
    public boolean edittextValidation(HashMap hm,Activity activity) {
        boolean valid = true;
        // hashmap iteration and printing
        Iterator<EditText> keyIterator = hm.keySet().iterator();
        while(keyIterator.hasNext()) {

            EditText key = keyIterator.next();
            if (key.getText().toString().length() == 0) {
                setValidationEditText(key, activity, (String) hm.get(key));
                valid = false;
            }

        }
        return valid;

    }
    /**
     * This method validates to the given edit text
     * should call when  validation requires
     * @param e
     * @param activity
     * @param error to show
     */
    public void setValidationEditText(final EditText e, final Activity activity, final String error) {

        e.setHint(error);
        e.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.exclamation5, 0, 0, 0);
        e.setHintTextColor(activity.getResources().getColor(R.color.red));
        e.setCompoundDrawablePadding(5);
    }
    /**
     * This methos sets the error message to the given edit text
     * Shouold call at oncreate method of activity
     * @param e Edittext
     * @param activity
     * @param hintText
     */
    public void  setErrorOnEditText(final EditText e,final Activity activity, final String hintText) {

        e.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                e.setHint(hintText);
                e.setHintTextColor(activity.getResources().getColor(R.color.gray_light));

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    /**
     * This methos validates the given phonen number by usig matcher from Android util
     * @param number
     * @return boolean if the given number is valid
     */
    public boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
