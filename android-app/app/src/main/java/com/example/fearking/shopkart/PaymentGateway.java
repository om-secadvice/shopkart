
package com.example.fearking.shopkart;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentGateway extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

    View textEntryView;
    EditText cardNumber,cvv,nameOnCard;
    TextView shippingAddress,pincode,amount;
   // EditText input1;
    RadioGroup radioGroup;
    Order order;
    boolean clearCart;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_gateway);
        getReferences();
        intent=getIntent();


        try {
            initializeViews();
        }catch (NullPointerException ne){
            Log.e("ORDER-ERROR","No order in intent");
            ne.printStackTrace();
        }
            radioGroup.setOnCheckedChangeListener(this);

        try{
            getSupportActionBar().setTitle("Payment Gateway");
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }}
        catch (NullPointerException e){

        }
        radioGroup.check(R.id.cod);
    }

    private void initializeViews() throws NullPointerException{
        amount.setText("Amount Payable:" + MyConstants.rupee + order.getAmount());
        if(order.getShippingAddress().trim().equals(""))
            shippingAddress.setText("Add a default address in \"My Profile\" section. \nFor now click \"Edit\" button below.");
        else {
            shippingAddress.setText(order.getShippingAddress());
            pincode.setText(order.getPincode());
        }
    }

    private void getReferences() {
        shippingAddress=(TextView) findViewById(R.id.shipping_address);
        pincode=(TextView)findViewById(R.id.pincode);
        amount=(TextView) findViewById(R.id.amount);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);

        order=getIntent().getParcelableExtra("order");
        clearCart=getIntent().getBooleanExtra("clearCart",false);

        TextView edit=(TextView)findViewById(R.id.edit_shipping);
        edit.setOnClickListener(this);

    }

    public void paymentProceed(View v) throws JSONException {
        if(order.getShippingAddress().trim().equals("")) {
            Message.message(this, "You need to enter a shipping address.");
            return;
        }
        if(order.getPincode().trim().equals("")){
            Message.message(this,"Enter Pincode");
            return;
        }
        if (order == null || order.getNoOfItems() == 0){
            Message.message(this,"No items in cart.");
            return;
         }
        Gson gson=new Gson();
        Type type=new TypeToken<Order>(){}.getType();
        String json=gson.toJson(order,type);


        final Map<String,String> headers=new HashMap<String, String>();
        headers.put("Cookie", MySessionData.session_cookie);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, MyConstants.HOST+"/orderProduct/",json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("ORDER STATUS:", "Confirmation No:"+response.getLong("confirmation_number")+" "+response.getString("msg"));
                            order.setConfirmationNumber(response.getLong("confirmation_number"));
                            startActivity(new Intent(getBaseContext(),ConfirmationPage.class).putExtra("order",order));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR/ORDER-PLACE",error.toString());
                Message.message(getApplicationContext(),"Error: "+error.toString());
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        if(clearCart){
            doClearCart();
        }

    }

    public void doClearCart(){

        final Map<String,String> headers=new HashMap<String, String>();
        headers.put("Cookie", MySessionData.session_cookie);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, MyConstants.HOST+"/clearCart/",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("CLEAR CART:", response.getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR/CLEAR-CART",error.toString());
                Message.message(getApplicationContext(),"Error: "+error.toString());
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.debitcard:

               final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setCancelable(false);
                LayoutInflater factory = LayoutInflater.from(this);
                textEntryView = factory.inflate(R.layout.cod_layout, null);
                cardNumber = (EditText) textEntryView.findViewById(R.id.card_number);
                nameOnCard = (EditText) textEntryView.findViewById(R.id.name_on_card);
                cvv=(EditText)textEntryView.findViewById(R.id.cod_cvv);
                alert.setView(textEntryView);
                alert.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                order.setCardNumber(cardNumber.getText().toString());
                                dialog.dismiss();

                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                    dialog.dismiss();
                            }
                        });
                alert.show();
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        else if(item.getItemId()==R.id.menu_home){
            startActivity(new Intent(getApplicationContext(),ShopHere.class));
            finish();
        }
        else if(item.getItemId()==R.id.myprofile){
            startActivity(new Intent(getApplicationContext(),MyProfile.class));
            finish();
        }
        else if(item.getItemId()==R.id.menu_logout){
            startActivity(new Intent(getApplicationContext(),ShopHere.class));
            finish();
        }
        else if(item.getItemId()==R.id.menu_contacus){
            startActivity(new Intent(getApplicationContext(),ShopHere.class));
            finish();
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return  true;
    }


    @Override
    public void onClick(View v) {
           final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(true);
            LayoutInflater inflater=LayoutInflater.from(this);
            builder.setTitle("Enter your shipping address :");
            builder.setIcon(R.drawable.icon);
            v=inflater.inflate(R.layout.edit_shipping_add,null);

            final EditText address1=(EditText)v.findViewById(R.id.edit_ship_add1);
            final EditText address2=(EditText)v.findViewById(R.id.edit_ship_add2);
            final EditText landmark=(EditText)v.findViewById(R.id.edit_ship_landmark);
            final EditText city=(EditText)v.findViewById(R.id.edit_ship_city);
            final EditText state=(EditText)v.findViewById(R.id.edit_ship_state);
            final EditText pincode=(EditText)v.findViewById(R.id.edit_ship_pin);

            builder.setView(v);
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                        order.setShippingAddress(new StringBuilder(address1.getText()+" ").append(address2.getText()+" ").append(landmark.getText()+" ").append(city.getText()+" ").append(state.getText()+" ").toString());
                        order.setPincode(pincode.getText().toString());
                        try{
                            initializeViews();
                        }catch (NullPointerException ne){
                            ne.printStackTrace();
                        }
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        Message.message(getApplicationContext(),"Change your shipping address");

    }
}
