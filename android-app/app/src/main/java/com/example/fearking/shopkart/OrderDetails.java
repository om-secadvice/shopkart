package com.example.fearking.shopkart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity {

    ArrayList<Order> orders;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        orders=new ArrayList<>();
        listView=(ListView)findViewById(R.id.order_details_listview);
        OrderedProductAdapater orderedProductAdapater=new OrderedProductAdapater(this,R.layout.myorder_product_items,orders);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
