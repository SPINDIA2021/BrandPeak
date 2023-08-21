package com.spindiabrand.ui.activities;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.CFDropCheckoutPayment;
import com.cashfree.pg.ui.api.CFPaymentComponent;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.spindiabrand.Ads.BannerAdManager;
import com.spindiabrand.MyApplication;
import com.spindiabrand.R;
import com.spindiabrand.adapters.SubsPlanAdapter;
import com.spindiabrand.api.ApiClient;
import com.spindiabrand.api.ApiResponse;
import com.spindiabrand.crypto.PaytmChecksum;
import com.spindiabrand.databinding.ActivitySubsPlanBinding;
import com.spindiabrand.databinding.DialogEnquiryBinding;
import com.spindiabrand.items.CashFreeOrder;
import com.spindiabrand.items.CouponItem;
import com.spindiabrand.items.SubsPlanItem;
import com.spindiabrand.items.UserItem;
import com.spindiabrand.paytmIntegration.Api;
import com.spindiabrand.paytmIntegration.CheckSumServiceHelper;
import com.spindiabrand.paytmIntegration.Checksum;
import com.spindiabrand.paytmIntegration.Constants;
import com.spindiabrand.paytmIntegration.Paytm;
import com.spindiabrand.ui.dialog.DialogMsg;
import com.spindiabrand.utils.Connectivity;
import com.spindiabrand.utils.Constant;
import com.spindiabrand.utils.PrefManager;
import com.spindiabrand.utils.Util;
import com.spindiabrand.viewmodel.SubsPlanViewModel;
import com.spindiabrand.viewmodel.UserViewModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubsPlanActivity extends AppCompatActivity implements PaymentResultListener, CFCheckoutResponseCallback {

    ActivitySubsPlanBinding binding;
    SubsPlanViewModel subPlanViewModel;
    SubsPlanAdapter subsPlanAdapter;
    DialogMsg dialogMsg;
    Connectivity connectivity;
    UserViewModel userViewModel;
    PrefManager prefManager;
    UserItem userItem;
    String planId = "";
    String planName = "";
    String planPrice = "";
    String couponCode = "";
    String payuTrans = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubsPlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        connectivity = new Connectivity(this);
        prefManager = new PrefManager(this);

//        prefManager.setString(Constant.PAYMENT_GATE_WAY, Constant.PayuMoney);
        if (prefManager.getString(Constant.PAYMENT_GATE_WAY).equals(Constant.Razorpay)) {
            Checkout.preload(getApplicationContext());
        }

        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
        setUpViewModel();

        if (prefManager.getString(Constant.PAYMENT_GATE_WAY).equals(Constant.Cashfree)) {
            try {
                CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
            } catch (CFException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpViewModel() {
        subPlanViewModel = new ViewModelProvider(this).get(SubsPlanViewModel.class);
        subPlanViewModel.getSubsPlanItems().observe(this, listResource -> {
            if (listResource != null) {

                Util.showLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {
                            setData(listResource.data);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            setData(listResource.data);
//                                        updateForgotBtnStatus();
                        }

                        break;
                    case ERROR:
                        // Error State

                        dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                        dialogMsg.show();

                        break;
                    default:

                        break;
                }

            } else {

                // Init Object or Empty Data
                Util.showLog("Empty Data");

            }
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getDbUserData(prefManager.getString(Constant.USER_ID)).observe(this, item -> {
            if (item != null) {
                userItem = item.user;
            }
        });


    }

    private void setData(List<SubsPlanItem> data) {
        subsPlanAdapter.subsPlanItemList(data);
        binding.shimmerViewContainer.stopShimmer();
        binding.shimmerViewContainer.setVisibility(GONE);
        binding.rvSubsplan.setVisibility(View.VISIBLE);
    }

    private void setUpUi() {
        binding.toolbar.toolName.setText(getResources().getString(R.string.subscribe));
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });
        subsPlanAdapter = new SubsPlanAdapter(this, item -> {

            planId = item.id;
            planPrice = item.planPrice;
            planName = item.planName;
            if (!connectivity.isConnected()) {
                Util.showToast(SubsPlanActivity.this, getResources().getString(R.string.error_message__no_internet));
                return;
            }

            if (!prefManager.getBoolean(Constant.IS_LOGIN)) {
                dialogMsg.showWarningDialog(getString(R.string.login_login), getString(R.string.login_first_login), getString(R.string.login_login), false);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(v -> {
                    startActivity(new Intent(SubsPlanActivity.this, LoginActivity.class));
                });
                return;
            }

            dialogMsg.showPaymentDialog(item);
            dialogMsg.show();

            dialogMsg.cbRazorPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMsg.cbRazorPay.setVisibility(GONE);
                    dialogMsg.pbPayment.setVisibility(View.VISIBLE);
                    if (prefManager.getString(Constant.PAYMENT_GATE_WAY).equals(Constant.Razorpay)) {
                        startPayment(dialogMsg.FINAL_PRICE, prefManager.getString(Constant.RAZORPAY_KEY_ID));
                    } else if (prefManager.getString(Constant.PAYMENT_GATE_WAY).equals(Constant.Cashfree)) {
                        createOrderCashfree(dialogMsg.FINAL_PRICE);
                    }else if (prefManager.getString(Constant.PAYMENT_GATE_WAY).equals(Constant.Paytm)) {
                        doPaytm(dialogMsg.FINAL_PRICE);
                        //startAutodebit(dialogMsg.FINAL_PRICE);
                    }
                 //   doPaytm(dialogMsg.FINAL_PRICE);
                }
            });

            dialogMsg.ivPayCancel.setOnClickListener(v -> {
                dialogMsg.cancel();
            });

            dialogMsg.btn_apply.setOnClickListener(v -> {

                if (dialogMsg.etCode.getText().toString().equals("")) {
                    Util.showToast(SubsPlanActivity.this, "Enter Code");
                    return;
                }

                dialogMsg.btn_apply.setEnabled(false);
                dialogMsg.btn_apply.setText("Checking...");
                dialogMsg.cbRazorPay.setEnabled(false);

                checkCoupon(userItem.userId, dialogMsg.etCode.getText().toString());
            });

        });
        binding.rvSubsplan.setAdapter(subsPlanAdapter);
    }

    private void createOrderCashfree(int final_price) {
        if (userItem.phone.equals("") || userItem.phone.length() > 10) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogEnquiryBinding binding = DialogEnquiryBinding.inflate(getLayoutInflater());
            dialog.setContentView(binding.getRoot());
            if (dialog.getWindow() != null) {
                dialog.getWindow().setAttributes(getLayoutParams(dialog));

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
            dialog.setCancelable(false);

            if (MyApplication.prefManager().getBoolean(Constant.IS_LOGIN)) {
                binding.etName.setText(MyApplication.prefManager().getString(Constant.USER_NAME));
                binding.etEmail.setText(MyApplication.prefManager().getString(Constant.USER_EMAIL));
                binding.etEmail.setEnabled(MyApplication.prefManager().getString(Constant.USER_EMAIL).equals("") ? true : false);
                binding.etMobile.setText(MyApplication.prefManager().getString(Constant.USER_PHONE));
            }
            binding.etDetails.setVisibility(GONE);
            binding.textViewi.setVisibility(GONE);

            binding.btnSave.setOnClickListener(v -> {

                if (binding.etEmail.getText().toString().trim().isEmpty()) {
                    binding.etEmail.setError(getResources().getString(R.string.enter_email));
                    binding.etEmail.requestFocus();
                    return;
                }
                if (binding.etName.getText().toString().trim().isEmpty()) {
                    binding.etName.setError(getResources().getString(R.string.enter_name));
                    binding.etName.requestFocus();
                    return;
                }
                if (!isEmailValid(binding.etEmail.getText().toString())) {
                    binding.etEmail.setError(getString(R.string.invalid_email));
                    binding.etEmail.requestFocus();
                    return;
                }
                if (binding.etMobile.getText().toString().trim().isEmpty()) {
                    binding.etMobile.setError(getResources().getString(R.string.please_enter_valid_mobile));
                    binding.etMobile.requestFocus();
                    return;
                }
                if (binding.etMobile.getText().toString().length() > 10) {
                    binding.etMobile.setError("Please Enter 10 Digit Mobile");
                    binding.etMobile.requestFocus();
                    return;
                }
//                if (binding.etDetails.getText().toString().trim().isEmpty()) {
//                    binding.etDetails.setError(getResources().getString(R.string.enter_details));
//                    binding.etDetails.requestFocus();
//                    return;
//                }

                String name = binding.etName.getText().toString().trim();
                String email = binding.etEmail.getText().toString().trim();
                String mobile = binding.etMobile.getText().toString().trim();

                dialog.dismiss();

                continueOrder(final_price, name, email, mobile);


            });
            binding.ivCancel.setVisibility(GONE);
            binding.ivCancel.setOnClickListener(v -> {
                dialog.cancel();
            });
            dialog.show();
        } else {

            continueOrder(final_price, userItem.userName, userItem.email, userItem.phone);

        }
    }

    private void continueOrder(int final_price, String name, String email, String mobile) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<CashFreeOrder> response = ApiClient.getApiService().createOrderCashFree(prefManager.getString(Constant.api_key),
                        prefManager.getString(Constant.USER_ID),
                        final_price,
                        name,
                        email,
                        mobile).execute();


                // Wrap with APIResponse Class
                ApiResponse<CashFreeOrder> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    Util.showLog("" + apiResponse.body);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doDropCheckoutPayment(apiResponse.body.order_id, apiResponse.body.payment_session_id);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showLog("EEE: " + apiResponse.errorMessage);
                            dialogMsg.cancel();
                            dialogMsg.showErrorDialog(apiResponse.errorMessage, getString(R.string.ok));
                            dialogMsg.show();
                        }
                    });
                }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showLog("EEE: " + "Coupon Code Not Valid");
                        dialogMsg.cancel();
                        dialogMsg.showErrorDialog("Error while Creating Order", getString(R.string.ok));
                        dialogMsg.show();
                    }
                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ");
    }

    private void checkCoupon(String userId, String couponCode) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<CouponItem> response = ApiClient.getApiService().checkCoupon(prefManager.getString(Constant.api_key),
                        userId,
                        couponCode).execute();


                // Wrap with APIResponse Class
                ApiResponse<CouponItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    Util.showLog("" + apiResponse.body);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            double discountPrice = Double.parseDouble(apiResponse.body.discount) * dialogMsg.FINAL_PRICE / 100;

                            double price = dialogMsg.FINAL_PRICE - discountPrice;

                            dialogMsg.tv_price.setText("" + price);

                            dialogMsg.FINAL_PRICE = (int) price;

                            planPrice = String.valueOf(dialogMsg.FINAL_PRICE);

                            dialogMsg.rlOpen.setVisibility(GONE);
                            dialogMsg.csApplied.setVisibility(View.VISIBLE);

                            dialogMsg.tv_code.setText(dialogMsg.etCode.getText().toString());
                            dialogMsg.tv_code_dec.setText(apiResponse.body.discount + "% " + getString(R.string.discount_on)
                                    + " " + dialogMsg.tv_plan_name.getText());
                            dialogMsg.btn_apply.setEnabled(true);
                            dialogMsg.cbRazorPay.setEnabled(true);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Util.showLog("EEE: " + apiResponse.errorMessage);
                            dialogMsg.tv_error.setText(apiResponse.errorMessage);
                            dialogMsg.tv_error.setVisibility(View.VISIBLE);

                            dialogMsg.btn_apply.setEnabled(true);
                            dialogMsg.cbRazorPay.setEnabled(true);
                            dialogMsg.btn_apply.setText(getString(R.string.apply));
                        }
                    });
                }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Util.showLog("EEE: " + "Coupon Code Not Valid");
                        dialogMsg.tv_error.setText("Coupon Code Not Valid");
                        dialogMsg.tv_error.setVisibility(View.VISIBLE);

                        dialogMsg.btn_apply.setEnabled(true);
                        dialogMsg.cbRazorPay.setEnabled(true);
                        dialogMsg.btn_apply.setText(getString(R.string.apply));
                    }
                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });

    }

    private void startPayment(int planPrice, String key) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID(key);
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);
        /**
         * Reference to current activity
         */
        final Activity activity = this;
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", userItem.userName);
            options.put("description", "Charge Of Plan");
            options.put("theme.color", "#f59614");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("currency", "INR");
            options.put("amount", (float) planPrice * 100);//pass amount in currency subunits
            options.put("prefill.email", userItem.email);
            if (userItem.phone != null && !userItem.phone.equals("")) {
                options.put("prefill.contact", userItem.phone);
            }
            checkout.open(activity, options);

        } catch (Exception e) {
            Util.showErrorLog("Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String paymentId) {
        postPayment(paymentId);
    }

    @Override
    public void onPaymentError(int i, String s) {
        String message = "";
        if (i == Checkout.PAYMENT_CANCELED) {
            message = "The user canceled the payment.";
        } else if (i == Checkout.NETWORK_ERROR) {
            message = "There was a network error, for example, loss of internet connectivity.";
        } else if (i == Checkout.INVALID_OPTIONS) {
            message = "An issue with options passed in checkout.open .";
        } else if (i == Checkout.TLS_ERROR) {
            message = "The device does not support TLS v1.1 or TLS v1.2.";
        }else {
            message = "Unknown Error";
        }
        dialogMsg.cancel();
        Util.showLog(i + " " + s);
        dialogMsg.showErrorDialog(message, getString(R.string.ok));
        dialogMsg.show();

    }

    @Override
    public void onPaymentVerify(String s) {
        postPayment(s);
    }

    private void postPayment(String paymentId) {
        Util.showLog("ORDERID: " + paymentId);
        subPlanViewModel.loadPayment(prefManager.getString(Constant.USER_ID), planId, paymentId,
                String.valueOf(dialogMsg.FINAL_PRICE), dialogMsg.tv_code.getText().toString(),
                prefManager.getString(Constant.REFER_CODE_BY), prefManager.getString(Constant.PAYMENT_GATE_WAY)).observe(this,
                result -> {
                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:
                                dialogMsg.pbPayment.setVisibility(GONE);
                                dialogMsg.cancel();

                                dialogMsg.showSuccessDialog(result.data.message, getString(R.string.ok));
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogMsg.cancel();
                                        userViewModel.getUserDataById().observe(SubsPlanActivity.this, listResource -> {
                                            if (listResource != null) {
                                                Util.showLog("Got Data "
                                                        + listResource.message +
                                                        listResource.toString());

                                                switch (listResource.status) {
                                                    case LOADING:
                                                        // Loading State
                                                        // Data are from Local DB

                                                        break;
                                                    case SUCCESS:
                                                        // Success State
                                                        // Data are from Server

                                                        if (listResource.data != null) {
                                                            userItem = listResource.data;
                                                            Constant.IS_SUBSCRIBED = listResource.data.isSubscribed;
                                                            prefManager.setString(Constant.REFER_CODE_BY, listResource.data.referralCode);
                                                            onBackPressed();
                                                        }

                                                        break;
                                                    case ERROR:
                                                        // Error State

                                                        Util.showLog("Error: " + listResource.message);

                                                        break;
                                                    default:
                                                        // Default
                                                        break;
                                                }

                                            } else {

                                                // Init Object or Empty Data
                                                Util.showLog("Empty Data");

                                            }
                                        });
                                        userViewModel.setUserById(prefManager.getString(Constant.USER_ID));
                                    }
                                });
                                break;

                            case ERROR:

                                dialogMsg.pbPayment.setVisibility(GONE);
                                dialogMsg.cancel();

                                dialogMsg.showErrorDialog(result.message, getString(R.string.ok));
                                dialogMsg.show();
                                break;
                        }
                    }

                });
    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {
        Util.showLog("Er: " + cfErrorResponse.getMessage() + " " + cfErrorResponse.toJSON().toString());
        dialogMsg.cancel();

        dialogMsg.showErrorDialog(cfErrorResponse.getDescription(), getString(R.string.ok));
        dialogMsg.show();
    }

    public void doDropCheckoutPayment(String orderId, String token) {
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(CFSession.Environment.SANDBOX)
                    .setPaymentSessionID(token)
                    .setOrderId(orderId)
                    .build();
            CFPaymentComponent cfPaymentComponent =
                    new CFPaymentComponent.CFPaymentComponentBuilder()
                            // Shows only Card and UPI modes
                            .add(CFPaymentComponent.CFPaymentModes.CARD)
                            .add(CFPaymentComponent.CFPaymentModes.UPI)
                            .build();
            // Replace with your application's theme colors
            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#fc2678")
                    .setNavigationBarTextColor("#ffffff")
                    .setButtonBackgroundColor("#fc2678")
                    .setButtonTextColor("#ffffff")
                    .setPrimaryTextColor("#000000")
                    .setSecondaryTextColor("#000000")
                    .build();
            CFDropCheckoutPayment cfDropCheckoutPayment = new CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                    .setSession(cfSession)
                    .setCFUIPaymentModes(cfPaymentComponent)
                    .setCFNativeCheckoutUITheme(cfTheme)
                    .build();
            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
            gatewayService.doPayment(SubsPlanActivity.this, cfDropCheckoutPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

    public void doPaytm(int FINAL_PRICE) {
    generateCheckSum(FINAL_PRICE);
    }


    private void generateCheckSum(int FINAL_PRICE) {

        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.M_KEY,
                Constants.CHANNEL_ID,
                String.valueOf(FINAL_PRICE),
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );


        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required


        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getPAYTM_MERCHANT_KEY(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+paytm.getOrderId(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {}
        });
    }



    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //use this when using for testing
        PaytmPGService Service = PaytmPGService.getProductionService();

        //use this when using for production
        // PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+paytm.getOrderId());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder((HashMap<String, String>) paramMap);

        //intializing the paytm service
        Service.initialize(order, null);
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {}
            public void onTransactionResponse(Bundle inResponse) {
                getData(inResponse);
            }
            public void networkNotAvailable() {
                Toast.makeText(SubsPlanActivity.this, "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
            }
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(SubsPlanActivity.this, "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
            }
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(SubsPlanActivity.this, "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
            }
            public void onBackPressedCancelTransaction() {
                Toast.makeText(SubsPlanActivity.this, "Transaction cancelled" , Toast.LENGTH_LONG).show();
            }
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(SubsPlanActivity.this, "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    String status,checksum,BANKNAME,TXNID,orderId,txnAmt,txnDate,mid,respCode,paymentMode,bankTxnId,currency,gatewayName,respMsg;
    private void getData(Bundle s) {
        Bundle str=s;

        status=  str.getString("STATUS");
        checksum= str.getString("CHECKSUMHASH");
        BANKNAME=str.getString("BANKNAME");
        TXNID=str.getString("TXNID");
        orderId=  str.getString("ORDERID");
        txnAmt =str.getString("TXNAMOUNT");
        txnDate = str.getString("TXNDATE");
        mid =   str.getString("MID");
        respCode =   str.getString("RESPCODE");
        paymentMode =  str.getString("PAYMENTMODE");
        bankTxnId =  str.getString("BANKTXNID");
        currency =  str.getString("CURRENCY");
        gatewayName =   str.getString("GATEWAYNAME");
        respMsg =  str.getString("RESPMSG");

        if (respCode.equals("01")) {
            postPayment(TXNID);
           // callServicePaymentResponseSave();
        }
        else{
            Toast.makeText(this,respMsg,Toast.LENGTH_SHORT).show();

        }
    }



    public void startAutodebit(int FINAL_PRICE) {

        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.M_KEY,
                Constants.CHANNEL_ID,
                String.valueOf(FINAL_PRICE),
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();



        JSONObject txnAmount = new JSONObject();
        try {
            txnAmount.put("value",paytm.getTxnAmount());
            txnAmount.put("currency", "INR");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("custId", paytm.getCustId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TreeMap<String,String> treemap=new TreeMap<String,String>();

        try {
            //creating a hashmap and adding all the values required


//Step 2:

            treemap.put("requestType", "NATIVE_SUBSCRIPTION");
            treemap.put("mid", paytm.getmId());
            treemap.put("websiteName", paytm.getWebsite());
            treemap.put("orderId", paytm.getOrderId());
            treemap.put("callbackUrl", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+paytm.getOrderId());
            treemap.put("subscriptionAmountType", "FIX");
            treemap.put("subscriptionFrequency", "2");
            treemap.put("subscriptionFrequencyUnit", "MONTH");
            treemap.put("subscriptionExpiryDate", "2031-05-20");
            treemap.put("subscriptionEnableRetry", "1");
            treemap.put("txnAmount", txnAmount.toString());
            treemap.put("userInfo", userInfo.toString());




            body.put("requestType", "NATIVE_SUBSCRIPTION");

            body.put("mid", paytm.getmId());
            body.put("websiteName", paytm.getWebsite());
            body.put("orderId", paytm.getOrderId());
            body.put("callbackUrl", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+paytm.getOrderId());
            body.put("subscriptionAmountType", "FIX");
            body.put("subscriptionFrequency", "2");
            body.put("subscriptionFrequencyUnit", "MONTH");
            body.put("subscriptionExpiryDate", "2031-05-20");
            body.put("subscriptionEnableRetry", "1");
            body.put("txnAmount", txnAmount);
            body.put("userInfo", userInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
         * Generate checksum by parameters we have in body
         * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
         * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
         */
        String checksum = null;
        try {
            checksum = PaytmChecksum.generateSignature(body.toString(), paytm.getPAYTM_MERCHANT_KEY());
       //     checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(paytm.getPAYTM_MERCHANT_KEY(),treemap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject head = new JSONObject();
        try {
            head.put("signature", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            paytmParams.put("body", body);
            paytmParams.put("head", head);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String post_data = paytmParams.toString();

        /* for Staging */
      //  URL url = new URL("https://securegw-stage.paytm.in/subscription/create?mid=YOUR_MID_HERE&orderId=ORDERID_98765");

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/subscription/create?mid=YOUR_MID_HERE&orderId=ORDERID_98765");

        URL url = null;
        try {
            url = new URL("https://securegw.paytm.in/subscription/create?mid=mkqrkg66337193948105&orderId="+paytm.getOrderId());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/subscription/create?mid=YOUR_MID_HERE&orderId=ORDERID_98765");

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);

            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }



    public void fetchPaymentOtions()
    {
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        try {
            body.put("mid", "YOUR_MID_HERE");
            body.put("orderId", "ORDERID_98765");
            body.put("returnToken", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject head = new JSONObject();
        try {
            head.put("tokenType", "TXN_TOKEN");
            head.put("token", "f0bed899539742309eebd8XXXX7edcf61588842333227");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            paytmParams.put("body", body);
            paytmParams.put("head", head);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw-stage.paytm.in/theia/api/v2/fetchPaymentOptions?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /* for Production */
// URL url = new URL("https://securegw.paytm.in/theia/api/v2/fetchPaymentOptions?mid=YOUR_MID_HERE&orderId=ORDERID_98765");

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

}