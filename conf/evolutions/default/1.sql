# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table actor (
  id                            integer auto_increment not null,
  name                          varchar(255),
  constraint pk_actor primary key (id)
);

create table actor_dvds (
  actor_id                      integer not null,
  dvds_id                       integer not null,
  constraint pk_actor_dvds primary key (actor_id,dvds_id)
);

create table author (
  id                            integer auto_increment not null,
  name                          varchar(255),
  constraint pk_author primary key (id)
);

create table author_book (
  author_id                     integer not null,
  book_id                       integer not null,
  constraint pk_author_book primary key (author_id,book_id)
);

create table book (
  id                            integer auto_increment not null,
  title                         varchar(255),
  genre                         varchar(255),
  borroweddate                  varchar(255),
  publicationdate               varchar(255),
  lastreserveddate              varchar(255),
  reader                        integer,
  publisher                     varchar(255),
  pages                         integer not null,
  constraint pk_book primary key (id)
);

create table dvds (
  id                            integer auto_increment not null,
  title                         varchar(255),
  genre                         varchar(255),
  borroweddate                  varchar(255),
  publicationdate               varchar(255),
  lastreserveddate              varchar(255),
  reader                        integer,
  languages                     varchar(1000),
  subtitles                     varchar(1000),
  producer                      varchar(255),
  constraint pk_dvds primary key (id)
);

create table languages (
  language                      varchar(255)
);

create table publisher (
  id                            integer auto_increment not null,
  name                          varchar(255),
  constraint pk_publisher primary key (id)
);

create table reader (
  id                            integer auto_increment not null,
  name                          varchar(255),
  mobile                        varchar(255),
  email                         varchar(255),
  constraint pk_reader primary key (id)
);

alter table actor_dvds add constraint fk_actor_dvds_actor foreign key (actor_id) references actor (id) on delete restrict on update restrict;
create index ix_actor_dvds_actor on actor_dvds (actor_id);

alter table actor_dvds add constraint fk_actor_dvds_dvds foreign key (dvds_id) references dvds (id) on delete restrict on update restrict;
create index ix_actor_dvds_dvds on actor_dvds (dvds_id);

alter table author_book add constraint fk_author_book_author foreign key (author_id) references author (id) on delete restrict on update restrict;
create index ix_author_book_author on author_book (author_id);

alter table author_book add constraint fk_author_book_book foreign key (book_id) references book (id) on delete restrict on update restrict;
create index ix_author_book_book on author_book (book_id);

alter table book add constraint fk_book_reader foreign key (reader) references reader (id) on delete restrict on update restrict;
create index ix_book_reader on book (reader);

alter table dvds add constraint fk_dvds_reader foreign key (reader) references reader (id) on delete restrict on update restrict;
create index ix_dvds_reader on dvds (reader);


# --- !Downs

alter table actor_dvds drop foreign key fk_actor_dvds_actor;
drop index ix_actor_dvds_actor on actor_dvds;

alter table actor_dvds drop foreign key fk_actor_dvds_dvds;
drop index ix_actor_dvds_dvds on actor_dvds;

alter table author_book drop foreign key fk_author_book_author;
drop index ix_author_book_author on author_book;

alter table author_book drop foreign key fk_author_book_book;
drop index ix_author_book_book on author_book;

alter table book drop foreign key fk_book_reader;
drop index ix_book_reader on book;

alter table dvds drop foreign key fk_dvds_reader;
drop index ix_dvds_reader on dvds;

drop table if exists actor;

drop table if exists actor_dvds;

drop table if exists author;

drop table if exists author_book;

drop table if exists book;

drop table if exists dvds;

drop table if exists languages;

drop table if exists publisher;

drop table if exists reader;

