package com.example.trialproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SpalshActivity extends AppCompatActivity {

    private TextView appName;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        appName=findViewById(R.id.app_message);
        Typeface typeface= ResourcesCompat.getFont(this,R.font.blacklist2);
        appName.setTypeface(typeface);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.myanim);

        appName.setAnimation(anim);

        mAuth=FirebaseAuth.getInstance();

       DbQuery.g_firestore= FirebaseFirestore.getInstance();

        new Thread()
        {
            @Override
            public void run()
            {
                try {
                    sleep(3000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }

                if(mAuth.getCurrentUser() !=null)
                {
                   DbQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {

                            Intent intent=new Intent(SpalshActivity.this,MainActivity.class);
                            startActivity(intent);
                            SpalshActivity.this.finish();

                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(SpalshActivity.this, "Something went wrong ! PLease try again later !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Intent intent=new Intent(SpalshActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SpalshActivity.this.finish();
                }

            }
        }.start();
    }
}