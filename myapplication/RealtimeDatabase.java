package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.List;

public class RealtimeDatabase {

    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static DatabaseReference dbref = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://auth-94ef9-default-rtdb.firebaseio.com/");



    public static void editAccountFields(String name, String password, String loc, Context con) {
        String userNameID = user.getUid();
        dbref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userNameID)) {

                    if (!name.equals("")) {
                        dbref.child("Users").child(userNameID).child("username").setValue(name);
                        System.out.println("nameid " + userNameID + " uuun " + name);
                        Toast.makeText(con, "successfull account edit", Toast.LENGTH_LONG).show();
                    } if (password.length() > 4) {
                        dbref.child("Users").child(userNameID).child("password").setValue(password);
                        Toast.makeText(con, "successfull account edit", Toast.LENGTH_LONG).show();
                    } if (!loc.equals("")) {
                        dbref.child("Users").child(userNameID).child("location").setValue(loc);
                        Toast.makeText(con, "successfull account edit", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(con, "please fill in all the fields!", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(con, "edit error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public static void getAccountData(TextView email, TextView username, TextView pass, TextView location) {
        String userNameID = user.getUid();
        dbref.child("Users").child(userNameID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String loc1 = "";
                    if (task.getResult().exists()) {
                        DataSnapshot ds = task.getResult();

                        String email1 = (String) ds.child("email").getValue();
                        String pass1 = (String) ds.child("password").getValue();
                        String name = (String) ds.child("username").getValue();
                        if (ds.child("location").exists()) {
                            System.out.println("exists trueee");
                            loc1 = (String) ds.child("location").getValue();
                        }

                        email.setText(email1);
                        username.setText(name);
                        pass.setText(pass1);
                        location.setText(loc1);
                        System.out.println("AAAAAA " + username.getText() + " ll " + location.getText());


                    } else {
                        System.out.println("data error");
                    }
                }
            }
        });
    }


    public static void getMovieData(JSONObject response, List<Movie> list) throws JSONException {

        JSONArray jsonArray = response.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject resoult = jsonArray.getJSONObject(i);

            String title = resoult.getString("title");
            String image = resoult.getString("poster_path");
            int id = resoult.getInt("id");
            String desc = resoult.getString("overview");
            double voteD = resoult.getDouble("vote_average");
            String vote = voteD + "/10";
            String date = resoult.getString("release_date");
            String[] dates = date.split("-");
            String year = dates[0];

            JSONArray genres = resoult.getJSONArray("genre_ids");
            List<String> genre_array = new ArrayList<>();

            for (int j = 0; j < genres.length(); j++) {
                int id_genre = genres.getInt(j);
                String s = findName(id_genre);

                if (s.equals("Other")) {
                    continue;
                } else if (!s.isEmpty() || !genre_array.contains(s)) {
                    genre_array.add(s);
                }
            }

            Movie m = new Movie(title, image, vote, year, desc, genre_array, id);
            list.add(m);

        }

    }

    public static String findName(int id) {
        if (id == 28)
            return "Action";
        else if (id == 12)
            return "Adventure";
        else if (id == 16)
            return "Animation";
        else if (id == 35)
            return "Comedy";
        else if (id == 14)
            return "Fantasy";
        else if (id == 18)
            return "Drama";
        else if (id == 9648)
            return "Mystery";
        else if (id == 80)
            return "Crime";
        else if (id == 99)
            return "Documentary";
        else if (id == 27)
            return "Horror";
        else
            return "Other";
    }


    public static void putMovieInBase(DatabaseReference db, String title, String image, String date, Context con){
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(title)) {
                    Toast.makeText(con, "movie already exists", Toast.LENGTH_SHORT).show();


                } else {
                    db.child(title).child("title").setValue(title);
                    db.child(title).child("image").setValue(image);
                    db.child(title).child("date").setValue(date);
                    Toast.makeText(con, "Successfully added movie", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void deleteMovie(DatabaseReference db, String title, Context con){
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(title)) {
                    db.child(title).removeValue();
                    Toast.makeText(con, "deleting movie", Toast.LENGTH_SHORT).show();

                } else {
                    System.out.println("movie is not in the list");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }





}
