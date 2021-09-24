package org.ebookdroid.ui.viewer.bookmark;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.foobnix.model.Note;
import com.foobnix.pdf.info.databinding.SimpleNoteItemBinding;

import org.ebookdroid.ui.viewer.BaseAdapter;

public class NoteAdapter extends BaseAdapter<Note, SimpleNoteItemBinding> {
    @Override
    protected SimpleNoteItemBinding initializeViewBinding(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return SimpleNoteItemBinding.inflate(inflater, parent, false);
    }

    @Override
    protected DiffUtil.ItemCallback<Note> initializeDiffItemCallback() {
        return new DiffUtil.ItemCallback<Note>() {
            @Override
            public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.getId() == newItem.getId();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<SimpleNoteItemBinding> holder, int position) {
        Note note = differ.getCurrentList().get(position);
        holder.binding.tvChapterName.setText(note.getChapterName());
        holder.binding.tvNote.setText(note.getNote());
    }
}
