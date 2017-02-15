
package com.example.fearking.shopkart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

public class ShopHere extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    ArrayList<Product> products;
    ProductListAdapter adapter;
    View v;
    User currentUser;
    EditText currentPassword,newPassword,newPasswordConfirm;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        email=(TextView)findViewById(R.id.nav_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MySessionData.USERID=getSharedPreferences("Session",Context.MODE_PRIVATE).getInt("User",0);
        MySessionData.session_cookie=getSharedPreferences("Session",Context.MODE_PRIVATE).getString("Cookie",null);


        products=new ArrayList<>();
        fetchUserDetails();
        getProductList("all");

        getSupportActionBar().setTitle("ShopKart");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ListView listView=(ListView)findViewById(R.id.listview);
        adapter=new ProductListAdapter(this,R.layout.custom_product_list,products);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product= (Product) adapterView.getItemAtPosition(i);
                Bitmap productImage=MyConstants.loadBitmapFromView(view.findViewById(R.id.image));

                //Start new activity for selected product description
                startActivity(new Intent(getBaseContext(),ProductDescription.class).putExtra("product",product).putExtra("productImage",productImage).putExtra("user",currentUser));
                Message.message(getApplication(),product.getName());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  email.setText(currentUser.getEmail());
        getProductList(MyConstants.CATEGORY_SELECTED);
        adapter.notifyDataSetChanged();

    }

    private void getProductList(String categoryId) {
        MyConstants.CATEGORY_SELECTED=categoryId;
        final Map<String,String> headers=new HashMap<String, String>();
        headers.put("Cookie", MySessionData.session_cookie);
        Type type=new TypeToken<ArrayList<User>>(){}.getType();
        final Gson gson=new Gson();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, MyConstants.HOST+"/product/"+categoryId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("PRODUCT LIST", response.toString());
                            products.clear();
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonProduct = (JSONObject) response.get(i);
                                products.add(gson.fromJson(jsonProduct.toString(), Product.class));

                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR-PRODUCT LIST",error.toString());
                        Message.message(getApplicationContext(),"Error: "+error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product, menu);
        menu.add("Check Login");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Message.message(this,"Loading profile...");
            startActivity(new Intent(getApplicationContext(),MyProfile.class).putExtra("user",currentUser));
            return true;
        }
        else if (id == R.id.log_out) {
            new MySessionData(this).logout();
            finish();
            return true;
        }
        else if (id == R.id.contact_us) {

            Toast.makeText(this,"Email us at debjyotipandit35@gmail.com",Toast.LENGTH_LONG).show();
            return true;
        }else if(item.getTitle()=="Check Login"){
            new MySessionData(this).checkLogin(null);

        } else if (item.getItemId() == R.id.change_password) {
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(true);
            LayoutInflater inflater=LayoutInflater.from(this);
            builder.setTitle("Change your password");
            v=inflater.inflate(R.layout.edit_password,null);
            newPassword=(EditText)v.findViewById(R.id.new_password);
            newPasswordConfirm=(EditText)v.findViewById(R.id.new_password_confirm);
            currentPassword=(EditText)v.findViewById(R.id.current_password);

            builder.setView(v);
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            try{
                            String current = currentPassword.getText().toString();
                            String new_pass = newPasswordConfirm.getText().toString();
                            String new_pass_confirm = newPassword.getText().toString();

                            /*if (new_pass.equals(new_pass_confirm) && !(new_pass.trim().equals("") || new_pass_confirm.trim().equals("") || current.trim().equals(""))) {*/
                                new CommonRequest(getApplicationContext(), null).changePassword(new_pass_confirm,current);
                            //}
                            }
                            catch (NullPointerException ne){
                                ne.printStackTrace();
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home) {
            getProductList("all");
            // Handle the camera action
        }
        else if (id == R.id.my_orders) {

            startActivity(new Intent(this,MyOrders.class));

        } else if (id == R.id.mens_clothing) {
            getProductList("1");
            Toast.makeText(this,"mensclothing selected",Toast.LENGTH_LONG).show();

        }  else if (id == R.id.womens_clothing) {
            getProductList("2");
            Toast.makeText(this,"womenscloth selected",Toast.LENGTH_LONG).show();

        } else if (id == R.id.kids_clothing) {
            getProductList("3");
            Toast.makeText(this,"kidscloth selected",Toast.LENGTH_LONG).show();

        }else if (id == R.id.mobiles) {
            getProductList("4");
            Toast.makeText(this,"mobiles selected",Toast.LENGTH_LONG).show();

        }  else if (id == R.id.laptops) {
            getProductList("5");
            Toast.makeText(this,"lappy selected",Toast.LENGTH_LONG).show();

        } else if (id == R.id.electronics) {
            getProductList("6");
            Toast.makeText(this,"electronic selected",Toast.LENGTH_LONG).show();

        }else if (id == R.id.food) {
            getProductList("7");
            Toast.makeText(this,"food selected",Toast.LENGTH_LONG).show();

        }  else if (id == R.id.games) {
            getProductList("8");
            Toast.makeText(this,"games selected",Toast.LENGTH_LONG).show();

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fetchUserDetails(){

        Map<String,String> headers=new HashMap<>();
        headers.put("Cookie", MySessionData.session_cookie);
        Type type=new TypeToken<ArrayList<User>>(){}.getType();
        final Gson gson=new Gson();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, MyConstants.HOST+"/customer/" + MySessionData.USERID,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("USER",response.toString());
                            JSONObject jsonUser=response.getJSONObject(0);
                             currentUser= gson.fromJson(jsonUser.toString(),User.class);
                         //   email.setText(currentUser.getEmail());
                            MySessionData.currentUser=currentUser;
                            Log.d("USER", currentUser.toString() + ":" + currentUser.getName());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("USER-FETCHING-ERROR",error.toString());
                        }
                    }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("Content-Type","application/json");
                map.put("charset","utf-8");

                map.put("Cookie",MySessionData.session_cookie);
                Log.d("COOKIE/ShopHere",""+MySessionData.session_cookie);
                return map;
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    Log.d("LOGIN COOKIE RECEIVED", response.headers.get("set-cookie").toString());
                    if (response.headers.get("set-cookie") != null) {
                        MySessionData.session_cookie = response.headers.get("set-cookie");
                        getSharedPreferences("Session", Context.MODE_PRIVATE).edit().putInt("User", MySessionData.USERID).putString("Cookie", MySessionData.session_cookie).apply();
                    }
                }catch (NullPointerException ne){}
                return super.parseNetworkResponse(response);
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                startActivity(new Intent(getBaseContext(),ViewCart.class).putExtra("user",currentUser));
                break;
        }

    }



}
