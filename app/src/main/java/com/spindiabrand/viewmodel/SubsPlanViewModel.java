package com.spindiabrand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.spindiabrand.api.ApiStatus;
import com.spindiabrand.api.common.common.Resource;
import com.spindiabrand.items.CouponItem;
import com.spindiabrand.items.SubsPlanItem;
import com.spindiabrand.repository.SubsPlanRepository;
import com.spindiabrand.utils.AbsentLiveData;
import com.spindiabrand.utils.Constant;
import com.spindiabrand.utils.PrefManager;

import java.util.List;

public class SubsPlanViewModel extends AndroidViewModel {

    private SubsPlanRepository subsPlanRepository;
    private MutableLiveData<String> postObj = new MutableLiveData<>();
    private MutableLiveData<String> paymentObj = new MutableLiveData<>();

    private MutableLiveData<TmpDataHolder> couponObj = new MutableLiveData<>();
    public LiveData<Resource<CouponItem>>couponData = new MutableLiveData<>();
    PrefManager prefManager;

    public SubsPlanViewModel(@NonNull Application application) {
        super(application);
        subsPlanRepository = new SubsPlanRepository(application);
        prefManager = new PrefManager(application);

        couponData = Transformations.switchMap(couponObj, obj -> {
            if(obj==null){
                return AbsentLiveData.create();
            }
            return subsPlanRepository.checkCoupon(prefManager.getString(Constant.api_key), obj.userId, obj.couponCode);
        });
    }

    public LiveData<Resource<List<SubsPlanItem>>> getSubsPlanItems() {
        postObj.setValue("PS");
        return Transformations.switchMap(postObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return subsPlanRepository.getSubsPlanItems(prefManager.getString(Constant.api_key));
        });
    }

    public LiveData<Resource<ApiStatus>> loadPayment(String userId, String planId, String paymentId, String planPrice,
                                                     String couponCode, String referralCode, String type) {
        paymentObj.setValue("PS");
        return Transformations.switchMap(paymentObj, obj -> {
            if(obj==null){
                return AbsentLiveData.create();
            }
            return subsPlanRepository.loadPayment(prefManager.getString(Constant.api_key), userId, planId, paymentId,
                    planPrice, couponCode, referralCode, type);
        });
    }

    public LiveData<Resource<CouponItem>> checkCoupon() {
        return couponData;
    }

    public void setCouponObj(String userId, String couponCode){
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.userId = userId;
        tmpDataHolder.couponCode = couponCode;
        couponObj.setValue(tmpDataHolder);
    }

    public class TmpDataHolder{
        public String userId = "";
        public String couponCode = "";
    }
}
