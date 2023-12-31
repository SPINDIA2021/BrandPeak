package com.spindiabrand.ui.activities;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.spindiabrand.BuildConfig;
import com.spindiabrand.R;
import com.spindiabrand.adapters.EarningAdapter;
import com.spindiabrand.adapters.WithdrawalAdapter;
import com.spindiabrand.databinding.ActivityReferralBinding;
import com.spindiabrand.databinding.DialogEarningBinding;
import com.spindiabrand.databinding.DialogWithdrawBinding;
import com.spindiabrand.items.ReferDetail;
import com.spindiabrand.items.ReferWithdrawalDetail;
import com.spindiabrand.items.UserLogin;
import com.spindiabrand.ui.dialog.DialogMsg;
import com.spindiabrand.utils.Constant;
import com.spindiabrand.utils.PrefManager;
import com.spindiabrand.utils.Util;
import com.spindiabrand.viewmodel.UserViewModel;

public class ReferralActivity extends AppCompatActivity {

    ActivityReferralBinding binding;
    UserViewModel userViewModel;
    PrefManager prefManager;
    EarningAdapter earningAdapter;
    WithdrawalAdapter withdrawalAdapter;
    int currentBalance;
    ProgressDialog prgDialog;
    DialogMsg dialogMsg;
    String referCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        prgDialog = new ProgressDialog(this);
        dialogMsg = new DialogMsg(this, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUserDataById().observe(this, listResource -> {
            if (listResource != null) {
                Util.showLog("Got Data "
                        + listResource.message +
                        listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {

                            setWalletShowOrNotData(new UserLogin(listResource.data.userId, true, listResource.data));
                        }

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            setWalletShowOrNotData(new UserLogin(listResource.data.userId, true, listResource.data));
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


        userViewModel.getReferWithdrawalDetail(prefManager.getString(Constant.USER_ID), "IQ").observe(this, resource -> {
            if (resource != null) {

                switch (resource.status) {
                    case LOADING:
                        if (resource.data != null) {
                            setWithdrawalData(resource.data);
                        }
                        break;
                    case SUCCESS:
                        if (resource.data != null) {
                            setWithdrawalData(resource.data);
                        }
                        break;
                    case ERROR:
                        break;
                    default:
                        break;
                }

            }
        });

        setUI();
        getData("IQ");
    }

    private void getData(String obj) {
        userViewModel.getReferDetail(prefManager.getString(Constant.USER_ID), obj).observe(this, resource -> {
            if (resource != null) {

                switch (resource.status) {
                    case LOADING:
                        if (resource.data != null) {
                            setData(resource.data);
                        }
                        break;
                    case SUCCESS:
                        if (resource.data != null) {
                            setData(resource.data);
                        }
                        break;
                    case ERROR:
                        break;
                    default:
                        break;
                }

            }
        });




    }




    private void setData(ReferDetail data) {
        currentBalance = Integer.parseInt(data.currentBalance);
        referCode = data.referralCode;
        binding.tvCurrentBalance.setText("₹ " + data.currentBalance);
        binding.tvTotalEarning.setText("₹ " + data.totalBalance);
        binding.tvReferralLink.setText(data.referralCode);
        binding.tvLogin.setText("Rs. " + prefManager.getString(Constant.REFER_LOGIN_POINT) + " On Login Time");
        binding.tvSubs.setText("Rs. " + prefManager.getString(Constant.REFER_SUBS_POINT) + " On Premium Purchase");
        binding.tvTotalRegister.setText(data.totalReferUser);
        binding.tvTotalPurchase.setText(data.totalSubscriptionUsingRefer);

        earningAdapter = new EarningAdapter(this, data.earningHistory);
    }



    private void setWithdrawalData(ReferWithdrawalDetail data) {

        withdrawalAdapter = new WithdrawalAdapter(this, data.withdrawalHistory);
    }

    private void setUI() {
        binding.toolbar.toolName.setText("Refer & Earn");
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.linearWalletHistory.setOnClickListener(v -> {
            if (earningAdapter != null) {
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogEarningBinding binding = DialogEarningBinding.inflate(getLayoutInflater());
                dialog.setContentView(binding.getRoot());
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setAttributes(getLayoutParams(dialog));

                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                }
                dialog.setCancelable(false);

                binding.rvEarning.setAdapter(earningAdapter);
                dialog.show();
                binding.ivCancel.setOnClickListener(vr -> {
                    dialog.dismiss();
                });
            }
        });

        binding.linearWithdrawalHistory.setOnClickListener(v -> {
            if (withdrawalAdapter != null) {
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogWithdrawBinding binding = DialogWithdrawBinding.inflate(getLayoutInflater());
                dialog.setContentView(binding.getRoot());
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setAttributes(getLayoutParams(dialog));

                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                }
                dialog.setCancelable(false);

                binding.rvWithdraw.setAdapter(withdrawalAdapter);
                dialog.show();
                binding.ivCancel.setOnClickListener(vr -> {
                    dialog.dismiss();
                });
            }
        });

        binding.linearWithdrawal.setOnClickListener(v -> {
            int limit = Integer.parseInt(prefManager.getString(Constant.WITHDRAW_POINT));
            if (currentBalance >= limit) {

                if (binding.etUpiId.getText().toString().isEmpty()) {
                    binding.etUpiId.setError("Please Enter Your UPI Id");
                    binding.etUpiId.requestFocus();
                    return;
                }

                prgDialog.show();
                userViewModel.setWithdrawalRequest(prefManager.getString(Constant.USER_ID), binding.etUpiId.getText().toString(),
                        currentBalance).observe(this, result -> {
                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:
                                prgDialog.cancel();
                                dialogMsg.showSuccessDialog("Successfully Place The Withdrawal Request, We will pay in 48 hours", getString(R.string.ok));
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogMsg.cancel();
                                        getData("NEW");
                                    }
                                });
                                break;

                            case ERROR:
                                prgDialog.cancel();
                                dialogMsg.showErrorDialog(getString(R.string.fail_message_contact), getString(R.string.ok));
                                dialogMsg.show();
                                break;
                        }
                    }
                });

            } else {
                Util.showToast(this, "You Have Required Minimum Rs. " + limit + " Balance");
            }
        });

        binding.tvAddUpiid.setOnClickListener(v -> {
            if (binding.etUpiId.getText().toString().isEmpty()) {
                binding.etUpiId.setError("Please Enter Your UPI Id");
                binding.etUpiId.requestFocus();
                return;
            }
            prefManager.setString("UPI_ID", binding.etUpiId.getText().toString());
            Util.showAlertBox(ReferralActivity.this, "You UPI Id is Saved, Send Withdraw Request",null);
            binding.tvAddUpiid.setVisibility(GONE);
        });
        binding.etUpiId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tvAddUpiid.setVisibility(View.VISIBLE);
            }
        });


        if (!prefManager.getString("UPI_ID").equals("")) {
            binding.etUpiId.setText(prefManager.getString("UPI_ID"));
            binding.tvAddUpiid.setVisibility(GONE);
        }

        binding.lvCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("ReferCode", binding.tvReferralLink.getText());
            clipboard.setPrimaryClip(clip);
            clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    Util.showToast(ReferralActivity.this, "Copy: " + binding.tvReferralLink.getText());
                }
            });
        });

        binding.ivWhatsapp.setOnClickListener(v -> {
            shareWith("whtsapp");
        });

        binding.ivInstagram.setOnClickListener(v -> {
            shareWith("insta");
        });
        binding.ivMore.setOnClickListener(v -> {
            shareWith("other");
        });

        binding.linearInviteFriends.setOnClickListener(v -> {
            shareWith("other");
        });

        binding.tvReadReferralPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(ReferralActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.REFUND_POLICY);
            startActivity(intent);
        });
    }

    public void shareWith(String shareTo) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            switch (shareTo) {
                case "whtsapp":
                    shareIntent.setPackage("com.whatsapp");
                    break;
                case "fb":
                    shareIntent.setPackage("com.facebook.katana");
                    break;
                case "insta":
                    shareIntent.setPackage("com.instagram.android");
                    break;
                case "twter":
                    shareIntent.setPackage("com.twitter.android");
                    break;
            }
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Refer User");
            String shareMessage = "Hi Friends,\n" +
                    "I am using Most Trustable Branding App for my Business Marketing & Promotion. \n \n " +
                    "Download App Now: " + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n"
                    + "Register With My Refer Code: " + referCode;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
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


    private void setWalletShowOrNotData(UserLogin data) {

        if (data.user.isSubscribed) {
            binding.linearWalletHistory.setVisibility(View.VISIBLE);
        }else {
            binding.linearWalletHistory.setVisibility(View.GONE);
        }
    }
}