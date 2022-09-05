package com.example.mywardrobe.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.entity.Clothes;

import com.example.mywardrobe.R;

import java.util.List;

public class OutfitDetailAdapter extends RecyclerView.Adapter {
    private List<Clothes> dataList;

    public OutfitDetailAdapter(List<Clothes> clothesList){this.dataList = clothesList;}
    private OutfitDetailAdapter.ItemClick itemClick;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clothes,parent,false);
        OutfitDetailHolder outfitDetailHolder = new OutfitDetailHolder(view);
        return outfitDetailHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Clothes clothes = dataList.get(position);
        View itemView = ((OutfitDetailHolder)holder).itemView;
        ImageView imageView = itemView.findViewById(R.id.imgClothes);
        Glide.with(itemView.getContext()).load(clothes.getImageUrl()).into(imageView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClick(clothes.getId());
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

    public void setDataList(List<Clothes> dataList){
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public interface ItemClick {
        void onItemClick(int id);
    }

    public void setItemClick(OutfitDetailAdapter.ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    private OutfitDetailAdapter.OnItemLongClickListener mOnItemLongClickListener;
    public void setOnItemLongClickListener(OutfitDetailAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

}
