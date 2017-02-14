
package com.example.fearking.shopkart;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

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

/**
 * Created by root on 13-02-2017.
 */

public class CommonRequest {

    Context context;
    Order order;
    public CommonRequest(Context context,@Nullable final Order order){
        this.context=context;
        this.order=order;
    }

    public void addToCart(int productId, int quantity){
        final String url=MyConstants.HOST+"/addToCart/product/"+productId+"/quantity/"+quantity;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Message.message(context,response.getString("msg"));
                            Log.d("CART/ADDED",response.getString("msg"));
                            order.getAmount();
                            context.startActivity(new Intent(context,DummyActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (NullPointerException ne){ne.printStackTrace();}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Message.message(context, error.toString());
                        Log.e("ERROR",error.toString());
                        error.printStackTrace();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("Cookie",MySessionData.session_cookie);
                Log.d("COOKIE/DESCRIPTION",""+MySessionData.session_cookie);
                return map;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Log.d("COOKIE/CHECK-LOGIN", response.headers.get("set-cookie"));
                    if (response.headers.get("set-cookie") != null) {
                        MySessionData.session_cookie = response.headers.get("set-cookie");
                        context.getSharedPreferences("Session", Context.MODE_PRIVATE).edit().putString("Cookie", MySessionData.session_cookie).apply();
                    }
                }catch (NullPointerException ne){ne.printStackTrace();}
                return super.parseNetworkResponse(response);
            }

        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void removeFromCart(int productId,int quantity){
        final String url=MyConstants.HOST+"/removeFromCart/product/"+productId+"/quantity/"+quantity;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Message.message(context,response.getString("msg"));
                            Log.d("CART/REMOVED",response.getString("msg"));
                            order.getAmount();
                            context.startActivity(new Intent(context,DummyActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (NullPointerException ne){ne.printStackTrace();}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Message.message(context, error.toString());
                        Log.e("ERROR",error.toString());
                        error.printStackTrace();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("Cookie",MySessionData.session_cookie);
                Log.d("COOKIE/DESCRIPTION",""+MySessionData.session_cookie);
                return map;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Log.d("COOKIE/CHECK-LOGIN", response.headers.get("set-cookie"));
                    if (response.headers.get("set-cookie") != null) {
                        MySessionData.session_cookie = response.headers.get("set-cookie");
                        context.getSharedPreferences("Session", Context.MODE_PRIVATE).edit().putString("Cookie", MySessionData.session_cookie).apply();
                    }
                }catch (NullPointerException ne){ne.printStackTrace();}
                return super.parseNetworkResponse(response);
            }

        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void registerCustomer(final Callable<Integer> function, User user,String userType,boolean updateBool){
        Gson gson=new Gson();
        String bodyUser=gson.toJson(user,User.class);
        Log.d("qwerty","qwrtyuiopppppppppppppppppppppppppppp");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,MyConstants.HOST+"/register/"+userType+"/updateBool/"+updateBool,bodyUser,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Message.message(context, response.getString("msg"));
                            try {
                                function.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Message.message(context, error.toString());
                        Log.e("ERROR",error.toString());
                        error.printStackTrace();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("Content-Type","application/json");
                map.put("charset","utf-8");
                map.put("Cookie",MySessionData.session_cookie);
                return map;
            }


        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void changePassword(String password,String currentPassword){
        Log.d("FOUND PASS CHANGE",password+":"+currentPassword);
        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("password",password);
            jsonObject.put("currentPassword",currentPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,MyConstants.HOST+"/changePassword",jsonObject.toString(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Message.message(context, response.getString("msg"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Message.message(context, "Can't change password.");
                        Log.e("ERROR",error.toString());
                        error.printStackTrace();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("Content-Type","application/json");
                map.put("charset","utf-8");
                map.put("Cookie",MySessionData.session_cookie);
                return map;
            }


        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }



}
