/**
 *
 */
package com.foobnix.pdf.info.wrapper;

import static com.foobnix.pdf.info.view.BrightnessHelper.appBrightness;
import static com.foobnix.pdf.info.view.BrightnessHelper.applyBrightNess;
import static com.foobnix.pdf.info.view.BrightnessHelper.applyBrigtness;
import static com.foobnix.pdf.info.view.BrightnessHelper.blueLightAlpha;
import static com.foobnix.pdf.info.view.BrightnessHelper.getSystemBrigtnessInt;
import static com.foobnix.pdf.info.view.BrightnessHelper.isEnableBlueFilter;

import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.foobnix.android.utils.Apps;
import com.foobnix.android.utils.Dips;
import com.foobnix.android.utils.IntegerResponse;
import com.foobnix.android.utils.Keyboards;
import com.foobnix.android.utils.LOG;
import com.foobnix.android.utils.ResultResponse;
import com.foobnix.android.utils.TxtUtils;
import com.foobnix.android.utils.Vibro;
import com.foobnix.model.AppSP;
import com.foobnix.model.AppState;
import com.foobnix.pdf.info.AppsConfig;
import com.foobnix.pdf.info.BookmarksData;
import com.foobnix.pdf.info.DictsHelper;
import com.foobnix.pdf.info.ExtUtils;
import com.foobnix.pdf.info.OutlineHelper;
import com.foobnix.pdf.info.OutlineHelper.Info;
import com.foobnix.pdf.info.R;
import com.foobnix.pdf.info.TintUtil;
import com.foobnix.pdf.info.UiSystemUtils;
import com.foobnix.pdf.info.adapters.ChapterAdapter;
import com.foobnix.pdf.info.databinding.ActivityVerticalViewBinding;
import com.foobnix.pdf.info.model.BookCSS;
import com.foobnix.pdf.info.model.OutlineLinkWrapper;
import com.foobnix.pdf.info.presentation.OutlineAdapter;
import com.foobnix.pdf.info.view.AnchorHelper;
import com.foobnix.pdf.info.view.BrightnessHelper;
import com.foobnix.pdf.info.view.CustomSeek;
import com.foobnix.pdf.info.view.DragingDialogs;
import com.foobnix.pdf.info.view.DrawView;
import com.foobnix.pdf.info.view.MyPopupMenu;
import com.foobnix.pdf.info.widget.DraggbleTouchListener;
import com.foobnix.pdf.info.widget.ShareDialog;
import com.foobnix.pdf.search.activity.msg.MessagePageXY;
import com.foobnix.pdf.search.activity.msg.MessegeBrightness;
import com.foobnix.pdf.search.view.CloseAppDialog;
import com.foobnix.sys.TempHolder;
import com.foobnix.tts.MessagePageNumber;
import com.foobnix.tts.TTSEngine;
import com.foobnix.tts.TTSService;
import com.foobnix.tts.TtsStatus;
import com.foobnix.ui2.AppDB;
import com.foobnix.ui2.MainTabs2;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.ebookdroid.BookType;
import org.ebookdroid.LibreraApp;
import org.ebookdroid.ui.viewer.bookmark.BookmarkHighlightAndNoteFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author iivanenko
 */
public class DocumentWrapperUI {

    final DocumentController dc;
    final Handler handler = new Handler();
    final Handler handlerTimer = new Handler();
    public View.OnClickListener onLockUnlock = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            AppSP.get().isLocked = !AppSP.get().isLocked;
            updateLock();
        }
    };
    public View.OnClickListener onNextType = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            LOG.d("DEBUG", "Click");
            doChooseNextType(arg0);
        }
    };
    public View.OnClickListener onSun = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            arg0.setEnabled(false);
            dc.onNightMode();
        }
    };
    public View.OnClickListener toPage = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            dc.toPageDialog();
        }
    };
    public View.OnClickListener onClose = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            //ImageLoader.getInstance().clearAllTasks();
            //Glide.with(LibreraApp.context).
            closeDialogs();
            closeAndRunList();
        }
    };
    public View.OnClickListener onMoveLeft = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            dc.onSrollLeft();
        }
    };
    public View.OnClickListener onMoveCenter = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            dc.alignDocument();
        }
    };
    public View.OnClickListener onMoveRight = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            dc.onSrollRight();
        }
    };
    public View.OnClickListener onNextPage = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            nextChose(false);
        }
    };
    public View.OnClickListener onPrevPage = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            prevChose(false);
        }
    };
    public View.OnClickListener onPlus = new View.OnClickListener() {

        public void onClick(final View arg0) {
            dc.onZoomInc();
        }
    };
    public View.OnClickListener onMinus = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            dc.onZoomDec();
        }
    };
    public View.OnClickListener onModeChangeClick = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            MyPopupMenu p = new MyPopupMenu(v.getContext(), v);

            p.getMenu().add(R.string.one_page).setIcon(R.drawable.glyphicons_two_page_one).setOnMenuItemClickListener(new OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    closeDialogs();
//                    onModeChange.setImageResource(R.drawable.glyphicons_two_page_one);
                    AppSP.get().isCut = !false;
                    onCut.onClick(null);
                    hideShowEditIcon();
                    return false;
                }
            });
            p.getMenu().add(R.string.half_page).setIcon(R.drawable.glyphicons_page_split).setOnMenuItemClickListener(new OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    closeDialogs();
//                    onModeChange.setImageResource(R.drawable.glyphicons_page_split);
                    AppSP.get().isCut = !true;
                    onCut.onClick(null);
                    hideShowEditIcon();
                    return false;
                }
            });
            p.show();
            Keyboards.hideNavigation(dc.getActivity());

        }
    };
    AppCompatActivity a;
    ActivityVerticalViewBinding binding;
    String bookTitle;
    View overlay;
    Toolbar toolbar;
    DrawerLayout mainDrawer;
    LinearLayoutCompat llPages;
    public View.OnLongClickListener onCloseLongClick = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(final View v) {
            Vibro.vibrate();
            CloseAppDialog.showOnLongClickDialog(a, v, getController());
            hideAds();
            return true;
        }
    };
    View line1, line2, parentParent;
    FrameLayout anchor;
    public View.OnClickListener onShowContext = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            DragingDialogs.showContent(anchor, dc);
        }
    };
    public View.OnClickListener onBCclick = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            DragingDialogs.contrastAndBrigtness(anchor, dc, new Runnable() {

                @Override
                public void run() {
//update BC
                    dc.updateRendering();
                }
            }, null);
        }
    };
    public View.OnClickListener onShowSearch = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            showSearchDialog();
        }

    };
    ImageView anchorX, anchorY;
    DrawView drawView;
    public View.OnClickListener onShowHideEditPanel = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            if (AppSP.get().isCrop) {
                onCrop.onClick(null);
            }

            DragingDialogs.editColorsPanel(anchor, dc, drawView, false);
        }
    };
    public View.OnClickListener onHideShowToolBar = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            LOG.d("DEBUG", "Click");
            doHideShowToolBar();
        }
    };
    String quickBookmark;
    Runnable clearFlags = new Runnable() {

        @Override
        public void run() {
            try {
                dc.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                LOG.d("FLAG clearFlags", "FLAG_KEEP_SCREEN_ON", "clear");
            } catch (Exception e) {
                LOG.e(e);
            }
        }
    };
    Runnable updateTimePower = new Runnable() {

        @Override
        public void run() {
            try {

                String currentTime = UiSystemUtils.getSystemTime(dc.getActivity());

                int myLevel = UiSystemUtils.getPowerLevel(dc.getActivity());

            } catch (Exception e) {
                LOG.e(e);
            }
            LOG.d("Update time and power");
            handlerTimer.postDelayed(updateTimePower, AppState.APP_UPDATE_TIME_IN_UI);

        }
    };
    View.OnClickListener onRecent = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DragingDialogs.recentBooks(anchor, dc);
        }
    };
    View.OnClickListener onTextToSpeach = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            if (AppSP.get().isCut) {
//                onModeChange.setImageResource(R.drawable.glyphicons_two_page_one);
                onCut.onClick(null);
                return;
            }
            DragingDialogs.textToSpeachDialog(anchor, dc);
        }
    };
    View.OnClickListener onThumbnail = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            DragingDialogs.gotoPageDialog(anchor, dc);
        }
    };
    SeekBar.OnSeekBarChangeListener onSeek = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(final SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(final SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
            dc.onGoToPage(progress + 1);
            Apps.accessibilityText(a, a.getString(R.string.m_current_page) + " " + dc.getCurentPageFirst1());
            //updateUI();
        }
    };
    SeekBar.OnSeekBarChangeListener onSpeed = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(final SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(final SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
            AppState.get().continuousAutoScrollSpeed = progress + 1;
            updateSpeedLabel();

            // hideSeekBarInReadMode();
        }
    };
    Runnable hideSeekBar = new Runnable() {

        @Override
        public void run() {
            if (!dc.isMusicianMode()) {
            }

        }
    };
    Runnable onRefresh = new Runnable() {

        @Override
        public void run() {
            dc.saveCurrentPageAsync();
            initToolBarPlusMinus();
            updateSeekBarColorAndSize();
            hideShow();
            updateUI();
            TTSEngine.get().stop();
            BrightnessHelper.updateOverlay(overlay);
            showPagesHelper();
            hideShowPrevNext();
        }
    };
    public View.OnClickListener onAutoScroll = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            onContinuousAutoScrollClick();
        }
    };
    public View.OnClickListener onLinkHistory = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            dc.onLinkHistory();
            updateUI();
        }
    };
    public View.OnClickListener onBookmarks = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            DragingDialogs.showBookmarksDialog(anchor, dc, new Runnable() {

                @Override
                public void run() {
                    showHideHistory();
                    showPagesHelper();
                    updateUI();
                }
            });
        }
    };
    public View.OnLongClickListener onBookmarksLong = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(final View arg0) {
            DragingDialogs.addBookmarksLong(anchor, dc);
            showPagesHelper();
            return true;
        }
    };
    public View.OnClickListener onMenu = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            LOG.d("DEBUG", "Click");
            doShowHideWrapperControlls();
        }
    };
    public View.OnClickListener onReverseKeys = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            LOG.d("DEBUG", "Click");
            AppState.get().isReverseKeys = !AppState.get().isReverseKeys;
            updateUI();
        }
    };
    public View.OnClickListener onFull = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            DocumentController.showFullScreenPopup(dc.getActivity(), v, id -> {
                AppState.get().fullScreenMode = id;

                if (dc.isTextFormat()) {
                    onRefresh.run();
                    dc.restartActivity();
                }
                DocumentController.chooseFullScreen(a, AppState.get().fullScreenMode);
                return true;
            }, AppState.get().fullScreenMode);
        }
    };
    public View.OnClickListener onCrop = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            DragingDialogs.customCropDialog(anchor, dc, new Runnable() {

                @Override
                public void run() {
                    dc.onCrop();
                    updateUI();

                    AppState.get().isEditMode = false;
                    hideShow();
                    hideShowEditIcon();
                }
            });
        }
    };
    public View.OnLongClickListener onCropLong = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            AppSP.get().isCrop = !AppSP.get().isCrop;

            dc.onCrop();
            updateUI();

            AppState.get().isEditMode = false;
            hideShow();
            hideShowEditIcon();
            return true;
        }
    };
    public View.OnClickListener onCut = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            AppSP.get().isCrop = false; // no crop with cut
            AppState.get().cutP = 50;
            AppSP.get().isCut = !AppSP.get().isCut;


            dc.onCrop();// crop false
            dc.updateRendering();
            dc.alignDocument();

            updateUI();

