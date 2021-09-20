package com.foobnix.pdf.info.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foobnix.pdf.info.databinding.SimpleChapterItemBinding;
import com.foobnix.pdf.info.model.OutlineLinkWrapper;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterViewHolder> {

    private List<OutlineLinkWrapper> outlines;

    private OnChapterItemClickListener listener;

    public void setOnClickListener(OnChapterItemClickListener listener) {
        this.listener = listener;
    }

    public void refreshOutlines(List<OutlineLinkWrapper> outlines) {
        this.outlines = outlines;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ChapterViewHolder(
                SimpleChapterItemBinding.inflate(inflater, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        OutlineLinkWrapper outline = outlines.get(position);
        holder.binding.tvChapterName.setText(outline.getTitleAsString());
        holder.binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.setOnChapterItemClick(outline);
            }
        });
    }

    @Override
    public int getItemCount() {
        return outlines == null ? 0 : outlines.size();
    }
}
