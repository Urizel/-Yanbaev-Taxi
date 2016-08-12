package com.test.taxitest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;


import com.test.taxitest.R;
import com.test.taxitest.ui.fragment.OrderDetailFragment;
import com.test.taxitest.ui.adapter.OrderRecyclerViewAdapter;
import com.test.taxitest.network.loaders.OrdersLoader;
import com.test.taxitest.model.Order;
import com.test.taxitest.network.response.Response;
import com.test.taxitest.ui.decorations.DividerItemDecoration;
import com.test.taxitest.ui.decorations.EndOffsetItemDecoration;
import com.test.taxitest.ui.listener.RecyclerOrderClickListener;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Response>,
            RecyclerOrderClickListener.OnItemClickListener,
            SwipeRefreshLayout.OnRefreshListener {


    private final static String TAG = OrderListActivity.class.getSimpleName();
    public static final String ARG_ORDER_LIST = "orders";

    private boolean mTwoPane;
    private OrderRecyclerViewAdapter adapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_order_list));
        setSupportActionBar(toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        List<Order> orders;
        if (savedInstanceState == null) {
            orders = null;
            getSupportLoaderManager().initLoader(R.id.orders_loader, Bundle.EMPTY, this);
            setRefreshing(true);
        } else {
            orders = savedInstanceState.getParcelableArrayList(ARG_ORDER_LIST);
            if (getSupportLoaderManager().getLoader(R.id.orders_loader) != null) {
                getSupportLoaderManager().restartLoader(R.id.orders_loader, Bundle.EMPTY, this);
            }
        }
        if (findViewById(R.id.order_detail_container) != null) {
            mTwoPane = true;
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.order_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView, orders);
    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(R.id.orders_loader, Bundle.EMPTY, this);
    }


    public void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void onItemClick(Order order, int position) {
        Bundle arguments = OrderDetailFragment.getBundle(order);
        if (mTwoPane) {
            OrderDetailFragment fragment = OrderDetailFragment.newInstance(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.order_detail_container, fragment)
                    .commit();
        } else {
            startActivity(new Intent(this, OrderDetailActivity.class).putExtras(arguments));
            overridePendingTransition(R.anim.slide_in_next,R.anim.slide_out_next);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView, @Nullable List<Order> orders) {
        recyclerView.addOnItemTouchListener(new RecyclerOrderClickListener(this, this));
        if (orders == null) {
            orders = new ArrayList<>();
        }
        adapter = new OrderRecyclerViewAdapter(orders);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.divider_transparent)));
        recyclerView.addItemDecoration(new EndOffsetItemDecoration(10));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARG_ORDER_LIST, adapter.getOrders());
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Response> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.orders_loader:
                return new OrdersLoader(this);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Response> loader, Response data) {
        int id = loader.getId();
        if (id == R.id.orders_loader) {
            switch (data.getRequestResult()) {
                case Response.OK:
                    List<Order> orders = data.getTypedAnswer();
                    adapter.swapOrders(orders);
                    getSupportLoaderManager().destroyLoader(id);
                    break;
                case Response.BAD_REQUEST:
                case Response.CREATED:
                case Response.ERROR_CONNECTION:
                case Response.NOT_FOUND:
                case Response.SERVER_ERROR:
                case Response.UNAUTHORIZED:
                    Snackbar.make(findViewById(R.id.order_list), R.string.error_orders_loading, Snackbar.LENGTH_LONG).show();
                    break;
            }
            setRefreshing(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<Response> loader) {
    }

}
