package com.example.fearking.shopkart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 14-02-2017.
 */

public class OrderedProductAdapater extends ArrayAdapter<Order> {
    public OrderedProductAdapater(Context context, int resource, List<Order> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(v==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.mycart_items, null);
        }
        final Order  orderedItem=getItem(position);
        TextView referenceNumber = (TextView) v.findViewById(R.id.order_number_myorders);
        TextView price = (TextView) v.findViewById(R.id.price_myorders);
        TextView status = (TextView) v.findViewById(R.id.status_myorders);
        RatingBar bar=(RatingBar)v.findViewById(R.id.ratingBar_myorderes);
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return  v;
    }
}
