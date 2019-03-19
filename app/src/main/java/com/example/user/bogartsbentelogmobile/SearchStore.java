package com.example.user.bogartsbentelogmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Food;
import com.example.user.bogartsbentelogmobile.Model.Store;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Locale;

public class SearchStore extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googApi;
    private LocationRequest locationRequest;
    private Location lastLoc;
    private Marker currentUserMark;
    RecyclerView recylerView;
    RecyclerStoreAdapter adapter,searchAdapter;


    private static final int Request_User_Location_Code = 99;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference storesRef = db.collection("Stores");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_store);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_searchstore);
        setSupportActionBar(myToolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpRecyclerView();
        loadStoreData();
    }
    private void setUpRecyclerView(){
        recylerView = findViewById(R.id.recycle_store_menu);
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void loadStoreData() {

//        Query query = db.collection("Food").whereEqualTo("CategID", categoryID);
        Query query = db.collection("Stores").orderBy("nameOfBranch");


        FirestoreRecyclerOptions<Store> options = new FirestoreRecyclerOptions.Builder<Store>()
                .setQuery(query,Store.class)
                .build();


        adapter = new RecyclerStoreAdapter(options);
//        adapter.setOnItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClickItemListener(DocumentSnapshot snapshot, int position) {
//                String id = snapshot.getId();
//
//                Intent foodDetail = new Intent(SearchFood.this,FoodDetail.class);
//                foodDetail.putExtra("FoodID", id);
//                Toast.makeText(SearchFood.this, "foodid"+ id, Toast.LENGTH_SHORT).show();
//                startActivity(foodDetail);
//
//            }
//        });


        recylerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.searchstoremenu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search_store);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStore(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SEARCH", newText + "CHECK NOW");
                searchStore(newText);
                return false;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchStore(String query) {

        if (query != null && !query.isEmpty() && !query.equals("null")) {

            Query query2 = db.collection("Stores").orderBy("nameOfBranch").startAt(query).endAt(query + "\uf8ff");

//
            FirestoreRecyclerOptions<Store> options2 = new FirestoreRecyclerOptions.Builder<Store>()
                    .setQuery(query2,Store.class)
                    .build();

            Log.d("SEARCH", query + "CHECK NOW2");


            searchAdapter = new RecyclerStoreAdapter(options2);
//            searchAdapter.setOnItemClickListener(new ItemClickListener() {
//                @Override
//                public void onClickItemListener(DocumentSnapshot snapshot, int position) {
//                    String id = snapshot.getId();
//
//                    Intent foodDetail = new Intent(SearchStore.this,FoodDetail.class);
//                    foodDetail.putExtra("FoodID", id);
//                    Toast.makeText(SearchStore.this, "foodid"+ id, Toast.LENGTH_SHORT).show();
//                    startActivity(foodDetail);
//
//                }
//
//            });
            adapter.stopListening();
            recylerView.setAdapter(searchAdapter);
            searchAdapter.startListening();

        }else{
            searchAdapter.stopListening();
            loadStoreData();
            adapter.startListening();
        }
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

        Intent i = new Intent(SearchStore.this, Home.class);
        startActivity(i);

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
        LatLng manila = new LatLng(14.5995,120.9842);
        mMap.addMarker(new MarkerOptions().position(manila).title("Marker in Manila"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(manila));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );

        db.collection("Stores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.exists()){
                                    Log.d("SearchStore", document.getId() + " => " + document.getData());
                                    GeoPoint geo = document.getGeoPoint("location");
                                    String name = document.getString("nameOfBranch");
                                    double lat = geo.getLatitude();
                                    double lng = geo.getLongitude();
                                    LatLng latLng = new LatLng(lat, lng);
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(name));
//                                    mMap.addCircle(new CircleOptions().center(latLng).radius(3000)
//                                            .strokeColor(Color.BLUE)
//                                            .fillColor(0x220000)
//                                            .strokeWidth(5.0f));
                                }

                            }
                        } else {
                            Log.d("SearchStore", "Error getting documents: ", task.getException());
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