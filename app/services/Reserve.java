package services;

import dto.Reader;
import util.DateTime;

public class Reserve  {
    Reader nextReader;
    DateTime reservedDate;
    DateTime nextAvail;

    public Reserve(Reader nextReader, DateTime reservedDate, DateTime nextAvail) {
        this.nextReader = nextReader;
        this.reservedDate = reservedDate;
        this.nextAvail = nextAvail;
    }

    public Reader getNextReader() {
        return nextReader;
    }

    public void setNextReader(Reader nextReader) {
        this.nextReader = nextReader;
    }

    public DateTime getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(DateTime reservedDate) {
        this.reservedDate = reservedDate;
    }

    public DateTime getNextAvail() {
        return nextAvail;
    }

    public void setNextAvail(DateTime nextAvail) {
        this.nextAvail = nextAvail;
    }
}
