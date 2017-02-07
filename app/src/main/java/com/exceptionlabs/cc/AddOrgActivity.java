package com.exceptionlabs.cc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AddOrgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_org);

        View vFrag = (View) findViewById(R.id.vFrag);
        ImageView ivStars = (ImageView) findViewById(R.id.ivStars);
        vFrag.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.slide_up));
        //ivStars.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_out));
    }
}
