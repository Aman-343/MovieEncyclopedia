package com.example.movieencyclopedia.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieencyclopedia.Model.MovieModel;
import com.example.movieencyclopedia.R;
import com.example.movieencyclopedia.utils.ApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MoviesViewModel extends ViewModel {

    private final MutableLiveData<MovieModel> movieDetails = new MutableLiveData<MovieModel>();
    private final MutableLiveData<MovieModel> favoriteMovieDetails = new MutableLiveData<MovieModel>();
    private final MutableLiveData<List<MovieModel>> moviesList = new MutableLiveData<List<MovieModel>>();
    private final MutableLiveData<List<MovieModel>> moviesFavoritesList = new MutableLiveData<List<MovieModel>>();
    MovieModel movieModel = new MovieModel();
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    public LiveData<MovieModel> getMovieData() {
        return movieDetails;
    }
    public LiveData<MovieModel> getFavoriteMovieData() {
        return favoriteMovieDetails;
    }
    public LiveData<List<MovieModel>> getFavoriteMovies() {
        return moviesFavoritesList;
    }
    public LiveData<List<MovieModel>> getMoviesList() {
        return moviesList;
    }

    List<MovieModel> movieList = new ArrayList<>();
    List<String> moviedetails = new ArrayList<>();
    List<MovieModel> movieFavList = new ArrayList<>();

    public void fetchMovieList(String keyword) {
        String getUrl = "https://www.omdbapi.com/?apikey=c0761da5&type=movie&s=" + keyword;

        ApiClient.get(getUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    movieList.clear();
                    assert response.body() != null;
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        if(Boolean.valueOf(json.getString("Response")))
                        {
                            JSONArray searchArray = json.getJSONArray("Search");
                            for (int i = 0; i < searchArray.length(); i++) {
                                JSONObject movieObject = searchArray.getJSONObject(i);
                                // Map JSON fields to MovieModel
                                MovieModel movie = new MovieModel();
                                movie.setTitle(movieObject.getString("Title"));
                                movie.setYear(movieObject.getString("Year"));
                                movie.setImdbID(movieObject.getString("imdbID"));
                                movie.setType(movieObject.getString("Type"));
                                movie.setPoster(movieObject.getString("Poster"));

                                movieList.add(movie);
                                Log.i("tag", String.valueOf(movieList));
                                Log.i("tag", responseData);
                            }

                            Log.i("Json", responseData);
                            Log.i("Tag",String.valueOf(movieList));
                            moviesList.postValue(movieList);
                        }
                        else
                        {
                            movieList.clear();
                            moviesList.postValue(movieList);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }

    public List<MovieModel> getmovieList() {
        return movieList;
    }
    public void getFavoritesMovies(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(userId).collection("movies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MovieModel movie = document.toObject(MovieModel.class);
                            movieFavList.add(movie);
                        }
                        moviesFavoritesList.postValue(movieFavList);

                    } else {
                        Log.w("Firestore", "Error getting movies", task.getException());
                    }
                });
    }

    public void fetchMovieDetails(String ID){
        moviedetails.clear();
        String getUrl = "https://www.omdbapi.com/?apikey=c0761da5&type=movie&i=" + ID;
        ApiClient.get(getUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        movieModel.setImdbID(ID);
                        movieModel.setYear(json.getString("Year"));
                        movieModel.setTitle(json.getString("Title"));
                        movieModel.setPlot(json.getString("Plot"));
                        movieModel.setPoster(json.getString("Poster"));
                        movieModel.setRating(json.getString("imdbRating"));
                        movieDetails.postValue(movieModel);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Log.i("List from View Model", String.valueOf(moviedetails));
                }
            }
        });
    }
    public void updateMovie(String imdbId, String newPlot) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId != null && imdbId != null && !newPlot.isEmpty()) {
            db.collection("users")
                    .document(userId)
                    .collection("movies")
                    .whereEqualTo("imdbID", imdbId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Loop through the results to get the document ID
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String movieId = document.getId(); // Get the document ID

                                // Update only the "Plot" field
                                db.collection("users")
                                        .document(userId)
                                        .collection("movies")
                                        .document(movieId)
                                        .update("Plot", newPlot)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Firestore", "Plot updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w("Firestore", "Error updating plot", e);
                                        });
                            }
                        } else if (task.getResult().isEmpty()) {
                            Log.d("Firestore", "No movie found with the provided IMDb ID: " + imdbId);
                        } else {
                            Log.w("Firestore", "Error querying movie: ", task.getException());
                        }
                    });
        } else {
            Log.w("Firestore", "User ID, IMDb ID, or new plot is invalid");
        }
    }
    public void deleteMovie(String imdbId){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .collection("movies")
                .whereEqualTo("imdbID", imdbId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String movieId = document.getId();
                            db.collection("users").document(userId).collection("movies").document(movieId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "Movie deleted successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("Firestore", "Error deleting movie", e);
                                    });
                        }
                    }
                });
    }
    public void addmovietoFavorites(List<String> details){

        db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> movie = new HashMap<>();
        movie.put("Title", details.get(0));
        movie.put("Poster", details.get(1));
        movie.put("Plot", details.get(2));
        movie.put("Year", details.get(3));
        movie.put("imdbID", details.get(4));
        String imdbID = details.get(4);
        if (userId != null) {

            db.collection("users")
                    .document(userId)
                    .collection("movies")
                    .whereEqualTo("imdbID", imdbID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            if (task.getResult().isEmpty()) {
                                String movieId = db.collection("users")
                                        .document(userId)
                                        .collection("movies")
                                        .document()
                                        .getId();

                                db.collection("users")
                                        .document(userId)
                                        .collection("movies")
                                        .document(movieId)
                                        .set(movie)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Firestore", "Movie added successfully to user's collection");

                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w("Firestore", "Error adding movie", e);

                                        });


                            } else {
                                Log.w("Firestore", "User is not authenticated");

                            }

                        }
                    });
        }
    }



}
