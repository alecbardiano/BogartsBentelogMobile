package com.example.user.bogartsbentelogmobile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;


/**
 * Created by user on 2/8/2019.
 */

public class RecyclerFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView foodName,foodDesc,foodPrice;
    public ImageView foodImage;
//    public ImageView categoryImage;
    public RecyclerFoodViewHolder(View itemView) {
        super(itemView);

        this.foodName = itemView.findViewById(R.id.food_name);
//        this.foodDesc = itemView.findViewById(R.id.food_desc);
//        this.foodPrice = itemView.findViewById(R.id.food_prce);
        this.foodImage = itemView.findViewById(R.id.food_image);

    }

    @Override
    public void onClick(View v) {

    }

}
