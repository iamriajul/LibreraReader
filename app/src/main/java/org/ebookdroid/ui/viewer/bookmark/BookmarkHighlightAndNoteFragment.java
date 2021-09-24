package org.ebookdroid.ui.viewer.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foobnix.model.Bookmark;
import com.foobnix.model.Highlight;
import com.foobnix.model.Note;
import com.foobnix.pdf.info.R;
import com.foobnix.pdf.info.databinding.FragmentBookmarkHighlightNoteBinding;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.ebookdroid.ui.viewer.BaseDialogFragment;

import java.util.Arrays;

public class BookmarkHighlightAndNoteFragment extends BaseDialogFragment {

    public static final String TAG = "BookmarkHighlightFrag";

    private FragmentBookmarkHighlightNoteBinding binding;

    private BookmarkAdapter bookmarkAdapter;
    private NoteAdapter noteAdapter;
    private HighlightAdapter highlightAdapter;

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
        noteAdapter = new NoteAdapter();
        highlightAdapter = new HighlightAdapter();

        binding.rvBookmark.setAdapter(bookmarkAdapter);
    }

    private void setupListeners() {
        binding.ivBack.setOnClickListener(v -> {
            dismiss();
        });

        binding.buttonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            String title = getString(R.string.bookmark);
            switch (checkedId) {
                case R.id.btnBookmark: {
                    title = getString(R.string.bookmark);
                    binding.rvBookmark.setAdapter(bookmarkAdapter);
                    break;
                }
                case R.id.btnHighLight: {
                    title = getString(R.string.highlight);
                    binding.rvBookmark.setAdapter(highlightAdapter);
                    break;
                }
                case R.id.btnNote: {
                    title = getString(R.string.note);
                    binding.rvBookmark.setAdapter(noteAdapter);
                    break;
                }
            }
            binding.tvTitle.setText(title);
        });


    }

    private void subscribeObservers() {
        bookmarkAdapter.differ.submitList(Arrays.asList(
                new Bookmark(1, "Book 01", "Chapter 02", 9),
                new Bookmark(2, "Book 01", "Chapter 02", 12),
                new Bookmark(3, "Book 01", "Chapter 03", 34),
                new Bookmark(4, "Book 01", "Chapter 04", 56)
        ));


        noteAdapter.differ.submitList(Arrays.asList(
                new Note(1, "Book 01", "Chapter 02", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque vitae ipsum aliquam, accumsan elit placerat, egestas ex. Curabitur sit amet ullamcorper massa. Vivamus vel mollis orci. In faucibus velit quis malesuada tincidunt. Nunc vel ligula quis dolor aliquam consequat id semper velit. Cras cursus ex non aliquet laoreet", 9, 0),
                new Note(2, "Book 01", "Chapter 02", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque vitae ipsum aliquam, accumsan elit placerat, egestas ex. Curabitur sit amet ullamcorper massa. Vivamus vel mollis orci. In faucibus velit quis malesuada tincidunt. Nunc vel ligula quis dolor aliquam consequat id semper velit. Cras cursus ex non aliquet laoreet", 12, 0)
        ));

        highlightAdapter.differ.submitList(Arrays.asList(
                new Highlight(1, "Book 01", "Chapter 02", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque vitae ipsum aliquam, accumsan elit placerat, egestas ex. Curabitur sit amet ullamcorper massa. Vivamus vel mollis orci. In faucibus velit quis malesuada tincidunt. Nunc vel ligula quis dolor aliquam consequat id semper velit. Cras cursus ex non aliquet laoreet", 9, 0),
                new Highlight(2, "Book 01", "Chapter 02", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque vitae ipsum aliquam, accumsan elit placerat, egestas ex. Curabitur sit amet ullamcorper massa. Vivamus vel mollis orci. In faucibus velit quis malesuada tincidunt. Nunc vel ligula quis dolor aliquam consequat id semper velit. Cras cursus ex non aliquet laoreet", 12, 0)
        ));

    }

}
