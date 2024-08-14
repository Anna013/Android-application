package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ActivityDetail;
import com.example.myapplication.Movie;
import com.example.myapplication.MovieItem;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Adapter2 extends RecyclerView.Adapter<Adapter2.ItemHolder> {

    private Context context;
    private List<MovieItem> list;

    public Adapter2(Context con, List<MovieItem> movies) {
        this.context = con;
        list = movies;

    }

    @NonNull
    @Override
    public Adapter2.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie2_item, parent, false);
        ItemHolder myHolder = new ItemHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter2.ItemHolder holder, int position) {
        MovieItem item = list.get(position);

        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());

        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + item.getImage()).into(holder.image);
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + item.getImage()).into(holder.image2);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        ImageView image, image2;
        TextView title;
        TextView date;
        ConstraintLayout item_layout;


        public ItemHolder(@NotNull View v) {
            super(v);
            image = v.findViewById(R.id.item2Image);
            image2 = v.findViewById(R.id.item2Image2);
            title = v.findViewById(R.id.item2Text);
            date = v.findViewById(R.id.item2Date);



        }
    }
}
