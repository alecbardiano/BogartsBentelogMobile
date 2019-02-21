package com.example.user.bogartsbentelogmobile;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Food;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/8/2019.
 */

public class RecyclerFoodAdapter extends FirestoreRecyclerAdapter<Food,RecyclerFoodViewHolder> implements ItemClickListener {

    FoodMenu foodActivity;
    private ItemClickListener listener;
    private List<Food> foods;

//    public RecyclerCategoryAdapter(@NonNull FirestoreRecyclerOptions<Category> options) {
//        super(options);
//
//    }

    public RecyclerFoodAdapter(@NonNull FirestoreRecyclerOptions<Food> options) {

        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull RecyclerFoodViewHolder holder, final int position, @NonNull Food model) {
        holder.foodName.setText(model.getName());
//        holder.foodDesc.setText(model.getName());
//        holder.foodPrice.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.foodImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onItemClickListner.onClick(data);
                listener.onClickItemListener(getSnapshots().getSnapshot(position),position);
            }
        });
//        Picasso.get().load(model.getImage()).into(holder.categoryImage);
    }

    @NonNull
    @Override
    public RecyclerFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_food_item,parent,false);

        return new RecyclerFoodViewHolder(view);
    }

    public void deleteFood(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @Override
    public void onClickItemListener(DocumentSnapshot snapshot, int position) {

    }
    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

//    public void updateList(List<Food> newList){
//
//        foods = new ArrayList<>();
//        foods.addAll(newList);
//        notifyDataSetChanged();
//    }
}