//            titleBar.setOnTouchListener(new HorizontallSeekTouchEventListener(onSeek, dc.getPageCount(), false));

        }
    };
    public View.OnClickListener onPrefTop = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            DragingDialogs.preferences(anchor, dc, onRefresh, new Runnable() {

                @Override
                public void run() {
                    updateUI();

                }
            });
        }
    };
    Runnable updateUIRunnable = new Runnable() {

        @Override
        public void run() {
            updateUI();
        }
    };
    View.OnClickListener onItemMenu = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            ShareDialog.show(a, dc.getCurrentBook(), new Runnable() {

                @Override
                public void run() {
                    if (dc.getCurrentBook().delete()) {
                        TempHolder.listHash++;
                        AppDB.get().deleteBy(dc.getCurrentBook().getPath());
                        dc.getActivity().finish();
                    }
                }
            }, dc.getCurentPage() - 1, dc, new Runnable() {

                @Override
                public void run() {
                    hideShow();

                }
            });
            Keyboards.hideNavigation(a);
            hideAds();
        }
    };
    View.OnClickListener onLirbiLogoClick = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            doShowHideWrapperControlls();
        }
    };
    View.OnClickListener onGoToPAge1 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            dc.onScrollY(0);
            updateUI();
        }
    };
    public View.OnClickListener onNormalMode = new View.OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            AppSP.get().readingMode = AppState.READING_MODE_BOOK;
            initUI(a, binding);
            hideShow();
        }
    };

    public DocumentWrapperUI(final DocumentController controller) {
        AppState.get().annotationDrawColor = "";
        AppState.get().editWith = AppState.EDIT_NONE;

        this.dc = controller;
        controller.setUi(this);

        EventBus.getDefault().register(this);

    }

    public static boolean isCJK(int ch) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
        if (Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(block) || Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS.equals(block) || Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(block)) {
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPageNumber(MessagePageNumber event) {
        try {
            if (dc != null) {
                dc.onGoToPage(event.getPage() + 1);
//                ttsActive.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            LOG.e(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTTSStatus(TtsStatus status) {
        try {
//            ttsActive.setVisibility(TxtUtils.visibleIf(!TTSEngine.get().isShutdown()));
        } catch (Exception e) {
            LOG.e(e);
        }

    }

    public void onSingleTap() {
        if (dc.isMusicianMode()) {
            onContinuousAutoScrollClick();
        } else {
            doShowHideWrapperControlls();
        }
    }

    // public void changeAutoScrollButton() {
    // if (AppState.get().isAutoScroll) {
    // autoScroll.setImageResource(android.R.drawable.ic_media_pause);
    // seekSpeedLayot.setVisibility(View.VISIBLE);
    // } else {
    // autoScroll.setImageResource(android.R.drawable.ic_media_play);
    // seekSpeedLayot.setVisibility(View.GONE);
    // }
    //
    // }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onLongPress(MotionEvent ev) {
        if (dc.isTextFormat() && TxtUtils.isFooterNote(AppState.get().selectedText)) {
            DragingDialogs.showFootNotes(anchor, dc, new Runnable() {

                @Override
                public void run() {
                    showHideHistory();
                }
            });
        } else {
            if (AppState.get().isRememberDictionary) {
                DictsHelper.runIntent(dc.getActivity(), AppState.get().selectedText);
                dc.clearSelectedText();
            } else {
                DragingDialogs.selectTextMenu(anchor, dc, true, updateUIRunnable);
            }
        }
    }

    public void showSelectTextMenu() {
        DragingDialogs.selectTextMenu(anchor, dc, true, updateUIRunnable);

    }

    public boolean checkBack(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == 0) {
            keyCode = event.getScanCode();
        }

        if (anchor == null) {
            closeAndRunList();
            return true;
        }
        if (AppState.get().isContinuousAutoScroll) {
            AppState.get().isContinuousAutoScroll = false;
            updateUI();
            return true;
        }

        if (dc.floatingBookmark != null) {
            dc.floatingBookmark = null;
            onRefresh.run();
            return true;
        }

        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (closeDialogs()) {
                return true;
            } else if (!dc.getLinkHistory().isEmpty()) {
                dc.onLinkHistory();
                return true;
            }
        }
        return false;
    }

    public boolean dispatchKeyEventUp(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == 0) {
            keyCode = event.getScanCode();
        }

        if (KeyEvent.KEYCODE_MENU == keyCode || KeyEvent.KEYCODE_M == keyCode) {
            doShowHideWrapperControlls();
            return true;
        }

        return false;

    }

    public boolean dispatchKeyEventDown(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == 0) {
            keyCode = event.getScanCode();
        }

        if (keyCode >= KeyEvent.KEYCODE_1 && keyCode <= KeyEvent.KEYCODE_9) {
            dc.onGoToPage(keyCode - KeyEvent.KEYCODE_1 + 1);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_0) {
            dc.toPageDialog();
            return true;
        }

        if (KeyEvent.KEYCODE_F == keyCode) {
            dc.alignDocument();
            return true;
        }

        if (KeyEvent.KEYCODE_ENTER == keyCode) {
            closeDialogs();
            AppState.get().isEditMode = false;
            hideShow();
            if (TTSEngine.get().isTempPausing()) {
                TTSService.playPause(dc.getActivity(), dc);
            } else {
                onContinuousAutoScrollClick();
            }
            return true;
        }

        if (KeyEvent.KEYCODE_S == keyCode || KeyEvent.KEYCODE_SEARCH == keyCode) {
            showSearchDialog();
            return true;
        }

        if (KeyEvent.KEYCODE_A == keyCode || KeyEvent.KEYCODE_SPACE == keyCode) {
            onContinuousAutoScrollClick();
            return true;
        }

        if (AppState.get().isUseVolumeKeys && AppState.get().isZoomInOutWithVolueKeys) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                dc.onZoomInc();
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                dc.onZoomDec();
                return true;
            }

        }

        if (AppState.get().isScrollSpeedByVolumeKeys && AppState.get().isUseVolumeKeys && AppState.get().isContinuousAutoScroll) {
            if (KeyEvent.KEYCODE_VOLUME_UP == keyCode) {
                if (AppState.get().continuousAutoScrollSpeed > 1) {
                    AppState.get().continuousAutoScrollSpeed -= 1;
                    dc.onContinuousAutoScroll();
                    updateUI();
                }
                return true;
            }
            if (KeyEvent.KEYCODE_VOLUME_DOWN == keyCode) {
                if (AppState.get().continuousAutoScrollSpeed <= AppState.MAX_SPEED) {
                    AppState.get().continuousAutoScrollSpeed += 1;
                }
                dc.onContinuousAutoScroll();
                updateUI();
                return true;
            }
        }

        Log.d("TAG", "dispatchKeyEventDown: " + AppState.get().isUseVolumeKeys);
        Log.d("TAG", "dispatchKeyEventDown: " + AppState.get().getNextKeys());
        Log.d("TAG", "dispatchKeyEventDown: " + AppState.get().getPrevKeys());
        if (!TTSEngine.get().isPlaying()) {
            if (AppState.get().isUseVolumeKeys && AppState.get().getNextKeys().contains(keyCode)) {
                if (closeDialogs()) {
                    return true;
                }
                nextChose(false, event.getRepeatCount());
                return true;
            }

            if (AppState.get().isUseVolumeKeys && AppState.get().getPrevKeys().contains(keyCode)) {
                if (closeDialogs()) {
                    return true;
                }
                prevChose(false, event.getRepeatCount());
                return true;
            }
        }

        if (AppState.get().isUseVolumeKeys && KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
            if (TTSEngine.get().isPlaying()) {
                if (AppState.get().isFastBookmarkByTTS) {
                    TTSEngine.get().fastTTSBookmakr(dc);
                } else {
                    TTSEngine.get().stop();
                }
            } else {
                //TTSEngine.get().playCurrent();
                TTSService.playPause(dc.getActivity(), dc);
                anchor.setTag("");
            }
            //TTSNotification.showLast();
            //DragingDialogs.textToSpeachDialog(anchor, dc);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            dc.onScrollDown();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            dc.onScrollUp();
            return true;
        }

        if (keyCode == 70) {
            dc.onZoomInc();
            return true;
        }

        if (keyCode == 69) {
            dc.onZoomDec();
            return true;
        }

