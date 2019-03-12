package com.example.user.bogartsbentelogmobile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 3/12/2019.
 */

public class RecyclerStoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView branchName, branchAddress;

    public RecyclerStoreHolder(View itemView) {
        super(itemView);
        branchName = (TextView)itemView.findViewById(R.id.store_name);
        branchAddress = (TextView)itemView.findViewById(R.id.store_address);
    }

    @Override
    public void onClick(View v) {

    }
}
