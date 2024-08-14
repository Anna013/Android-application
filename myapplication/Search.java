package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapters.HorizontalRec;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class Search extends AppCompatActivity {


    private RecyclerView searchRV, searchRV2, searchRV3, searchRV4;
    private HorizontalRec adapter;
    private List<Movie> searchList;
    private RequestQueue queue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);
        searchRV = findViewById(R.id.searchRV);
        searchList = new ArrayList<>();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url_search");
        getM(url, searchList);


    }


    public void getM(String url, List<Movie> list) {

        JsonObjectRequest json = new
                JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    RealtimeDatabase.getMovieData(response, list);
                    setRVsizeSW();
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

    public void setRV(RecyclerView rv, int col) {

            GridLayoutManager grid = new GridLayoutManager(Search.this, col, GridLayoutManager.VERTICAL, false);
            rv.setLayoutManager(grid);
            adapter = new HorizontalRec(Search.this, searchList);
            rv.setAdapter(adapter);
    }

    public void setRVsizeSW(){

        if(findViewById(R.id.searchRV) != null){
            setRV(searchRV, 2);
        }
        else if (findViewById(R.id.searchRV2) != null){
            searchRV2 = findViewById(R.id.searchRV2);
            setRV(searchRV2, 3);
        }
        else if(findViewById(R.id.searchRVTablet) != null){
            searchRV3 = findViewById(R.id.searchRVTablet);
            setRV(searchRV3,4);
        }
        else if(findViewById(R.id.searchRVTabletLand) != null){
            searchRV4 = findViewById(R.id.searchRVTabletLand);
            setRV(searchRV4,6);
        }
    }



}
