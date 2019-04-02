package com.example.user.bogartsbentelogmobile;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.bogartsbentelogmobile.Model.Food;
import com.example.user.bogartsbentelogmobile.Model.Store;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.Locale;

public class StoreDetail extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    TextView store_Name, store_Address, store_ContactNo;
    private GoogleMap mMap;
    private String storeID;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        store_Name = (TextView)findViewById(R.id.store_detail_nameBranch);
        store_Address = (TextView)findViewById(R.id.store_detail_address);
        store_ContactNo = (TextView)findViewById(R.id.store_detail_contactNumber);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_store_detail);
        setSupportActionBar(myToolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(getIntent() != null){
            storeID = getIntent().getStringExtra("StoreID");
            Toast.makeText(StoreDetail.this,"id " +storeID,Toast.LENGTH_SHORT).show();
        }

        getStoreDetail(storeID);
    }

    private void getStoreDetail (String storeID) {

        CollectionReference storeCollection = db.collection("Stores");
        DocumentReference docRef = storeCollection.document(storeID);


        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Store store = documentSnapshot.toObject(Store.class);
//                currFood = documentSnapshot.toObject(Food.class);
//               Picasso.get().load(currFood.getImage()).into(food_Image);
//                GlideApp.with(FoodDetail.this)
//                        .load(currFood.getImage())
//                        .centerCrop()
//                        .fitCenter()
//                        .skipMemoryCache(false)  //No memory cache
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)   //No disk cache
//                        .into(food_Image);
                Locale locale = new Locale("tl", "PH");
                NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                store_Name.setText(store.getNameOfBranch());
                store_Address.setText(store.getAddress());
                store_ContactNo.setText(store.getContactNo());



                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle("Store Locator");
                }

            }
        });



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DocumentReference docRef = db.collection("Stores").document(storeID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Store store = document.toObject(Store.class);
                        GeoPoint geo = document.getGeoPoint("location");
                        double lat = geo.getLatitude();
                        double lng = geo.getLongitude();
                        LatLng latLng = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in" + store.getNameOfBranch()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera( CameraUpdateFactory.zoomTo( 20.0f ) );
                        Log.d("StoreDetail" ,"DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("StoreDetail", "No such document");
                    }
                } else {
                    Log.d("StoreDetail", "get failed with ", task.getException());
                }
            }
        });
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

        Intent i = new Intent(StoreDetail.this, SearchStore.class);
        startActivity(i);

    }
}
