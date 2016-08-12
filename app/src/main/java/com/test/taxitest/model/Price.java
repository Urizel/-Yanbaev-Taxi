package com.test.taxitest.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.Currency;

public class Price implements Parcelable {

    @SerializedName("amount")
    protected long mAmount;

    @SerializedName("currency")
    protected String mCurrency;

    private transient String _StringFormat = null;

    public Price(long amount, String currency) {
        this.mCurrency = currency;
        this.mAmount = amount;
    }

    public String getStringFormat() {
        if (TextUtils.isEmpty(_StringFormat)) {
            Currency currency = Currency.getInstance(mCurrency);
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(currency.getDefaultFractionDigits());
            format.setCurrency(currency);
            _StringFormat = format.format(mAmount / Math.pow(10, currency.getDefaultFractionDigits()));
        }
        return _StringFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;

        Price other = (Price) o;

        return mAmount == other.mAmount &&
                TextUtils.equals(mCurrency, other.mCurrency);

    }

    @Override
    public int hashCode() {
        int result = (int) (mAmount ^ (mAmount >>> 32));
        result = 31 * result + (mCurrency != null ? mCurrency.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return  TextUtils.join(" ", new Object[] {Price.class.getSimpleName(), new Gson().toJson(this)});
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mAmount);
        dest.writeString(mCurrency);
    }

    public static final Parcelable.Creator<Price> CREATOR = new Parcelable.Creator<Price>() {
        public Price createFromParcel(Parcel in) {
            return new Price(in);
        }

        public Price[] newArray(int size) {
            return new Price[size];
        }
    };

    private Price(Parcel parcel) {
        mAmount = parcel.readLong();
        mCurrency = parcel.readString();
    }

    public static boolean equals(Price a, Price b) {
        return a != null ? a.equals(b) : b == null;
    }

}
