package dto;

public class Reader extends Person {

    private int readerID;
    private String readerName;

    public Reader(String readerName, int readerID) {
        super(readerName, readerID);
        this.readerName = readerName;
        this.readerID=readerID;
    }

    public int getReaderID() {
        return readerID;
    }

    public void setReaderID(int readerID) {
        this.readerID = readerID;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }
}
