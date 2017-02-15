
package com.example.fearking.shopkart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ViewCart extends AppCompatActivity{
    ArrayList<Product> cartItems;
    CartAdapter cartAdapter;
    ListView myCartList;
    User user;
    Button incr,decr;
    TextView price,amount,delivery;
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cart);


        price=(TextView)findViewById(R.id.price);
        amount=(TextView) findViewById(R.id.amount);
        delivery=(TextView) findViewById(R.id.delivery);

        myCartList=(ListView) findViewById(R.id.myCartList);
        user=getIntent().getParcelableExtra("user");

        try{
        getSupportActionBar().setTitle("MyCart");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }}
        catch (NullPointerException e){

        }

        cartItems=new ArrayList<>();
        fetchCartItems();

        cartAdapter=new CartAdapter(this,R.layout.mycart_items,cartItems,order);
        myCartList.setAdapter(cartAdapter);


    }


    private void initializeViews() {
        price.setText(""+MyConstants.rupee+order.getAmount());
        if(order.getAmount()>500 && order.getNoOfItems()>0){
            delivery.setText("FREE");
            amount.setText(""+MyConstants.rupee+order.getAmount());
        }else if(order.getNoOfItems()>0){
            delivery.setText(MyConstants.rupee+"50.00");
            amount.setText(MyConstants.rupee+(order.getAmount()+50));
        }else{
            delivery.setText(MyConstants.rupee+"0.00");
            amount.setText(MyConstants.rupee+(order.getAmount()));
        }
    }

    public void paymentGateway(View view){
        Order order=new Order(cartItems,user.getShippingAddress(),user.getId(),user.getPincode());
        if (order == null || order.getNoOfItems() == 0){
            Message.message(this,"No items in cart.");
            return;
        }
        Intent i=new Intent(this,PaymentGateway.class).putExtra("order",order).putExtra("clearCart",true);
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return  true;
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
        else if(item.getItemId()==R.id.menu_logout){
            new MySessionData(this).logout();
            finish();
        }
        else if(item.getItemId()==R.id.menu_contacus){
            startActivity(new Intent(getApplicationContext(),ShopHere.class));
            finish();
        }
        return  true;
    }
    public void fetchCartItems(){
        final Map<String,String> headers=new HashMap<String, String>();
        headers.put("Cookie", MySessionData.session_cookie);
        final Type type=new TypeToken<ArrayList<Product>>(){}.getType();
        final Gson gson=new Gson();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, MyConstants.HOST+"/viewCart/",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("CART ITEMS LIST", response.toString());

                        cartItems.addAll((ArrayList<Product>)gson.fromJson(String.valueOf(response),type));
                        order=new Order();
                        order.setProducts(cartItems);
                        initializeViews();

                        cartAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR-CART LIST",error.toString());
                Message.message(getApplicationContext(),"Error: "+error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

}
