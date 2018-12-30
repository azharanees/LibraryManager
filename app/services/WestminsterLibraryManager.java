package services;
import dto.*;
import dto.Book;
import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import io.ebean.SqlUpdate;
import models.*;
import util.DateTime;

import java.math.BigDecimal;
import java.util.*;

public class WestminsterLibraryManager implements LibraryManager {

    private List<Book> books; //For all the books available in the library fetched from db
    private List<DVD> dvds;  //For all the dvds


//This method adds a book getting the required fields
    @Override
    public void addBook(String isbn, String title, String authors, String publisher, String genre, String publicationDate,int pages) {

        //creates an instance of book model to insert into the database
        models.Book book = new models.Book(Integer.parseInt(isbn),title,genre,publicationDate,publisher,pages);


        Ebean.save(book); //using ebean plugin to save the book to the database

    }

    //this method adds a dvd to the database
    @Override
    public void addDVD(int isbn, String title,String genre,String publicationDate,String producer, List<ActorModel> actors, List<String> languages, List<String> subtitles) {
//same as adding a book
        DVDModel dvdModel = new DVDModel(isbn,title,genre,producer,(publicationDate),languages,subtitles,actors);
         Ebean.save(dvdModel);

    }
   @Override
   public void addDVD(DVDModel dvdModel) {
//same as adding a book

        Ebean.save(dvdModel);

    }

    //this is an extra function to add readers manually to the library by getting required details
    @Override
    public void addReader(String id,String name, String mobile, String email){
        ReaderModel reader = new ReaderModel();
        reader.setId(Integer.parseInt(id));
        reader.setName(name);
        reader.setMobile(mobile);
        reader.setEmail(email);
        Ebean.save(reader);
    }


    //This method returns a list of books from the database
    @Override
    public List<Book> getAllBooks() {

        List<models.Book> bookModels = Ebean.find(models.Book.class).findList();

        List<dto.Book> books = new ArrayList<>();

        //Looping through the list of bookmodels and getting the book dto
        //where im using dto's to send them to UI(frontend) with my custom data set
        for (models.Book bookModel : bookModels) {
            dto.Book book = getBookDTObyModel(bookModel);
            books.add(book);

        }

        this.books = books;
        return books;
    }


    //This method is used to get an item by its title
    @Override
    public LibraryItemModel getItemByName(String title){


        List<models.Book> modelItems= Ebean.find(models.Book.class).findList();
        List<DVDModel> dvdModels = Ebean.find(DVDModel.class).findList();

        List<LibraryItemModel> allList = new ArrayList<>();
        allList.addAll(modelItems);
        allList.addAll(dvdModels);
        LibraryItemModel toFind = null;

        for (LibraryItemModel l: allList ) {
            if(title.equals(l.getName())){
                toFind= l;
                break;
            }

        }

        return toFind;

    }




    //This will search actor by name from the db
    @Override
    public ActorModel searchActorDB(String name){

        List<ActorModel> modelItems= Ebean.find(models.ActorModel.class).findList();

        ActorModel toFind = null;
        for (ActorModel l: modelItems ) {
            if(name.equals(l.getName())){
                toFind= l;
                break;
            }
        }
        return toFind;
    }


    //This method will remove an item when isbn is provided
    public void removeItem(Integer isbn){
        LibraryItemModel l = findItem(isbn);
        if(l.getClass().getName().equals(models.Book.class.getName())){
            removeBook(isbn);
        }else
            removeDVD(isbn);

    }

    //this method removes a book based on its isbn
    @Override
    public void removeBook(int isbn) {
        models.Book itemModel = Ebean.find(models.Book.class, isbn);
        Ebean.delete(itemModel);
    }

    //This method removes a dvd
    @Override
    public void removeDVD(int isbn) {
        models.DVDModel itemModel = Ebean.find(models.DVDModel.class, isbn);

        Ebean.delete(itemModel);
    }

