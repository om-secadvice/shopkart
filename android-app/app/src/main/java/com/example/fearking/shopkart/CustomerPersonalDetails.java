package com.example.fearking.shopkart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class CustomerPersonalDetails extends AppCompatActivity {

    User user;
    String[] questions = {
            "What is your birthplace?",
            "What is your pet name?",
            "What is your mother's maiden name?"
    };
    EditText username,password,name,phone,email;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutomer_register);
        initialization();
        user =new User();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, questions);

        AutoCompleteTextView textView =
                (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        textView.setThreshold(0);
        textView.setAdapter(adapter);

        try{
            getSupportActionBar().setTitle("MyCart");
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }}
        catch (NullPointerException e){

        }
    }
    public void initialization(){

        username=(EditText)findViewById(R.id.register_user);
        password=(EditText)findViewById(R.id.register_password);
        name=(EditText)findViewById(R.id.register_fullname);
        phone=(EditText)findViewById(R.id.reg_ph);
        email=(EditText)findViewById(R.id.reg_email);
        button=(Button)findViewById(R.id.proceed_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=new User(0,name.getText().toString(),username.getText().toString(),password.getText().toString(),phone.getText().toString(),email.getText().toString(),null,null,null,null,null,null);
                Intent i=new Intent(getBaseContext(),CustomerAddressRegister.class);
                i.putExtra("user",user);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


}