//        if (PageImageState.get().hasSelectedWords()) {
//            dc.clearSelectedText();
//            return true;
//        }

        return false;

    }

    public void closeAndRunList() {
        EventBus.getDefault().unregister(this);

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

//        if (titleBar != null) {
//            titleBar.removeCallbacks(null);
//        }
        dc.saveCurrentPageAsync();
        dc.onCloseActivityAdnShowInterstial();
        dc.closeActivity();

    }

    public void updateSpeedLabel() {

        Info info = OutlineHelper.getForamtingInfo(dc, true);
        String pages = "Pages " + info.textMax + " of " + info.textPage;
        binding.pageInfo.tvPages.setText(pages);

        showChapter();

    }

    public void updateUI() {
        final int max = dc.getPageCount();
        final int current = dc.getCurentPage();

        updateSpeedLabel();

        binding.pageInfo.sbPages.setOnSeekBarChangeListener(null);
        binding.pageInfo.sbPages.setMax(max - 1);
        binding.pageInfo.sbPages.setProgress(current - 1);
        binding.pageInfo.sbPages.setOnSeekBarChangeListener(onSeek);
//
//        speedSeekBar.setOnSeekBarChangeListener(null);
//        speedSeekBar.setMax(AppState.MAX_SPEED);
//        speedSeekBar.setProgress(AppState.get().autoScrollSpeed);
//        speedSeekBar.setOnSeekBarChangeListener(onSpeed);

        showChapter();

        hideShow();
        initNextType();
        initToolBarPlusMinus();

        showHideHistory();

        updateLock();


        if (dc.isTextFormat()) {
//            onModeChange.setVisibility(View.GONE);
            if (Dips.isEInk() || AppState.get().appTheme == AppState.THEME_INK || AppState.get().isEnableBC) {
//                onBC.setVisibility(View.VISIBLE);
            } else {
//                onBC.setVisibility(View.GONE);
            }
            if (AppSP.get().isCrop) {

            }
            if (AppSP.get().isCut) {

            }
        }


        if (AppState.get().inactivityTime > 0) {
            dc.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            LOG.d("FLAG addFlags", "FLAG_KEEP_SCREEN_ON", "add", AppState.get().inactivityTime);
            handler.removeCallbacks(clearFlags);
            handler.postDelayed(clearFlags, TimeUnit.MINUTES.toMillis(AppState.get().inactivityTime));
        }

        if (AppState.get().isContinuousAutoScroll) {
//            pagesBookmark.setVisibility(View.GONE);
        } else {
//            pagesBookmark.setVisibility(View.VISIBLE);
        }


        dc.saveCurrentPage();
        //SharedBooks.save(bs);

        LOG.d("dc.floatingBookmark", dc.floatingBookmark);
        if (dc.floatingBookmark != null) {
            dc.floatingBookmark.p = dc.getPercentage();

            BookmarksData.get().add(dc.floatingBookmark);
            showPagesHelper();
        } else {
        }

        try {
            if (!dc.isTextFormat()) {
//                TempHolder.get().documentTitleBarHeight = documentTitleBar.getHeight();
            } else {
                TempHolder.get().documentTitleBarHeight = 0;
            }
        } catch (Exception e) {
            TempHolder.get().documentTitleBarHeight = 0;
        }

        final int dashHeight = Math.min(Dips.dpToPx(220), Dips.screenHeight() / 3);
        line1.getLayoutParams().height = dashHeight;
        line2.getLayoutParams().height = dashHeight;

        line1.setLayoutParams(line1.getLayoutParams());
        line2.setLayoutParams(line2.getLayoutParams());

    }

    public void hideShowPrevNext() {
        if (dc.isMusicianMode()) {
            if (AppState.get().isShowRectangularTapZones) {
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
            } else {
                line1.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);

            }
        }
    }

    public void showChapter() {

        String chapterName = "";
        if (TxtUtils.isNotEmpty(dc.getCurrentChapter())) {
            chapterName = dc.getCurrentChapter();
        } else {
            chapterName = bookTitle;
        }
        binding.pageInfo.tvChapterName.setText(chapterName);

    }

    public void updateLock() {
        // int mode = View.VISIBLE;

        if (AppSP.get().isLocked) {
        } else {
        }

    }

    public void showHideHistory() {
//        linkHistory.setVisibility(dc.getLinkHistory().isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Subscribe
    public void showHideTextSelectors(MessagePageXY event) {
        if (event.getType() == MessagePageXY.TYPE_HIDE) {
            anchorX.setVisibility(View.GONE);
            anchorY.setVisibility(View.GONE);

        }
        if (event.getType() == MessagePageXY.TYPE_SHOW) {
            anchorX.setVisibility(View.VISIBLE);
            anchorY.setVisibility(View.VISIBLE);

            AnchorHelper.setXY(anchorX, event.getX(), event.getY());
            AnchorHelper.setXY(anchorY, event.getX1(), event.getY1());

        }

    }


    public void initUI(final AppCompatActivity a, final ActivityVerticalViewBinding binding) {
        this.a = a;
        this.binding = binding;
        quickBookmark = a.getString(R.string.fast_bookmark);
        toolbar = a.findViewById(R.id.titleToolbar);
        llPages = a.findViewById(R.id.llPages);
        mainDrawer = a.findViewById(R.id.main_drawer);
        mainDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setupSettingsView(a);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_more) {
                toggleSettingDrawer();
                return true;
            }
            return false;
        });


        binding.pageInfo.sbPages.setAccessibilityDelegate(new View.AccessibilityDelegate());


        setupListeners();


