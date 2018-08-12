package app.moviewdb.com.br.moviedb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private List<Movie> list;

    public MovieAdapter(Context context, List<Movie> list){
        this.context = context;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        //Glide.with(context).load(list.get(i).getPosterPath()).placeholder(R.drawable.load).into(viewHolder.thumbnail);
        Glide.with(context).load(list.get(position).getPosterPath()).into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount(){

        return list.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView title;
        //public TextView userRating;
        public ImageView thumbnail;

        public ViewHolder (View itemView){
            super(itemView);

            //this.title = itemView.findViewById(R.id.title);
            //this.userRating = itemView.findViewById(R.id.userRating);
            this.thumbnail = itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        Movie selectedMovie = list.get(position);
                        Intent intent = new Intent(context, InfoActivity.class);
                        intent.putExtra("movie", selectedMovie);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
