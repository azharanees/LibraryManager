package models;

import io.ebean.Model;

import util.DateTime;

import javax.persistence.*;


@MappedSuperclass
public class LibraryItemModel extends Model {

    @Id
    @Column(name = "id")
    public int isbn;

    @Column(name = "title")
    public String title;

    @Column(name = "genre")
    public String genre;

    @Column(name = "borrowedDate")
    public String borrowedDate;


    @Column(name = "publicationDate")
    public String publicationDate;

    @Column(name = "lastReservedDate")
    private String lastReservedDate;



    @ManyToOne
    @JoinColumn(name = "reader", referencedColumnName = "id")
    public ReaderModel reader;
    public ReaderModel getReader() {
        return reader;
    }

    public void setReader(ReaderModel reader) {
        if(reader==null){
            this.reader=null;
        }else {
            setBorrowedDate(DateTime.now().getDate());
            this.reader = reader;

        }

    }

    public String getLastReservedDate() {
        return lastReservedDate;
    }

    public void setLastReservedDate(String lastReservedDate) {
        this.lastReservedDate = lastReservedDate;
    }

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public int getId() {
        return isbn;
    }

    public void setId(int id) {
        this.isbn = id;
    }


    public LibraryItemModel(int isbn,String title,String genre,String publicationDate) {
        this.isbn = isbn;
        this. title=  title;
        this.genre = genre;
        this.publicationDate = publicationDate;
    }
    public String getPublicationDate() {
        return this.publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


}