//        seekBar = (SeekBar) a.findViewById(R.id.seekBar);
//        seekBar.setAccessibilityDelegate(new View.AccessibilityDelegate());
//        speedSeekBar = (SeekBar) a.findViewById(R.id.seekBarSpeed);
        anchor = (FrameLayout) a.findViewById(R.id.anchor);

        anchorX = (ImageView) a.findViewById(R.id.anchorX);
        anchorY = (ImageView) a.findViewById(R.id.anchorY);

//        floatingBookmarkTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dc.floatingBookmark = null;
//                onRefresh.run();
//                onBookmarks.onClick(v);
//            }
//        });
//        floatingBookmarkTextView.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                dc.floatingBookmark = null;
//                onRefresh.run();
//                return true;
//            }
//        });

        TintUtil.setTintImageWithAlpha(anchorX, AppState.get().isDayNotInvert ? Color.BLUE : Color.YELLOW, 150);
        TintUtil.setTintImageWithAlpha(anchorY, AppState.get().isDayNotInvert ? Color.BLUE : Color.YELLOW, 150);

        anchorX.setVisibility(View.GONE);
        anchorY.setVisibility(View.GONE);

        DraggbleTouchListener touch1 = new DraggbleTouchListener(anchorX, (View) anchorX.getParent());
        DraggbleTouchListener touch2 = new DraggbleTouchListener(anchorY, (View) anchorY.getParent());

        final Runnable onMoveActionOnce = new Runnable() {

            @Override
            public void run() {
                float x = anchorX.getX() + anchorX.getWidth();
                float y = anchorX.getY() + anchorX.getHeight() / 2;

                float x1 = anchorY.getX();
                float y1 = anchorY.getY();
                EventBus.getDefault().post(new MessagePageXY(MessagePageXY.TYPE_SELECT_TEXT, dc.getCurentPage(), x, y, x1, y1));
            }
        };
        final Runnable onMoveAction = new Runnable() {

            @Override
            public void run() {
                handler.removeCallbacks(onMoveActionOnce);
                handler.postDelayed(onMoveActionOnce, 150);

            }
        };

        Runnable onMoveFinish = new Runnable() {

            @Override
            public void run() {
                onMoveAction.run();
                if (AppState.get().isRememberDictionary) {
                    final String text = AppState.get().selectedText;
                    DictsHelper.runIntent(dc.getActivity(), text);
                    dc.clearSelectedText();

                } else {
                    DragingDialogs.selectTextMenu(anchor, dc, true, updateUIRunnable);
                }

            }
        };

        touch1.setOnMoveFinish(onMoveFinish);
        touch2.setOnMoveFinish(onMoveFinish);

        touch1.setOnMove(onMoveAction);
        touch2.setOnMove(onMoveAction);

//        titleBar = a.findViewById(R.id.titleBar);
//        titleBar.setOnClickListener(onMenu);

        overlay = a.findViewById(R.id.overlay);
        overlay.setVisibility(View.VISIBLE);

        line1 = a.findViewById(R.id.line1);
        line1.setOnClickListener(onPrevPage);

        line2 = a.findViewById(R.id.line2);
        line2.setOnClickListener(onNextPage);


//        lirbiLogo = (TextView) a.findViewById(R.id.lirbiLogo);
//        lirbiLogo.setText(AppState.get().musicText);
//        lirbiLogo.setOnClickListener(onLirbiLogoClick);

//        editTop2 = (ImageView) a.findViewById(R.id.editTop2);
//        editTop2.setOnClickListener(onShowHideEditPanel);

        // nextPage.setOnClickListener(onNextPage);
        // prevPage.setOnClickListener(onPrevPage);


        // if (Dips.isEInk(dc.getActivity())) {
        // brightness.setVisibility(View.GONE);
        // AppState.get().isDayNotInvert = true;
        // }

//        a.findViewById(R.id.toPage).setOnClickListener(toPage);


        if (AppSP.get().isCut) {

        }


//        onModeChange = (ImageView) a.findViewById(R.id.onModeChange);
//        onModeChange.setOnClickListener(onModeChangeClick);
//        onModeChange.setImageResource(AppSP.get().isCut ? R.drawable.glyphicons_page_split : R.drawable.glyphicons_two_page_one);

