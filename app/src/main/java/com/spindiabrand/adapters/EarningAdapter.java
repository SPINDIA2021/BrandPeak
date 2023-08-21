package com.spindiabrand.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spindiabrand.R;
import com.spindiabrand.items.EarningItem;

import java.util.List;

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.MyViewHolder> {

    Context context;
    List<EarningItem> earningItemList;

    public EarningAdapter(Context context, List<EarningItem> earningItemList) {
        this.context = context;
        this.earningItemList = earningItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_earning, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(earningItemList.get(position).userName);


        if(earningItemList.get(position).credit.equals("0")){
            holder.tvCredit.setTextColor(ColorStateList.valueOf(context.getColor(R.color.green_800)));
            holder.tvCredit.setText("");
        }else {
            holder.tvCredit.setTextColor(ColorStateList.valueOf(context.getColor(R.color.green_800)));
            holder.tvCredit.setText("₹ " + earningItemList.get(position).credit);
        }


        if(earningItemList.get(position).debit.equals("0")){
            holder.tvDebit.setText("");
        }else {
            holder.tvDebit.setTextColor(ColorStateList.valueOf(context.getColor(R.color.red_800)));
            holder.tvDebit.setText("₹ " + earningItemList.get(position).debit);
        }

        holder.tvCurrent.setText("₹ " + earningItemList.get(position).cur_balance);
        holder.tvDate.setText( earningItemList.get(position).date);

    }

    @Override
    public int getItemCount() {
        return earningItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCredit,tvDebit,tvCurrent,tvDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvCredit = itemView.findViewById(R.id.tv_credit);
            tvDebit = itemView.findViewById(R.id.tv_debit);
            tvCurrent = itemView.findViewById(R.id.tv_current);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
