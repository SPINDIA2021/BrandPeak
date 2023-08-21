package com.spindiabrand.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spindiabrand.items.BusinessCategoryItem;
import com.spindiabrand.items.CategoryItem;
import com.spindiabrand.items.CustomCategory;
import com.spindiabrand.items.CustomInModel;
import com.spindiabrand.items.CustomVideoItem;
import com.spindiabrand.items.EarningItem;
import com.spindiabrand.items.FeatureItem;
import com.spindiabrand.items.FestivalItem;
import com.spindiabrand.items.PostItem;
import com.spindiabrand.items.ProductCatItem;
import com.spindiabrand.items.ProductItem;
import com.spindiabrand.items.StickerCategory;
import com.spindiabrand.items.StickerItem;
import com.spindiabrand.items.StickerModel;
import com.spindiabrand.items.StoryItem;
import com.spindiabrand.items.WithdrawItem;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class DataConverters implements Serializable {

    @TypeConverter // note this annotation
    public String fromOptionValuesList(List<String> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<String> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromStickers(List<StickerItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StickerItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<StickerItem> toStickers(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StickerItem>>() {
        }.getType();
        List<StickerItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromEarning(List<EarningItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<EarningItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<EarningItem> toEarning(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<EarningItem>>() {
        }.getType();
        List<EarningItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromWithdraw(List<WithdrawItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<WithdrawItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<WithdrawItem> toWithdraw(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<WithdrawItem>>() {
        }.getType();
        List<WithdrawItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromStickerCate(List<StickerCategory> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StickerCategory>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<StickerCategory> toStickerCate(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StickerCategory>>() {
        }.getType();
        List<StickerCategory> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromProductCat(List<ProductCatItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductCatItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<ProductCatItem> toProductCat(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductCatItem>>() {
        }.getType();
        List<ProductCatItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromProduct(List<ProductItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<ProductItem> toProduct(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductItem>>() {
        }.getType();
        List<ProductItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }


    @TypeConverter // note this annotation
    public String fromStickerModel(List<StickerModel> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StickerModel>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<StickerModel> toStickerModel(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StickerModel>>() {
        }.getType();
        List<StickerModel> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromStory(List<StoryItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StoryItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<StoryItem> toStory(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StoryItem>>() {
        }.getType();
        List<StoryItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromFestival(List<FestivalItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<FestivalItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<FestivalItem> toFestival(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<FestivalItem>>() {
        }.getType();
        List<FestivalItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }



    @TypeConverter // note this annotation
    public String fromBusinessCategory(List<BusinessCategoryItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BusinessCategoryItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<BusinessCategoryItem> toBusinessCategory(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BusinessCategoryItem>>() {
        }.getType();
        List<BusinessCategoryItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }


    @TypeConverter // note this annotation
    public String fromCategory(List<CategoryItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CategoryItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<CategoryItem> toCategory(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CategoryItem>>() {
        }.getType();
        List<CategoryItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }


    @TypeConverter // note this annotation
    public String fromCustomVideo(List<CustomVideoItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomVideoItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<CustomVideoItem> toCustomVideo(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomVideoItem>>() {
        }.getType();
        List<CustomVideoItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }





    @TypeConverter // note this annotation
    public String fromPost(List<PostItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<PostItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<PostItem> toPost(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<PostItem>>() {
        }.getType();
        List<PostItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }


    @TypeConverter // note this annotation
    public String fromFeature(List<FeatureItem> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<FeatureItem>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<FeatureItem> toFeature(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<FeatureItem>>() {
        }.getType();
        List<FeatureItem> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromCustomModel(List<CustomCategory> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomCategory>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<CustomCategory> toCustomModel(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomCategory>>() {
        }.getType();
        List<CustomCategory> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromCustomInModel(List<CustomInModel> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomInModel>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<CustomInModel> toCustomInModel(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomInModel>>() {
        }.getType();
        List<CustomInModel> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

}
