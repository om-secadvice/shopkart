
package com.example.fearking.shopkart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Location_after_login extends AppCompatActivity {
    TextView textView;
    ListView listView;
    String places[] = {
            "Kolkata", "Delhi", "Mumbai", "Chennai", "asansol", "andal","Siliguri", "barauni", "liluah", "patna", "chandil", "dumdum", "malda", "durgapur", "kota", "sonagachi"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_after_login);
        textView=(TextView)findViewById(R.id.selected_place);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places);
       // listView=getListView();
        listView = (ListView)findViewById(R.id.place_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s=listView.getItemAtPosition(i).toString();
                textView.setText(s);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,"http://192.168.2.16:8080/check-login",
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    Message.message(getApplicationContext(), response.getString("msg"));
                                    Log.d("TAGGED",response.getString("msg"));
                                    startActivity(new Intent().setClass(getApplicationContext(),ShopHere.class));
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Message.message(getApplicationContext(), error.toString());
                                Log.e("ERROR",error.toString());
                                error.printStackTrace();
                            }
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        super.getHeaders();
                        HashMap<String,String> map=new HashMap<String, String>();
                        map.put("Content-Type","application/json");
                        map.put("charset","utf-8");
                        map.put("Cookie",MySessionData.session_cookie);
                        Log.d("SESSION SENT",MySessionData.session_cookie);
                        return map;
                    }
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                        MySessionData.session_cookie=response.headers.get("set-cookie");
                        Log.d("UPDATE","Updating Session "+MySessionData.session_cookie);
                        return super.parseNetworkResponse(response);
                    }

                };

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);



                /*startActivity(new Intent(getApplicationContext(),ShopHere.class));
                finish();*/

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onPause();
    }

}
