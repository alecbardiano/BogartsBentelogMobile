package com.example.user.bogartsbentelogmobile.Interface;

import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Created by user on 1/31/2019.
 */

public interface ItemClickListener {

    void onClickItemListener(DocumentSnapshot snapshot, int position);

}
