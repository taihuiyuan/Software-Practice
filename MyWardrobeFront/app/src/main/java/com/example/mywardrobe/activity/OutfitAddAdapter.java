package com.example.mywardrobe.activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mywardrobe.R;
import com.example.mywardrobe.entity.Clothes;

import java.util.ArrayList;
import java.util.List;

public class OutfitAddAdapter extends RecyclerView.Adapter {
    private List<Clothes> dataList;
    private List<Integer> choices = new ArrayList<>();

    public OutfitAddAdapter(List<Clothes> clothesList){this.dataList = clothesList;}


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addoutfit,parent,false);
        OutfitAddHolder outfitAddHolder = new OutfitAddHolder(view);
        return outfitAddHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Clothes clothes = this.dataList.get(position);
        View itemView = ((OutfitAddHolder)holder).itemView;
        CheckBox checkBox = itemView.findViewById(R.id.ckCloth);
        ImageView imgCloth = itemView.findViewById(R.id.imgClothes);
        Glide.with(itemView.getContext()).load(clothes.getImageUrl()).into(imgCloth);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    addCloth(clothes.getId());
                    Log.i("choose",clothes.getId().toString());
                }else{
                    removeCloth(clothes.getId());
                    Log.i("unchoose",clothes.getId().toString());
                }
            }
        });
    }

    public void setData(List<Clothes> dataList){
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public List<Integer> getChoices(){
        return choices;
    }


    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    private void addCloth(Integer id){
        this.choices.add(id);
    }

    private void removeCloth(Integer id){
        for(int i=0;i<choices.size();i++){
            if(id.equals(choices.get(i))){
                choices.remove(i);
            }
        }
    }


}
