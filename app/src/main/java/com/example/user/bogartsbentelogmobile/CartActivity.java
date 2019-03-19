package com.example.user.bogartsbentelogmobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bogartsbentelogmobile.Common.Common;
import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Category;
import com.example.user.bogartsbentelogmobile.Model.Food;
import com.example.user.bogartsbentelogmobile.Model.Order;
import com.example.user.bogartsbentelogmobile.Model.Store;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.google.firebase.firestore.GeoPoint;
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

public class CartActivity extends AppCompatActivity{
    RecyclerView recylerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final DocumentReference ref = db.collection("Users").document(currUser.getID());
    int totalPriceOfFoods;
    final ArrayList<Order> orderList = new ArrayList<>();

    private GoogleMap mMap;
    Location currLocation = new Location("YOU");
    TextView txtLat;

    TextView totalPrice;
    FButton buttonPlaceOrder;
    AlertDialog.Builder alertConfirm, alertConfirmAddress, alertConfirm2, alertConfirmDeleteFoodInCart;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST__LOCATION = 1;
    private static final double MAXIMUM_DISTANCE = 3000.00;
    private static int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private StringBuilder stringBuilder;


    private boolean isContinue = false;
    private boolean isGPS = false;

    GPSTracker gpsTracker;
    Button b;
    Context context = this;
    private static ListenerRegistration listener;

//    List<Order> cart = new ArrayList<>();

    RecyclerCartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_cart);
