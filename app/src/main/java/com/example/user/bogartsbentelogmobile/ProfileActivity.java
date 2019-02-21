package com.example.user.bogartsbentelogmobile;

import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.bogartsbentelogmobile.Common.Common;
import com.example.user.bogartsbentelogmobile.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private EditText editLastName, editFirstName, editMiddleInitial, editContactNumber, editAddress;
    public final String TAG = "ProfileActivity";
    private Button saveInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,SignIn.class));
        }


        editLastName = (EditText) findViewById(R.id.lastName);
        editFirstName = (EditText) findViewById(R.id.firstName);
        editMiddleInitial = (EditText) findViewById(R.id.middleInit);
        editContactNumber = (EditText) findViewById(R.id.contactNumber);
        PhoneNumberUtils.formatNumber(editContactNumber.getText().toString());
        editAddress = (EditText) findViewById(R.id.address);
        saveInfo = (Button) findViewById(R.id.save);
//        myString != null && !myString.isEmpty()
        if (Common.currUser.getFirstName() != null  ){
            editLastName.setText(Common.currUser.getLastName());
            editFirstName.setText(Common.currUser.getFirstName());
            editMiddleInitial.setText(Common.currUser.getMiddleInit());
            editContactNumber.setText(Common.currUser.getContactNumber());
            editAddress.setText(Common.currUser.getAddress());
        }else{
            editLastName.setText("");
            editFirstName.setText("");
            editMiddleInitial.setText("");
            editContactNumber.setText("");
            editAddress.setText("");
        }

        saveInfo.setOnClickListener(this);



        Toast.makeText(this, Common.currUser.getFirstName(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, Common.currUser.getID(), Toast.LENGTH_SHORT).show();


    }

    private void saveUserInformation (){
        String lastName = editLastName.getText().toString().trim();
        String firstName = editFirstName.getText().toString().trim();
        String middleInit = editMiddleInitial.getText().toString().trim();
        String contactNumber = editContactNumber.getText().toString().trim();
        String Address = editAddress.getText().toString().trim();



        Map<String, Object> user = new HashMap<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = fUser.getEmail();
        String id = fUser.getUid();

        CollectionReference users = db.collection("Users");

        user.put("lastName", lastName );
        user.put("firstName", firstName);
        user.put("middleInit", middleInit);
        user.put("contactNumber", contactNumber);
        user.put("address", Address);
        user.put("email", email);
        user.put("id", id);
        users.document(id).set(user);



//        db.collection("Users")
//        .add(user)
//        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//            }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Error adding document", e);
//            }
//        });
//




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

           case android.R.id.home:
               onBackPressed();
              return true;
       }

        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed(){

        Intent i = new Intent(ProfileActivity.this, Home.class);
        startActivity(i);

    }
    @Override
    public void onClick(View view){
        if (view == saveInfo){
            saveUserInformation ();
            Intent i = new Intent(ProfileActivity.this, Home.class);
            startActivity(i);
        }
    }
}
