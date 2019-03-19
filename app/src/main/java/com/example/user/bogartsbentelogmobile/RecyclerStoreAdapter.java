package com.example.user.bogartsbentelogmobile;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bogartsbentelogmobile.Model.Request;
import com.example.user.bogartsbentelogmobile.Model.Store;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * Created by user on 3/12/2019.
 */

public class RecyclerStoreAdapter extends FirestoreRecyclerAdapter<Store,RecyclerStoreHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecyclerStoreAdapter(@NonNull FirestoreRecyclerOptions<Store> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerStoreHolder holder, int position, @NonNull Store model) {
        holder.branchName.setText(model.getNameOfBranch());
        holder.branchAddress.setText(model.getAddress());
        holder.branchContactNumber.setText(model.getContactNo());
    }

    @NonNull
    @Override
    public RecyclerStoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_store,parent,false);

        return new RecyclerStoreHolder(view);
    }
}
