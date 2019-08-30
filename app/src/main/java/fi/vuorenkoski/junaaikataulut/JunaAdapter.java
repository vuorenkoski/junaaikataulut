package fi.vuorenkoski.junaaikataulut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JunaAdapter extends RecyclerView.Adapter<JunaAdapter.ViewHolder> {
    private ArrayList<Juna> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    JunaAdapter(Context context, ArrayList<Juna> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_rivi, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tunnusView.setText(""+mData.get(position).getTunnus());
        holder.raideView.setText(""+mData.get(position).getRaide());
        holder.lahtoaikaView.setText(mData.get(position).getLahtoAikaStr());
        holder.huomautusView.setText(mData.get(position).getHuomautus());
        holder.saapumisaikaView.setText(mData.get(position).getSaapumisAikaStr());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tunnusView;
        TextView raideView;
        TextView lahtoaikaView;
        TextView huomautusView;
        TextView saapumisaikaView;

        ViewHolder(View itemView) {
            super(itemView);
            tunnusView=itemView.findViewById(R.id.juna_tunnus);
            raideView=itemView.findViewById(R.id.juna_raide);
            lahtoaikaView=itemView.findViewById(R.id.juna_lahtoaika);
            huomautusView=itemView.findViewById(R.id.juna_huomautus);
            saapumisaikaView=itemView.findViewById(R.id.juna_saapumisaika);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).toString();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
