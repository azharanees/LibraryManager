package dto;

import util.DateTime;


import java.util.List;

public class DVD extends LibraryItem {

    private List<String> languages;
    private List<String> subtitles;
    private String publisher;
    private List<Actor> actors;

    public DVD(int isbn, String title, String genre, String publicationDate, List<String> languages, List<String> subtitles, String publisher, List<Actor> actors) {
        super(isbn, title, genre, publicationDate);
        this.languages = languages;
        this.subtitles = subtitles;
        this.publisher = publisher;
        this.actors = actors;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
