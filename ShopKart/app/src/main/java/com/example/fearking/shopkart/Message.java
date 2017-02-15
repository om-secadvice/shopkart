package com.example.fearking.shopkart;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by root on 07-02-2017.
 */

public abstract class Message {
    static public void message(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }
}
