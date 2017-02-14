
package com.example.fearking.shopkart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ConfirmationPage extends AppCompatActivity {

    TextView referenceNumber,price,shippingAddress,delivery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page);

        getReferences();
        initializeViews();
        try{
            getSupportActionBar().setTitle("Confirmation Page");
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }}
        catch (NullPointerException e){

        }
    }

    private void initializeViews() {
        Order order=getIntent().getParcelableExtra("order");

        referenceNumber.setText(""+order.getConfirmationNumber());
        price.setText(""+MyConstants.rupee+order.getAmount());
        if(order.getAmount()-50>500){
            delivery.setText("FREE");
        }else{
            delivery.setText(MyConstants.rupee+"+50");
        }
        shippingAddress.setText(order.getShippingAddress()+"\nPin code:-"+order.getPincode());
    }

    private void getReferences() {
        delivery=(TextView) findViewById(R.id.delivery);
        referenceNumber=(TextView)findViewById(R.id.referenceNumber);
        price=(TextView) findViewById(R.id.price);
        shippingAddress=(TextView) findViewById(R.id.shippingAddress);


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
        else if(item.getItemId()==R.id.myprofile){
            startActivity(new Intent(getApplicationContext(),MyProfile.class));
            finish();
        }
        else if(item.getItemId()==R.id.menu_home){
            startActivity(new Intent(getApplicationContext(),ShopHere.class));
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
        return  true;
    }
    public void confirmation(View view){
        startActivity(new Intent(getApplicationContext(),MyOrders.class));
        finish();
    }
}
