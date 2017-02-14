
package com.example.fearking.shopkart;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class LoginPageActivity extends FragmentActivity
{
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        viewPager=(ViewPager)findViewById(R.id.pager);
        Adapter adapter=new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);



        if(new MySessionData(this).checkLogin(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return changeActivity();
            }

        })){
            Message.message(getApplicationContext(),"Hi");
        }

    }

    private Integer changeActivity() {
        startActivity(new Intent(getApplicationContext(),ShopHere.class));
        finish();
        return Integer.valueOf(1);
    }


}

