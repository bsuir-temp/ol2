package com.bsuir.oitip.lab2.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuir.oitip.lab2.R;

public class LayerVH extends RecyclerView.ViewHolder {

    public ImageView icon;

    public LayerVH(@NonNull View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.layericon);
    }

}
