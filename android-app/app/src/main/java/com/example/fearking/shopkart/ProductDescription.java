
package com.example.fearking.shopkart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ProductDescription extends AppCompatActivity {
    Product product;
    User user;
    TextView productTitle,productMrp,productPrice,description,services,productStockCount;
    RatingBar productRatingBar;
    private ImageView productImage;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_description);

        getReferences();
        initializeValues();
        getSupportActionBar().setTitle("ShopKart");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    private void initializeValues() {


        Random r = new Random();
        int Low = 10;
        int High = 65;
        int discount = ((r.nextInt(High-Low) + Low)*product.getPrice())/100;

        productTitle.setText(product.getName());
        productMrp.setText("MRP: "+MyConstants.rupee+(product.getPrice()+discount));
        productPrice.setText("Selling Price\n \u20B9"+product.getPrice());
        productRatingBar.setRating((float) product.getRating());
        services.setText("Ratings:"+product.getNoOfRating());
        description.setText(product.getDescription());

        if(product.getStock()==0) {
            productStockCount.setText("OUT OF STOCK");
            productStockCount.setTextColor(Color.RED);
        }
        else {
            productStockCount.setText(product.getStock() + " IN STOCK");
            productStockCount.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        }

        productImage.setImageBitmap(image);


    }

    private void getReferences() {
        user=getIntent().getParcelableExtra("user");
        product=getIntent().getParcelableExtra("product");
        image=getIntent().getParcelableExtra("productImage");
        productImage=(ImageView)findViewById(R.id.product_image);
        productTitle=(TextView) findViewById(R.id.product_title);
        productMrp=(TextView)findViewById(R.id.product_mrp);
        productPrice=(TextView) findViewById(R.id.product_price);
        productRatingBar=(RatingBar) findViewById(R.id.product_ratingBar);
        description=(TextView)findViewById(R.id.description);
        services=(TextView) findViewById(R.id.services);
        productStockCount=(TextView) findViewById(R.id.product_stock_count);
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
            new MySessionData(this).logout();
            //startActivity(new Intent(getApplicationContext(),CustomerLoginFragment.class));
            finish();
        }
        else if(item.getItemId()==R.id.menu_contacus){
            startActivity(new Intent(getApplicationContext(),ShopHere.class));
            finish();
        }
        return  true;
    }

    public  void addToCart(View view){

        new CommonRequest(this,null).addToCart(product.getId(),1);
    }
    public void buyNow(View view){
        ArrayList<Product> products=new ArrayList<Product>();
        products.add(product);
        Order order =new Order(products,user.getShippingAddress(),user.getId(),user.getPincode());
        Log.d("AMOUNT",String.valueOf(order.getAmount()));
        Message.message(getApplicationContext(),String.valueOf(order.getAmount()));
        startActivity(new Intent(getApplicationContext(),PaymentGateway.class).putExtra("order", order).putExtra("clearCart",false));

    }
}
