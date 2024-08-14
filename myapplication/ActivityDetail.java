package com.example.myapplication;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ActivityDetail extends YouTubeBaseActivity {

    //private String apiKey = "AIzaSyDpp6lxj5JSMCbbBfEktXO2gwwPvZCfl2U";
    private String userID, genres;
    private YouTubePlayerView player;
    private RequestQueue queue;
    private LottieAnimationView lottie, lottie2;
    private boolean switchOnWatched, switchOnSave;
    private Button button2;
    private List<MovieItem> items;
    private MovieItem movieItem;
    private DatabaseReference dbrefSave, dbrefWatch, dbsaved;
    private final DatabaseReference dbref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://auth-94ef9-default-rtdb.firebaseio.com/");
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private NotificationManagerCompat nmc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        queue = Volley.newRequestQueue(this);
        ImageView imageView = findViewById(R.id.imageDetail);
        ImageView imageView2 = findViewById(R.id.imageDetail2);
        TextView title = findViewById(R.id.titleDetail);
        TextView overview = findViewById(R.id.descDetail);
        TextView year = findViewById(R.id.year);
        TextView genre = findViewById(R.id.genre);
        items = new ArrayList<>();



        player = findViewById(R.id.youtube_player_view);
        lottie = findViewById(R.id.saveMovie);
        lottie2 = findViewById(R.id.watchMovie);
        userID = user.getUid();
        button2 = findViewById(R.id.button2);
        dbsaved = dbref.child("Users").child(userID).child("savedMovies");


        Bundle bundle = getIntent().getExtras();

        int id = bundle.getInt("id");
        String movie_title = bundle.getString("title");
        String image = bundle.getString("image");
        String movie_year = "Year: " + bundle.getString("year");
        String movie_desc = "Description: " + bundle.getString("desc");
        ArrayList<String> movie_genres = bundle.getStringArrayList("genres");

        Glide.with(this).load(image).into(imageView);
        Glide.with(this).load(image).into(imageView2);

        title.setText(movie_title);
        year.setText(movie_year);
        overview.setText(movie_desc);
        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(date1);


        movieItem = new MovieItem();
        movieItem.setTitle(movie_title);
        movieItem.setImage(image);
        movieItem.setDate(date);

        dbrefSave = dbref.child("Users").child(userID).child("savedMovies");
        dbrefWatch = dbref.child("Users").child(userID).child("watchedMovies");
        isMovieSaved();


        for (int i = 0; i < movie_genres.size(); i++) {
            String name = movie_genres.get(i);
            if (i == 0) {
                genres = "Ganres: " + name;
                genre.setText(genres);
            }
            else {
                genres = genres + ", " + name;
                genre.setText(genres);
            }

        }

        String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=85c7bc5ea26c2d5c430fe4b23752c44e";
        getMovieKey(url);

        lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    setLottieSaveOnChange();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLottieWatchOnChange();
            }

        });

        nmc = NotificationManagerCompat.from(this);

    }


    public void setLottieSaveOnChange(){
        if (switchOnSave) {
            lottie.setMinAndMaxProgress(0.5f, 1.0f);
            lottie.playAnimation();
            //delete from base
            deleteMovie(dbrefSave);
            switchOnSave = false;

        } else {
            lottie.setMinAndMaxProgress(0.0f, 0.5f);
            lottie.playAnimation();
            //add movie in base
            putMovieinBase(dbrefSave);
            switchOnSave = true;
            getSavedList(dbsaved);




        }
    }

    public void setLottieWatchOnChange(){
        if (switchOnWatched) {
            lottie2.setMinAndMaxProgress(0.5f, 1.0f);
            lottie2.playAnimation();
            button2.setText("Unwatched");
            //delete from base
            deleteMovie(dbrefWatch);
            switchOnWatched = false;


        } else {
            lottie2.setMinAndMaxProgress(0.0f, 0.5f);
            lottie2.playAnimation();
            button2.setText("Watched");
            //add movie in base
            putMovieinBase(dbrefWatch);
            switchOnWatched = true;



        }
    }


    public void getMovieKey(String url) {

        JsonObjectRequest json = new
                JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    YouTubeInitializer.getMovieYouTubeKey(response, ActivityDetail.this, player);
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


    public void putMovieinBase(DatabaseReference db) {
        String titleName = movieItem.getTitle();
        String dateString = movieItem.getDate();
        String imageString = movieItem.getImage();
        RealtimeDatabase.putMovieInBase(db,titleName, imageString, dateString, ActivityDetail.this);

    }



    public void deleteMovie(DatabaseReference db) {
        String titleName = movieItem.getTitle();
        RealtimeDatabase.deleteMovie(db, titleName, ActivityDetail.this);

    }



    public void isMovieSaved() {
        String t = movieItem.getTitle();
        dbref.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean saved = snapshot.child("savedMovies").hasChild(t);
                boolean watched = snapshot.child("watchedMovies").hasChild(t);
               setLottieOnStart(saved, watched);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void setLottieOnStart(boolean saved, boolean watched){
        if (saved) {
            switchOnSave = true;
            lottie.setMinAndMaxProgress(0.5f, 1.0f);

        }

        if (!saved) {
            switchOnSave = false;
            lottie.setMinAndMaxProgress(0.0f, 0.5f);
        }
        if (watched) {
            switchOnWatched = true;
            lottie2.setMinAndMaxProgress(0.5f, 1.0f);
            button2.setText("Watched");
        }

        if (!watched) {
            switchOnWatched = false;
            lottie2.setMinAndMaxProgress(0.0f, 0.5f);
            button2.setText("Unwatched");
        }
    }

    public void sendOnChannelNotification(){


        android.app.Notification noti = new NotificationCompat.Builder(this, Notification.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon2)
                .setContentTitle("Movies awaits you to watch them!")
                .setContentText("You saved a lot of great movies. Remember to watch them!")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        nmc.notify(1,noti);
    }


    public void getSavedList(DatabaseReference db) {
         items = new ArrayList<>();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MovieItem o = ds.getValue(MovieItem.class);
                    items.add(o);


                }

                System.out.println("ITEMS11 " + items.size());

                if(items.size() > 7) {

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            sendOnChannelNotification();

                        }
                    }, 2000);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


}