//        showSearch = (ImageView) a.findViewById(R.id.onShowSearch);
//        showSearch.setOnClickListener(onShowSearch);
//        autoScroll = ((ImageView) a.findViewById(R.id.autoScroll));
//        autoScroll.setOnClickListener(onAutoScroll);

        // ((View)
        // a.findViewById(R.id.onScreenMode)).setOnClickListener(onScreenMode);

//        nextTypeBootom = (TextView) a.findViewById(R.id.nextTypeBootom);
//
//        nextTypeBootom.setOnClickListener(onNextType);

//        onDocDontext = (ImageView) a.findViewById(R.id.onDocDontext);
//        onDocDontext.setOnClickListener(onShowContext);
//
//        ttsActive = a.findViewById(R.id.ttsActive);
//        ttsActive.setDC(dc);
//        ttsActive.addOnDialogRunnable(new Runnable() {

//            @Override
//            public void run() {
//                AppState.get().isEditMode = true;
//                hideShow();
//                DragingDialogs.textToSpeachDialog(anchor, dc);
//            }
//        });

//        textToSpeach = (ImageView) a.findViewById(R.id.textToSpeach);
//        textToSpeach.setOnClickListener(onTextToSpeach);
//        textToSpeach.setOnLongClickListener(v -> {
//            AlertDialogs.showTTSDebug(dc);
//            hideShow();
//            return true;
//        });

        drawView = (DrawView) a.findViewById(R.id.drawView);

//        View bookmarks = a.findViewById(R.id.onBookmarks);
//        bookmarks.setOnClickListener(onBookmarks);
//        bookmarks.setOnLongClickListener(onBookmarksLong);

//        toastBrightnessText = (TextView) a.findViewById(R.id.toastBrightnessText);
//        toastBrightnessText.setVisibility(View.GONE);
//        TintUtil.setDrawableTint(toastBrightnessText.getCompoundDrawables()[0], Color.WHITE);

//        TextView modeName = (TextView) a.findViewById(R.id.modeName);
//        modeName.setText(AppState.get().nameVerticalMode);

//        bookName = (TextView) a.findViewById(R.id.bookName);


//        View thumbnail = a.findViewById(R.id.thumbnail);
//        thumbnail.setOnClickListener(onThumbnail);

//        View bookMenu = a.findViewById(R.id.bookMenu);
//        bookMenu.setOnClickListener(onItemMenu);
//        modeName.setOnClickListener(onItemMenu);
//        modeName.setOnLongClickListener(onCloseLongClick);
//        modeName.setOnLongClickListener(new OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                dc.onChangeTextSelection();
//                AppState.get().isEditMode = false;
//                hideShow();
//                return true;
//            }
//        });


        AppState.get().isContinuousAutoScroll = false;

//        ImageView recent = (ImageView) a.findViewById(R.id.onRecent);
//        recent.setOnClickListener(onRecent);

        anchor.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onGlobalLayout() {
                if (anchor.getVisibility() == View.VISIBLE || dc.isMusicianMode()) {
                } else {
                    if (AppState.get().isEditMode) {
                    } else {
                    }
                }

                if (anchor.getX() < 0) {
                    anchor.setX(0);
                }
                if (anchor.getY() < 0) {
                    anchor.setY(0);
                }
            }

        });
        updateSeekBarColorAndSize();
        BrightnessHelper.updateOverlay(overlay);

        // bottom 1
        TintUtil.setStatusBarColor(a);

//        TintUtil.setTintBgSimple(a.findViewById(R.id.menuLayout), AppState.get().transparencyUI);
//        TintUtil.setTintBgSimple(a.findViewById(R.id.bottomBar1), AppState.get().transparencyUI);
//        TintUtil.setBackgroundFillColorBottomRight(lirbiLogo, ColorUtils.setAlphaComponent(TintUtil.color, AppState.get().transparencyUI));
        tintSpeed();

//        pageshelper = (LinearLayout) a.findViewById(R.id.pageshelper);
//        musicButtonPanel = a.findViewById(R.id.musicButtonPanel);
//        musicButtonPanel.setVisibility(View.GONE);

//        pagesBookmark = a.findViewById(R.id.pagesBookmark);
//        pagesBookmark.setOnClickListener(onBookmarks);
//        pagesBookmark.setOnLongClickListener(onBookmarksLong);

        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);

        if (dc.isMusicianMode()) {
            AppState.get().isEditMode = false;
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);

