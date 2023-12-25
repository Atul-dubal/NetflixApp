package app9.apcoders.netflix.Retrofit;

import static app9.apcoders.netflix.Retrofit.RetrofitClient.BASE_URL;

import java.util.List;

import app9.apcoders.netflix.Modal.AllCategory;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Apiinterface {
    @GET(BASE_URL)
    Observable<List<AllCategory>> getAllCategoryMovies();
}
