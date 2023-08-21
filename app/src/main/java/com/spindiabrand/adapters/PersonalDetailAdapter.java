package com.spindiabrand.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.spindiabrand.MyApplication;
import com.spindiabrand.R;
import com.spindiabrand.binding.GlideBinding;
import com.spindiabrand.databinding.ItemPersonalDetailBinding;
import com.spindiabrand.databinding.ItemPostDetailBinding;
import com.spindiabrand.items.PostItem;
import com.spindiabrand.listener.ClickListener;
import com.spindiabrand.ui.activities.SubsPlanActivity;
import com.spindiabrand.utils.Constant;
import com.spindiabrand.utils.PrefManager;
import com.spindiabrand.utils.Util;

import java.util.List;

public class PersonalDetailAdapter extends RecyclerView.Adapter<PersonalDetailAdapter.MyViewHolder> {

    Context context;
    ClickEvent clickEvent;

    List<PostItem> postItemList;
    private int itemWidth = 0;
    int column;
    float width;
    PrefManager prefManager;

    public PersonalDetailAdapter(Context context, ClickEvent clickEvent, int column, float width) {
        this.context = context;
        this.clickEvent = clickEvent;
        this.column = column;
        this.width = width;
        prefManager = new PrefManager(context);
        itemWidth = MyApplication.getColumnWidth(column, width);
    }

    public void setData(List<PostItem> postItemList) {
        this.postItemList = postItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPersonalDetailBinding binding = ItemPersonalDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setPostdata(postItemList.get(position));

        int index = 0;
        for (int i = 0; i < position; i++) {
            index++;
            if (index == 9) {
                index = 0;
            }
        }
        String[] colorsTxt = context.getResources().getStringArray(R.array.personal_colors);

        holder.binding.view7.setBackgroundColor(Color.parseColor(colorsTxt[index]));
        holder.binding.ivImage.setBorderColor(Color.parseColor(colorsTxt[index]));

        if (Constant.IS_SUBSCRIBED) {
            holder.binding.ivFrameWatermark.setVisibility(View.GONE);
            holder.binding.ivCancel.setVisibility(View.GONE);
        } else {
            holder.binding.ivFrameWatermark.setVisibility(View.VISIBLE);
            holder.binding.ivCancel.setVisibility(View.VISIBLE);
        }

        holder.binding.ivCancel.setOnClickListener(v -> {
            context.startActivity(new Intent(context, SubsPlanActivity.class));
        });

        holder.binding.btnWhatsapp.setOnClickListener(v -> {
            if (prefManager.getString(Constant.PERSONAL_NAME).equals("")) {
                clickEvent.onEditClick();
                return;
            }
            holder.binding.ivCancel.setVisibility(View.GONE);
            holder.binding.ivFrameWatermark.setVisibility(Constant.IS_SUBSCRIBED ? View.GONE : View.VISIBLE);
            clickEvent.onWhatsappClick(holder.binding.cvBase, postItemList.get(position));
        });

        holder.binding.btnDownload.setOnClickListener(v -> {
            if (prefManager.getString(Constant.PERSONAL_NAME).equals("")) {
                clickEvent.onEditClick();
                return;
            }
            holder.binding.ivCancel.setVisibility(View.GONE);
            holder.binding.ivFrameWatermark.setVisibility(Constant.IS_SUBSCRIBED ? View.GONE : View.VISIBLE);
            clickEvent.onDownloadClick(holder.binding.cvBase, postItemList.get(position));
        });

        holder.binding.btnEdit.setOnClickListener(v -> {
            clickEvent.onEditClick();
        });

        holder.binding.ivImage.setOnClickListener(v -> {
            clickEvent.onEditClick();
        });

        if (prefManager.getBoolean(Constant.IS_LOGIN)) {
            if (!prefManager.getString(Constant.PERSONAL_IMAGE).equals("")) {
                GlideBinding.bindImage(holder.binding.ivImage, prefManager.getString(Constant.PERSONAL_IMAGE));
            }
            if (prefManager.getString(Constant.PERSONAL_NAME).equals("")) {
//                holder.binding.tvName.setText(prefManager.getString(Constant.USER_NAME));
                holder.binding.tvName.setText("Add Your Name");
            } else {
                holder.binding.tvName.setText(prefManager.getString(Constant.PERSONAL_NAME));
            }

            if (!prefManager.getString(Constant.PERSONAL_NUMBER).equals("")) {
                holder.binding.tvNumber.setText(prefManager.getString(Constant.PERSONAL_NUMBER));
            } else {
                holder.binding.tvNumber.setText("+91 1234567890");
            }

        }

        if (postItemList.get(position).is_video) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.cvBase.getLayoutParams();
            params.width = itemWidth;
            params.height = itemWidth;
            holder.binding.cvBase.requestLayout();
            holder.binding.cvBase.setLayoutParams(params);
        } else {
            float f = 1.0f;
            String width = postItemList.get(position).postWidth;
            String height = postItemList.get(position).postHeight;

            f = Float.parseFloat(height) / Float.parseFloat(width);

            Util.showLog("SS: " + f);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.cvBase.getLayoutParams();
            params.width = itemWidth;
            params.height = (int) (itemWidth * f);

            holder.binding.cvBase.requestLayout();
            holder.binding.cvBase.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        if (postItemList != null && postItemList.size() > 0) {
            return postItemList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPersonalDetailBinding binding;

        public MyViewHolder(@NonNull ItemPersonalDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ClickEvent {
        void onWhatsappClick(View view, PostItem postItem);

        void onDownloadClick(View view, PostItem postItem);

        void onEditClick();
    }
}
