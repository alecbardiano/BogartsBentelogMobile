package com.example.user.bogartsbentelogmobile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by user on 3/25/2019.
 */

public class RecyclerOrderRequestHolder extends RecyclerView.ViewHolder {

        TextView cartFoodName, cartFoodPrice, cartPricePerPiece, cartFoodCount;
        Button addButton, subButton;

    public RecyclerOrderRequestHolder(View itemView) {
            super(itemView);
            cartFoodName = (TextView)itemView.findViewById(R.id.order_request_item_name);
            cartFoodPrice = (TextView)itemView.findViewById(R.id.order_request_item_price);
            cartFoodCount = (TextView)itemView.findViewById(R.id.order_request_item_quantity);
            cartPricePerPiece = (TextView)itemView.findViewById(R.id.order_request_item_price_per_pc);
        }
}
