package com.foobnix.model;

public class Note {

    private int id;
    private String bookName;
    private String chapterName;
    private String note;
    private int pageNumber;
    private int colorCode;


    public Note() {
    }


    public Note(int id, String bookName, String chapterName, String note, int pageNumber, int colorCode) {
        this.id = id;
        this.bookName = bookName;
        this.chapterName = chapterName;
        this.note = note;
        this.pageNumber = pageNumber;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}
