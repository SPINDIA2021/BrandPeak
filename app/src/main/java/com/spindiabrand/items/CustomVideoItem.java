package com.spindiabrand.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "category", primaryKeys = "id")
public class CustomVideoItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("status")
    public boolean status;

    @SerializedName("url")
    public String url;


    public CustomVideoItem(int id, boolean status, String url) {
        this.id = id;
        this.status = status;
        this.url = url;

    }

    @Override
    public String toString() {
        return "CustomVideoItem{" +
                "id='" + id + '\'' +
                ", name='" + status + '\'' +
                ", url=" + url +
                '}';
    }
}


