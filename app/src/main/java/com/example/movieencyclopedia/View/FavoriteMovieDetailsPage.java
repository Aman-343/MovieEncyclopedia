package com.example.movieencyclopedia.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieencyclopedia.R;
import com.example.movieencyclopedia.ViewModel.MoviesViewModel;
import com.example.movieencyclopedia.databinding.ActivityFavoriteMovieDetailsPageBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMovieDetailsPage extends AppCompatActivity {
    ActivityFavoriteMovieDetailsPageBinding binding;
    String title ;
    String plot;
    String poster;
    String year;
    String imdbId;
    List<String> details = new ArrayList<>();
    FirebaseFirestore db;
    MoviesViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        db = FirebaseFirestore.getInstance();
        viewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        setContentView(R.layout.activity_favorite_movie_details_page);
        Intent intentObj = getIntent();
        title=intentObj.getStringExtra("Title");
        imdbId=intentObj.getStringExtra("ID");
        year=intentObj.getStringExtra("Year");
        poster=intentObj.getStringExtra("Poster");
        binding = ActivityFavoriteMovieDetailsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.favoritesMovieName.setText(intentObj.getStringExtra("Title"));
        binding.favoritesMoviePlot.setText(intentObj.getStringExtra("Plot"));
        Picasso.get()
                .load(intentObj.getStringExtra("Poster"))
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .into(binding.favoritesMoviePoster);
        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plot = binding.favoritesMoviePlot.getText().toString();
                if(plot.isEmpty()){
                    Toast.makeText(FavoriteMovieDetailsPage.this, "Plot is required for update", Toast.LENGTH_SHORT).show();
                }
                else {
                    viewModel.updateMovie(imdbId, plot);
                    Toast.makeText(FavoriteMovieDetailsPage.this, "Movie's plot has been updated",Toast.LENGTH_SHORT).show();
                    Intent intentobj = new Intent(getApplicationContext(), UserScreen.class);
                    startActivity(intentobj);

                }
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteMovie(imdbId);
                Toast.makeText(FavoriteMovieDetailsPage.this, "Movie has been removed from favorites",Toast.LENGTH_SHORT).show();
                Intent intentobj = new Intent(getApplicationContext(),UserScreen.class);
                startActivity(intentobj);
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}