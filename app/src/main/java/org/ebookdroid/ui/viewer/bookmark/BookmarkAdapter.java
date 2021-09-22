package org.ebookdroid.ui.viewer.bookmark;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.foobnix.model.Bookmark;
import com.foobnix.pdf.info.databinding.SimpleBookmarkItemBinding;

import org.ebookdroid.ui.viewer.BaseAdapter;

public class BookmarkAdapter extends BaseAdapter<Bookmark, SimpleBookmarkItemBinding> {
    @Override
    protected SimpleBookmarkItemBinding initializeViewBinding(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return SimpleBookmarkItemBinding.inflate(inflater, parent, false);
    }

    @Override
    protected DiffUtil.ItemCallback<Bookmark> initializeDiffItemCallback() {
        return new DiffUtil.ItemCallback<Bookmark>() {
            @Override
            public boolean areItemsTheSame(@NonNull Bookmark oldItem, @NonNull Bookmark newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Bookmark oldItem, @NonNull Bookmark newItem) {
                return oldItem.getId() == newItem.getId();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<SimpleBookmarkItemBinding> holder, int position) {
        Bookmark bookmark = differ.getCurrentList().get(position);
        holder.binding.tvBookName.setText(bookmark.getBookName());
        holder.binding.tvPageNumber.setText(String.valueOf(bookmark.getPageNumber()));

    }
}
