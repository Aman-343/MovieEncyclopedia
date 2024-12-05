package com.example.movieencyclopedia.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieencyclopedia.Model.MovieModel;
import com.example.movieencyclopedia.utils.MovieClickListener;
import com.example.movieencyclopedia.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<MovieModel> movies;
    private MovieClickListener clickListener;

    public MyAdapter(Context context, List<MovieModel> movies) {
        this.context = context;
        this.movies = movies;
    }
    public void setClickListener(MovieClickListener myListener){
        this.clickListener = myListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false);
        return new MyViewHolder(itemView,clickListener);
    }

    public MovieModel getItem(int position) {
        return movies.get(position);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MovieModel movie = movies.get(position);

        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYear());
        Picasso.get()
                .load(movie.getPoster())
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
