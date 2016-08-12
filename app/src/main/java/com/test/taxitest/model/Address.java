package com.test.taxitest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


public class Address implements Parcelable {
    @SerializedName("city")
    protected String mCity;

    @SerializedName("address")
    protected String mAddress;

    public Address(String city, String address) {
        this.mCity = city;
        this.mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public String getAddress() {
        return mAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address other = (Address) o;

        return TextUtils.equals(mCity, other.mCity) &&
                TextUtils.equals(mAddress, other.mAddress);

    }

    @Override
    public int hashCode() {
        int result = mCity != null ? mCity.hashCode() : 0;
        result = 31 * result + (mAddress != null ? mAddress.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  TextUtils.join(" ", new Object[] {Address.class.getSimpleName(), new Gson().toJson(this)});
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCity);
        dest.writeString(mAddress);
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    private Address(Parcel parcel) {
        mCity = parcel.readString();
        mAddress = parcel.readString();
    }

    public static boolean equals(Address a, Address b) {
        return a != null ? a.equals(b) : b == null;
    }


}
