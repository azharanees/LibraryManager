package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.LibraryItem;
import dto.Reader;
import models.*;
import play.libs.Json;
import play.mvc.*;
import io.ebean.Ebean;
import services.LibraryManager;
import services.WestminsterLibraryManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import play.libs.ws.WSClient;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller implements Serializable {

    LibraryManager west = new WestminsterLibraryManager();


    @Inject
    WSClient ws;
    public CompletionStage<Result> getImage() {
        return ws
                .url("http://www.maine-coon-cat-nation.com/image-files/orange-maine-coon-cat.jpg")
                .get()
                .thenApply(file -> ok(file.getBodyAsStream()).as("image/jpeg"));
    }


    public Result index() {

        List<Book> books = Ebean.find(Book.class).findList();

        List<Book> fin = new ArrayList<>();
        for (Book b:books ) {

            if(b.getReader()==null){

            }else {
                fin.add(b);
            }
        }
        return ok(Json.toJson(books));
    }

    public Result doReservation(){
        JsonNode json = request().body().asJson();
        int x = json.findPath("isbn").asInt();
        String s = json.findPath("reader").asText();


        west.doReservation(json.findPath("isbn").asInt(),json.findPath("reader").asText());
        return ok("Reserved Successfully");
    }
public Result getBookCount(){

        return ok(Json.toJson(west.getBookCount()));
}

public Result getDVDCount(){
    return ok(Json.toJson(west.getDVDCount()));

}

    public Result addDVD(){

        JsonNode json = request().body().asJson();
        JsonNode j = Json.parse(json.toString());


        DVDModel dvd = Json.fromJson(j,DVDModel.class);
        if(Ebean.find(DVDModel.class,dvd.isbn)!=null){
            return badRequest(("ISBN already exists"));
        }else if(Ebean.find(Book.class,dvd.isbn)!=null){
            return badRequest(("ISBN already exists"));
        }

        {
            if (dvd.title == null) {
                return badRequest("Missing parameter [title]");
            } else {
                if (west.getBookCount() >= 50) {
                    return badRequest("Unavailable space for dvd, max limit reached");
                } else {
                    west.addDVD(dvd);
                }

                return ok("DVD Added successfully ");
            }
        }
        }       //

    public Result getAllDVDs() {
        List<dto.DVD> dvds = west.getAllDVD();
        return ok(Json.toJson(dvds));
    }

    public Result addReader(){
        JsonNode json = request().body().asJson();

        String id = json.findPath("id").textValue();
        String name = json.findPath("name").textValue();
        String mobile = json.findPath("mobile").textValue();
        String email = json.findPath("email").textValue();
        west.addReader(id,name,mobile,email);
        return ok("Reader added successfully ");
    }

    public Result updateCurrentReader(){
        JsonNode json = request().body().asJson();
        String readerName = json.findPath("memberName").textValue();
        int id  = json.findPath("isbn").intValue();
        try{
            west.borrowItem((id),readerName);

        }catch (Exception e){
            return badRequest(e.getMessage());
        }

//        rId = Integer.parseInt(json.findPath("reader.id").textValue());
//        id = Integer.parseInt(json.findPath("id").textValue());
        //west.returnItem(id,rId);
    //    west.getItemByName();
        return ok(Json.toJson("Issued successfully"));
    }

    public Result returnItem(){
        JsonNode json = request().body().asJson();
        String returnDate = json.findPath("returnDate").textValue();
        int id  = json.findPath("isbn").intValue();
        double d = west.getFineByISBN(id).doubleValue();
       JsonNode j = Json.toJson(d);
      try{
          west.returnItem(id,returnDate);
      }catch (Exception e){
          return badRequest(e.getMessage());
      }
        return ok(j);
    }
public Result deleteItem(Integer isbn){

        LibraryItemModel item  = west.findItem(isbn);
        if(item==null){
            return internalServerError("Invalid ISBN");
        }
        west.removeItem(isbn);
        return ok("successfully deleted");

}

public Result getFine(Integer isbn){

return ok(Json.toJson(west.getFineByISBN(isbn)));
}

    public Result getAllReaders(){
        List<ReaderModel> readers = Ebean.find(ReaderModel.class).findList();
        return ok(Json.toJson(readers));

    }
    public Result getAllItems(){
        List<LibraryItem> l = west.getAllItem();
        return ok(Json.toJson(l));
    }


    public Result addBook() {


//FIXME  - NOT COMPLETE!!
        JsonNode body = request().body().asJson();
            String title = body.findPath("title").asText();
            String id = body.findPath("isbn").asText();
            String author = body.findPath("authors").asText();
            String publisher = body.findPath("publisher").asText();
            String genre = body.findPath("genre").asText();
            int pages = body.findPath("pages").asInt();
            String publicationDate = body.findPath("publicationDate").asText();

    if(Ebean.find(Book.class,id)!=null || Ebean.find(DVDModel.class,id)!=null){
        return badRequest(("ISBN already exists"));
    }

            {
                if (title == null) {
                    return badRequest("Missing parameter [title]");
                } else {
                    if (west.getBookCount() >= 100) {
                        return badRequest("Unavailable space for books, max limit reached");
                    } else {
                        west.addBook(id, title, author, publisher, genre, publicationDate, pages);
                    }

                    return ok("Book Added successfully ");
                }
            }

        }


    public Result removeBook(){
        JsonNode body = request().body().asJson();
        int id =Integer.parseInt(body.findPath("id").textValue());
        west.removeBook(id);
        return ok("Removed Successfully\nBook space available - " + west.getBookCount()+"\nAvailable DVD space - "+west.getDVDCount());


    }
    public Result login(){
        return ok("Login Success");
    }

    public Result getAllBooks() {

        List<dto.Book> books = west.getAllBooks();

        return ok(Json.toJson(books));
    }

    public Result addAuthor(){
        JsonNode j = request().body().asJson();
        j.findPath("id").textValue();

        west.addAuthor(21,"also JKR");

        return ok("Author added successfully");

    }

    public Result updateAuthor(){
        JsonNode j = request().body().asJson();
        west.updateBookAuthor(j.findPath("isbn").asInt());
        return ok("Book Author updated");
    }
//    public Result index() {
//        return ok("<h1>My App<h1>").as("text/html");
//    }
    public Result getAvailISBN(){
        west.generateReport();
        return ok(""+(west.getTotalCount()+1));
    }

    public Result getReport(){
       List<LibraryItem> list =  west.generateReport();
        return ok(Json.toJson(list));
    }

}
