package com.example.movieencyclopedia.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieencyclopedia.Model.MovieModel;
import com.example.movieencyclopedia.utils.MovieClickListener;
import com.example.movieencyclopedia.R;
import com.example.movieencyclopedia.ViewModel.MoviesViewModel;
import com.example.movieencyclopedia.ViewModel.MyAdapter;
import com.example.movieencyclopedia.databinding.ActivityUserScreenBinding;

import java.util.ArrayList;
import java.util.List;

public class UserScreen extends AppCompatActivity {
    private ActivityUserScreenBinding binding;
    MoviesViewModel viewModel;
    String keyword;
    List<MovieModel> itemList = new ArrayList<>();
    MyAdapter myAdapter;
    Fragment searchFragment;
    Fragment favoriteFragment;

    MovieClickListener myListner = new MovieClickListener() {
        @Override
        public void onCLick(View v, int pos) {
            Toast.makeText(getApplicationContext(), "You Choose: "+ itemList.get(pos).getTitle(), Toast.LENGTH_SHORT).show();
            Intent intentObj = new Intent(getApplicationContext(), MovieDetailsPage.class);
            intentObj.putExtra("ID", itemList.get(pos).getImdbID());
            intentObj.putExtra("Year", itemList.get(pos).getYear());
            startActivity(intentObj);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        binding = ActivityUserScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        searchFragment = new Search();
        favoriteFragment = new Favorites();
        replaceFragment(searchFragment);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
            switch (item.getItemId())
            {
                case (R.id.searchID):
                    replaceFragment(searchFragment);
                    break;
                case (R.id.favoritesID):
                    replaceFragment(favoriteFragment);
                    break;

            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.linearLayout, fragment);
        ft.commit();

    }
}