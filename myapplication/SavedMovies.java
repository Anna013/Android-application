package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.adapters.Adapter2;
import com.example.myapplication.adapters.HorizontalRec;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedMovies extends AppCompatActivity {

    private Adapter2 adapter;
    private RecyclerView recyclerView, recyclerView2, recyclerViewTablet, recyclerViewTabletLand;
    private String userID, type;
    private List<MovieItem> items;
    private NotificationManagerCompat nmc;
    private TextView text;
    String search_url = "https://api.themoviedb.org/3/search/movie?api_key=85c7bc5ea26c2d5c430fe4b23752c44e&query=";
    private DatabaseReference dbsaved, dbwatched;
    private DatabaseReference dbref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://auth-94ef9-default-rtdb.firebaseio.com/");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_movies);
        userID = user.getUid();
        items = new ArrayList<>();
        recyclerView = findViewById(R.id.savedMovieRV);
        text = findViewById(R.id.listText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nmc = NotificationManagerCompat.from(this);

        dbsaved = dbref.child("Users").child(userID).child("savedMovies");
        dbwatched = dbref.child("Users").child(userID).child("watchedMovies");


        Bundle bun = getIntent().getExtras();
        type = bun.getString("type");

        if (type.equals("watched")) {
            text.setText("Watched Movies");
            getSavedList(dbwatched);
        } else {
            text.setText("Saved Movies");
            getSavedList(dbsaved);

        }


    }
//    public void sendOnChannelNotification(){
//
//
//        android.app.Notification noti = new NotificationCompat.Builder(this, Notification.CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon2)
//                .setContentTitle("Movies awaits you to watch them!")
//                .setContentText("You saved a lot of great movies. Remember to watch them!")
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .build();
//        nmc.notify(1,noti);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menuItemSearch);
        System.out.println("manuu ");
        SearchView search = (SearchView) item.getActionView();
        search.setQueryHint("Type here to search");

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != "") {
                    String url = search_url + query;
                    Intent intent = new Intent(SavedMovies.this, Search.class);
                    intent.putExtra("url_search", url);
                    startActivity(intent);
                    finish();

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


    public void setRV(RecyclerView rv, List<MovieItem> li, int col) {

        androidx.recyclerview.widget.GridLayoutManager grid = new androidx.recyclerview.widget.GridLayoutManager(SavedMovies.this, col, androidx.recyclerview.widget.GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(grid);
        adapter = new Adapter2(SavedMovies.this, li);
        rv.setAdapter(adapter);
    }


    public void getSavedList(DatabaseReference db) {
        items = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MovieItem o = ds.getValue(MovieItem.class);
                    items.add(o);

                }

                setRVsizeSW();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    public void setRVsizeSW(){

        if (findViewById(R.id.savedMovieRV) != null) {
            setRV(recyclerView, items, 2);
        }

        else if (findViewById(R.id.savedMovieRVLand) != null) {
            recyclerView2 = findViewById(R.id.savedMovieRVLand);
            setRV(recyclerView2, items, 3);
        }

        else if (findViewById(R.id.savedMovieRVTablet) != null) {
            recyclerViewTablet = findViewById(R.id.savedMovieRVTablet);
            setRV(recyclerViewTablet, items, 4);
        }

        else if (findViewById(R.id.savedMovieRV2TabletLand) != null){
            recyclerViewTabletLand = findViewById(R.id.savedMovieRV2TabletLand);
            setRV(recyclerViewTabletLand, items, 6); //?
        }
    }


}