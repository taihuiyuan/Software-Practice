package com.example.mywardrobe.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywardrobe.R;
import com.example.mywardrobe.entity.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter {
    private  List<Category> dataList;
    private ItemClick moitmClick;

    public CategoryListAdapter( List<Category> categoryList){
        this.dataList = categoryList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent,false);
        CategoryListHolder categoryListHolder = new CategoryListHolder(view);
        return categoryListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Category category = dataList.get(position);
        View itemView =((CategoryListHolder)holder).itemView;
        TextView tvItem = itemView.findViewById(R.id.tvCategoryItem);
        tvItem.setText(category.getName());

        //为recycle view里面的item内部按钮添加listener
        Button btnList = itemView.findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moitmClick.onItemButtonClick(category.getName(),category.getId());
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

    public void setData(List<Category> dataList) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public interface ItemClick {
        void onItemButtonClick(String name, int id);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    private OnItemLongClickListener mOnItemLongClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setItemClick(ItemClick itemClick) {
        moitmClick = itemClick;
    }
}

