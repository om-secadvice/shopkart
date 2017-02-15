
package com.example.fearking.shopkart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 12-02-2017.
*/

public class MyOrdersAdapter extends ArrayAdapter<Order> {
    public MyOrdersAdapter(Context context, int resource, ArrayList<Order> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(v==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            v=inflater.inflate(R.layout.ordered_product,null);
        }

        Order item=getItem(position);
        if (item!=null){
            TextView amount= (TextView) v.findViewById(R.id.amount);
            TextView referenceNumber=(TextView)v.findViewById(R.id.reference_number);
            TextView dateCreated=(TextView) v.findViewById(R.id.date_created);
            TextView shippingAddress= (TextView) v.findViewById(R.id.shipping_address);
            TextView modeOfPayment=(TextView) v.findViewById(R.id.modeofpayment);
            TextView pincode=(TextView) v.findViewById(R.id.pincode);
            if(amount!=null)
                amount.setText(MyConstants.rupee+String.valueOf(item.getAmount()));
            if(referenceNumber!=null)
                referenceNumber.setText("Reference ID: "+String.valueOf(item.getConfirmationNumber()));
            if(modeOfPayment!=null){
                if(item.getCardNumber()!=null)
                    modeOfPayment.setText(" Credit/Debit Card");
                else
                    modeOfPayment.setText(" Cash On Delivery");
            }


            if(dateCreated!=null){

                String[] date=item.getDateCreated().split("T")[0].split("-");
                String[] time=item.getDateCreated().split("T")[1].split(":");
                int year=Integer.parseInt(date[0])-1900;
                int month=Integer.parseInt(date[1])-1;
                int day=Integer.parseInt(date[2]);

                int hour=Integer.parseInt(time[0]);
                int min=Integer.parseInt(time[1]);

                Timestamp timestamp=new Timestamp(year,month,day,hour,min,0,0);
                Log.d("DATE",timestamp.toGMTString());
                dateCreated.setText(" "+timestamp.toLocaleString());
            }
            if(shippingAddress!=null){
                shippingAddress.setText("Shipping Address:\n"+item.getShippingAddress());
            }

            if(pincode!=null){
                pincode.setText("Pincode:"+item.getPincode());
            }
        }
        if(position%2==1){
            v.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
        }
        return v;
    }
}