//        setSupportActionBar(myToolbar);
//
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setTitle("My Cart");
//        }

        totalPrice = (TextView) findViewById(R.id.totalPriceCart);
        buttonPlaceOrder = (FButton) findViewById(R.id.buttonPlaceOrder);
        txtLat = (TextView)findViewById(R.id.textview1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GPSTracker(this).turnGPSOn(new GPSTracker.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        if (!isContinue) {
                            txtLat.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        } else {
                            stringBuilder.append(wayLatitude);
                            stringBuilder.append("-");
                            stringBuilder.append(wayLongitude);
                            stringBuilder.append("\n\n");
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        if (!isGPS) {
            Toast.makeText(CartActivity.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        isContinue = false;
        getLocation();

        setUpRecyclerView();
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
                        alertConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (totalPriceOfFoods <= 100) {
                                    Toast.makeText(CartActivity.this, "Total price must be greater than or equal to 100", Toast.LENGTH_SHORT).show();
                                } else {
                                    addCartToRequest(totalPriceOfFoods, editAddress.getText().toString());
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

                        alertConfirm2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (totalPriceOfFoods <= 100) {
                                    Toast.makeText(CartActivity.this, "Total price must be greater than or equal to 100", Toast.LENGTH_SHORT).show();
                                } else {
                                    addCartToRequest(totalPriceOfFoods);
                                }
                            }
                        });
                        alertConfirm2.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(CartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CartActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CartActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Common.LOCATION_REQUEST);

        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            currLocation.setLatitude(location.getLatitude());
                            currLocation.setLatitude(location.getLongitude());
                            txtLat.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        } else {
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    }
                });
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    wayLatitude = location.getLatitude();
                                    wayLongitude = location.getLongitude();
                                    txtLat.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                                } else {
                                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Common.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }



    private void setUpRecyclerView(){
        recylerView = findViewById(R.id.recycle_cart);
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void addCartToRequest(final int total){

//        Query query = db.collection("Users").document(currUser.getID()).collection("Cart");
        final Map<String, Object> requests = new HashMap<>();

        requests.put("Address", currUser.getAddress());
        requests.put("Name", currUser.getLastName() + " " + currUser.getFirstName() + " " + currUser.getMiddleInit());
        requests.put("Phone", currUser.getContactNumber());
        requests.put("status", "0");
        requests.put("total", Integer.toString(total));
        requests.put("dateOfOrder",FieldValue.serverTimestamp());
        requests.put("userID",currUser.getID());

        db.collection("Stores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {

                           float minDistance = 0;
                           String nameOfBranch = "";
                           int ite =0;
                           for (QueryDocumentSnapshot document : task.getResult()) {

                               if(document.exists()){

                                   Store store = document.toObject(Store.class);
                                   float[] result = new float[1];
                                   Log.d("SearchStore", document.getId() + " => " + document.getData());
                                   GeoPoint geo = document.getGeoPoint("location");
                                   double lat = geo.getLatitude();
                                   double lng = geo.getLongitude();
                                   Location.distanceBetween(lat,lng,wayLatitude,wayLongitude,result);
                                   Log.d("minDistance",  " Minimum Distance " + minDistance);
                                   if (ite == 0){
                                       minDistance = result[0];
                                       nameOfBranch = store.getStoreID();
                                       ite +=1;
                                       continue;
                                   }
                                   if ( minDistance > result[0]){
                                       minDistance = result[0];
                                       nameOfBranch = store.getStoreID();
                                   }else{
                                       minDistance = minDistance;
                                   }
                               }
                           }
                           Log.d("nameOfBranch",  " Minimum Distance " + nameOfBranch);
                           Log.d("minDistance",  " Last Minimum Distance " + minDistance);
                           if(minDistance >= 3000.00){
                               Toast.makeText(CartActivity.this,"Sorry you are to far from our stores",Toast.LENGTH_SHORT).show();
                           }else{
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
                               final String finalNameOfBranch = nameOfBranch;
                               db.collection("Requests")
                                       .add(requests)
                                       .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                           @Override
                                           public void onSuccess(DocumentReference documentReference) {
                                               Toast.makeText(CartActivity.this,"added requests data",Toast.LENGTH_SHORT).show();
                                               String myId = documentReference.getId();
//                        Map<String, Object> addIDdata = new HashMap<>();
//                        addIDdata.put("orderID", myI);
                                               db.collection("Requests").document(myId)
                                                       .update(
                                                               "orderID", myId,
                                                               "storeID", finalNameOfBranch
                                                       );

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
                           }
                       }else {
                           Log.d("SearchStore", "Error getting documents: ", task.getException());
                       }
                   }
               });




        orderList.clear();


    }
    private void addCartToRequest(final int total, String address){

//        Query query = db.collection("Users").document(currUser.getID()).collection("Cart");

        final Map<String, Object> requests = new HashMap<>();

        requests.put("Address", address);
        requests.put("Name", currUser.getLastName() + " " + currUser.getFirstName() + " " + currUser.getMiddleInit());
        requests.put("Phone", currUser.getContactNumber());
        requests.put("status", "0");
        requests.put("total", Integer.toString(total));
        requests.put("dateOfOrder",FieldValue.serverTimestamp());
        requests.put("userID",currUser.getID());
//        requests.put("orderID"," ");

        db.collection("Stores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            float[] result = new float[1];
                            float minDistance = 0;
                            String nameOfBranch ="";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.exists()){
                                    Log.d("SearchStore", document.getId() + " => " + document.getData());
                                    GeoPoint geo = document.getGeoPoint("location");
                                    String name = document.getString("storeID");
                                    double lat = geo.getLatitude();
                                    double lng = geo.getLongitude();
                                    Location.distanceBetween(lat,lng,currLocation.getLatitude(),currLocation.getLongitude(),result);
                                    if ( minDistance < result[0]){
                                        minDistance = result[0];
                                        nameOfBranch = name;
                                    }
                                }
                            }
                            if(minDistance >= 3000){        // 3 kilometers
                                Toast.makeText(CartActivity.this,"Sorry you are to far from our stores",Toast.LENGTH_SHORT).show();
                            }else{
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
                                final String finalNameOfBranch = nameOfBranch;
                                db.collection("Requests")
                                        .add(requests)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(CartActivity.this,"added requests data",Toast.LENGTH_SHORT).show();
                                                String myId = documentReference.getId();
                                                db.collection("Requests").document(myId)
                                                        .update(
                                                                "orderID", myId,
                                                                "storeID", finalNameOfBranch
                                                        );
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

                            }
                        } else {
                            Log.d("SearchStore", "Error getting documents: ", task.getException());
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

        adapter = new RecyclerCartAdapter(options,this);
        recylerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                alertConfirmDeleteFoodInCart = new AlertDialog.Builder(CartActivity.this);
                alertConfirmDeleteFoodInCart.setTitle("Confirm Action");
                alertConfirmDeleteFoodInCart.setMessage("Are you sure you want to remove this to your cart?");

                alertConfirmDeleteFoodInCart.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.deleteFoodInCart(viewHolder.getAdapterPosition());
                    }
                });
                alertConfirmDeleteFoodInCart.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertConfirmDeleteFoodInCart.show();

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

        Intent i = new Intent(CartActivity.this, Home.class);
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
