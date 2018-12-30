package util;

import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;


public class DateTime {
    private int day = 18;
    private int month = 12;
    private int year = 1998;
    private int hour = 0;
    private int minute = 0;

    public DateTime(int minute, int hour,int day, int month, int year){
        if( minute>=0 && minute>60){
            throw new IllegalArgumentException("Minute should be less than 60");
        }else {
            this.minute = minute;

        }
        if(hour >=0 && hour>24){
            throw new IllegalArgumentException("Hour should be less than to 24");
        }else {
            this.hour = hour;

        }
        if(day >=0 && day>31){
            throw new IllegalArgumentException("Day should be less than to 31");
        }else {
            this.day = day;
        }
        if(month >=0 && month>12){
            throw new IllegalArgumentException("Month should be less than to 12");
        }else {
            this.month = month;
        }

        this.year = year;
    }
    public DateTime(String ddmmyyyyHHMM){
        String[] date = ddmmyyyyHHMM.split(",");
        this.day=Integer.parseInt(date[0]);
        this.month = Integer.parseInt(date[1]);
        this.year = Integer.parseInt(date[2]);
        this.hour = Integer.parseInt(date[3]);
        this.minute = Integer.parseInt(date[4]);

    }

    public String getDate(){

        String out = String.format  ("%02d/%02d/%04d %02d:%02d",day,month,year,hour,minute);
        return out;

    }

    public static DateTime convertfromSTD(String date){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd,MM,yyyy,HH,mm");

Date converted = new Date();
        try {
           converted =  sdf.parse(date); // Now use today date.
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String output = sdf2.format(converted);
        DateTime newDate = new DateTime(output);
        // System.out.println(newDate.getDate());
        return  newDate;
    }
    public String convertFromStandard(String standardDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MM,yyyy,HH,mm");
        Date date = new Date();
        String thisDate = sdf.format(standardDate);
        return thisDate;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public static DateTime now(){
        //TODO : TRY TO CONVERT FROM SYSTEM.CURRENTTIME()
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MM,yyyy,HH,mm");
        Date date = new Date();
        String now = sdf.format(date);
        DateTime dateTime = new DateTime(now);
    return dateTime;
    }

    public DateTime addDays(int days){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd,MM,yyyy,HH,mm");
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(sdf.parse(this.getDate())); // Now use today date.
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, days); // Adding  days
        String output = sdf2.format(c.getTime());
        DateTime newDate = new DateTime(output);
       // System.out.println(newDate.getDate());

      return  newDate;
    }
//    public DateTime subtractDays(int days){
//
//    }
    public int subtractDate(DateTime dateTime){

        org.joda.time.DateTime thisDate= org.joda.time.DateTime.parse(this.getDate(),DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
        org.joda.time.DateTime date = org.joda.time.DateTime.parse(dateTime.getDate(),
                DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
        Hours d = Hours.hoursBetween(date, thisDate);

        int hours = d.getHours();
        return hours;
    }

    @Override
    public String toString() {
        return "Date is " + day + "/" + month + "/" + year +" " +hour+":"+minute;
    }
}
