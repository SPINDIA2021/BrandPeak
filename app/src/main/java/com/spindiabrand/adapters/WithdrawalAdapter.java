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
import com.spindiabrand.items.WithdrawItem;

import java.util.List;

public class WithdrawalAdapter extends RecyclerView.Adapter<WithdrawalAdapter.MyViewHolder> {

    Context context;
    List<WithdrawItem> withdrawalItemList;

    public WithdrawalAdapter(Context context, List<WithdrawItem> withdrawalItemList) {
        this.context = context;
        this.withdrawalItemList = withdrawalItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_withdraw, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(withdrawalItemList.get(position).userName);


        holder.tvAmount.setText("₹ " + withdrawalItemList.get(position).amount);



        if(withdrawalItemList.get(position).status.equals("0")){
            holder.tvStatus.setText("Pending");
            holder.tvStatus.setTextColor(ColorStateList.valueOf(context.getColor(R.color.red_800)));
        }else if(withdrawalItemList.get(position).status.equals("1")){
            holder.tvStatus.setText("Success");
            holder.tvStatus.setTextColor(ColorStateList.valueOf(context.getColor(R.color.green_800)));
        }else if(withdrawalItemList.get(position).status.equals("2")){
            holder.tvStatus.setText("Rejected");
            holder.tvStatus.setTextColor(ColorStateList.valueOf(context.getColor(R.color.red_800)));
        }

        holder.tvUPIId.setText( withdrawalItemList.get(position).upiid);
        holder.tvDate.setText( withdrawalItemList.get(position).date);

    }

    @Override
    public int getItemCount() {
        return withdrawalItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUPIId,tvAmount,tvStatus,tvDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_withdraw);
            tvUPIId = itemView.findViewById(R.id.tv_upiid_withdraw);
            tvAmount = itemView.findViewById(R.id.tv_amount_withdraw);
            tvStatus = itemView.findViewById(R.id.tv_status_withdraw);
            tvDate = itemView.findViewById(R.id.tv_date_withdraw);
        }
    }
}
