package com.example.mytv;

import java.io.Serializable;

public class Movie implements Serializable {
    private long id;
    private String title;
    private String studio;
    private String description;
    private String cardImageUrl;
    private String backgroundImageUrl;

    public Movie() {
    }

    public Movie(long id, String title, String studio, String description,
                 String cardImageUrl, String backgroundImageUrl) {
        this.id = id;
        this.title = title;
        this.studio = studio;
        this.description = description;
        this.cardImageUrl = cardImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStudio() {
        return studio;
    }

    public String getDescription() {
        return description;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    @Override
    public String toString() {
        return title;
    }
}
