package com.example.user.bogartsbentelogmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Category;
import com.example.user.bogartsbentelogmobile.Model.Food;
import com.example.user.bogartsbentelogmobile.Model.Order;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import info.hoang8f.widget.FButton;

import static com.example.user.bogartsbentelogmobile.Common.Common.currUser;

public class CartActivity extends AppCompatActivity {
    RecyclerView recylerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final DocumentReference ref = db.collection("Users").document(currUser.getID());
    int totalPriceOfFoods;
    final ArrayList <Order> orderList = new ArrayList<>();

    TextView totalPrice;
    FButton buttonPlaceOrder;
    AlertDialog.Builder alertConfirm, alertConfirmAddress, alertConfirm2;

    private static ListenerRegistration listener;

//    List<Order> cart = new ArrayList<>();

    RecyclerCartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setUpRecyclerView();

        totalPrice = (TextView)findViewById(R.id.totalPriceCart);
        buttonPlaceOrder = (FButton)findViewById(R.id.buttonPlaceOrder);
        loadCartFoods();



        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertConfirmAddress = new AlertDialog.Builder(CartActivity.this);
                alertConfirmAddress.setTitle("Please Confirm Address");
                alertConfirmAddress.setMessage("Do you want to deliver to new address?");


                alertConfirmAddress.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertConfirm = new AlertDialog.Builder(CartActivity.this);
                        alertConfirm.setTitle("Please Confirm");
                        alertConfirm.setMessage("The order will be placed once you click the Yes Button After you typed the new Address");
                        final EditText editAddress = new EditText(CartActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        editAddress.setLayoutParams(lp);
                        alertConfirm.setView(editAddress);
                        alertConfirm.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (totalPriceOfFoods <= 100){
                                    Toast.makeText(CartActivity.this,"Total price must be greater than or equal to 100",Toast.LENGTH_SHORT).show();
                                }else{
                                    addCartToRequest(totalPriceOfFoods,editAddress.getText().toString());
                                }
                            }
                        });
                        alertConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertConfirm.show();
                    }
                });


                alertConfirmAddress.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertConfirm2 = new AlertDialog.Builder(CartActivity.this);
                        alertConfirm2.setTitle("Please Confirm");
                        alertConfirm2.setMessage("The order will be placed once you click the Yes Button");

                        alertConfirm2.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                if (totalPriceOfFoods <= 100){
                                    Toast.makeText(CartActivity.this,"Total price must be greater than or equal to 100",Toast.LENGTH_SHORT).show();
                                }else{
                                    addCartToRequest(totalPriceOfFoods);
                                }
                            }
                        });
                        alertConfirm2.setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertConfirm2.show();
                    }
                });

                alertConfirmAddress.show();
            }

        });



    }

    private void setUpRecyclerView(){
        recylerView = findViewById(R.id.recycle_cart);
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void addCartToRequest(final int total){

//        Query query = db.collection("Users").document(currUser.getID()).collection("Cart");
        Map<String, Object> requests = new HashMap<>();

        requests.put("Address", currUser.getAddress());
        requests.put("Name", currUser.getLastName() + " " + currUser.getFirstName() + " " + currUser.getMiddleInit());
        requests.put("Phone", currUser.getContactNumber());
        requests.put("status", "0");
        requests.put("total", Integer.toString(total));
        requests.put("dateOfOrder",FieldValue.serverTimestamp());
        requests.put("userID",currUser.getID());

        db.collection("Users").document(currUser.getID()).collection("Cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Order order = document.toObject(Order.class);

                                orderList.add(order);
                            }
                        } else {
                            Log.d("REQUESTS", "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("Requests")
                .add(requests)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CartActivity.this,"added requests data",Toast.LENGTH_SHORT).show();
                        String myId = documentReference.getId();
                        for(int i =0; i< orderList.size(); i++){
                            Map<String, Object> foodmap = new HashMap<>();
                            foodmap.put("Name", orderList.get(i).getProductName());
                            foodmap.put("Price", orderList.get(i).getPrice());
                            foodmap.put("Quantity", orderList.get(i).getQuantity());
                            db.collection("Requests").document(myId).collection("Foods")
                                .add(foodmap)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(CartActivity.this,"added food to requests data",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }

                    }

                });

        orderList.clear();


    }
    private void addCartToRequest(final int total, String address){

//        Query query = db.collection("Users").document(currUser.getID()).collection("Cart");

        Map<String, Object> requests = new HashMap<>();

        requests.put("Address", address);
        requests.put("Name", currUser.getLastName() + " " + currUser.getFirstName() + " " + currUser.getMiddleInit());
        requests.put("Phone", currUser.getContactNumber());
        requests.put("status", "0");
        requests.put("total", Integer.toString(total));

        db.collection("Users").document(currUser.getID()).collection("Cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Order order = document.toObject(Order.class);
                                orderList.add(order);
                            }
                        } else {
                            Log.d("REQUESTS", "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("Requests")
                .add(requests)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CartActivity.this,"added requests data",Toast.LENGTH_SHORT).show();
                        String myId = documentReference.getId();
                        for(int i =0; i< orderList.size(); i++){
                            Map<String, Object> foodmap = new HashMap<>();
                            foodmap.put("Name", orderList.get(i).getProductName());
                            foodmap.put("Price", orderList.get(i).getPrice());
                            foodmap.put("Quantity", orderList.get(i).getQuantity());
                            db.collection("Requests").document(myId).collection("Foods")
                                    .add(foodmap)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(CartActivity.this,"added food to requests data",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }

                });

        orderList.clear();


    }


    private void loadCartFoods(){
//        Query query = categRef.orderBy("Priority", Query.Direction.ASCENDING);
        Query query = db.collection("Users").document(currUser.getID()).collection("Cart").orderBy("latestUpdateTimestamp", Query.Direction.ASCENDING);
        db.collection("Users").document(currUser.getID()).collection("Cart").orderBy("latestUpdateTimestamp")
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

        adapter = new RecyclerCartAdapter(options,this);
        recylerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteFoodInCart(viewHolder.getAdapterPosition());
                db.collection("Users").document(currUser.getID()).collection("Cart")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                totalPriceOfFoods = 0;
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Order order = document.toObject(Order.class);
                                        totalPriceOfFoods += (Integer.parseInt(order.getPrice()) * (Integer.parseInt(order.getQuantity())));
//                                Order order = document.getData();
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                    }
                                } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                                Locale locale = new Locale("tl", "PH");
                                NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                                totalPrice.setText(format.format(totalPriceOfFoods));
                            }
                        });
            }
        }).attachToRecyclerView(recylerView);

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
