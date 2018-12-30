package models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dto.Actor;
import io.ebean.Model;
import io.ebean.annotation.DbArray;
import util.DateTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dvds")
public class DVDModel extends LibraryItemModel {


    @Column(name = "languages")
    @DbArray
    private List<String> languages;

    @Column(name = "subtitles")
    @DbArray
    private List<String> subtitles;

    @JsonIgnoreProperties("movies")
    @JsonBackReference
    @ManyToMany(mappedBy = "movies")
    private List<ActorModel> actors;

    @Column(name = "producer")
    private String producer;



    public DVDModel(int isbn, String title, String genre, String producer, String publicationDate, List<String> languages, List<String> subtitles, List<ActorModel> actors) {
        super(isbn,title,genre,publicationDate);
        this.title = title;
        this.languages = languages;
        this.subtitles = subtitles;
        this.genre = genre;
        this.actors = actors;
        this.producer = producer;

    }



    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<String> subtitles) {
        this.subtitles = subtitles;
    }

    public List<ActorModel> getActors() {
        return actors;
    }

    public void setActors(List<ActorModel> actors) {
        this.actors = actors;
    }


    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }


}