//            modeName.setText(AppState.get().nameMusicianMode);
        }


        hideShowPrevNext();
        dc.initAnchor(anchor);

    }


    private void setupListeners() {

        /*binding.brightness.cvWhite.setOnClickListener {
            isNightMode = true
            toggleBlackTheme()
            UiUtil.setColorResToDrawable(
                    R.color.white,
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_dark_mode)
            )
            UiUtil.setColorIntToDrawable(
                    config.themeColor,
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_light_mode)
            )
//            updateThemes()
            setAudioPlayerBackground()
        }

        binding.brightness.cvBlack.setOnClickListener {
            isNightMode = false
            toggleBlackTheme()
            UiUtil.setColorResToDrawable(
                    R.color.black,
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_light_mode)
            )
            UiUtil.setColorIntToDrawable(
                    config.themeColor,
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_dark_mode)
            )
            setAudioPlayerBackground()
        }*/

        /**
         * Bottom menu item selected listener
         */

        setupRecyclerViews();
        setupBrightNessAndThemeListener();
        setupBottomMenuItemListener();


        /*binding.brightness.sbBrightness.setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                config.currentBrightness = progress
//                AppUtil.saveConfig(applicationContext, config)
//                EventBus.getDefault().post(ReloadDataEvent())
                val layoutParams = window.attributes // Get Params
                layoutParams.screenBrightness = (progress / 255f) // Set Value
                window.attributes = layoutParams

                binding.brightness.tvCurrentBrightness.text = progress.toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })*/

    }


    private final int maxBrightNess = 255;

    private void setupBrightNessAndThemeListener() {


        final int systemBrightNessInt = getSystemBrigtnessInt(a);

        int current = 0;
        if (appBrightness() == AppState.AUTO_BRIGTNESS) {
            current = systemBrightNessInt;
        } else {
            current = isEnableBlueFilter() ? blueLightAlpha() * -1 : appBrightness();
        }

        binding.brightness.tvCurrentBrightness.setText(String.valueOf(current));
        binding.brightness.sbBrightness.setMax(maxBrightNess);
        binding.brightness.sbBrightness.setProgress(current);

        WindowManager.LayoutParams layoutParams = a.getWindow().getAttributes();
        layoutParams.screenBrightness = current;
        a.getWindow().setAttributes(layoutParams);

        binding.brightness.sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                WindowManager.LayoutParams layoutParams = a.getWindow().getAttributes(); // Get Params
                layoutParams.screenBrightness = (progress / 255f); // Set Value
                a.getWindow().setAttributes(layoutParams);
                binding.brightness.tvCurrentBrightness.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private boolean hasBrightNessUi = false;
    private boolean hasPageInfoUi = true;
    private boolean hasFontFamilyUi = false;
    private boolean hasPortrait = false;

    private void setupBottomMenuItemListener() {

        binding.bottomMenus.buttonMenuGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) switch (checkedId) {
                case R.id.btnChapters: {
                    clearMenuItemChecked();
                    toggleStartDrawer();
                }
                break;
                case R.id.btnBookmark: {
                    clearMenuItemChecked();
                    showBookmarkHighlightAndNote();
                }
                break;
                case R.id.btnFullScreen: {
//                    hasFullScreen = !hasFullScreen
//                    fullScreenMode()
                }
                break;
                case R.id.btnBrightness: {
                    hasBrightNessUi = true;
                    hasPageInfoUi = false;
                    hasFontFamilyUi = false;
                    showPageInfoOrOthers();
                }
                break;
                case R.id.btnRotate: {
                    hasBrightNessUi = false;
                    hasPageInfoUi = true;
                    hasFontFamilyUi = false;
                    showPageInfoOrOthers();
                    hasPortrait = !hasPortrait;
                    a.setRequestedOrientation(
                            hasPortrait ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                    : ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                    );
                }
                break;
                case R.id.btnFontFamily: {
                    hasBrightNessUi = false;
                    hasPageInfoUi = false;
                    hasFontFamilyUi = true;
                    showPageInfoOrOthers();
                }
                break;
            }
            else {
                if (checkedId == R.id.btnRotate) {
                    hasPortrait = true;
                    a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    hasPageInfoUi = true;
                    hasBrightNessUi = false;
                    hasFontFamilyUi = false;
                    showPageInfoOrOthers();
                }
            }
        });

        hasBrightNessUi = false;
        hasPageInfoUi = true;
        hasFontFamilyUi = false;
        showPageInfoOrOthers();
    }

    private void showBookmarkHighlightAndNote() {
        BookmarkHighlightAndNoteFragment fragment = new BookmarkHighlightAndNoteFragment();
        fragment.show(a.getSupportFragmentManager(), BookmarkHighlightAndNoteFragment.TAG);
    }

    private void showPageInfoOrOthers() {
        showOrHideUI(binding.pageInfo.getRoot(), hasPageInfoUi);
        showOrHideUI(binding.brightness.getRoot(), hasBrightNessUi);
        showOrHideUI(binding.fontFamily.getRoot(), hasFontFamilyUi);
    }

    private void showOrHideUI(View view, boolean isShow) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    private void clearMenuItemChecked() {
        binding.bottomMenus.buttonMenuGroup.clearChecked();
        hasBrightNessUi = false;
        hasPageInfoUi = true;
        hasFontFamilyUi = false;
        showPageInfoOrOthers();
    }


    private void toggleStartDrawer() {
        if (binding.mainDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.mainDrawer.closeDrawer(GravityCompat.START);
        } else {
            binding.mainDrawer.openDrawer(GravityCompat.START);
            initAdapter();
        }
    }

    private void setupRecyclerViews() {
        binding.chaptersRecyclerView.setHasFixedSize(true);
        binding.chaptersRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        a,
                        DividerItemDecoration.VERTICAL
                )
        );
    }

    private ChapterAdapter chapterAdapter;

    private void initAdapter() {
        chapterAdapter = new ChapterAdapter();
        binding.chaptersRecyclerView.setAdapter(chapterAdapter);
        chapterAdapter.setOnClickListener(outline -> {
            toggleStartDrawer();
            if (outline.targetPage != -1) {
                int pageCount = dc.getPageCount();
                if (outline.targetPage < 1 || outline.targetPage > pageCount) {
                    Toast.makeText(anchor.getContext(), "no", Toast.LENGTH_SHORT).show();
                } else {
                    dc.onGoToPage(outline.targetPage);
                }
            }

        });

        dc.getOutline(outlines -> {
            if (outlines != null && outlines.size() > 0) {
                binding.chaptersRecyclerView.post(() -> {
                    chapterAdapter.refreshOutlines(outlines);
                });
            }
            return false;
        }, true);

    }


    private void setupSettingsView(Activity a) {
        binding.settingLayout.closeImageView.setOnClickListener(v -> toggleSettingDrawer());
        //Auto Scroll Config
        /*binding.settingLayout.autoScrollSwitch.setChecked(AppState.get().isAutoScroll);
        binding.settingLayout.autoScrollIntervalSlider.setEnabled(AppState.get().isAutoScroll);
        binding.settingLayout.autoScrollIntervalSlider.setValue(AppState.get().autoScrollInterval);
        binding.settingLayout.autoScrollSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                binding.settingLayout.autoScrollIntervalSlider.setEnabled(isChecked)
        );
        binding.settingLayout.autoScrollIntervalSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                AppState.get().autoScrollInterval = (int) value;
            }
        });*/

        //Continuous Auto Scoll Config
        /*binding.settingLayout.continuousAutoScrollSwitch.setChecked(AppState.get().isContinuousAutoScroll);
        binding.settingLayout.contiguousAutoScrollIntervalSlider.setEnabled(AppState.get().isContinuousAutoScroll);
        binding.settingLayout.contiguousAutoScrollIntervalSlider.setValue(AppState.get().continuousAutoScrollSpeed);
        binding.settingLayout.continuousAutoScrollSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                binding.settingLayout.contiguousAutoScrollIntervalSlider.setEnabled(isChecked)
        );
        binding.settingLayout.contiguousAutoScrollIntervalSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                AppState.get().continuousAutoScrollSpeed = (int) value;
            }
        });*/

        //Use volume key for page navigation
        binding.settingLayout.volumeToControlSwitch.setChecked(AppState.get().isUseVolumeKeys);
        binding.settingLayout.volumeToControlSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppState.get().isUseVolumeKeys = isChecked
        );

        //Blue Light Filter
        binding.settingLayout.blueLightSlider.setProgress(BrightnessHelper.blueLightAlpha());
        binding.settingLayout.blueLightTextView.setText(BrightnessHelper.blueLightAlpha() + "%");
//        TextView blueLightTextView = binding.settingLayout.blueLightTextView;
        binding.settingLayout.blueLightSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    BrightnessHelper.blueLightAlpha(progress);
                    BrightnessHelper.updateOverlay(binding.overlay);
                    binding.settingLayout.blueLightTextView.setText(progress + "%");
