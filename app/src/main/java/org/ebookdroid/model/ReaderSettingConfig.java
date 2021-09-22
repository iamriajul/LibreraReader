package org.ebookdroid.model;

import com.foobnix.model.AppState;
import com.foobnix.pdf.info.model.BookCSS;
import com.foobnix.pdf.info.view.BrightnessHelper;

public class ReaderSettingConfig {
    private boolean autoScroll = AppState.get().isAutoScroll;
    private int autoScrollInterval = AppState.get().autoScrollInterval;
    private boolean continuousAutoScroll = AppState.get().isContinuousAutoScroll;
    private int continuousAutoScrollSpeed = AppState.get().continuousAutoScrollSpeed;
    private boolean wholePageAtATime = AppState.get().wholePageAtATime;
    private boolean useVolumeKeyToNavigate = AppState.get().isUseVolumeKeys;
    private boolean swipeToControlBrightness = AppState.get().isBrighrnessEnable;
    private int blueLightFilter = BrightnessHelper.blueLightAlpha();
    private int inactiveTime = AppState.get().inactivityTime;
    private boolean shakeToTakeScreenShort = AppState.get().shakeToTakeScreenShort;
    private int alignment = BookCSS.get().textAlign;
    private int lineHeight = BookCSS.get().lineHeight;
    private boolean hyphenation = AppState.get().isDefaultHyphenLanguage;

    public void setBlueLightFilter(int blueLightFilter) {
        this.blueLightFilter = blueLightFilter;
        BrightnessHelper.blueLightAlpha(blueLightFilter);
    }

    public int getBlueLightFilter() {
        return blueLightFilter;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        BookCSS.get().lineHeight = lineHeight;
    }

    public boolean isAutoScroll() {
        return autoScroll;
    }

    public void setAutoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
        AppState.get().isAutoScroll = autoScroll;
    }

    public int getAutoScrollInterval() {
        return autoScrollInterval;
    }

    public void setAutoScrollInterval(int autoScrollInterval) {
        this.autoScrollInterval = autoScrollInterval;
        AppState.get().autoScrollInterval = autoScrollInterval;
    }

    public boolean isContinuousAutoScroll() {
        return continuousAutoScroll;
    }

    public void setContinuousAutoScroll(boolean continuousAutoScroll) {
        this.continuousAutoScroll = continuousAutoScroll;
        AppState.get().isContinuousAutoScroll = continuousAutoScroll;
    }

    public int getContinuousAutoScrollSpeed() {
        return continuousAutoScrollSpeed;
    }

    public void setContinuousAutoScrollSpeed(int continuousAutoScrollSpeed) {
        this.continuousAutoScrollSpeed = continuousAutoScrollSpeed;
        AppState.get().continuousAutoScrollSpeed = continuousAutoScrollSpeed;
    }

    public boolean isWholePageAtATime() {
        return wholePageAtATime;
    }

    public void setWholePageAtATime(boolean wholePageAtATime) {
        this.wholePageAtATime = wholePageAtATime;
        AppState.get().wholePageAtATime = wholePageAtATime;
    }

    public boolean isUseVolumeKeyToNavigate() {
        return useVolumeKeyToNavigate;
    }

    public void setUseVolumeKeyToNavigate(boolean useVolumeKeyToNavigate) {
        this.useVolumeKeyToNavigate = useVolumeKeyToNavigate;
        AppState.get().isUseVolumeKeys = useVolumeKeyToNavigate;
    }

    public boolean isSwipeToControlBrightness() {
        return swipeToControlBrightness;
    }

    public void setSwipeToControlBrightness(boolean swipeToControlBrightness) {
        this.swipeToControlBrightness = swipeToControlBrightness;
        AppState.get().isBrighrnessEnable = swipeToControlBrightness;
    }

    public int getInactiveTime() {
        return inactiveTime;
    }

    public void setInactiveTime(int inactiveTime) {
        this.inactiveTime = inactiveTime;
        AppState.get().inactivityTime = inactiveTime;
    }

    public boolean isShakeToTakeScreenShort() {
        return shakeToTakeScreenShort;
    }

    public void setShakeToTakeScreenShort(boolean shakeToTakeScreenShort) {
        this.shakeToTakeScreenShort = shakeToTakeScreenShort;
        AppState.get().shakeToTakeScreenShort = shakeToTakeScreenShort;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
        BookCSS.get().textAlign = alignment;
    }

    public int getAlignment() {
        return alignment;
    }

    public boolean isHyphenation() {
        return hyphenation;
    }

    public void setHyphenation(boolean hyphenation) {
        this.hyphenation = hyphenation;
        AppState.get().isDefaultHyphenLanguage = hyphenation;
    }
}