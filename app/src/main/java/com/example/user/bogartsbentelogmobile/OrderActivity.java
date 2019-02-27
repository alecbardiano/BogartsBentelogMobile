package com.example.user.bogartsbentelogmobile;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.bogartsbentelogmobile.Model.Order;
import com.example.user.bogartsbentelogmobile.Model.Request;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.user.bogartsbentelogmobile.Common.Common.currUser;

public class OrderActivity extends AppCompatActivity {
    RecyclerView recylerView;
    RecyclerOrderAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setUpRecyclerView();

        loadRequests();
    }

    private void setUpRecyclerView(){
        recylerView = findViewById(R.id.recycle_order);
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private void loadRequests(){

        final CollectionReference reqRef = db.collection("Requests");

        Query query = reqRef.whereEqualTo("userID", currUser.getID()).orderBy("dateOfOrder",Query.Direction.DESCENDING).limit(10);
        FirestoreRecyclerOptions<Request> options = new FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(query,Request.class)
                .build();

        adapter = new RecyclerOrderAdapter(options,this);
        recylerView.setAdapter(adapter);

        reqRef.whereEqualTo("userID", currUser.getID()).orderBy("dateOfOrder",Query.Direction.DESCENDING).limit(10)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
            }
        });
    }

    protected void onStart (){
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop (){
        super.onStop();
        adapter.stopListening();

    }
}
