package org.ebookdroid.ui.viewer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.foobnix.pdf.info.R;

import java.util.Objects;

public abstract class BaseDialogFragment extends DialogFragment {

    protected boolean fullscreen = true;
    protected boolean transparent = false;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fullscreen) setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        else setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog);
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        try {
            Window window = Objects.requireNonNull(getDialog()).getWindow();
            if (fullscreen) {
                if (window != null) {
                    window.setLayout(MATCH_PARENT, MATCH_PARENT);
                }
            }

            if (transparent) {
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }
        } catch (NullPointerException e) {
        }
    }
}
