package com.spindiabrand.adapters;

import static com.spindiabrand.MyApplication.context;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spindiabrand.databinding.ItemProductBinding;
import com.spindiabrand.items.ProductItem;
import com.spindiabrand.ui.activities.ProductDetailActivity;

import java.io.Serializable;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    List<ProductItem> productItemList;
    Context context;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setData(productItemList.get(position));
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT", (Serializable) productItemList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (productItemList != null && productItemList.size() > 0) {
            return productItemList.size();
        }
        return 0;
    }

    public void setData(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public MyViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
