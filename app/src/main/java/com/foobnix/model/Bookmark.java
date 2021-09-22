package com.foobnix.model;

import java.util.Objects;

public class Bookmark {

    private int id;
    private String bookName;
    private String chapterName;
    private int pageNumber;

    public Bookmark() {
    }


    public Bookmark(int id, String bookName, String chapterName, int pageNumber) {
        this.id = id;
        this.bookName = bookName;
        this.chapterName = chapterName;
        this.pageNumber = pageNumber;
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


}
