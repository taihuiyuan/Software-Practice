package com.example.mywardrobe.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClothesListHolder extends RecyclerView.ViewHolder{
    View itemView;
    public ClothesListHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

}