    //this method adds an author to the db getting the name and id
    @Override
    public void addAuthor(int id, String name) {
        AuthorModel author = new AuthorModel();
        author.setId(id);
        author.setName(name);
        author.setBooks(Ebean.find(models.Book.class).findList());
        Ebean.save(author);
    }

    //This method sets authorlist for a book
    public List<AuthorModel> setBookAurthors(String authors){
        List<String> names = Arrays.asList(authors.split(",")); //comma separated authors
        List<AuthorModel> athrs = Ebean.find(AuthorModel.class).findList();
        List<AuthorModel> authorList = new ArrayList<>();
        AuthorModel a = athrs.get(athrs.size()-1);
        int lastID = a.getId();

        for (String name: names ) {
            AuthorModel newAuthor = new AuthorModel();
            newAuthor.setName(name);
            newAuthor.setId(++lastID);
            Ebean.save(newAuthor);
            authorList.add(newAuthor);
        }
        return authorList;
    }



    @Override
    public void updateBookAuthor(int isbn) {
        models.Book bookModel = Ebean.find(models.Book.class, isbn);
        List<AuthorModel> authorModel = Ebean.find(models.AuthorModel.class).findList();
        bookModel.setAuthors(authorModel);
        Ebean.save(bookModel);
    }

    //This method takes in a book model instance and returns a book dto
    private Book getBookDTObyModel(models.Book bookModel) {
        DateTime availDate;
        Book book = new Book(
                bookModel.getId(),bookModel.getName(),
                bookModel.getGenre(),
                bookModel.getPublicationDate(),
                bookModel.getPages(),
                bookModel.getPublisher(),
                getAuthorDTObyModel(bookModel.getAuthors()));
        book.setTitle(bookModel.getName());
        book.setIsbn(bookModel.getId());
        book.setAuthors(getAuthorDTObyModel(bookModel.getAuthors()));
        if(bookModel.getReader()!=null){
            book.setReader(getReaderDTObyModel(bookModel.getReader()));
            book.setBorrowedDate(bookModel.getBorrowedDate());

            if(bookModel.getLastReservedDate()!=null){
                availDate = DateTime.convertfromSTD(bookModel.getLastReservedDate());
                book.setAvailDate(availDate);
            }else {

                book.setAvailDate((book.getBorrowedDate().addDays(averageUsage(bookModel.isbn))));
            }
        }

//        Reader reader = getReaderDTObyModel(bookModel.getReader());
//        book.setReader(reader);


        return book;
    }



    //This method gets a list of author model and returns the dto
    private List<Author> getAuthorDTObyModel(List<AuthorModel> authors) {
        List<Author> list = new ArrayList<>();
        for (models.AuthorModel authorModel : authors) {
            Author author = new Author(authorModel.getId(),authorModel.getName());
            author.setAuthorID(authorModel.getId());
            author.setAuthorName(authorModel.getName());
            list.add(author);
        }
        return list;
    }


    //this method returns a reader dto from a readerModel
    private Reader getReaderDTObyModel(ReaderModel readerModel) {

            Reader reader = new Reader(readerModel.getName(),readerModel.getId());
           return reader;
    }


    //This method returns the count of current books in the library
    @Override
    public int getBookCount(){

        List<Book> bookList = getAllBooks();

        return bookList.size();
    }

    //This method returns the dvd count

    @Override
    public int getDVDCount(){
        List<DVD> dvdList = getAllDVD();
        return dvdList.size();
    }


    //This method returns the total count of items in the library
    @Override
    public int getTotalCount(){
        return getBookCount()+getDVDCount();
    }

    //This method returns all the dvds in the db converting it to dto's
    @Override
    public List<DVD> getAllDVD() {

        List<models.DVDModel> dvdModels = Ebean.find(models.DVDModel.class).findList();

        List<dto.DVD> dvds = new ArrayList<>();

        for (models.DVDModel dvdModel : dvdModels) {
            dto.DVD book = getDvdDTObyModel(dvdModel);
            dvds.add(book);
        }

        this.dvds = dvds;
        return dvds;
    }

