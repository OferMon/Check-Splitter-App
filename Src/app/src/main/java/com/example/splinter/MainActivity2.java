package com.example.splinter;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        SharedPreferences mPreferences = getSharedPreferences("Splinter", 0);
        SharedPreferences.Editor sedt = mPreferences.edit();
        sedt.clear();
        sedt.apply();

        ImageView img1 = (ImageView) findViewById(R.id.food1_pic);
        ObjectAnimator mover = ObjectAnimator.ofFloat(img1, "translationY", 400f,0f);
        mover.setDuration(3500);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(img1,"alpha", 0f, 1f);
        fadeIn.setDuration(3500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeIn).with(mover);
        animatorSet.start();
        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, NamesActivity.class);
                startActivity(intent);
            }
        });
    }
}