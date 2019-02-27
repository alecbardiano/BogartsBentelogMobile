package com.example.user.bogartsbentelogmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
//import android.widget.TextView;

import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.example.user.bogartsbentelogmobile.Common.Common;
import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Category;
import com.example.user.bogartsbentelogmobile.Model.Food;
import com.example.user.bogartsbentelogmobile.Model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //    public TextView txtFirst;
    TextView txt_first;
    private RecyclerCategoryAdapter adapter;
    private RecyclerFoodAdapter searchAdapter;
    RecyclerView recylerView;
    ArrayList<Category> categArrayList;
    final CollectionReference categRef = db.collection("Category");

    Client client = new Client("QESWFYOCCZ","43d7b481f17a76a27e2b286d0b8ab8e4");
    Index index = client.getIndex("foods");

    private List<Food> foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        categArrayList = new ArrayList<Category>();
        setUpRecyclerView();
        loadCategoryData();

        loadUser(navigationView);

        Toast.makeText(this, Common.currUser.getFirstName(), Toast.LENGTH_SHORT).show();

    }

    private void setUpRecyclerView() {
        recylerView = findViewById(R.id.recycle_menu);
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void loadUser(NavigationView navigationView) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        final String id = fUser.getUid();

        final CollectionReference users = db.collection("Users");

        db.collection("Users").whereEqualTo("id", id)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (isEmpty) {
                                User user = new User();
                                Common.currUser = user;
                            } else {
                                DocumentReference userRef = users.document(id);
                                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot snap = task.getResult();
                                            if (snap.exists()) {
                                                User user = snap.toObject(User.class);
                                                Common.currUser = user;
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

        View headerView = navigationView.getHeaderView(0);
        txt_first = (TextView) headerView.findViewById(R.id.txt_first_name_nav);
        txt_first.setText(Common.currUser.getFirstName());
    }

//    private void addCategorySample () {
//        Random random = new Random();
//
//        for ( int i =0; i<5; i++ ){
//            Map<String,String > dataMap = new HashMap<>();
//            dataMap.put("Name", "try categ name" + random.nextInt());
//            dataMap.put("Priority", "try categ priority" + random.nextInt());
//
//            db.collection("Category")
//                    .add(dataMap)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Toast.makeText(Home.this,"added data",Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//
//
//    }

//    private void loadFoods() {
//
//        db.collection("Food")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Food food = document.toObject(Food.class);
//                                foods.add(food);
//                            }
//                        } else {
////                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//    }

    private void loadCategoryData() {

        Query query = categRef.orderBy("Priority", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .build();


        adapter = new RecyclerCategoryAdapter(options, this);
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClickItemListener(DocumentSnapshot snapshot, int position) {
                String id = snapshot.getId();

                Intent foodMenu = new Intent(Home.this, FoodMenu.class);
                foodMenu.putExtra("CategID", id);
                Toast.makeText(Home.this, "categid" + id, Toast.LENGTH_SHORT).show();
                startActivity(foodMenu);

            }
        });
        recylerView.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(Home.this, ProfileActivity.class));

        } else if (id == R.id.nav_search_food) {
            startActivity(new Intent(Home.this, SearchFood.class));
        }
        else if( id == R.id.nav_my_orders){
            startActivity(new Intent(Home.this, OrderActivity.class));
        }
        else if (id == R.id.nav_search_store) {
//            startActivity(new Intent(Home.this, SearchFood.class));
        }
        else if (id == R.id.nav_contact) {
//            startActivity(new Intent(Home.this, SearchFood.class));
            startActivity(new Intent(Home.this, FranchiseActivity.class));
        }
        else if (id == R.id.nav_log_out) {
            Intent signIn = new Intent(Home.this, MainActivity.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(signIn);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        String input = s.toLowerCase();
        Query query = db.collection("Food").orderBy("Name").startAt(input).endAt(input + "\uf8ff");



        FirestoreRecyclerOptions<Food> options = new FirestoreRecyclerOptions.Builder<Food>()
                .setQuery(query,Food.class)
                .build();

        searchAdapter = new RecyclerFoodAdapter(options,this);
        searchAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClickItemListener(DocumentSnapshot snapshot, int position) {
                String id = snapshot.getId();

                Intent foodDetail = new Intent(Home.this,FoodDetail.class);
                foodDetail.putExtra("FoodID", id);
                Toast.makeText(Home.this, "foodid"+ id, Toast.LENGTH_SHORT).show();
                startActivity(foodDetail);

            }
        });
//        searchAdapter.updateList();
        recylerView.setAdapter(searchAdapter);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

//        loadFoods();

//        com.algolia.search.saas.Query queryAlg = new com.algolia.search.saas.Query(newText.toLowerCase())
//                .setAttributesToRetrieve("Name")
//                .setHitsPerPage(50);
//        index.searchAsync(queryAlg, new CompletionHandler() {
//            @Override
//            public void requestCompleted(JSONObject content, AlgoliaException error) {
//
//            }
//        });

        String input = newText.toLowerCase();
        Query query = db.collection("Food").orderBy("Name").startAt(input).endAt(input + "\uf8ff");



        FirestoreRecyclerOptions<Food> options = new FirestoreRecyclerOptions.Builder<Food>()
                .setQuery(query,Food.class)
                .build();

        searchAdapter = new RecyclerFoodAdapter(options,this);
        searchAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClickItemListener(DocumentSnapshot snapshot, int position) {
                String id = snapshot.getId();

                Intent foodDetail = new Intent(Home.this,FoodDetail.class);
                foodDetail.putExtra("FoodID", id);
                Toast.makeText(Home.this, "foodid"+ id, Toast.LENGTH_SHORT).show();
                startActivity(foodDetail);

            }
        });
//        searchAdapter.updateList();
        recylerView.setAdapter(searchAdapter);
        return false;
    }



}

