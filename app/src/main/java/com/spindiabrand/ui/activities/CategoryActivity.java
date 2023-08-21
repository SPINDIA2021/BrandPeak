package com.spindiabrand.ui.activities;

import static com.spindiabrand.utils.Constant.CATEGORY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.spindiabrand.Ads.BannerAdManager;
import com.spindiabrand.R;
import com.spindiabrand.adapters.CategoryAdapter;
import com.spindiabrand.databinding.ActivityCategoryBinding;
import com.spindiabrand.items.CategoryItem;
import com.spindiabrand.listener.ClickListener;
import com.spindiabrand.utils.Constant;
import com.spindiabrand.viewmodel.CategoryViewModel;

public class CategoryActivity extends AppCompatActivity implements ClickListener<CategoryItem> {

    ActivityCategoryBinding binding;
    CategoryAdapter categoryAdapter;
    CategoryViewModel categoryViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
        initViewModel();
    }

    private void setUpUi() {
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_category));

        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        categoryAdapter = new CategoryAdapter(this, this, false);
        binding.rvCategory.setAdapter(categoryAdapter);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            categoryViewModel.setCategoryObj("Category");
        });
    }

    private void initViewModel() {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryViewModel.setCategoryObj("Category");
        categoryViewModel.getCategories().observe(this, result->{
            if (result.data!=null){
                binding.swipeRefresh.setRefreshing(false);
                if (result.data.size()>0){
                    binding.animationView.setVisibility(View.GONE);
                    categoryAdapter.setCategories(result.data);
                }else{
                    binding.animationView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(CategoryItem data) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, CATEGORY);
        intent.putExtra(Constant.INTENT_FEST_ID, data.id);
        intent.putExtra(Constant.INTENT_FEST_NAME, data.name);
        intent.putExtra(Constant.INTENT_POST_IMAGE, "");
        intent.putExtra(Constant.INTENT_VIDEO, data.video);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}