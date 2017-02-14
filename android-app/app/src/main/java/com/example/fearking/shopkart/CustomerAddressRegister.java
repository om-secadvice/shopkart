
package com.example.fearking.shopkart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CustomerAddressRegister extends AppCompatActivity {

    EditText one,two,landmark,city,state,pincode;
    Button register;
    Context context;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_address_register);


        TextView  skip=(TextView)findViewById(R.id.skip_address_register);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.message(getApplicationContext(),"This step is skipped");
            }
        });
        try{
            getSupportActionBar().setTitle("MyCart");
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }}
        catch (NullPointerException e){

        }

        context=getApplicationContext();
        user=getIntent().getParcelableExtra("user");
        one=(EditText)findViewById(R.id.add_lin1);
        two=(EditText)findViewById(R.id.add_lin2);
        landmark=(EditText)findViewById(R.id.add_land);
        city=(EditText)findViewById(R.id.add_city);
        state=(EditText)findViewById(R.id.add_state);
        pincode=(EditText)findViewById(R.id.add_pin);
        register=(Button)findViewById(R.id.register_button);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.setAddressLine1(one.getText().toString());
                user.setAddressLine2(two.getText().toString());
                user.setLandmark(landmark.getText().toString());
                user.setCity(city.getText().toString());
                user.setState(state.getText().toString());
                user.setPincode(pincode.getText().toString());

                Log.d("REGISTER CLICK","Register button clicked.");
                new CommonRequest(getBaseContext(),null).registerCustomer(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return goToLoginActivity();
                    }
                },user,"customer",false);
            }
        });
    }

    private Integer goToLoginActivity() {
        startActivity(new Intent(this,LoginPageActivity.class));
        finish();
        return Integer.valueOf(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
