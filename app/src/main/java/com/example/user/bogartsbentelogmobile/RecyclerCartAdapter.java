package com.example.user.bogartsbentelogmobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.user.bogartsbentelogmobile.Model.Category;
import com.example.user.bogartsbentelogmobile.Model.Food;
import com.example.user.bogartsbentelogmobile.Model.Order;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 2/13/2019.
 */

public class RecyclerCartAdapter  extends FirestoreRecyclerAdapter<Order,RecyclerCartHolder> {

    private List<Order> orderList = new ArrayList<>();
    private Context context;


    public RecyclerCartAdapter(@NonNull FirestoreRecyclerOptions<Order> options, List<Order> orderList, Context context) {
        super(options);
        this.orderList = orderList;
        this.context = context;
    }

    public RecyclerCartAdapter(@NonNull FirestoreRecyclerOptions<Order> options, Context context) {
        super(options);
        this.context = context;
    }

    public void deleteFoodInCart(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    @Override
    protected void onBindViewHolder(@NonNull RecyclerCartHolder holder, final int position, @NonNull final Order model) {
//        TextDrawable drawable = TextDrawable.builder().buildRound("" + model.getQuantity(), Color.RED);
        if(Integer.parseInt(model.getQuantity()) == 0){
            deleteFoodInCart(position);
        }

        int price = (Integer.parseInt(model.getPrice()) * (Integer.parseInt(model.getQuantity())));
        Locale locale = new Locale("tl", "PH");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale); // Philippines

        holder.cartFoodCount.setText(model.getQuantity());
        holder.cartPricePerPiece.setText(format.format(Integer.parseInt(model.getPrice())));
        holder.cartFoodPrice.setText(format.format(price));
        holder.cartFoodName.setText(model.getProductName());

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSnapshots().getSnapshot(position).getReference().update("quantity", Integer.toString((Integer.parseInt(model.getQuantity()) + 1)));
            }
        });

        holder.subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(model.getQuantity()) == 0){
                    deleteFoodInCart(position);
                }
                getSnapshots().getSnapshot(position).getReference().update("quantity", Integer.toString((Integer.parseInt(model.getQuantity()) - 1)));
            }
        });


//        Tagalog, Philippines (tl_PH)
    }

    @NonNull
    @Override
    public RecyclerCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_layout,parent,false);

        return new RecyclerCartHolder(view);
    }
}
