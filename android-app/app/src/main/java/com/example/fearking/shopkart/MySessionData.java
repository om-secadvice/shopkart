
package com.example.fearking.shopkart;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by root on 08-02-2017.
 */

public final class MySessionData {
    public static int USERID;
    public static String session_cookie;
    public static User currentUser;
    public Context context;
    public static boolean isLogin;
    public static boolean hasSession;

    public MySessionData(Context context) {
        this.context = context;
    }



    public synchronized boolean checkLogin(final Callable<Integer> function){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,MyConstants.HOST+"/check-login",
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Message.message(context,response.getString("msg")+"\nUserId:"+response.getInt("id"));
                            MySessionData.USERID=response.getInt("id");
                            Log.d("CHECK-LOGIN",response.getString("msg")+"\nUserId:"+MySessionData.USERID);
                            try {
                                function.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            isLogin=true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Message.message(context, "Not logged in");
                        Log.e("ERROR",error.toString());
                        error.printStackTrace();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("Cookie",context.getSharedPreferences("Session",Context.MODE_PRIVATE).getString("Cookie",null));
                Log.d("COOKIE",""+context.getSharedPreferences("Session",Context.MODE_PRIVATE).getString("Cookie",null));
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
               }catch (NullPointerException ne){}
                return super.parseNetworkResponse(response);
            }

        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        return isLogin;
    }
    public void logout(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,MyConstants.HOST+"/logout",
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Message.message(context,response.getString("msg"));
                            Log.d("LOGOUT",response.getString("msg"));
                            context.getSharedPreferences("Session", Context.MODE_PRIVATE).edit().putString("Cookie",null).putInt("User",0).apply();
                            MySessionData.session_cookie=null;
                            MySessionData.USERID=0;
                            context.startActivity(new Intent(context,LoginPageActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
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
                        context.startActivity(new Intent(context,LoginPageActivity.class));
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("Cookie",session_cookie);
                Log.d("COOKIE/Logout",""+session_cookie);
                return map;
            }

        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /*public void checkSession(){
        hasSession=false;
        session_cookie=context.getSharedPreferences("Session",Context.MODE_PRIVATE).getString("Cookie",null);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,MyConstants.HOST,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                Message.message(context,response.getString("msg"));
                                Log.d("CHECKED SESSION",response.getString("msg"));
                                context.getSharedPreferences("Session", Context.MODE_PRIVATE).edit().putString("Cookie",null).putInt("User",0).apply();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                context.startActivity(new Intent(context,LoginPageActivity.class));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                Message.message(context, error.toString());
                                Log.e("ERROR", error.toString());
                                error.printStackTrace();

                            }catch (Exception e){}
                            finally {
                                context.startActivity(new Intent(context, LoginPageActivity.class));
                            }
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    super.getHeaders();
                    HashMap<String,String> map=new HashMap<String, String>();
                    try {
                        map.put("Cookie", session_cookie);
                        Log.d("COOKIE/SESSION_CHECK", "" + session_cookie);
                    }catch (NullPointerException ne){}
                    return map;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try{
                        if(response.headers.get("set-cookie")!=null){
                            context.getSharedPreferences("Session",Context.MODE_PRIVATE).edit().putString("Cookie",response.headers.get("set-cookie")).apply();
                            hasSession=false;
                        }else hasSession=true;
                    }catch (NullPointerException ne){
                        hasSession=true;
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }*/
}
