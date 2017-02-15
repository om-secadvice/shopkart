
package com.example.fearking.shopkart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerLoginFragment extends Fragment implements View.OnClickListener{
    Context context;
    EditText username, password;
    Button loginButton,signButton;
    TextView newUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.login_customer, container, false);
        username = (EditText) view.findViewById(R.id.editText_username);
        password = (EditText) view.findViewById(R.id.editText_password);
        loginButton=(Button) view.findViewById(R.id.button_login);
        signButton=(Button) view.findViewById(R.id.signup_button);
        newUser=(TextView)view.findViewById(R.id.forget_pass_retailer);

        loginButton.setOnClickListener(this);
        signButton.setOnClickListener(this);
        newUser.setOnClickListener(this);


        /*newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/



        context=getActivity();
        return view;
    }
    public void signUpCustomer() {
        startActivity(new Intent(getActivity(),CustomerPersonalDetails.class));
    }
    public void loginCustomer() {

        //Map<String,String> body=new HashMap<>();
        JSONObject body=new JSONObject();
        try {
            body.put("username",username.getText().toString());
            body.put("password",password.getText().toString());
            body.put("userType","customer");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,MyConstants.HOST+"/login",body.toString(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            MySessionData.USERID=response.getInt("id");
                            getActivity().getSharedPreferences("Session",Context.MODE_PRIVATE).edit().putInt("User",MySessionData.USERID).apply();
                            Message.message(getActivity(),response.getString("msg")+"\nUserId:"+MySessionData.USERID);
                            //Goto Product activity
                            Intent intent = new Intent(getActivity(), ShopHere.class);
                            startActivity(intent);
                            getActivity().finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Message.message(getActivity(), error.toString());
                        Log.e("ERROR",error.toString());
                        username.setText("");
                        password.setText("");
                        error.printStackTrace();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String,String> map=new HashMap<>();
                map.put("Content-Type","application/json");
                map.put("charset","utf-8");

                map.put("Cookie",getActivity().getSharedPreferences("Session", Context.MODE_PRIVATE).getString("Cookie",null));
                Log.d("COOKIE-SharedPrefs",""+getActivity().getSharedPreferences("Session", Context.MODE_PRIVATE).getString("Cookie",null));

                return map;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Log.d("LOGIN COOKIE RECEIVED", response.headers.get("set-cookie"));
                    if (response.headers.get("set-cookie") != null) {
                        MySessionData.session_cookie = response.headers.get("set-cookie");
                        getActivity().getSharedPreferences("Session", Context.MODE_PRIVATE).edit().putString("Cookie", MySessionData.session_cookie).apply();
                    }
                }catch (NullPointerException ne){}
                return super.parseNetworkResponse(response);
            }

        };


        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);



        /*if ((user.equals("q")) && (pass.equals("q"))) {

        } else {
            Message.message(getActivity(),"Enter correct credentials!");
        }*/

        /*final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        progressDialog.setMax(50);
            new Thread() {
                @Override
                public void run(){
                    super.run();
                    for (int i = 0; i < progressDialog.getMax(); i++) {
                        progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                            final String user = username.getText().toString();
                            final String pass = password.getText().toString();
                            if ((user.equals("q")) && (pass.equals("q"))) {
                                progressDialog.dismiss();
                                startActivity(new Intent().setClass(getActivity(), Location_after_login.class));
                            } else {
                                progressDialog.dismiss();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        username.setText("");
                                        password.setText("");
                                        Toast.makeText(getActivity(),"plllllll",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                                try {
                                    Thread.sleep(100);
                                    }
                                catch (InterruptedException e){
                                        e.getStackTrace();
                                    }
                            }
                    }
                }
            }.start();*/
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login:
                Message.message(getActivity(),"Hello");
                loginCustomer();
                break;
            case R.id.signup_button:
                signUpCustomer();
                break;
            case R.id.forget_pass_retailer:
                startActivity(new Intent().setClass(getContext(),Recovery_Account.class));
                break;
        }
    }
}

