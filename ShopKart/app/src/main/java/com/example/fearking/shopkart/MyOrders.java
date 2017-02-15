
package com.example.fearking.shopkart;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrders extends AppCompatActivity implements AdapterView.OnItemClickListener{

    MyOrdersAdapter  adapter;
    ArrayList<Order> orders;
    ListView myOrdersListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_orders);


        myOrdersListview=(ListView)findViewById(R.id.my_orders_listview);
        orders=new ArrayList<>();
        loadMyOrders();
        adapter=new MyOrdersAdapter(this,R.layout.ordered_product,orders);


        try{

            myOrdersListview.setAdapter(adapter);
        }catch (NullPointerException ne){
            ne.printStackTrace();
        }

       try{
            getSupportActionBar().setTitle("MyOrders");
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }}
        catch (NullPointerException e){
        }
        myOrdersListview.setOnItemClickListener(this);


    }

    public void loadMyOrders() {
        final Map<String,String> headers=new HashMap<>();
        headers.put("Cookie", MySessionData.session_cookie);
        final Type type=new TypeToken<ArrayList<Order>>(){}.getType();
        final Gson gson=new Gson();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, MyConstants.HOST+"/myOrders/"+MySessionData.USERID,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("MY ORDERS:", response.toString());

                        orders.addAll((ArrayList<Order>)gson.fromJson(String.valueOf(response),type));
                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR-CART LIST",error.toString());
                Message.message(getBaseContext(),"Error: "+error.toString());
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

   @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Order order=(Order) parent.getItemAtPosition(position);

        startActivity(new Intent(this,OrderDetails.class).putExtra("order",order));

    }
}