    private DVD getDvdDTObyModel(DVDModel dvdModel) {
        DVD dvd = new DVD(
                dvdModel.getId(),dvdModel.getName(),
                dvdModel.getGenre(),
                dvdModel.getPublicationDate(),
                dvdModel.getLanguages(),
                dvdModel.getSubtitles(),
                dvdModel.getProducer(),
                getActorDTObyModel(dvdModel.getActors()));
        if(dvdModel.getReader()!=null){
       dvd.setReader(getReaderDTObyModel(dvdModel.getReader()));
       dvd.setBorrowedDate(dvdModel.borrowedDate);
        }


        return dvd;
    }


    //This method returns actor dtos from model list
    private List<Actor> getActorDTObyModel(List<ActorModel> actors) {
        List<Actor> list = new ArrayList<>();
        for (models.ActorModel actorModel : actors) {
            Actor actor = new Actor(actorModel.getName(),actorModel.getId());
            list.add(actor);
        }
        return list;

    }

    //This method let the user update the server on return details
    @Override
    public void returnItem(int isbn,String returnedDate) {
        returnedDate = DateTime.now().getDate();
        if( Ebean.find(models.Book.class, isbn)!=null ){
            models.Book bookModel = Ebean.find(models.Book.class, isbn);
            if( bookModel.reader==null){
                throw new IllegalArgumentException("Book not issued");
            }
            SqlUpdate insert = Ebean.createSqlUpdate("UPDATE `borrowedhistory` SET returnDate =' "+returnedDate+"' WHERE isbn ="+isbn+" and readerID ="+bookModel.getReader().getId()+" and borrowedDate ='"+bookModel.getBorrowedDate()+"'");
            insert.execute();
            bookModel.setReader(null);
            Ebean.update(bookModel);
        }else if( ( Ebean.find(models.DVDModel.class, isbn)!=null)){
            models.DVDModel dvdModel = Ebean.find(models.DVDModel.class, isbn);
            if( dvdModel.reader==null){
                throw new IllegalArgumentException("DVD not issued");
            }
            SqlUpdate insert = Ebean.createSqlUpdate("UPDATE `borrowedhistory` SET returnDate =' "+returnedDate+"' WHERE isbn ="+isbn+" and readerID ="+dvdModel.getReader().getId()+" and borrowedDate ='"+dvdModel.getBorrowedDate()+"'");
            insert.execute();
            dvdModel.setReader(null);
            Ebean.update(dvdModel);
        }else {
            throw new IllegalArgumentException("Book/DVD not found ");
        }

    }


    //This method updates item on borrow details
        @Override
        public void borrowItem(int isbn, String reader) {

           List<ReaderModel> all =  Ebean.find(ReaderModel.class).findList();
           ReaderModel foundReader = null;
            for (ReaderModel r: all ) {
                if(r.getName().equalsIgnoreCase(reader)){
                    foundReader = r;
                    break;
                }

            }
            if(foundReader==null) {
                foundReader = new ReaderModel();
                foundReader.setName(reader);
                Ebean.save(foundReader);
            }



            if( Ebean.find(models.Book.class, isbn)!=null){
                models.Book bookModel = Ebean.find(models.Book.class, isbn);
                if(bookModel.reader!=null){
                    throw new IllegalArgumentException("Book is currently being read, Please reserve");
                }
                bookModel.setReader(foundReader);
                Ebean.update(bookModel);
                SqlUpdate insert = Ebean.createSqlUpdate("INSERT INTO borrowedhistory(isbn,readerID,borrowedDate)" +
                        " VALUES ("+isbn +","+ bookModel.getReader().getId()+",'"+bookModel.getBorrowedDate()+"');");
                insert.execute();

            }else if( ( Ebean.find(models.DVDModel.class, isbn)!=null)){
                models.DVDModel dvdModel = Ebean.find(models.DVDModel.class, isbn);
                if(dvdModel.reader!=null){
                    throw new IllegalArgumentException("DVD is already issued, please reserve");
                }
                dvdModel.setReader(foundReader);
                Ebean.update(dvdModel);
                SqlUpdate insert = Ebean.createSqlUpdate("INSERT INTO borrowedhistory(isbn,readerID,borrowedDate)" +
                        " VALUES ("+isbn +","+ dvdModel.getReader().getId()+",'"+dvdModel.getBorrowedDate()+"');");
                insert.execute();
            }else {
                throw new IllegalArgumentException("Book/DVD not found");
            }

        }

