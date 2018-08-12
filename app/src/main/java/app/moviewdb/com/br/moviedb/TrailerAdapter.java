package app.moviewdb.com.br.moviedb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context context;
    private List<Trailer> list;

    public TrailerAdapter (Context context, List<Trailer> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_card, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder viewHolder, int position){
        viewHolder.trailerTitle.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView trailerTitle;
        public ImageView trailerThumbnail;


        public ViewHolder(View itemView){
            super(itemView);
            trailerTitle = itemView.findViewById(R.id.trailerTitle);
            trailerThumbnail = itemView.findViewById(R.id.trailerThumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        Trailer selectedTrailer = list.get(position);
                        String videoKey = selectedTrailer.getKey();
                        String youtube = "https://www.youtube.com/watch?v=" + videoKey;

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube));
                        intent.putExtra("VIDEO_ID", videoKey);
                        context.startActivity(intent);
                    }
                }
            });

        }


    }

}