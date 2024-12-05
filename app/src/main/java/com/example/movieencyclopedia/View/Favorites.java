package com.example.movieencyclopedia.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movieencyclopedia.Model.MovieModel;
import com.example.movieencyclopedia.utils.MovieClickListener;
import com.example.movieencyclopedia.ViewModel.MoviesViewModel;
import com.example.movieencyclopedia.ViewModel.MyAdapter;
import com.example.movieencyclopedia.databinding.FragmentFavoritesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favorites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favorites extends Fragment {

    FragmentFavoritesBinding binding;
    MoviesViewModel viewModel;
    String keyword;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    MyAdapter myAdapter;
    List<MovieModel> moviesList = new ArrayList<>();

    MovieClickListener myListner = new MovieClickListener() {
        @Override
        public void onCLick(View v, int pos) {
            Toast.makeText(getContext(), "You Choose: " + myAdapter.getItem(pos).getTitle(), Toast.LENGTH_SHORT).show();
            Intent intentObj = new Intent(getActivity(), FavoriteMovieDetailsPage.class);
            intentObj.putExtra("ID", myAdapter.getItem(pos).getImdbID());
            Log.i("ID", myAdapter.getItem(pos).getImdbID());
            intentObj.putExtra("Poster", myAdapter.getItem(pos).getPoster());
            intentObj.putExtra("Plot", myAdapter.getItem(pos).getPlot());
            intentObj.putExtra("Year", myAdapter.getItem(pos).getYear());
            intentObj.putExtra("Title", myAdapter.getItem(pos).getTitle());
            intentObj.putExtra("Rating", myAdapter.getItem(pos).getRating());
            startActivity(intentObj);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        viewModel.getFavoriteMovies().observe(getViewLifecycleOwner() , movieDetails-> {

            myAdapter = new MyAdapter(getContext(), movieDetails);
            binding.recyclerViewFavorites.setAdapter(myAdapter);
            myAdapter.setClickListener(myListner);
        });
        viewModel.getFavoritesMovies();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.recyclerViewFavorites.setLayoutManager(layoutManager);
        return view;
    }

}