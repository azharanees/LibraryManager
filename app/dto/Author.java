package dto;

public class Author extends Person{

    private int authorID;
    private String authorName;

    public Author(int authorID, String authorName) {
        super(authorName, authorID);
    }


    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
