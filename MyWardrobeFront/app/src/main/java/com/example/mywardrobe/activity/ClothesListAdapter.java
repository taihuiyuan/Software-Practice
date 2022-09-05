package com.example.mywardrobe.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.R;
import com.example.mywardrobe.entity.Clothes;

import java.net.URL;
import java.util.List;

public class ClothesListAdapter extends RecyclerView.Adapter {
    private List<Clothes> dataList;
    private ClothesListAdapter.ItemClick moitmClick;

    public ClothesListAdapter( List<Clothes> clothesList){
        this.dataList = clothesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clothes, parent,false);
        ClothesListHolder clothesListHolder = new ClothesListHolder(view);
        return clothesListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Clothes clothes = dataList.get(position);
        View itemView =((ClothesListHolder)holder).itemView;
        ImageView imgClothes = itemView.findViewById(R.id.imgClothes);
        Log.i("clothes adapter",clothes.toString());
        Glide.with(itemView.getContext()).load(clothes.getImageUrl()).into(imgClothes);
//        imgClothes.setImageURL(clothes.getImageUrl());

        //为recycle view添加listener
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moitmClick.onItemClick(clothes.getId());
            }
        });

        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }

            });

        }
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    public void setData(List<Clothes> dataList) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public interface ItemClick {
        void onItemClick(int id);
    }

    public void setItemClick(ClothesListAdapter.ItemClick itemClick) {
        moitmClick = itemClick;
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    private ClothesListAdapter.OnItemLongClickListener mOnItemLongClickListener;
    public void setOnItemLongClickListener(ClothesListAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
}
