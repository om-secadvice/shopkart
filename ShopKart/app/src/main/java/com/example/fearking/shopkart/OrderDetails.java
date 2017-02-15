
package com.example.fearking.shopkart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    ArrayList<Product> products;
    Order order;
    OrderedProductAdapater orderedProductAdapater;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        products=new ArrayList<>();
        order=getIntent().getParcelableExtra("order");
        getOrderedProductsByConfirmation(order.getConfirmationNumber());
        listView=(ListView)findViewById(R.id.order_details_listview);
        orderedProductAdapater=new OrderedProductAdapater(this,R.layout.myorder_product_items,products);
        listView.setAdapter(orderedProductAdapater);

        try{
            getSupportActionBar().setTitle("MyOrders");
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }}
        catch (NullPointerException e){
        }
    }

    private void getOrderedProductsByConfirmation(long confirmationNumber) {
        final Map<String,String> headers=new HashMap<String, String>();
        headers.put("Cookie", MySessionData.session_cookie);
        final Type type=new TypeToken<ArrayList<Product>>(){}.getType();
        final Gson gson=new Gson();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, MyConstants.HOST+"/getOrderDetails/$"+confirmationNumber,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ORDERED ITEMS LIST", response.toString());
                        products.clear();
                        products.addAll((ArrayList<Product>)gson.fromJson(String.valueOf(response),type));
                        orderedProductAdapater.notifyDataSetChanged();


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
