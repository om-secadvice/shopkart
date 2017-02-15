package com.example.fearking.shopkart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RetailerLogin extends Fragment {
    EditText username, password;
    Button button;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.login_retailer, container, false);
        username = (EditText) view.findViewById(R.id.editText_username_retailer);
        password = (EditText) view.findViewById(R.id.editText_password_retailer);
        button = (Button) view.findViewById(R.id.button_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        textView=(TextView)view.findViewById(R.id.forget_pass_retailer);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(getContext(),Recovery_Account.class));
            }
        });
        return view;
    }

    public void login() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        username.setText("");
        password.setText("");
        if ((user.equals("q")) && (pass.equals("q"))) {
            startActivity(new Intent().setClass(getActivity(), Retailer_after_login.class));
            Message.message(getActivity(), "CustomerLoginFragment Success");
        } else {
            Message.message(getActivity(), "Enter correct credentials!");
        }
    }

}
