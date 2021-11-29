 package com.example.spectrumadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

 public class LoginActivity extends AppCompatActivity {
Button loginbtn;
EditText memail;
EditText mpassword;
FirebaseAuth fAuth;

Animation scaleUp,scaleDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn= findViewById(R.id.signin_btn);
        memail=findViewById(R.id.email_txt);
        fAuth=FirebaseAuth.getInstance();
        mpassword=findViewById(R.id.password_txt);

        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);

        final LoadingDialog loadingDialog=new LoadingDialog(LoginActivity.this);


        loginbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    loginbtn.startAnimation(scaleUp);
                } else if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    loginbtn.startAnimation(scaleDown);
                }
                return false;
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= memail.getText().toString().trim();
                String password=mpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    memail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mpassword.setError("Password is required...ofc");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){



                          Toast.makeText(LoginActivity.this,"welcome admin",Toast.LENGTH_SHORT).show();
                          Intent Intent= new Intent(LoginActivity.this,MainActivity.class);
                          startActivity(Intent);

                          loadingDialog.startLoadingDialog();
                          Handler handler=new Handler();
                          handler.postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  loadingDialog.dismissDialog();
                              }
                          },3000);
                          //finish();
                      }
                      else {
                          Toast.makeText(LoginActivity.this,"error!!"+task.getException().getMessage(),Toast.LENGTH_SHORT);
                      }
                    }
                });
            }
                /*Intent Intent= new Intent(LoginActivity.this,MainActivity.class);
                startActivity(Intent);
                loadingDialog.startLoadingDialog();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                },3000);        */

            //}


        });

    }


}