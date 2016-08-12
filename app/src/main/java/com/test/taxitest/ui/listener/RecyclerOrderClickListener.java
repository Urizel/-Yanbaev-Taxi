package com.test.taxitest.ui.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.test.taxitest.model.Order;
import com.test.taxitest.ui.adapter.OrderRecyclerViewAdapter;

public class RecyclerOrderClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Order order, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerOrderClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            if (view.getAdapter() instanceof OrderRecyclerViewAdapter) {
                int position = view.getChildAdapterPosition(childView);
                if (mListener != null) {
                    mListener.onItemClick(((OrderRecyclerViewAdapter) view.getAdapter()).getOrder(position), position);
                }
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}