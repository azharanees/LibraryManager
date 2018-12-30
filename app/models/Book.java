package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "book")
@AttributeOverride(name="isbn",column=@Column(name = "id"))
public class Book extends LibraryItemModel {


    @Column(name = "publisher")
    private String publisher;

    @Column(name = "pages")
    private int pages;


    @JsonManagedReference
    @ManyToMany(mappedBy = "books")
    private List<AuthorModel> authors;


    public Book(int isbn,String title,String genre,String publicationDate,String publisher, int pages) {
        super(isbn,title,genre,publicationDate);
        this.publisher = publisher;
        this.pages = pages;
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



    public List<AuthorModel> getAuthors() {
        return authors;
    }


    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }



    public void setAuthors(List<AuthorModel> authors) {
        this.authors = authors;
    }

}