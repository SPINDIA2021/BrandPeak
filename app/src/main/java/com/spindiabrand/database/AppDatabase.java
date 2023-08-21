package com.spindiabrand.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.spindiabrand.items.AppInfo;
import com.spindiabrand.items.AppVersion;
import com.spindiabrand.items.BusinessCategoryItem;
import com.spindiabrand.items.BusinessItem;
import com.spindiabrand.items.BusinessSubCategoryItem;
import com.spindiabrand.items.CategoryItem;
import com.spindiabrand.items.CustomCategory;
import com.spindiabrand.items.CustomModel;
import com.spindiabrand.items.DynamicFrameItem;
import com.spindiabrand.items.EarningItem;
import com.spindiabrand.items.FestivalItem;
import com.spindiabrand.items.FrameCategoryItem;
import com.spindiabrand.items.HomeItem;
import com.spindiabrand.items.ItemVcard;
import com.spindiabrand.items.LanguageItem;
import com.spindiabrand.items.MainStrModel;
import com.spindiabrand.items.NewsItem;
import com.spindiabrand.items.OfferItem;
import com.spindiabrand.items.PersonalItem;
import com.spindiabrand.items.PostItem;
import com.spindiabrand.items.ProductCatItem;
import com.spindiabrand.items.ProductItem;
import com.spindiabrand.items.ProductModel;
import com.spindiabrand.items.ReferDetail;
import com.spindiabrand.items.ReferWithdrawalDetail;
import com.spindiabrand.items.StickerCategory;
import com.spindiabrand.items.StickerItem;
import com.spindiabrand.items.StickerModel;
import com.spindiabrand.items.StoryItem;
import com.spindiabrand.items.SubjectItem;
import com.spindiabrand.items.SubsPlanItem;
import com.spindiabrand.items.UserFrame;
import com.spindiabrand.items.UserItem;
import com.spindiabrand.items.UserLogin;
import com.spindiabrand.items.WithdrawItem;

@Database(entities = {StoryItem.class, FestivalItem.class, CategoryItem.class, PostItem.class,
        LanguageItem.class, UserItem.class,
        UserLogin.class, BusinessItem.class, SubsPlanItem.class,
        SubjectItem.class, NewsItem.class, AppVersion.class, AppInfo.class, CustomCategory.class, HomeItem.class,
        BusinessCategoryItem.class, CustomModel.class, UserFrame.class, ItemVcard.class,
        StickerItem.class, StickerCategory.class, StickerModel.class, MainStrModel.class, OfferItem.class,
        DynamicFrameItem.class, ProductCatItem.class, ProductItem.class, ProductModel.class, ReferDetail.class, ReferWithdrawalDetail.class, WithdrawItem.class,
        EarningItem.class, BusinessSubCategoryItem.class, PersonalItem.class, FrameCategoryItem.class}, version = 32, exportSchema = false)
@TypeConverters({DataConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "festival_database";

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }

    public abstract StoryDao getStoryDao();

    public abstract FestivalDao getFestivalDao();

    public abstract CategoryDao getCategoryDao();

    public abstract PostDao getPostDao();

    public abstract LanguageDao getLanguageDao();

    public abstract UserDao getUserDao();

    public abstract BusinessDao getBusinessDao();

    public abstract SubsPlanDao getSubsPlanDao();

    public abstract NewsDao getNewsDao();

    public abstract UserLoginDao getUserLoginDao();

    public abstract CustomCategoryDao getCustomCategoryDao();

    public abstract HomeDao getHomeDao();

    public abstract VCardDao getVCardDao();

    public abstract FrameDao getFrameDao();

    public abstract ProductDao getProductDao();
}

