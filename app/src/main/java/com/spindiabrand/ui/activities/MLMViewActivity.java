package com.spindiabrand.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.spindiabrand.R;
import com.spindiabrand.utils.Constant;
import com.spindiabrand.utils.PrefManager;

public class MLMViewActivity extends AppCompatActivity {
   ImageView imgBack;
    WebView mlmWebview;
    PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlm);
        mlmWebview=findViewById(R.id.mlm_webview);
        imgBack=findViewById(R.id.img_back);
        
        initView();
    }

    private void initView() {
        prefManager = new PrefManager(this);
        mlmWebview.loadUrl("http://poster.spindiabazaar.com/public/view.php?referalCode=" + prefManager.getString(Constant.REFER_CODE));
        mlmWebview.getSettings().setJavaScriptEnabled(true);
        mlmWebview.setInitialScale(1);
        mlmWebview.getSettings().setLoadWithOverviewMode(true);
        mlmWebview.getSettings().setUseWideViewPort(true);
        mlmWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mlmWebview.setScrollbarFadingEnabled(false);
        mlmWebview.setBackgroundColor(Color.TRANSPARENT);
        mlmWebview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MLMViewActivity.this, MainActivity.class));
            }
        });
    }
}
