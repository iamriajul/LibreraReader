package com.foobnix.pdf.info.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.foobnix.pdf.info.databinding.SimpleChapterItemBinding;

public class ChapterViewHolder extends RecyclerView.ViewHolder {
    SimpleChapterItemBinding binding;

    public ChapterViewHolder(SimpleChapterItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
