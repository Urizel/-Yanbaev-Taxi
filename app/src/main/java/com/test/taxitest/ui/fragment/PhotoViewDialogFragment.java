package com.test.taxitest.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.test.taxitest.R;
import com.test.taxitest.network.loaders.ImageLoader;
import com.test.taxitest.network.response.Response;



public class PhotoViewDialogFragment extends DialogFragment
        implements LoaderManager.LoaderCallbacks<Response>, View.OnClickListener {

    public final static String TAG = PhotoViewDialogFragment.class.getCanonicalName();

    ProgressBar mProgressBar;
    ImageView mImageView;
    public static final String ARG_URL = "url";
    private String mUrl;

    public static PhotoViewDialogFragment newInstance(String url) {
        PhotoViewDialogFragment fragment = new PhotoViewDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoViewDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_URL)) {
            mUrl = getArguments().getString(ARG_URL);
        }

        if (mUrl != null) {
            getLoaderManager().restartLoader(R.id.image_loader, ImageLoader.getBundle(mUrl), this);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_view_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mImageView = (ImageView) view.findViewById(R.id.image_view);
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    protected void showImage(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
        mProgressBar.setVisibility(View.GONE);
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
