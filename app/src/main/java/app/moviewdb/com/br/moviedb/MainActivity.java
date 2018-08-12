package app.moviewdb.com.br.moviedb;

import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    protected ActionBar actionBar;

    protected RecyclerView recyclerView;
    protected MovieAdapter movieAdapter;
    protected List<Movie> movieList;
    protected SwipeRefreshLayout swipeContainer;

    protected ApiClient client;
    protected ApiService apiService;

    protected int currentPage = 1;
    protected  boolean popularMovies = true;

    protected  boolean scrolling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();


        this.prepareView();
        this.loadData();



    }

    private void prepareView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        movieList =  new ArrayList<Movie>();
        movieAdapter = new MovieAdapter(this, movieList);

        client = new ApiClient();
        apiService = ApiClient.getClient().create(ApiService.class);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView, newState);
                scrolling = true;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int deltaX, int deltaY){
                super.onScrolled(recyclerView, deltaX, deltaY);

                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();

                int currentItems = manager.getChildCount();
                int totalItems = manager.getItemCount();
                int outItems = manager.findFirstVisibleItemPosition();

                if(scrolling && (currentItems + outItems) == totalItems){
                    scrolling = false;
                    currentPage++;
                    loadData();
                }
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(currentPage >= 2){
                    currentPage--;
                    loadData();
                } else {
                    if(swipeContainer.isRefreshing())
                        swipeContainer.setRefreshing(false);
                }
            }
        });
    }

    private void loadData(){
        try{
            Call<MovieResponse> call;
            if(popularMovies) {
                this.actionBar.setTitle(R.string.popular_name);
                call = this.apiService.getPopularMovies(BuildConfig.MOVIEDB_API_KEY, currentPage);
            } else {
                this.actionBar.setTitle(R.string.top_rated_name);
                call = this.apiService.getTopRatedeMovies(BuildConfig.MOVIEDB_API_KEY, currentPage);
            }

            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if(swipeContainer.isRefreshing())
                        swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }
            });
        } catch (Exception e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_top_rated:
                popularMovies = false;
                break;

            case R.id.menu_popular:
                popularMovies = true;
                break;

            default:
        }

        currentPage = 1;
        loadData();

        return super.onOptionsItemSelected(item);
    }

}
