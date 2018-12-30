package models;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "languages")
public class LanguageModel extends Model {

    @Column(name = "language")
    private String languge;


}
