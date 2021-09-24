package com.foobnix.model;

public class Highlight {

    private int id;
    private String bookName;
    private String chapterName;
    private int pageNumber;
    private String text;
    private int colorCode;


    public Highlight() {
    }

    public Highlight(int id, String bookName, String chapterName, String text, int pageNumber, int colorCode) {
        this.id = id;
        this.bookName = bookName;
        this.chapterName = chapterName;
        this.pageNumber = pageNumber;
        this.text = text;
        this.colorCode = colorCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}
