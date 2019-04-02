package com.example.user.bogartsbentelogmobile;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Order;
import com.example.user.bogartsbentelogmobile.Model.Request;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_order);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Order History");
        }


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

        adapter = new RecyclerOrderAdapter(options,this);
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClickItemListener(DocumentSnapshot snapshot, int position) {
                String id = snapshot.getId();

                Intent orderRequest = new Intent(OrderActivity.this,OrderRequest.class);
                orderRequest.putExtra("orderID", id);
                Toast.makeText(OrderActivity.this, "foodid"+ id, Toast.LENGTH_SHORT).show();
                startActivity(orderRequest);

            }
        });
        recylerView.setAdapter(adapter);


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

        Intent i = new Intent(OrderActivity.this, Home.class);
        startActivity(i);

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
