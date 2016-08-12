package com.test.taxitest.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.test.taxitest.network.NetworkFactory;

import java.util.Date;

public class Order implements Parcelable {

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    protected long id;
    protected Address startAddress;
    protected Address endAddress;
    protected Price price;
    protected Date orderTime;
    protected Vehicle vehicle;

    public Order(long id, Address startAddress, Address endAddress, Price price, Date orderTime, Vehicle vehicle) {
        this.id = id;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.price = price;
        this.orderTime = orderTime;
        this.vehicle = vehicle;
    }



    public long getId() {
        return id;
    }

    public Address getStartAddress() {
        return startAddress;
    }

    public Address getEndAddress() {
        return endAddress;
    }

    public Price getPrice() {
        return price;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order other = (Order) o;

        return id == other.id &&
                orderTime != null ? orderTime.equals(other.orderTime) : other.orderTime == null &&
                Address.equals(startAddress, other.startAddress) &&
                Address.equals(endAddress, other.endAddress) &&
                Price.equals(price, other.price) &&
                Vehicle.equals(vehicle, other.vehicle);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (startAddress != null ? startAddress.hashCode() : 0);
        result = 31 * result + (endAddress != null ? endAddress.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (orderTime != null ? orderTime.hashCode() : 0);
        result = 31 * result + (vehicle != null ? vehicle.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return TextUtils.join(" ", new Object[] {Order.class.getSimpleName(), NetworkFactory.GSON.toJson(this)});
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(startAddress, flags);
        dest.writeParcelable(endAddress, flags);
        dest.writeParcelable(price, flags);
        dest.writeParcelable(vehicle, flags);
        dest.writeInt(orderTime == null ? 0 : 1);
        if (orderTime != null) {
            dest.writeLong(orderTime.getTime());
        }
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    private Order(Parcel parcel) {
        id = parcel.readLong();
        startAddress = parcel.readParcelable(Address.class.getClassLoader());
        endAddress = parcel.readParcelable(Address.class.getClassLoader());
        price = parcel.readParcelable(Price.class.getClassLoader());
        vehicle = parcel.readParcelable(Vehicle.class.getClassLoader());
        int flagNull = parcel.readInt();
        orderTime = flagNull == 1 ? new Date(parcel.readLong()) : null;
    }
}
