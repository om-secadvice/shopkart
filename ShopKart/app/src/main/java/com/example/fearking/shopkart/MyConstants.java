
package com.example.fearking.shopkart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by root on 10-02-2017.
 */

public final class MyConstants {
    public static final String HOST = "http://192.168.43.18:8080";
    public static final String rupee="₹";
    public static final int REQUEST_CODE_DUMMY = 100;
    public static  String CATEGORY_SELECTED ;

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}