        //This method provides a accurate value of the average usage duration of an item
        //consuming the isbn

        private int averageUsage(int isbn){

            String sql =   "select * from borrowedhistory where isbn = "+isbn;
            SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
            List<SqlRow> list = sqlQuery.findList();
            String bd = "";
            String rd = "";
            int hours = 72;
            int totalDays=1;
            for (SqlRow a:list) {
                bd = a.getString("borroweddate");
                rd = a.getString("returndate");
                if (rd!=null) {
                   DateTime borD =  DateTime.convertfromSTD(bd);
                   DateTime retD = DateTime.convertfromSTD(rd);
                   hours += retD.subtractDate(borD);
                   totalDays++;

                }else {
                        continue;
                }
            }

return (hours/(24*totalDays));




        }


        //This method does reservation if an item is currently unavailable
@Override
        public void doReservation(int isbn, String readerName){
    List<ReaderModel> all =  Ebean.find(ReaderModel.class).findList();
    ReaderModel foundReader = null;
    DateTime br;
    for (ReaderModel r: all ) {
        if(r.getName().equalsIgnoreCase(readerName)){
            foundReader = r;
            break;
        }

    }
    if(foundReader==null) {
        foundReader = new ReaderModel();
        foundReader.setName(readerName);
        Ebean.save(foundReader);
    }
            String reserveDate = "";
            LibraryItemModel item = findItem(isbn);

            String a = item.getLastReservedDate();
            if(a!=null){
             br = DateTime.convertfromSTD(item.getLastReservedDate());
            }else {
                br = DateTime.convertfromSTD(item.getBorrowedDate());
            }
            reserveDate =  br.addDays(averageUsage(isbn)).getDate();
            SqlUpdate insert = Ebean.createSqlUpdate("INSERT INTO reserve(isbn,readerid,reservedate)" +
                    " VALUES ("+isbn +","+ foundReader.getId()+",'"+reserveDate+"');");
            insert.execute();
            item.setLastReservedDate(reserveDate);
            Ebean.save(item);


        }
        //This method returns a sorted ordered list of overdues decending order
       @Override public List<LibraryItem> generateReport(){

        List<LibraryItem> items = getAllItem();
        List<LibraryItem> sortedList =  new ArrayList<>();
            BigDecimal fine ;

            for (LibraryItem item :items) {
                fine = item.calFine();


                if(!(fine.compareTo((new BigDecimal(0)))<=0) ){
                item.fineAmnt = fine;
                    sortedList.add(item);
                }
            }
           sortedList.sort(Comparator.comparing(LibraryItem::getFine));

           Collections.reverse(sortedList);

return sortedList;

        }

        @Override
        public BigDecimal getFineByISBN(int isbn){
        List<LibraryItem> l = generateReport();
        LibraryItem item = null;
            for (LibraryItem a:l ) {
                if(a.isbn==isbn){
                    item  = a;
                    return item.fineAmnt;
                }else
                    continue;
            }

           return new BigDecimal(0);
        }

        //This method finds an item, isbn provided
  public LibraryItemModel findItem(Integer isbn){
        if(Ebean.find(models.Book.class,isbn)!=null){
            return Ebean.find(models.Book.class,isbn);
        }else if(Ebean.find(models.DVDModel.class,isbn)!=null) {
            return Ebean.find(models.DVDModel.class, isbn);
        }else
            return null;
    }

    //This method returns a list of all the library item in dto

       @Override public List<LibraryItem> getAllItem(){
        List<LibraryItem> allItems = new ArrayList<>();
        allItems.addAll(getAllBooks());
        allItems.addAll(getAllDVD());
        return allItems;
    }


}
