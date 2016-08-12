package com.test.taxitest.model.comparator;

import com.test.taxitest.model.Order;

import java.util.Comparator;

public class OrderDescendComparator implements Comparator<Order> {


    @Override
    public int compare(Order lhs, Order rhs) {
        return rhs.getOrderTime().compareTo(lhs.getOrderTime());
    }

    @Override
    public Comparator<Order> reversed() {
        return new OrderAscendComparator();
    }
}