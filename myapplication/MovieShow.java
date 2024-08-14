package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapters.HorizontalRec;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class MovieShow extends AppCompatActivity {

    private RecyclerView rv1,rv2,rv3,rv4,rv5,rv6;
    private int brojac;
    private RequestQueue queue;
    private List<Movie> movies, documentary;
    private List<Movie> first, secound;
    private FirebaseAuth auth;

    String url_genre = "https://api.themoviedb.org/3/discover/movie?api_key=85c7bc5ea26c2d5c430fe4b23752c44e&with_genres=";

    String action = "28";
    String adventure = "12";
    String animation = "16";
    String fantasy = "14";
    String crime = "80";
    String mystery = "9648";
    String comedy = "35";
    String drama = "18";
    String doc = "99";

    String popular_url = "https://api.themoviedb.org/3/movie/popular?api_key=85c7bc5ea26c2d5c430fe4b23752c44e";
    String search_url = "https://api.themoviedb.org/3/search/movie?api_key=85c7bc5ea26c2d5c430fe4b23752c44e&query=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_show);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ProgressDialog progressDialog = new ProgressDialog(this);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


        rv1 = findViewById(R.id.horizontalScrollView);
        rv2 = findViewById(R.id.horizontalScrollView2);
        rv3 = findViewById(R.id.horizontalScrollView3);
        rv4 = findViewById(R.id.horizontalScrollView4);
        rv5 = findViewById(R.id.horizontalScrollView5);
        rv6 = findViewById(R.id.horizontalScrollView6);

        auth = FirebaseAuth.getInstance();
        movies = new ArrayList<>();
        documentary = new ArrayList<>();
        first = new ArrayList<>();
        secound = new ArrayList<>();

        progressDialog.setMessage("please wait");
        progressDialog.setTitle("Movies uploading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1000);


        brojac = 0;
        queue = Volley.newRequestQueue(this);

        getMovies(popular_url, movies);

        getMovies(url_genre + action, first);
        getMovies(url_genre + adventure, secound);

        getMovies(url_genre + animation, first);
        getMovies(url_genre + fantasy, secound);

        getMovies(url_genre + crime, first);
        getMovies(url_genre + mystery, secound);

        getMovies(url_genre + comedy, first);
        getMovies(url_genre + drama, secound);

        getMovies(url_genre + doc, documentary);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
                Intent intent = new Intent(this, Account.class);
                this.startActivity(intent);
                break;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menuItemSearch);
        SearchView search = (SearchView) item.getActionView();
        search.setQueryHint("Type here to search");


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    String url = search_url + query;
                    Intent intent = new Intent(MovieShow.this, Search.class);
                    intent.putExtra("url_search", url);
                    startActivity(intent);
                    return true;

                } else {
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }



    public void getMovies(String url, List<Movie> list) {

        JsonObjectRequest json = new
                JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    RealtimeDatabase.getMovieData(response, list);
                    List<Movie> temp = new ArrayList<>();
                    brojac++;
                    setRVByList(temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
                error.printStackTrace();
            }
        });

        queue.add(json);

    }


    public void setRV(RecyclerView rv, List<Movie> li) {
        RecyclerView.LayoutManager lin = new LinearLayoutManager(MovieShow.this, LinearLayoutManager.HORIZONTAL, false);
        HorizontalRec adapter = new HorizontalRec(MovieShow.this, li);
        rv.setLayoutManager(lin);
        rv.setAdapter(adapter);
    }

    public void setRVByList(List<Movie> temp) {

        if (brojac == 1)
            setRV(rv1, movies);

        if (!first.isEmpty() && !secound.isEmpty()) {
            brojac++;
            System.out.println("brojac " + brojac);
            int br = first.size();
            first.addAll(br, secound);
            Set<Movie> set = new HashSet<>(first);
            temp = new ArrayList<>(set);
            first.clear();
            secound.clear();
        }

        if (brojac == 4) {
            setRV(rv2, temp);
        } else if (brojac == 7) {
            setRV(rv3, temp);
        } else if (brojac == 10) {
            setRV(rv4, temp);
        } else if (brojac == 13) {
            setRV(rv5, temp);
        } else if (brojac == 14) {
            setRV(rv6, documentary);
        }
    }



}