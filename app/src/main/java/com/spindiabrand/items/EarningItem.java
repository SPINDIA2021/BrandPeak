package com.spindiabrand.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "earning")
public class EarningItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("user")
    public String userName;

    @SerializedName("amount")
    public String amount;

    @SerializedName("date")
    public String date;

    @SerializedName("credit")
    public String credit;

    @SerializedName("debit")
    public String debit;

    @SerializedName("cur_balance")
    public String cur_balance;




    public EarningItem(int id, String userName, String amount, String date, String credit, String debit, String cur_balance) {
        this.id = id;
        this.userName = userName;
        this.amount = amount;
        this.date = date;
        this.credit = credit;
        this.debit = debit;
        this.cur_balance = cur_balance;
    }


}
