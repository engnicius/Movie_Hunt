package app.moviewdb.com.br.moviedb;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends AppCompatActivity {

    protected ImageView imageView;
    protected ImageView imageView2;

    protected TextView movieTitle;
    protected TextView userRating;
    protected TextView releaseDate;
    protected TextView popularity;
    protected TextView overview;

    protected Movie movie;

    protected RecyclerView recyclerView;

    protected TrailerAdapter trailerAdapter;
    protected List<Trailer> trailerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar();

        imageView = (ImageView) findViewById(R.id.image_cover);
        imageView2 = (ImageView) findViewById(R.id.thumbnail_info);

        movieTitle = (TextView) findViewById(R.id.movieTitle);
        userRating = (TextView) findViewById(R.id.movieUserRating);
        releaseDate = (TextView) findViewById(R.id.movieReleaseDate);
        popularity = (TextView) findViewById(R.id.popularity);
        overview = (TextView) findViewById(R.id.overview);

        trailerList = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(this, trailerList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(intent.hasExtra("movie")){

            movie = intent.getParcelableExtra("movie");

            String backdropPath = movie.getBackdropPath();
            String posterPath = movie.getPosterPath();
            String title = movie.getOriginalTitle();
            String releaseDateStr = movie.getReleaseDate();
            String popularityStr = Double.toString(movie.getPopularity());
            String overviewStr = movie.getOverview();
            String voteAverage = Double.toString(movie.getVoteAverage());
            String array[] = new String[3];
            array = releaseDateStr.split("-");

            Glide.with(this).load(backdropPath).into(imageView);
            Glide.with(this).load(posterPath).into(imageView2);
            movieTitle.setText(title);
            releaseDate.setText(array[0]);
            userRating.setText(voteAverage);
            popularity.setText(popularityStr);
            overview.setText(overviewStr);
            //synopsis.setText(overview);
            //releaseDate.setText("releaseDateStr");

            loadData();
        }
    }


    private void collapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            boolean show = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + i == 0){
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_info));
                    show = true;
                } else if (show) {
                    collapsingToolbarLayout.setTitle(" ");
                    show = false;
                }
            }
        });
    };


    @Override
    public void onBackPressed(){
        Context context = getApplicationContext();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();

        return;
    }

    protected void loadData(){
        try {
            ApiClient cliente = new ApiClient();
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<TrailerResponse> call = apiService.getMovieTrailer(movie.getId(), BuildConfig.MOVIEDB_API_KEY);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    List<Trailer> trailer = response.body().getResults();
                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailer));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
