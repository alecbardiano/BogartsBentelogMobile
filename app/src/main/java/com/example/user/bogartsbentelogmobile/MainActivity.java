package com.example.user.bogartsbentelogmobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.bogartsbentelogmobile.Common.Common;
import com.example.user.bogartsbentelogmobile.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    Button signIn,signUp;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    TextView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signIn = (Button)findViewById(R.id.btnSignIn);
        signUp = (Button)findViewById(R.id.btnSignUp);
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
