package com.example.user.bogartsbentelogmobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Category;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.util.ArrayList;

import static java.lang.System.load;

/**
 * Created by user on 2/5/2019.
 */

public class RecyclerCategoryAdapter extends FirestoreRecyclerAdapter<Category,RecylerCategoryViewHolder> implements ItemClickListener {
    Home homeActivity;
    private ItemClickListener listener;
    public RecylerCategoryViewHolder holder;
    private Context context;

    public RecyclerCategoryAdapter(@NonNull FirestoreRecyclerOptions<Category> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull com.example.user.bogartsbentelogmobile.RecylerCategoryViewHolder holder, final int position, @NonNull Category model) {
        holder.categoryName.setText(model.getName());
//        Picasso.get().load(model.getImage()).into(holder.categoryImage);
        GlideApp.with(this.context)
                .load(model.getImage())
                .centerCrop()
                .fitCenter()
                .skipMemoryCache(false)  //No memory cache
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //No disk cache
                .into(holder.categoryImage);

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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
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
