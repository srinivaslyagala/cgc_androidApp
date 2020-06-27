package com.rolvatech.cgc.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by admin on 10/3/2017.
 */

public class DensityConverter {


    private static DensityConverter mDensityConverter;

    public static DensityConverter getInstance() {
        if (mDensityConverter == null)
            mDensityConverter = new DensityConverter();

        return mDensityConverter;
    }

    /**
     * Convert a DP value back to pixels.
     *
     * @param context the application context.
     *                *
     * @param dp      the density pixels to convert.
     *                *
     * @return the associated pixel value.
     */
    int toPx(Context context, int dp) {
        return convert(context, dp, TypedValue.COMPLEX_UNIT_PX);
    }

    /**
     * Convert a PX value to density pixels.
     *
     * @param context the application context.
     *                *
     * @param px      the pixels to convert.
     *                *
     * @return the associated density pixel value.
     */
    int toDp(Context context, int px) {
        return convert(context, px, TypedValue.COMPLEX_UNIT_DIP);
    }

    private int convert(Context context, int amount, int conversionUnit) {
        if (amount < 0) {
            throw new IllegalArgumentException("px should not be less than zero");
        }
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(conversionUnit, amount, r.getDisplayMetrics());
    }
}
