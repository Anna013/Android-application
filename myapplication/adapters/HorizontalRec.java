package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.ActivityDetail;
import com.example.myapplication.Movie;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HorizontalRec extends RecyclerView.Adapter<HorizontalRec.MyHolder> {

    private Context context;
    private List<Movie> list;

    public HorizontalRec(Context con, List<Movie> movies){
        this.context = con;
        list = movies;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        MyHolder myHolder = new MyHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Movie movie = list.get(position);
        holder.title.setText(movie.getTitle());
        holder.vote.setText(movie.getVote_average());

        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movie.getImage()).into(holder.image);
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movie.getImage()).into(holder.image2);


        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityDetail.class);
                Bundle bundle = new Bundle();

                bundle.putString("title", movie.getTitle());
                bundle.putInt("id", movie.getID());
                bundle.putString("desc", movie.getOverview());
                bundle.putString("year", movie.getYear());
                bundle.putString("image", "https://image.tmdb.org/t/p/w500" +  movie.getImage());

                ArrayList<String> array = new ArrayList<>();
                array.addAll(movie.getGenres());
                bundle.putStringArrayList("genres", array);

                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageView image2;
        TextView title;
        TextView vote;
        ConstraintLayout item_layout;

        public MyHolder(@NotNull View v){
            super(v);
            image = v.findViewById(R.id.imageView);
            image2 = v.findViewById(R.id.imageView2);
            title = v.findViewById(R.id.titleText);
            vote = v.findViewById(R.id.ratingItem);
            item_layout = v.findViewById(R.id.item_layout);


        }
    }


}
