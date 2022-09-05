package com.example.mywardrobe.activity;

import android.app.Application;
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
import com.example.mywardrobe.AppClient;
import com.example.mywardrobe.R;
import com.example.mywardrobe.entity.Clothes;
import com.example.mywardrobe.entity.Outfit;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class OutfitListAdapter extends RecyclerView.Adapter{
    private List<Outfit> dataList;
    private OutfitListAdapter.ItemClick itemClick;

    private OutfitActivity activity;

    public OutfitListAdapter( List<Outfit> outfitList){
        this.dataList = outfitList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outfit, parent,false);
        OutfitListHolder outfitListHolder = new OutfitListHolder(view);
        this.activity = (OutfitActivity) parent.getContext();
        return outfitListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Outfit outfit = dataList.get(position);
        List<Clothes> clothes = outfit.getClothes();
        View itemView =((OutfitListHolder)holder).itemView;
        TextView tvItem = itemView.findViewById(R.id.tvOutfitName);
        tvItem.setText(outfit.getOutfitName());

        if (clothes.size() != 0){
            ImageView imgClothes = itemView.findViewById(R.id.imgOutfit);
            Glide.with(this.activity.getBaseContext()).load(clothes.get(0).getImageUrl()).into(imgClothes);
        }

        //为recycle view里面的item内部按钮添加listener
        Button btnList = itemView.findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemButtonClick(outfit.getOutfitName(),outfit.getClothes());
            }
        });

        Button btnDelete = itemView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemDeleteClick(outfit.getOutfitName());
            }
        });

    }

//    private void requestUrl(URL url) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = null;
//                try {
//                    bitmap = BitmapFactory.decodeStream(url.openStream());
//
//                    showImg(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void showImg(Bitmap bitmap) {
//        this.activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imgOutfit.setImageBitmap(bitmap);
//            }
//        });
//    }

    public void setData(List<Outfit> dataList) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public interface ItemClick {
        void onItemButtonClick(String name, List<Clothes> clothes);
        void onItemDeleteClick(String outfitName);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setItemClick(OutfitListAdapter.ItemClick itemClick) {
        this.itemClick = itemClick;
    }



}
