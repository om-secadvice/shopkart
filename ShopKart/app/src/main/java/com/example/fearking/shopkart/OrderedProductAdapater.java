package com.example.fearking.shopkart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by root on 14-02-2017.
 */

public class OrderedProductAdapater extends ArrayAdapter<Product> {
    public OrderedProductAdapater(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(v==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.myorder_product_items, null);
        }
        final Product  orderedItem=getItem(position);
        TextView referenceNumber = (TextView) v.findViewById(R.id.order_number_myorders);
        TextView price = (TextView) v.findViewById(R.id.price_myorders);
        TextView status = (TextView) v.findViewById(R.id.status_myorders);
        TextView name = (TextView) v.findViewById(R.id.name);
        final RatingBar bar=(RatingBar)v.findViewById(R.id.ratingBar_myorderes);
        NetworkImageView pic=(NetworkImageView) v.findViewById(R.id.picProduct);

        ImageLoader imageLoader = MySingleton.getInstance(getContext()).getImageLoader();
        final String url = MyConstants.HOST+"/img/"+orderedItem.getCategoryId()+"/" + orderedItem.getId() + ".jpeg";
        imageLoader.get(url, ImageLoader.getImageListener(pic, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        pic.setImageUrl(url, imageLoader);

        referenceNumber.setText(String.valueOf(orderedItem.getConfirmationNumber()));
        price.setText(MyConstants.rupee+String.valueOf(orderedItem.getPrice()));
        status.setText(orderedItem.getStatus());
        name.setText(orderedItem.getName());
        bar.setRating((float) orderedItem.getRating());
        if(orderedItem.getRating()!=0)
            bar.setIsIndicator(true);
        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                bar.setRating(rating);
                new CommonRequest(getContext(),null).rateProduct(orderedItem.getId(),orderedItem.getConfirmationNumber(),rating);
                }
        });

        return  v;
    }
}
