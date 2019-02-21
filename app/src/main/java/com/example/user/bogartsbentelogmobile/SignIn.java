package com.example.user.bogartsbentelogmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.bogartsbentelogmobile.Common.Common;
import com.example.user.bogartsbentelogmobile.Model.Food;
import com.example.user.bogartsbentelogmobile.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class SignIn extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";


    private EditText edtEmail, edtPassword;
    private Button signIn;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText)findViewById(R.id.edtpassword);
        edtEmail = (MaterialEditText)findViewById(R.id.edtemail);
        signIn = (Button)findViewById(R.id.btnSignIn);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){


        }

        signIn.setOnClickListener(this);





        //FIREBASE
//        final FirebaseDatabase db = FirebaseDatabase.getInstance();
//        final DatabaseReference table_user = db.getReference("Users");



    }
    @Override
    public void onClick(View view){
        if (view == signIn){
            userLogIn();
        }
    }
    private void userLogIn(){

        final String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        final CollectionReference users = db.collection("Users");

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

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                        final String id = fUser.getUid();

                        db.collection("Users").whereEqualTo("id", id)
                                .limit(1).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            boolean isEmpty = task.getResult().isEmpty();

                                            if (isEmpty){
                                                User user = new User();
                                                Common.currUser = user;
                                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                                finish();
                                            }else{
                                                DocumentReference userRef = users.document(id);
                                                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            DocumentSnapshot snap = task.getResult();
                                                            if (snap.exists()){
                                                                User user = snap.toObject(User.class);
                                                                Common.currUser = user;

                                                                startActivity(new Intent(getApplicationContext(), Home.class));
                                                                finish();

                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                        }
                    }
                });
            }



}
