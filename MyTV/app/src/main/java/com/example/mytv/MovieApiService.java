package com.example.mytv;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieApiService {
    // Giả sử backend trả về danh sách phim tại endpoint /movies
    @GET("movies")
    Call<List<Movie>> getMovies();
}
