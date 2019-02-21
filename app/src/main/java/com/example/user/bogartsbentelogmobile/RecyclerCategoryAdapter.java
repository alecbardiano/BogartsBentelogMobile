package com.example.user.bogartsbentelogmobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Category;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 2/5/2019.
 */

public class RecyclerCategoryAdapter extends FirestoreRecyclerAdapter<Category,RecylerCategoryViewHolder> implements ItemClickListener {
    Home homeActivity;
    private ItemClickListener listener;
    public RecylerCategoryViewHolder holder;

    public RecyclerCategoryAdapter(@NonNull FirestoreRecyclerOptions<Category> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull com.example.user.bogartsbentelogmobile.RecylerCategoryViewHolder holder, final int position, @NonNull Category model) {
        holder.categoryName.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.categoryImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onItemClickListner.onClick(data);
                listener.onClickItemListener(getSnapshots().getSnapshot(position),position);
            }
        });
    }

    @NonNull
    @Override
    public com.example.user.bogartsbentelogmobile.RecylerCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_menu_item,parent,false);



        return new RecylerCategoryViewHolder(view);
    }


    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClickItemListener(DocumentSnapshot snapshot, int position) {

    }

//    @Override
//    public int getItemCount() {
//        return categoryArrayList.size();
//    }


}
