
package com.example.fearking.shopkart;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 11-02-2017.
 */
public class CartAdapter extends ArrayAdapter<Product> {
    Order order;
    public CartAdapter(Context context, int resource, List<Product> items,@Nullable final Order order) {
        super(context, resource, items);
        this.order=order;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=convertView;
        if(v==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.mycart_items, null);

        }


        final Product item=getItem(position);
        Log.d("Name:",item.getName());
        if(item!=null) {
            TextView name = (TextView) v.findViewById(R.id.mycartProductName);
            TextView price = (TextView) v.findViewById(R.id.mrp);
            TextView seller = (TextView) v.findViewById(R.id.seller);
            TextView stock = (TextView) v.findViewById(R.id.myCartStock);
            final TextView quantity=(TextView) v.findViewById(R.id.mycartQuantity);
          Button decrement=(Button)v.findViewById(R.id.decrement);
           decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

               int q=Integer.parseInt(quantity.getText().toString());
                   if(q>1){
                        q--;
                        item.decrementQuantity();
                        quantity.setText(String.valueOf(q));
                        Message.message(getContext(),"Quantity of cart item decreased to "+quantity.getText().toString());
                       new CommonRequest(getContext(),order).removeFromCart(item.getId(),1);
                   }
                    else {
                       Message.message(getContext(),"Click Remove to Remove Item.");
                   }
                }
            });
            Button increment=(Button)v.findViewById(R.id.increment);
            increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int q=Integer.parseInt(quantity.getText().toString());
                    if(q<=10){
                        q++;
                        item.incrementQuantity();
                        quantity.setText(String.valueOf(q));
                        Message.message(getContext(),"Quantity of cart item increased to "+quantity.getText().toString());
                        new CommonRequest(getContext(),order).addToCart(item.getId(),1);
                    }
                    else {
                        Message.message(getContext(),"no more item can be added, SORRY");
                    }
                }
            });
            Button remove=(Button)v.findViewById(R.id.mycartRemoveButton);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CommonRequest(getContext(),order).removeFromCart(item.getId(),item.getQuantity());
                    Message.message(getContext(),"Item removed");


                }
            });

            NetworkImageView pic = (NetworkImageView) v.findViewById(R.id.mycartImage);
            ImageLoader imageLoader = MySingleton.getInstance(getContext()).getImageLoader();
            if (name != null)
                name.setText(item.getName());
            if (price != null)
                price.setText("₹"+String.valueOf(item.getPrice()));
            if (pic != null) {
                final String url = MyConstants.HOST+"/img/"+item.getCategoryId()+"/" + item.getId() + ".jpeg";
                imageLoader.get(url, ImageLoader.getImageListener(pic, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
                pic.setImageUrl(url, imageLoader);
            }
            if (seller != null) {
                seller.setText("Sold By:" + item.getRetailerName());
            }
            if (stock != null) {
                if (item.getStock() == 0) {
                    stock.setText("OUT OF STOCK");
                    stock.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_light));
                }else {
                    stock.setText(item.getStock()+" IN STOCK");
                    stock.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_light));
                }

            }
            if(quantity!=null){
                quantity.setText(""+item.getQuantity());
            }
        }
        return v;

    }


}
