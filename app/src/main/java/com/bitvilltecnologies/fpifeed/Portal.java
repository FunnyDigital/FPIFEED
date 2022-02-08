package com.bitvilltecnologies.fpifeed;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class Portal extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.notification:
                    Intent intent =new Intent(Portal.this,NewsFeed.class);
                    finish();
                    startActivity(intent);
                    return true;

                case R.id.schoolportal:

                    return true;


                case R.id.profile:
                    Intent intent2 =new Intent(Portal.this,Profile.class);
                    finish();
                    startActivity(intent2);
                    return true;

            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);

        BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.schoolportal);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://student.fepoda.edu.ng/#");

    }
}
