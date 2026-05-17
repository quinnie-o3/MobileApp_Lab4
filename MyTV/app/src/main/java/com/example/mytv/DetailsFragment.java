package com.example.mytv;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailsFragment extends DetailsSupportFragment {

    private Movie selectedMovie;
    private BackgroundManager backgroundManager;
    private ArrayObjectAdapter adapter;

    private Target backgroundTarget;
    private Target posterTarget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedMovie = (Movie) requireActivity()
                .getIntent()
                .getSerializableExtra(DetailsActivity.MOVIE);

        if (selectedMovie == null) {
            // Fallback to avoid crash
            selectedMovie = MovieList.getSeries().get(0);
        }

        setupBackground();
        setupAdapter();
        setupEventListeners();
    }

    private void setupBackground() {
        backgroundManager = BackgroundManager.getInstance(requireActivity());
        backgroundManager.attach(requireActivity().getWindow());

        backgroundTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                backgroundManager.setBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.get()
                .load(selectedMovie.getBackgroundImageUrl())
                .resize(1280, 720)
                .centerCrop()
                .error(R.drawable.default_background)
                .into(backgroundTarget);
    }

    private void setupAdapter() {
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());

        detailsPresenter.setBackgroundColor(Color.rgb(30, 30, 30));

        FullWidthDetailsOverviewSharedElementHelper helper =
                new FullWidthDetailsOverviewSharedElementHelper();
        detailsPresenter.setListener(helper);
        detailsPresenter.setParticipatingEntranceTransition(false);
        detailsPresenter.setActionsBackgroundColor(Color.rgb(20, 20, 20));

        ClassPresenterSelector presenterSelector = new ClassPresenterSelector();
        presenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
        presenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

        adapter = new ArrayObjectAdapter(presenterSelector);

        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(selectedMovie);
        detailsOverview.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
        );

        loadPoster(detailsOverview);

        // Setup Actions
        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();
        if (selectedMovie.getTitle().toLowerCase().contains("sherlock")) {
            actionAdapter.add(new Action(1, "SHERLOCK", "SEASON 1"));
            actionAdapter.add(new Action(2, "SHERLOCK", "SEASON 2"));
            actionAdapter.add(new Action(3, "SHERLOCK", "SEASON 3"));
            actionAdapter.add(new Action(4, "SHERLOCK", "SEASON 4"));
        } else {
            actionAdapter.add(new Action(1, "WATCH"));
            actionAdapter.add(new Action(2, "TRAILER"));
            actionAdapter.add(new Action(3, "ADD TO FAVORITE"));
            actionAdapter.add(new Action(4, "MORE INFO"));
        }
        detailsOverview.setActionsAdapter(actionAdapter);

        adapter.add(detailsOverview);
        setAdapter(adapter);
    }

    private void loadPoster(final DetailsOverviewRow detailsOverview) {
        posterTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                detailsOverview.setImageBitmap(requireContext(), bitmap);
                adapter.notifyArrayItemRangeChanged(0, adapter.size());
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                detailsOverview.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                detailsOverview.setImageDrawable(placeHolderDrawable);
            }
        };

        Picasso.get()
                .load(selectedMovie.getCardImageUrl())
                .resize(274, 400)
                .centerCrop()
                .error(R.drawable.default_background)
                .into(posterTarget);
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                       RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof Action) {
                    Action action = (Action) item;
                    Toast.makeText(getActivity(), "Selected: " + action.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
