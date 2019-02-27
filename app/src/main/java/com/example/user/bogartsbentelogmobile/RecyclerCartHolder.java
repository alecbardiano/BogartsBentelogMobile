package com.example.user.bogartsbentelogmobile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2/13/2019.
 */

public class RecyclerCartHolder extends RecyclerView.ViewHolder {

    TextView cartFoodName, cartFoodPrice, cartPricePerPiece, cartFoodCount;
    Button addButton, subButton;

    public RecyclerCartHolder(View itemView) {
        super(itemView);
        cartFoodName = (TextView)itemView.findViewById(R.id.cart_item_name);
        cartFoodPrice = (TextView)itemView.findViewById(R.id.cart_item_price);
        cartFoodCount = (TextView)itemView.findViewById(R.id.cart_item_quantity);
        cartPricePerPiece = (TextView)itemView.findViewById(R.id.cart_item_price_per_pc);
        addButton = (Button)itemView.findViewById(R.id.add_cart_button);
        subButton = (Button)itemView.findViewById(R.id.subtract_cart_button);
    }
}
