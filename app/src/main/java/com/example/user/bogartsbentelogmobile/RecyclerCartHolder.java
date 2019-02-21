package com.example.user.bogartsbentelogmobile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2/13/2019.
 */

public class RecyclerCartHolder extends RecyclerView.ViewHolder {

    TextView cartFoodName, cartFoodPrice, cartPricePerPiece;
    ImageView cartFoodCount;

    public RecyclerCartHolder(View itemView) {
        super(itemView);
        cartFoodName = (TextView)itemView.findViewById(R.id.cart_item_name);
        cartFoodPrice = (TextView)itemView.findViewById(R.id.cart_item_price);
        cartFoodCount = (ImageView)itemView.findViewById(R.id.cart_item_quantity);
        cartPricePerPiece = (TextView)itemView.findViewById(R.id.cart_item_price_per_pc);
    }
}
