package services;

import dto.Book;
import dto.DVD;
import dto.LibraryItem;
import models.ActorModel;
import models.DVDModel;
import models.LibraryItemModel;

import java.math.BigDecimal;
import java.util.List;

public interface LibraryManager {

    void addBook(String isbn, String title, String authors, String publisher, String genre, String publicationDate, int pages);

    void addDVD(DVDModel dvdModel);

    void addReader(String id, String name, String mobile, String email);

    int getTotalCount();

    List<DVD> getAllDVD();

    void returnItem(int isbn, String reader);

    void borrowItem(int bookID, String reader);

    List<Book> getAllBooks();

    void updateBookAuthor(int isbn);


    LibraryItemModel getItemByName(String title);

    void removeBook(int isbn);

    void removeDVD(int isbn);

    void addAuthor(int id, String name);

    int getBookCount();

    int getDVDCount();

    void addDVD(int isbn, String title, String genre, String publicationDate, String producer, List<ActorModel> actors, List<String> languages, List<String> subtitles);
     ActorModel searchActorDB(String name);


    void doReservation(int isbn, String readerid);

    List<LibraryItem> generateReport();

    List<LibraryItem> getAllItem();

    BigDecimal getFineByISBN(int isbn);

    LibraryItemModel findItem(Integer isbn);

    void removeItem(Integer isbn);
}
