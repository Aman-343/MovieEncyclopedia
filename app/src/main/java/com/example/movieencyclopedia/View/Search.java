package com.example.movieencyclopedia.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movieencyclopedia.utils.MovieClickListener;
import com.example.movieencyclopedia.ViewModel.MoviesViewModel;
import com.example.movieencyclopedia.ViewModel.MyAdapter;
import com.example.movieencyclopedia.databinding.FragmentSearchBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {

    FragmentSearchBinding binding;
    MoviesViewModel viewModel;
    String keyword;
    MyAdapter myAdapter;
    MovieClickListener myListner = new MovieClickListener() {
        @Override
        public void onCLick(View v, int pos) {
            Toast.makeText(getContext(), "You Choose: " + myAdapter.getItem(pos).getTitle(), Toast.LENGTH_SHORT).show();
            Intent intentObj = new Intent(getActivity(), MovieDetailsPage.class);
            intentObj.putExtra("ID", myAdapter.getItem(pos).getImdbID());
            intentObj.putExtra("Year", myAdapter.getItem(pos).getYear());
            startActivity(intentObj);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        viewModel.getMoviesList().observe(getViewLifecycleOwner() , moviesList-> {

            myAdapter = new MyAdapter(getContext(), moviesList);
            binding.recyclerView.setAdapter(myAdapter);
            myAdapter.setClickListener(myListner);
        });


        binding.searchmovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = String.valueOf(binding.etSearch.getText());
                if(keyword.length() == 0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a keyword.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    viewModel.fetchMovieList(keyword);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        return view;
    }
}