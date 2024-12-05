package com.example.movieencyclopedia.ViewModel;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movieencyclopedia.utils.MovieClickListener;
import com.example.movieencyclopedia.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView title;
    TextView year;
    MovieClickListener clickListener;

    public MyViewHolder(@NonNull View itemView, MovieClickListener clickListener) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageview);
        title = itemView.findViewById(R.id.title_txt);
        year = itemView.findViewById(R.id.description_text);
        this.clickListener = clickListener;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "onClick: ");
                clickListener.onCLick(v, getAdapterPosition());
            }
        });
    }
}
