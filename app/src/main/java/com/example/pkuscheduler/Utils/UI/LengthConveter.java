package com.example.pkuscheduler.Utils.UI;

import android.content.Context;
import android.util.TypedValue;

public final class LengthConveter {
    static final public int DpToPx(float dp, Context context){
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()));
    }
}
