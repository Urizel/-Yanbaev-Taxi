package com.test.taxitest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Vehicle implements Parcelable {

    @SerializedName("regNumber")
    protected String mRegNumber;

    @SerializedName("modelName")
    protected String mModelName;

    @SerializedName("photo")
    protected String mPhoto;

    @SerializedName("driverName")
    protected String mDriverName;

    public Vehicle(String regNumber, String modelName, String photo, String driverName) {
        this.mRegNumber = regNumber;
        this.mModelName = modelName;
        this.mPhoto = photo;
        this.mDriverName = driverName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;

        Vehicle other = (Vehicle) o;

        return TextUtils.equals(mRegNumber, other.mRegNumber) &&
                TextUtils.equals(mModelName, other.mModelName) &&
                TextUtils.equals(mPhoto, other.mPhoto) &&
                TextUtils.equals(mDriverName, other.mDriverName);
    }

    @Override
    public int hashCode() {
        int result = mRegNumber != null ? mRegNumber.hashCode() : 0;
        result = 31 * result + (mModelName != null ? mModelName.hashCode() : 0);
        result = 31 * result + (mPhoto != null ? mPhoto.hashCode() : 0);
        result = 31 * result + (mDriverName != null ? mDriverName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  TextUtils.join(" ", new Object[] {Vehicle.class.getSimpleName(), new Gson().toJson(this)});
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] strings = new String[] {
                mRegNumber,
                mModelName,
                mPhoto,
                mDriverName
        };
        dest.writeStringArray(strings);
    }

    public static final Parcelable.Creator<Vehicle> CREATOR = new Parcelable.Creator<Vehicle>() {
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    private Vehicle(Parcel parcel) {
        String[] strings = new String[4];
        // XXX why array?
        parcel.readStringArray(strings);

        mRegNumber = strings[0];
        mModelName = strings[1];
        mPhoto = strings[2];
        mDriverName = strings[3];
    }

    public static boolean equals(Vehicle a, Vehicle b) {
        return a != null ? a.equals(b) : b == null;
    }

    public String getRegNumber() {
        return mRegNumber;
    }

    public String getModelName() {
        return mModelName;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public String getDriverName() {
        return mDriverName;
    }
}
