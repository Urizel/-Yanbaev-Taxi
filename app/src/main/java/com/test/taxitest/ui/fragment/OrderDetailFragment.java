package com.test.taxitest.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.taxitest.R;
import com.test.taxitest.databinding.OrderDetailBinding;
import com.test.taxitest.model.Order;
import com.test.taxitest.network.loaders.ImageLoader;
import com.test.taxitest.network.response.Response;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class OrderDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Response>, View.OnClickListener {

    public static final String ARG_ORDER = "order";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault());

    private Order mOrder;
    private OrderDetailBinding binding;

    public static Bundle getBundle(Order order) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(OrderDetailFragment.ARG_ORDER, order);
        return arguments;
    }

    public static OrderDetailFragment newInstance(Bundle arguments) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static OrderDetailFragment newInstance(Order order) {
        return newInstance(getBundle(order));
    }

    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ORDER)) {
            mOrder = getArguments().getParcelable(ARG_ORDER);
        }

        if (mOrder != null && mOrder.getVehicle() != null) {
            String imageName = mOrder.getVehicle().getPhoto();
            getLoaderManager().restartLoader(R.id.image_loader, ImageLoader.getBundle(imageName), this);
        }
    }

    // XXX Purpose?
    @Override
    public void onClick(View view) {
        if (mOrder == null || mOrder.getVehicle() == null)
            return;
        PhotoViewDialogFragment photoViewDialogFragment = PhotoViewDialogFragment.newInstance(mOrder.getVehicle().getPhoto());
        photoViewDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PhotoDialogTheme);
        photoViewDialogFragment.show(getFragmentManager(), PhotoViewDialogFragment.TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) this.getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_order_detail_args, mOrder.getId()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = OrderDetailBinding.inflate(inflater, container, false);
        binding.setDateFormat(dateFormat);
        binding.setOrder(mOrder);
        binding.imageView.setOnClickListener(this);
        return binding.getRoot();
    }

    // XXX Protected?
    protected void showImage(Bitmap bitmap) {
        binding.imageView.setImageBitmap(bitmap);
    }

    @Override
    public Loader<Response> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.image_loader:
                return new ImageLoader(getContext(), args);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Response> loader, Response data) {
        int id = loader.getId();
        if (id == R.id.image_loader) {
            switch (data.getRequestResult()) {
                case Response.OK:
                    Bitmap bitmap = data.getTypedAnswer();
                    if (bitmap != null) {
                        showImage(bitmap);
                    }
                    break;
                case Response.BAD_REQUEST:
                case Response.CREATED:
                case Response.ERROR_CONNECTION:
                case Response.NOT_FOUND:
                case Response.SERVER_ERROR:
                case Response.UNAUTHORIZED:
                    break;
            }
            getLoaderManager().destroyLoader(id);
        }
    }

    @Override
    public void onLoaderReset(Loader<Response> loader) {
    }

}
