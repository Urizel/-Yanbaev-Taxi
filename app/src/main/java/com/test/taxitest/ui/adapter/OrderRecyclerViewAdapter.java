package com.test.taxitest.ui.adapter;


import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.test.taxitest.databinding.OrderListContentBinding;
import com.test.taxitest.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    private final List<Order> mOrders;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM HH:mm", Locale.getDefault());

    public OrderRecyclerViewAdapter(@NonNull List<Order> orders) {
        setHasStableIds(true);
        mOrders = orders;
    }

    @Override
    public final long getItemId(int position) {
        return mOrders.get(position).getId();
    }

    public void swapOrders(@Nullable List<Order> orders) {
        if (orders != null) {
            mOrders.clear();
            mOrders.addAll(orders);
            notifyDataSetChanged();
        }
    }

    public ArrayList<Order> getOrders() {
        return new ArrayList<>(mOrders);
    }

    public Order getOrder(int position) {
        return mOrders.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderListContentBinding binding = OrderListContentBinding.inflate(inflater, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.binding.setDateFormat(dateFormat);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setOrder(getOrder(position));
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OrderListContentBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);

        }
    }
}