package com.example.user.bogartsbentelogmobile;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bogartsbentelogmobile.Model.Order;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

public class OrderRequest extends AppCompatActivity {

    RecyclerOrderRequestAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recylerView;
    String orderID = "";
    int totalPriceOfFoods;

    TextView totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_order_request);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Orders");
        }
        if(getIntent() != null){
            orderID = getIntent().getStringExtra("orderID");
            Toast.makeText(OrderRequest.this,"id " +orderID,Toast.LENGTH_SHORT).show();
        }

        totalPrice = (TextView) findViewById(R.id.totalPriceCart);

        setUpRecyclerView();
        loadCartFoods();
    }

    private void setUpRecyclerView(){
        recylerView = findViewById(R.id.recycle_order_request);
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void loadCartFoods() {
        Query query = db.collection("Requests").document(orderID).collection("Foods");
        db.collection("Requests").document(orderID).collection("Foods")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        totalPriceOfFoods = 0;
                        if (e != null) {
                            return;
                        }
                        List<String> food = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {

                            Order order = doc.toObject(Order.class);
                            if (Integer.parseInt(order.getQuantity()) == 0){
                                continue;
                            }
                            totalPriceOfFoods += (Integer.parseInt(order.getPrice()) * (Integer.parseInt(order.getQuantity())));
                        }

                        Locale locale = new Locale("tl", "PH");
                        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                        totalPrice.setText(format.format(totalPriceOfFoods));
                    }
                });
        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query,Order.class)
                .build();

        adapter = new RecyclerOrderRequestAdapter(options,this);
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

        Intent i = new Intent(OrderRequest.this, OrderActivity.class);
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
