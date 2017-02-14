
package com.example.fearking.shopkart;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by root on 10-02-2017.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {


    public ProductListAdapter(Context context, int resource, List<Product> items) {
        super(context, resource, items);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=convertView;
        if(v==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.custom_product_list, null);
        }


        Product p=getItem(position);
        Log.d("Name:",p.getName());
        if(p!=null) {
            TextView name = (TextView) v.findViewById(R.id.tv_title);
            TextView price = (TextView) v.findViewById(R.id.tv_price);
            TextView retailer = (TextView) v.findViewById(R.id.tv_retailer);
            NetworkImageView pic = (NetworkImageView) v.findViewById(R.id.image);
            ImageLoader imageLoader = MySingleton.getInstance(getContext()).getImageLoader();
            if (name != null)
                name.setText(p.getName());
            if (retailer != null)
                retailer.setText(p.getRetailerName());
            if (price != null)
                price.setText(MyConstants.rupee+String.valueOf(p.getPrice())+".00");
            if (pic != null){
                final String url = MyConstants.HOST+"/img/"+p.getCategoryName().replace(" ","%20")+"/"+p.getId()+".jpeg";
                imageLoader.get(url, ImageLoader.getImageListener(pic, R.mipmap.ic_launcher,android.R.drawable.ic_dialog_alert));
                pic.setImageUrl(url, imageLoader);
            }

        }

        if (position % 2 != 1) {
            v.setBackgroundColor(Color.parseColor("#30011011"));
        } else {
            v.setBackgroundColor(Color.parseColor("#20330010"));
        }
        return v;

    }
}
