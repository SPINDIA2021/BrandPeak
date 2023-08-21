package com.spindiabrand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.spindiabrand.api.common.common.Resource;
import com.spindiabrand.items.LanguageItem;
import com.spindiabrand.repository.LanguageRepository;
import com.spindiabrand.utils.AbsentLiveData;
import com.spindiabrand.utils.Constant;
import com.spindiabrand.utils.PrefManager;

import java.util.List;

public class LanguageViewModel extends AndroidViewModel {

    public LanguageRepository repository;
    public MutableLiveData<String> languageObj = new MutableLiveData<>();
    LiveData<Resource<List<LanguageItem>>> languages;
    PrefManager prefManager;

    public LanguageViewModel(@NonNull Application application) {
        super(application);

        repository = new LanguageRepository(application);
        prefManager = new PrefManager(application);

        languages = Transformations.switchMap(languageObj, obj->{
            if(obj==null){
                return AbsentLiveData.create();
            }
            return repository.getLanguages(prefManager.getString(Constant.api_key));
        });
    }

    public LiveData<Resource<List<LanguageItem>>> getLanguages() {
        return languages;
    }
    public void setLanguageObj(){
        languageObj.setValue("PS");
    }
}
