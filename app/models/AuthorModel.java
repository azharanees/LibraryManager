package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.ebean.Model;
import io.ebean.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "author")
public class AuthorModel extends Model {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;


    @ManyToMany
    @JsonBackReference
    private List<Book> books;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
