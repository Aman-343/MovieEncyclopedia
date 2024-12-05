package com.example.movieencyclopedia.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieencyclopedia.Model.MovieModel;
import com.example.movieencyclopedia.R;
import com.example.movieencyclopedia.ViewModel.MoviesViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDetailsPage extends AppCompatActivity {
    TextView title;
    TextView description;
    TextView Year;
    ImageView poster;
    List<String> details = new ArrayList<>();
    MoviesViewModel viewModel;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviedetail_layout);
        Intent intentObj = getIntent();
        title = findViewById(R.id.movieName);
        description = findViewById(R.id.moviePlot);
        poster = findViewById(R.id.moviePoster);
        viewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        viewModel.getMovieData().observe(this, movieDetails -> {
            details.add(0, movieDetails.getTitle());
            details.add(1, movieDetails.getPoster());
            details.add(2, movieDetails.getPlot());
            details.add(3, movieDetails.getYear());
            details.add(4, movieDetails.getImdbID());
            Picasso.get()
                    .load(movieDetails.getPoster())
                    .placeholder(R.drawable.error)
                    .error(R.drawable.error)
                    .into(poster);
            title.setText(movieDetails.getTitle());
            description.setText("Year: " + movieDetails.getYear() + "\n\n" + movieDetails.getPlot() + "\n\n" + "imdbRating: " + movieDetails.getRating() + "\n\n" + "Studio: N/A");
        });

        String ID = intentObj.getStringExtra("ID");
        viewModel.fetchMovieDetails(ID);


        Button backButton = findViewById(R.id.backButton);
        Button favourtiesButton = findViewById(R.id.favortiesButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        favourtiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = FirebaseFirestore.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String imdbID = details.get(4);
                db.collection("users")
                        .document(userId)
                        .collection("movies")
                        .whereEqualTo("imdbID", imdbID)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                if(task.getResult().isEmpty())
                                {
                                    viewModel.addmovietoFavorites(details);
                                    Toast.makeText(MovieDetailsPage.this, "Movie added to favorties", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MovieDetailsPage.this, "Movie is already added to favorties", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
