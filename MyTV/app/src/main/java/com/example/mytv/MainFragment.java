package com.example.mytv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MainFragment extends BrowseSupportFragment {

    private ArrayObjectAdapter rowsAdapter;
    private BackgroundManager backgroundManager;
    private Target backgroundTarget;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupUi();
        setupBackgroundManager();
        loadRows();
        setupEventListeners();
    }

    private void setupUi() {
        setTitle(getString(R.string.browse_title));

        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // Set brand color (màu chủ đạo của app)
        setBrandColor(Color.parseColor("#0D47A1"));
        // Set search icon color
        setSearchAffordanceColor(Color.WHITE);
    }

    private void setupBackgroundManager() {
        backgroundManager = BackgroundManager.getInstance(requireActivity());
        backgroundManager.attach(requireActivity().getWindow());
        
        backgroundTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                backgroundManager.setBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                backgroundManager.setDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                    backgroundManager.setDrawable(placeHolderDrawable);
                }
            }
        };
    }

    private void loadRows() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();

        List<Movie> movies = MovieList.getSeries();

        // Row 1: ErrorFragment (Header mẫu)
        HeaderItem errorHeader = new HeaderItem(0, "ErrorFragment");
        rowsAdapter.add(new ListRow(errorHeader, new ArrayObjectAdapter(cardPresenter)));

        // Row 2: New Movies
        ArrayObjectAdapter newMoviesAdapter = new ArrayObjectAdapter(cardPresenter);
        newMoviesAdapter.addAll(0, movies);
        HeaderItem newMoviesHeader = new HeaderItem(1, "New Movies");
        rowsAdapter.add(new ListRow(newMoviesHeader, newMoviesAdapter));

        // Row 3: Old Movies
        ArrayObjectAdapter oldMoviesAdapter = new ArrayObjectAdapter(cardPresenter);
        oldMoviesAdapter.addAll(0, movies);
        HeaderItem oldMoviesHeader = new HeaderItem(2, "Old Movies");
        rowsAdapter.add(new ListRow(oldMoviesHeader, oldMoviesAdapter));

        // Row 4: Series
        ArrayObjectAdapter seriesAdapter = new ArrayObjectAdapter(cardPresenter);
        seriesAdapter.addAll(0, movies);
        HeaderItem seriesHeader = new HeaderItem(3, "Series");
        rowsAdapter.add(new ListRow(seriesHeader, seriesAdapter));

        setAdapter(rowsAdapter);
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());

        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                       RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof Movie) {
                    updateBackground(((Movie) item).getBackgroundImageUrl());
                }
            }
        });
    }

    private void updateBackground(String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .resize(1280, 720)
                .centerCrop()
                .error(R.drawable.default_background)
                .into(backgroundTarget);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder,
                                  Object item,
                                  RowPresenter.ViewHolder rowViewHolder,
                                  Row row) {
            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);
                startActivity(intent);
            }
        }
    }
}
