package org.ebookdroid.ui.viewer.bookmark;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.foobnix.model.Highlight;
import com.foobnix.pdf.info.databinding.SimpleHighlightItemBinding;

import org.ebookdroid.ui.viewer.BaseAdapter;

public class HighlightAdapter extends BaseAdapter<Highlight, SimpleHighlightItemBinding> {
    @Override
    protected SimpleHighlightItemBinding initializeViewBinding(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return SimpleHighlightItemBinding.inflate(inflater, parent, false);
    }

    @Override
    protected DiffUtil.ItemCallback<Highlight> initializeDiffItemCallback() {
        return new DiffUtil.ItemCallback<Highlight>() {
            @Override
            public boolean areItemsTheSame(@NonNull Highlight oldItem, @NonNull Highlight newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Highlight oldItem, @NonNull Highlight newItem) {
                return oldItem.getId() == newItem.getId();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<SimpleHighlightItemBinding> holder, int position) {
        Highlight highlight = differ.getCurrentList().get(position);
        holder.binding.tvChapterName.setText(highlight.getChapterName());
        holder.binding.tvHighlight.setText(highlight.getText());
    }
}
