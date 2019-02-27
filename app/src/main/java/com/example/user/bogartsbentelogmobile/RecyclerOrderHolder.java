package com.example.user.bogartsbentelogmobile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by user on 2/27/2019.
 */

public class RecyclerOrderHolder extends RecyclerView.ViewHolder {

    TextView reqNameOfCustomer, reqAddressOfCustomer, reqStatusOfOrder, reqTotalPriceOrder, reqDateOfOrder;

    public RecyclerOrderHolder(View itemView) {
        super(itemView);
        reqNameOfCustomer = (TextView)itemView.findViewById(R.id.req_name);
        reqAddressOfCustomer = (TextView)itemView.findViewById(R.id.req_address);
        reqStatusOfOrder = (TextView)itemView.findViewById(R.id.req_status);
        reqTotalPriceOrder = (TextView)itemView.findViewById(R.id.req_totalOrder);
        reqDateOfOrder = (TextView)itemView.findViewById(R.id.req_dateOrder);
    }
}
