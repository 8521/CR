package com.ilaquidain.constructionreportersfi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import com.google.android.gms.ads.MobileAds;

public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.splasscreen);
        //MobileAds.initialize(this,"ca-app-pub-9978137837326968/1638389074");

        Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
        SplashScreenActivity.this.startActivity(intent);
        SplashScreenActivity.this.finish();
    }

}
