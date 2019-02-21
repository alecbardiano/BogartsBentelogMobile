package com.example.user.bogartsbentelogmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    EditText edtEmail, edtPassword;
    private Button btnsignUp;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        edtEmail = (EditText)findViewById(R.id.edtemail);
        edtPassword = (EditText)findViewById(R.id.edtpassword);

        btnsignUp = (Button)findViewById(R.id.btnSignUp);
        btnsignUp.setOnClickListener(this);

    }

    private void registerUser(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            return;
            // stop the app further
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password", Toast.LENGTH_SHORT).show();
            return;
            // stop the app further
        }

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // user is registered and will log in
                            Toast.makeText(SignUp.this, "Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SignIn.class));

                        }else{
                            Toast.makeText(SignUp.this, "Not Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    @Override
    public void onClick(View view){
        if (view == btnsignUp){
            registerUser();
        }
    }
}
