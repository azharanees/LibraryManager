package models;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "publisher")
public class PublisherModel extends Model {

        @Id
        @Column(name = "id")
        private int id;

        @Column(name = "name")
        private String name;
}
