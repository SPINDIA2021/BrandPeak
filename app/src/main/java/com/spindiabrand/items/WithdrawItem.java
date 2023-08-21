package com.spindiabrand.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "withdraw")
public class WithdrawItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("user")
    public String userName;

    @SerializedName("amount")
    public String amount;

    @SerializedName("upiid")
    public String upiid;

    @SerializedName("date")
    public String date;

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;




    public WithdrawItem(int id, String userName, String amount, String upiid, String date, String status, String msg) {
        this.id = id;
        this.userName = userName;
        this.amount = amount;
        this.upiid = upiid;
        this.date = date;
        this.status = status;
        this.msg = msg;
    }


}
