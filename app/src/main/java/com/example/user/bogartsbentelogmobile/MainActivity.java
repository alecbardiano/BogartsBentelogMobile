package com.example.user.bogartsbentelogmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button signIn,signUp;
//    TextView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = (Button)findViewById(R.id.btnSignIn);
        signUp = (Button)findViewById(R.id.btnSignUp);

//        signIn.setOnClickListener(new View.OnClickListener()) {
//            @Override
//            public void onClick(View view){
//
//            }
//        };
//
//        signUp.setOnClickListener(new View.OnClickListener()) {
//            @Override
//            public void onClick(View view){
//
//            }
//        };

        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }

        });
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent signup = new Intent(MainActivity.this,SignUp.class);
                startActivity(signup);
            }

        });

        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent signin = new Intent(MainActivity.this,SignIn.class);
                startActivity(signin);

            }

        });



    }
}