//                    binding.settingLayout.blueLightTextView.setText(BrightnessHelper.blueLightAlpha() + "%");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        binding.settingLayout.alignmentJustifySwitch.setChecked(BookCSS.get().textAlign == BookCSS.TEXT_ALIGN_JUSTIFY);
        binding.settingLayout.alignmentJustifySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                BookCSS.get().textAlign = BookCSS.TEXT_ALIGN_JUSTIFY;
            } else {
                BookCSS.get().textAlign = BookCSS.TEXT_ALIGN_LEFT;
            }
        });
        binding.settingLayout.lineHeightSlider.setValue(BookCSS.get().lineHeight);
        binding.settingLayout.lineHeightSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if (fromUser) {
                    BookCSS.get().lineHeight = (int) value;
                }
            }
        });
        binding.settingLayout.hyphenationSwitch.setChecked(AppState.get().isDefaultHyphenLanguage);
        binding.settingLayout.hyphenationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppState.get().isDefaultHyphenLanguage = isChecked;
            }
        });

        binding.settingLayout.inactiveDimSwitch.setChecked(AppState.get().inactivityTime != 0);
        binding.settingLayout.inactiveDimSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setInactiveTime(binding.settingLayout.inactiveDimTimesRadioGroup.getCheckedRadioButtonId());
                } else {
                    AppState.get().inactivityTime = 0;
                }
            }
        });
        binding.settingLayout.inactiveDimTimesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setInactiveTime(checkedId);
            }
        });
    }

    private void setInactiveTime(int checkedId) {
        if (checkedId == R.id.five_min_radio_btn) {
            AppState.get().inactivityTime = 5;
        } else if (checkedId == R.id.ten_min_radio_btn) {
            AppState.get().inactivityTime = 10;
        } else if (checkedId == R.id.fifteen_min_radio_btn) {
            AppState.get().inactivityTime = 15;
        }
    }

    private void toggleSettingDrawer() {
        if (mainDrawer.isDrawerOpen(GravityCompat.END)) {
            mainDrawer.closeDrawer(GravityCompat.END);
        } else {
            mainDrawer.openDrawer(GravityCompat.END);
        }
    }

    public void updateSeekBarColorAndSize() {
//        lirbiLogo.setText(AppState.get().musicText);
        // TintUtil.setBackgroundFillColorBottomRight(ttsActive,
        // ColorUtils.setAlphaComponent(TintUtil.color, 230));

//        TintUtil.setTintText(bookName, TintUtil.getStatusBarColor());

        int titleColor = AppState.get().isDayNotInvert ? MagicHelper.otherColor(AppState.get().colorDayBg, -0.05f) : MagicHelper.otherColor(AppState.get().colorNigthBg, 0.05f);
//        titleBar.setBackgroundColor(titleColor);

        int progressColor = AppState.get().isDayNotInvert ? AppState.get().statusBarColorDay : MagicHelper.otherColor(AppState.get().statusBarColorNight, +0.2f);

        // textSize
//        bookName.setTextSize(AppState.get().statusBarTextSizeAdv);
//        lirbiLogo.setTextSize(AppState.get().statusBarTextSizeAdv);

        int iconSize = Dips.spToPx(AppState.get().statusBarTextSizeAdv);
        int smallIconSize = iconSize - Dips.dpToPx(5);


    }

    @Subscribe
    public void onMessegeBrightness(MessegeBrightness msg) {
//        BrightnessHelper.onMessegeBrightness(handler, msg, toastBrightnessText, overlay);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void tintSpeed() {
    }

    public void showEditDialogIfNeed() {
        DragingDialogs.editColorsPanel(anchor, dc, drawView, true);
    }

    public void doDoubleTap(int x, int y) {
        if (dc.isMusicianMode()) {
            if (AppState.get().doubleClickAction1 == AppState.DOUBLE_CLICK_ADJUST_PAGE) {
                dc.alignDocument();
            }
        } else {
            if (AppState.get().doubleClickAction1 == AppState.DOUBLE_CLICK_ZOOM_IN_OUT) {
                dc.onZoomInOut(x, y);
                AppState.get().isEditMode = false;
                hideShow();
            } else if (AppState.get().doubleClickAction1 == AppState.DOUBLE_CLICK_ADJUST_PAGE) {
                dc.alignDocument();
            } else if (AppState.get().doubleClickAction1 == AppState.DOUBLE_CLICK_CENTER_HORIZONTAL) {
                dc.centerHorizontal();
            } else if (AppState.get().doubleClickAction1 == AppState.DOUBLE_CLICK_CONTIUOUSAUTOSCROLL) {
                onContinuousAutoScrollClick();
            } else if (AppState.get().doubleClickAction1 == AppState.DOUBLE_CLICK_CLOSE_BOOK) {
                closeAndRunList();
            } else if (AppState.get().doubleClickAction1 == AppState.DOUBLE_CLICK_CLOSE_HIDE_APP) {
                Apps.showDesctop(a);
            } else if (AppState.get().doubleClickAction1 == AppState.DOUBLE_CLICK_START_STOP_TTS) {
                TTSService.playPause(dc.getActivity(), dc);

            } else if (AppState.get().doubleClickAction1 == AppState.DOUBLE_CLICK_CLOSE_BOOK_AND_APP) {
                dc.onCloseActivityFinal(new Runnable() {

                    @Override
                    public void run() {
                        MainTabs2.closeApp(dc.getActivity());
                    }
                });
                dc.closeActivity();
            }
        }
    }

    public void doShowHideWrapperControlls() {
        AppState.get().isEditMode = !AppState.get().isEditMode;
        hideShow();
        Keyboards.invalidateEink(parentParent);

    }

    public void showHideHavigationBar() {
        if (!AppState.get().isEditMode && AppState.get().fullScreenMode == AppState.FULL_SCREEN_FULLSCREEN) {
            Keyboards.hideNavigation(a);
        }
    }

    public void doChooseNextType(View view) {
        final MyPopupMenu popupMenu = new MyPopupMenu(view.getContext(), view);

        String pages = dc.getString(R.string.by_pages);
        String screen = dc.getString(R.string.of_screen).toLowerCase(Locale.US);
        String screens = dc.getString(R.string.by_screans);
        final List<Integer> values = Arrays.asList(AppState.NEXT_SCREEN_SCROLL_BY_PAGES, 100, 95, 75, 50, 25, 10);

        for (int i = 0; i < values.size(); i++) {
            final int n = i;
            String name = i == AppState.NEXT_SCREEN_SCROLL_BY_PAGES ? pages : values.get(i) + "% " + screen;
            if (values.get(i) == 100) {
                name = screens;
            }

            popupMenu.getMenu().add(name).setOnMenuItemClickListener(new OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AppState.get().nextScreenScrollBy = values.get(n);
                    initNextType();
                    Keyboards.hideNavigation(dc.getActivity());
                    return false;
                }
            });
        }

        popupMenu.getMenu().add(R.string.custom_value).setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Activity a = dc.getActivity();
                final AlertDialog.Builder builder = new AlertDialog.Builder(a);
                builder.setTitle(R.string.custom_value);

                final CustomSeek myValue = new CustomSeek(a);
                myValue.init(1, 100, AppState.get().nextScreenScrollMyValue);
                myValue.setOnSeekChanged(new IntegerResponse() {

                    @Override
                    public boolean onResultRecive(int result) {
                        AppState.get().nextScreenScrollMyValue = result;
                        myValue.setValueText(AppState.get().nextScreenScrollMyValue + "%");
                        return false;
                    }
                });
                myValue.setValueText(AppState.get().nextScreenScrollMyValue + "%");

                builder.setView(myValue);

                builder.setPositiveButton(R.string.apply, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppState.get().nextScreenScrollBy = AppState.get().nextScreenScrollMyValue;
                        initNextType();
                        Keyboards.hideNavigation(dc.getActivity());

                    }
                });
                builder.setNegativeButton(R.string.cancel, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

                return false;
            }

        });

        popupMenu.show();

    }

    public void doHideShowToolBar() {
        AppState.get().isShowToolBar = !AppState.get().isShowToolBar;
        initToolBarPlusMinus();
    }

    public void initToolBarPlusMinus() {
        if (AppState.get().isEditMode || AppState.get().isShowToolBar) {
//            titleBar.setVisibility(View.VISIBLE);
        } else {
//            titleBar.setVisibility(View.GONE);
        }

    }

    public void initNextType() {
        if (AppState.get().nextScreenScrollBy == AppState.NEXT_SCREEN_SCROLL_BY_PAGES) {
//            nextTypeBootom.setText(R.string.by_pages);

        } else {
            if (AppState.get().nextScreenScrollBy == 100) {
//                nextTypeBootom.setText(dc.getString(R.string.by_screans));
            } else {
//                nextTypeBootom.setText(AppState.get().nextScreenScrollBy + "% " + dc.getString(R.string.of_screen));
            }

        }

    }

    public void hideShow() {
        if (AppState.get().isEnableAccessibility) {
            AppState.get().isEditMode = true;
        }

        if (AppState.get().isEditMode) {
            DocumentController.turnOnButtons(a);
            show();
        } else {
            DocumentController.turnOffButtons(a);

            hide();
        }
        initToolBarPlusMinus();

        if (AppState.get().isContinuousAutoScroll) {
//            autoScroll.setImageResource(R.drawable.glyphicons_37_file_pause);
        } else {
//            autoScroll.setImageResource(R.drawable.glyphicons_37_file_play);
        }

        if (dc.isMusicianMode()) {
            if (AppState.get().isContinuousAutoScroll) {
            } else {
            }
        } else {
            if (AppState.get().isEditMode && AppState.get().isContinuousAutoScroll) {
            } else {
            }
        }

        if (dc.isMusicianMode()) {
//            lirbiLogo.setVisibility(View.VISIBLE);
        } else {
//            lirbiLogo.setVisibility(View.GONE);
        }

        // hideSeekBarInReadMode();
        // showHideHavigationBar();
        DocumentController.chooseFullScreen(dc.getActivity(), AppState.get().fullScreenMode);
        showPagesHelper();


        //try eink fix

    }

    public void hide() {
        toolbar.setVisibility(View.GONE);
        llPages.setVisibility(View.GONE);
//        imageMenuArrow.setImageResource(android.R.drawable.arrow_down_float);

        // speedSeekBar.setVisibility(View.GONE);

    }

    public void _hideSeekBarInReadMode() {
        if (!AppState.get().isEditMode) {
            handler.removeCallbacks(hideSeekBar);
            handler.postDelayed(hideSeekBar, 5000);
        }
    }

    public void show() {
        llPages.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);

        updateLock();

