package org.ebookdroid.model;

public class ReaderSettingConfig {
    private int blueLightFilter;
    private int lineHeight;

    public ReaderSettingConfig() {
    }

    public ReaderSettingConfig(int blueLightFilter, int lineHeight) {
        this.blueLightFilter = blueLightFilter;
        this.lineHeight = lineHeight;
    }

    public void setBlueLightFilter(int blueLightFilter) {
        this.blueLightFilter = blueLightFilter;
    }

    public int getBlueLightFilter() {
        return blueLightFilter;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }
}