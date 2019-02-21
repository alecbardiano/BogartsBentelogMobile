package com.example.user.bogartsbentelogmobile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Category;

import org.w3c.dom.Text;

/**
 * Created by user on 2/5/2019.
 */

public class RecylerCategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView categoryName,categoryPriority;
    public ItemClickListener listener;
    public ImageView categoryImage;
    RecyclerCategoryAdapter adapter;

//    adapter.getSnapshots();



    public RecylerCategoryViewHolder(View itemView) {
        super(itemView);

        this.categoryName = itemView.findViewById(R.id.category_name);
        this.categoryImage = itemView.findViewById(R.id.category_image);



    }
}