//        bottomBar.setVisibility(View.VISIBLE);

//        imageMenuArrow.setImageResource(android.R.drawable.arrow_up_float);


        // if (AppState.get().isAutoScroll &&
        // AppState.get().isEditMode) {
        // seekSpeedLayot.setVisibility(View.VISIBLE);
        // }

    }

    public void showSearchDialog() {
        if (AppSP.get().isCut) {
//            onModeChange.setImageResource(R.drawable.glyphicons_two_page_one);
            AppSP.get().isCut = !false;
            onCut.onClick(null);
        }
        if (AppSP.get().isCrop) {
            onCrop.onClick(null);
        }

        DragingDialogs.searchMenu(anchor, dc, "");
    }

    public void onContinuousAutoScrollClick() {
        if (dc.isVisibleDialog()) {
            return;
        }

        AppState.get().isContinuousAutoScroll = !AppState.get().isContinuousAutoScroll;
        // changeAutoScrollButton();
        dc.onContinuousAutoScroll();
        updateUI();
    }

    private boolean closeDialogs() {
        return dc.closeDialogs();
    }

    public void hideAds() {
    }

    public void nextChose(boolean animate) {
        nextChose(animate, 0);
    }

    public void nextChose(boolean animate, int repeatCount) {
        LOG.d("nextChose");
        dc.checkReadingTimer();

        if (AppState.get().isEditMode) {
            AppState.get().isEditMode = false;
        }

        if (AppState.get().nextScreenScrollBy == AppState.NEXT_SCREEN_SCROLL_BY_PAGES) {
            dc.onNextPage(animate);
        } else {
            if (AppState.get().nextScreenScrollBy <= 50 && repeatCount == 0) {
                animate = true;
            }
            dc.onNextScreen(animate);
        }

        //updateUI();

    }

    public void prevChose(boolean animate) {
        prevChose(animate, 0);
    }

    public void prevChose(boolean animate, int repeatCount) {
        dc.checkReadingTimer();

        if (AppState.get().isEditMode) {
            AppState.get().isEditMode = false;
        }

        if (AppState.get().nextScreenScrollBy == AppState.NEXT_SCREEN_SCROLL_BY_PAGES) {
            dc.onPrevPage(animate);
        } else {
            if (AppState.get().nextScreenScrollBy <= 50 && repeatCount == 0) {
                animate = true;
            }
            dc.onPrevScreen(animate);
        }

        //updateUI();
    }

    public void setTitle(final String title) {
        this.bookTitle = title;

        hideShowEditIcon();

    }

    public void hideShowEditIcon() {
        if (dc != null && !BookType.PDF.is(dc.getCurrentBook().getPath())) {
//            editTop2.setVisibility(View.GONE);
        } else if (AppSP.get().isCrop || AppSP.get().isCut) {
//            editTop2.setVisibility(View.GONE);
        } else {
            boolean passwordProtected = dc.isPasswordProtected();
            LOG.d("passwordProtected", passwordProtected);
            if (dc != null && passwordProtected) {
//                editTop2.setVisibility(View.GONE);
            } else {
                if (LibreraApp.MUPDF_VERSION == AppsConfig.MUPDF_1_11) {
//                    editTop2.setVisibility(View.VISIBLE);
                } else {
//                    editTop2.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    public DocumentController getController() {
        return dc;
    }

    public DrawView getDrawView() {
        return drawView;
    }

    public void showHelp() {
        if (AppSP.get().isFirstTimeVertical) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    AppSP.get().isFirstTimeVertical = false;
                    AppState.get().isEditMode = true;
                    hideShow();
                }
            }, 1000);
        }
    }

    public void showPagesHelper() {
        try {
//            BookmarkPanel.showPagesHelper(pageshelper, musicButtonPanel, dc, pagesBookmark, quickBookmark, onRefresh);
        } catch (Exception e) {
            LOG.e(e);
        }
    }

    public void showOutline(final List<OutlineLinkWrapper> list, final int count) {
        try {
            dc.activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Apps.accessibilityText(a, a.getString(R.string.book_is_open), a.getString(R.string.m_current_page), " " + dc.getCurentPageFirst1());

//                    titleBar.setOnTouchListener(new HorizontallSeekTouchEventListener(onSeek, dc.getPageCount(), false));
                    if (TxtUtils.isListEmpty(list)) {
//                        TintUtil.setTintImageWithAlpha(onDocDontext, Color.LTGRAY);
                    }

                    if (ExtUtils.isNoTextLayerForamt(dc.getCurrentBook().getPath())) {
//                        TintUtil.setTintImageWithAlpha(textToSpeach, Color.LTGRAY);
                    }
                    if (dc.isTextFormat()) {
                        // TintUtil.setTintImage(lockUnlock, Color.LTGRAY);
                    }


                    showHelp();

                    hideShowEditIcon();

                    updateSpeedLabel();

//                    DialogsPlaylist.dispalyPlaylist(a, dc);
//                    HypenPanelHelper.init(parentParent, dc);


                    showPagesHelper();

                }
            });
        } catch (Exception e) {
            LOG.e(e);
        }

    }

    public void onResume() {
        LOG.d("DocumentWrapperUI", "onResume");
        handlerTimer.post(updateTimePower);

        if (dc != null) {
            dc.goToPageByTTS();
        }

//        if (ttsActive != null) {
//            ttsActive.setVisibility(TxtUtils.visibleIf(TTSEngine.get().isTempPausing()));
//        }

    }

    public void onPause() {
        LOG.d("DocumentWrapperUI", "onPause");
        handlerTimer.removeCallbacks(updateTimePower);

    }

    public void onDestroy() {
        LOG.d("DocumentWrapperUI", "onDestroy");
        handlerTimer.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);

    }

    public void onConfigChanged() {
        try {
            updateUI();
        } catch (Exception e) {
            LOG.e(e);
        }
    }

    public void onLoadBookFinish() {
    }
}
