
package com.example.fearking.shopkart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);

        startAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView.startAnimation(startAnimation);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(getApplicationContext(),LoginPageActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(startAnimation);


        /*MySessionData mySessionData=new MySessionData(this);
        mySessionData.checkSession();
        if(MySessionData.hasSession){
            startActivity(new Intent(this,ShopHere.class));
            finish();
        }*/
    }

/*
     public void onClick(View view){
        startActivity(new Intent(this,LoginPageActivity.class));
        finish();
     }*/


        /*SharedPreferences sharedPreferences=getSharedPreferences("Session", Context.MODE_PRIVATE);
        if(sharedPreferences!=null) {
            MySessionData.session_cookie = sharedPreferences.getString("Cookie", null);
            MySessionData.USERID = sharedPreferences.getInt("User", 0);
            Log.d("PREVIOUS SESSION DATA:-",MySessionData.session_cookie+" "+MySessionData.USERID);
            if(MySessionData.session_cookie!=null)
                startActivity(new Intent(this,ShopHere.class));

        }
        else {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("User",MySessionData.USERID);
            editor.putString("Cookie",MySessionData.session_cookie);
            editor.apply();
        }
    }*/
}
