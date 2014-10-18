package com.github.airk.easynote.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.airk.easynote.R;
import com.github.airk.easynote.bean.Note;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kevin on 2014/10/18.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    Context context;
    int width;

    List<Note> notes;

    public interface ClickListener {
        public void onItemClicked(long timestamp);
    }
    private ClickListener listener;

    public NoteAdapter(Context context, ClickListener listener) {
        this.context = context;
        this.listener = listener;
        width = (context.getResources().getDisplayMetrics().widthPixels
                - context.getResources().getDimensionPixelSize(R.dimen.screen_space)) / 2;
    }

    public void setData(List<Note> data) {
        notes = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_note, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final long timestamp = notes.get(position).timestamp;
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(timestamp);
            }
        });
        viewHolder.title.setText(notes.get(position).title);
        viewHolder.content.setText(notes.get(position).content);
    }

    @Override
    public int getItemCount() {
        if (notes == null) {
            return 0;
        }
        return notes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.card_view)
        CardView cardView;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.content)
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

            cardView.getLayoutParams().width = width;
        }
    }
}
