package com.test.taxitest.model.comparator;

import com.test.taxitest.model.Order;

import java.util.Comparator;

public class OrderAscendComparator implements Comparator<Order> {


    @Override
    public int compare(Order lhs, Order rhs) {
        return lhs.getOrderTime().compareTo(rhs.getOrderTime());
    }

    @Override
    public Comparator<Order> reversed() {
        return new OrderDescendComparator();
    }
}
