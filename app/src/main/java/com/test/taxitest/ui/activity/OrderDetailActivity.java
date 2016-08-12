package com.test.taxitest.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.test.taxitest.R;
import com.test.taxitest.ui.fragment.OrderDetailFragment;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            OrderDetailFragment fragment = OrderDetailFragment.newInstance(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.order_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_prev, R.anim.slide_out_prev);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, OrderListActivity.class));
            overridePendingTransition(R.anim.slide_in_prev, R.anim.slide_out_prev);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
