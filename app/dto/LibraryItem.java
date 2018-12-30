package dto;

import io.ebean.Ebean;
import models.Book;
import models.DVDModel;
import models.LibraryItemModel;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import services.Reserve;
import util.DateTime;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class LibraryItem {

    public int isbn;
    public String title;
    private Reader reader;
    private String genre;
    private String publicationDate;
    private DateTime borrowedDate;
    private static final double FINERATE =  0.2;
    private static final double FINERATE2 =  0.5;

    private DateTime lastReservedDate;
    public LibraryItem(int isbn, String title, String genre, String publicationDate) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.publicationDate = publicationDate;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public DateTime getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {

        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd,MM,yyyy,HH,mm");
// Printing the date
        org.joda.time.DateTime date = org.joda.time.DateTime.parse(borrowedDate,
                DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
        DateTime a = new DateTime(dtfOut.print(date));
        this.borrowedDate = a;
        //availableIn();
    }

    public DateTime getAvailDate() {
        return availDate;
    }

    public void setAvailDate(DateTime availDate) {
        this.availDate = availDate;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {

        this.reader = reader;
    }
public BigDecimal fineAmnt;
    public DateTime availDate;


public BigDecimal getFine(){
        return  this.fineAmnt;
    }
    public BigDecimal calFine() {
        double totalFine = 0;
        int maxDays = 7;
        if(this.getClass().equals(DVD.class)){
            maxDays = 3;
        }
        if (getBorrowedDate()!=null) {
            DateTime availOn = getBorrowedDate().addDays(maxDays);
            int hours = DateTime.now().subtractDate(availOn);

            if (hours > 0) {
                if (hours >= 72) {
                    totalFine += 72 * 0.2;
                    hours -= 72;
                    totalFine += hours * 0.5;
                } else
                    totalFine += hours * 0.2;
            } else
                totalFine = 0;
        }

        BigDecimal fine = new BigDecimal(totalFine);
        fine = fine.setScale(2,BigDecimal.ROUND_HALF_UP);
        return fine;
    }

    public int availableIn() {
        //calFine();
        DateTime availDate;
        if (getBorrowedDate() != null) {
            if(getClass().getName().equals(DVDModel.class.getName())){
                availDate = getBorrowedDate().addDays(3);
            }else {
                availDate = getBorrowedDate().addDays(7);
            }
            this.availDate = availDate;
            return  this.getBorrowedDate().subtractDate(availDate);
        } else
            return 0;
    }

    public void doReservation(LibraryItemModel item){
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd,MM,yyyy,HH,mm");
// Printing the date
        if(item.getLastReservedDate()!=null){
        org.joda.time.DateTime date = org.joda.time.DateTime.parse(item.getLastReservedDate(),
                DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));

        DateTime a = new DateTime(dtfOut.print(date));
        this.lastReservedDate = a;

    DateTime lastReservedDate = a;
    DateTime nextAvailDate = lastReservedDate.addDays(7);
    this.lastReservedDate = nextAvailDate;
    item.setLastReservedDate(this.lastReservedDate.getDate());
            Ebean.save(item);

}else {
            org.joda.time.DateTime a = org.joda.time.DateTime.parse(item.getBorrowedDate(),
                    DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
            DateTime borrowedDate = new DateTime(dtfOut.print(a));

    this.lastReservedDate = (borrowedDate.addDays(availableIn()/24));
    item.setLastReservedDate(lastReservedDate.getDate());
    Ebean.save(item);
}
    }

   public int  findAverageReturnDuration(){
        return 3;
    }

    @Override
    public String toString() {
        return "ISBN - " + isbn + "\nTitle - " + title + "\nGenre - " + this.genre + "\nPublication Date-" + this.publicationDate;
    }
}
