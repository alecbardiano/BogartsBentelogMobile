package com.example.user.bogartsbentelogmobile;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bogartsbentelogmobile.Model.Request;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.example.user.bogartsbentelogmobile.Model.Order;

import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 2/27/2019.
 */

public class RecyclerOrderAdapter extends FirestoreRecyclerAdapter<Request,RecyclerOrderHolder> {

    private Context context;

    public RecyclerOrderAdapter(@NonNull FirestoreRecyclerOptions<Request> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerOrderHolder holder, int position, @NonNull Request model) {
        holder.reqTotalPriceOrder.setText(model.getTotal());
        holder.reqStatusOfOrder.setText(model.getStatus());
        holder.reqAddressOfCustomer.setText(model.getAddress());
        holder.reqNameOfCustomer.setText(model.getName());
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(model.getDateOfOrder());
        holder.reqDateOfOrder.setText(currentDateTimeString);

    }


    @NonNull
    @Override
    public RecyclerOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_request,parent,false);

        return new RecyclerOrderHolder(view);

    }
}
