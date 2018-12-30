package dto;

import util.DateTime;

import java.util.List;

public class Book extends LibraryItem {

private int pages;
private String publisher;
    private List<Author> authors;

    public Book(int isbn, String title, String genre, String publicationDate, int pages, String publisher, List<Author> authors) {
        super(isbn, title, genre, publicationDate);
        this.pages = pages;
        this.publisher = publisher;
        this.authors = authors;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }


    @Override
    public String toString() {
        return super.toString() + "\nPages - " + pages + "\nPublisher - "+ publisher +"\nAuthors - "+authors;
    }
}
