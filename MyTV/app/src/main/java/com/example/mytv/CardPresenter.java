package com.example.mytv;

import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.squareup.picasso.Picasso;

public class CardPresenter extends Presenter {

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(
                parent.getContext(),
                androidx.leanback.R.style.Widget_Leanback_ImageCardView
        );

        ImageCardView cardView = new ImageCardView(contextThemeWrapper);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);

        Drawable defaultCardImage = ContextCompat.getDrawable(
                parent.getContext(),
                R.drawable.default_background
        );

        cardView.setMainImage(defaultCardImage);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Movie movie = (Movie) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        cardView.setTitleText(movie.getTitle());
        cardView.setContentText(movie.getStudio());
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);

        Picasso.get()
                .load(movie.getCardImageUrl())
                .resize(CARD_WIDTH, CARD_HEIGHT)
                .centerCrop()
                .error(R.drawable.default_background)
                .into(cardView.getMainImageView());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setMainImage(null);
    }
}