
package com.example.fearking.shopkart;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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

public class MyProfile extends AppCompatActivity implements View.OnClickListener{

    User user;
    TextView name,username,mobile,email,addressLine1,addressLine2,landmark,pincode,city,state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        TextView edit=(TextView)findViewById(R.id.editprofile_textview);
        edit.setOnClickListener(this);

        getReferences();
        initializeViews();
        try{
            getSupportActionBar().setTitle("MyProfile");
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }}
        catch (NullPointerException e){

        }
    }

    private void initializeViews() {
        try {
            name.setText(user.getName());
            username.setText(user.getUsername());
            email.setText(user.getEmail());
            mobile.setText(user.getPhone());
            addressLine1.setText(user.getAddressLine1());
            addressLine2.setText(user.getAddressLine2());
            pincode.setText(user.getPincode());
            city.setText(user.getCity());
            state.setText(user.getState());
        }catch(NullPointerException ne){
            ne.printStackTrace();
        }
       // return Integer.valueOf(1);
    }

    private void getReferences() {
        user=getIntent().getParcelableExtra("user");
        name=(TextView) findViewById(R.id.name);
        username=(TextView) findViewById(R.id.username);
        email=(TextView) findViewById(R.id.email);
        mobile=(TextView) findViewById(R.id.mobile);
        addressLine1=(TextView) findViewById(R.id.address_line_1);
        addressLine2=(TextView) findViewById(R.id.address_line_2);
        pincode=(TextView) findViewById(R.id.pincode);
        city=(TextView) findViewById(R.id.city);
        state=(TextView) findViewById(R.id.state);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return  true;
    }

    @Override
    public void onClick(View v) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        LayoutInflater inflater=LayoutInflater.from(this);
        builder.setTitle("Enter your address :");
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

                        if (!(address1.getText().toString().trim().equals("") || address2.getText().toString().trim().equals("") || landmark.getText().toString().trim().equals("") || pincode.getText().toString().trim().equals("") || city.getText().toString().trim().equals("") || state.getText().toString().trim().equals(""))) {
                            user.setAddressLine1(address1.getText().toString());
                            user.setAddressLine2(address2.getText().toString());
                            user.setPincode(pincode.getText().toString());
                            user.setLandmark(landmark.getText().toString());
                            user.setCity(city.getText().toString());
                            user.setState(state.getText().toString());

                            updateAddress();
                        }else{
                            Message.message(getBaseContext(),"Fill all fields properly.");
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
        Message.message(getApplicationContext(),"Change your address");

    }

    private void updateAddress() {
        new CommonRequest(this,null).registerCustomer(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                initializeViews();
                return Integer.valueOf(1);
            }

        }, user, "customer",true);

    }
}
