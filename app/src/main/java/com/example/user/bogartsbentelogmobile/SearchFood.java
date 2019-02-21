package com.example.user.bogartsbentelogmobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Food;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFood extends AppCompatActivity {

    RecyclerView recylerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerFoodAdapter adapter, searchAdapter;

    Client client = new Client("QESWFYOCCZ","43d7b481f17a76a27e2b286d0b8ab8e4");
    Index index = client.getIndex("foods");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_foodmenu);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setUpRecyclerView();
        loadFoodData();




    }

    private void setUpRecyclerView() {

        recylerView = findViewById(R.id.recycle_food_menu);
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadFoodData() {

//        Query query = db.collection("Food").whereEqualTo("CategID", categoryID);
        Query query = db.collection("Food").orderBy("Name");


        FirestoreRecyclerOptions<Food> options = new FirestoreRecyclerOptions.Builder<Food>()
                .setQuery(query,Food.class)
                .build();


        adapter = new RecyclerFoodAdapter(options);
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClickItemListener(DocumentSnapshot snapshot, int position) {
                String id = snapshot.getId();

                Intent foodDetail = new Intent(SearchFood.this,FoodDetail.class);
                foodDetail.putExtra("FoodID", id);
                Toast.makeText(SearchFood.this, "foodid"+ id, Toast.LENGTH_SHORT).show();
                startActivity(foodDetail);

            }
        });


        recylerView.setAdapter(adapter);
    }

//    public void updateList(@NonNull FirestoreRecyclerOptions<Food> options, String s )
//    {
//        Query query2 = db.collection("Food").orderBy("Name").startAt(s).endAt(s + "\uf8ff");
//        FirestoreRecyclerOptions<Food> options2 = options;
//        adapter = new RecyclerFoodAdapter(options2);
//
//        adapter.notifyDataSetChanged();
//    }

    private void searchFood(String s) {

        if (s != null && !s.isEmpty() && !s.equals("null")) {

            Query query2 = db.collection("Food").orderBy("Name").startAt(s).endAt(s + "\uf8ff");

//
            FirestoreRecyclerOptions<Food> options2 = new FirestoreRecyclerOptions.Builder<Food>()
                    .setQuery(query2,Food.class)
                    .build();

            Log.d("SEARCH", s + "CHECK NOW2");


            searchAdapter = new RecyclerFoodAdapter(options2);
            searchAdapter.setOnItemClickListener(new ItemClickListener() {
                @Override
                public void onClickItemListener(DocumentSnapshot snapshot, int position) {
                    String id = snapshot.getId();

                    Intent foodDetail = new Intent(SearchFood.this,FoodDetail.class);
                    foodDetail.putExtra("FoodID", id);
                    Toast.makeText(SearchFood.this, "foodid"+ id, Toast.LENGTH_SHORT).show();
                    startActivity(foodDetail);

                }

            });
            adapter.stopListening();
            recylerView.setAdapter(searchAdapter);
            searchAdapter.startListening();

        }else{
            searchAdapter.stopListening();
            loadFoodData();
            adapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.searchfoodmenu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchFood(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SEARCH", newText + "CHECK NOW");
                searchFood(newText);
                return false;

            }
        });
        return super.onCreateOptionsMenu(menu);
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

        Intent i = new Intent(SearchFood.this, Home.class);
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
