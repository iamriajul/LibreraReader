package org.ebookdroid.ui.viewer.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foobnix.model.Bookmark;
import com.foobnix.pdf.info.databinding.FragmentBookmarkHighlightNoteBinding;

import org.ebookdroid.ui.viewer.BaseDialogFragment;

import java.util.Arrays;

public class BookmarkHighlightAndNoteFragment extends BaseDialogFragment {

    public static final String TAG = "BookmarkHighlightFrag";

    private FragmentBookmarkHighlightNoteBinding binding;

    private BookmarkAdapter bookmarkAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookmarkHighlightNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerViews();
        setupListeners();
        subscribeObservers();
    }

    private void setupRecyclerViews() {
        bookmarkAdapter = new BookmarkAdapter();
        binding.rvBookmark.setAdapter(bookmarkAdapter);
    }

    private void setupListeners() {
        binding.ivBack.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void subscribeObservers() {
        bookmarkAdapter.differ.submitList(Arrays.asList(
                new Bookmark(1, "Book 01", "Chapter 02", 9),
                new Bookmark(2, "Book 01", "Chapter 02", 12),
                new Bookmark(3, "Book 01", "Chapter 03", 34),
                new Bookmark(4, "Book 01", "Chapter 04", 56)
        ));
    }

}
