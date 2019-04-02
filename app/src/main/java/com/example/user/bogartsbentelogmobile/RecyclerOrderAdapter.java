package com.example.user.bogartsbentelogmobile;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bogartsbentelogmobile.Common.Common;
import com.example.user.bogartsbentelogmobile.Interface.ItemClickListener;
import com.example.user.bogartsbentelogmobile.Model.Request;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.example.user.bogartsbentelogmobile.Model.Order;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 2/27/2019.
 */

public class RecyclerOrderAdapter extends FirestoreRecyclerAdapter<Request,RecyclerOrderHolder> implements ItemClickListener {

    private Context context;
    private ItemClickListener listener;

    public RecyclerOrderAdapter(@NonNull FirestoreRecyclerOptions<Request> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerOrderHolder holder, final int position, @NonNull Request model) {

        Locale locale = new Locale("tl", "PH");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        holder.reqTotalPriceOrder.setText(format.format(Integer.parseInt(model.getTotal())));
//        holder.reqStatusOfOrder.setText(model.getStatus());
        holder.reqAddressOfCustomer.setText(model.getAddress());
        holder.reqNameOfCustomer.setText(model.getName());
        holder.reqStatusOfOrder.setText(Common.convertCodeToStatus(model.getStatus()));
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(model.getDateOfOrder());
        holder.reqDateOfOrder.setText(currentDateTimeString);
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
    public RecyclerOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_request,parent,false);

        return new RecyclerOrderHolder(view);

    }

    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClickItemListener(DocumentSnapshot snapshot, int position) {

    }
}
