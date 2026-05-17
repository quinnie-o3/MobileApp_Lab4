package com.example.mytv;

import java.util.ArrayList;
import java.util.List;

public class MovieList {
    public static List<Movie> getSeries() {
        List<Movie> list = new ArrayList<>();
        list.add(new Movie(
                0,
                "Sherlock",
                "BBC One",
                "A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.",
                "https://image.tmdb.org/t/p/w500/09RbbqzIP6Sj6HJu61vTSB1pSAn.jpg",
                "https://image.tmdb.org/t/p/w1280/f999tNBm4Y99u9aQ369Y6Z799ub.jpg"));
        list.add(new Movie(
                1,
                "Stranger Things",
                "Netflix",
                "When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces and one strange little girl.",
                "https://image.tmdb.org/t/p/w500/49WJfev0moxmBEEp7ZFr9YvYqok.jpg",
                "https://image.tmdb.org/t/p/w1280/56v2KjIuLle0hy6QK3uS7PT1n9i.jpg"));
        list.add(new Movie(
                2,
                "Black Mirror",
                "Netflix",
                "A contemporary British anthology series that explores the dark side of life and technology.",
                "https://image.tmdb.org/t/p/w500/769XvO8Yp77Y9I6Xmcl9v9ZTMqL.jpg",
                "https://image.tmdb.org/t/p/w1280/769XvO8Yp77Y9I6Xmcl9v9ZTMqL.jpg"));
        list.add(new Movie(
                3,
                "Dark",
                "Netflix",
                "A family saga with a supernatural twist, set in a German town, where the disappearance of two young children exposes the relationships among four families.",
                "https://image.tmdb.org/t/p/w500/apbr089V0Iu3S998fOQ8B9sN8S6.jpg",
                "https://image.tmdb.org/t/p/w1280/apbr089V0Iu3S998fOQ8B9sN8S6.jpg"));
        list.add(new Movie(
                4,
                "Inception",
                "Warner Bros.",
                "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
                "https://image.tmdb.org/t/p/w500/edv5CZvRjS99ayvT69o6D6DTviZ.jpg",
                "https://image.tmdb.org/t/p/w1280/s3TBrjBwsZ6P6urqMlvDcr4uPhz.jpg"));
        list.add(new Movie(
                5,
                "Interstellar",
                "Paramount Pictures",
                "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                "https://image.tmdb.org/t/p/w500/gEU2QniE6E07Qv8djTsJuHTvMWf.jpg",
                "https://image.tmdb.org/t/p/w1280/xJHaxYvOIJmXRC79rN9SuN9SuaY.jpg"));
        return list;
    }

    public static List<Movie> getMovies() {
        return getSeries();
    }
}
