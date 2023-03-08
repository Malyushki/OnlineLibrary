import java.io.Serializable;

public class Book implements Serializable {
    private  String bookName;
    private  String bookAuthor;
    private  String content;
    private  String idISBN;

    public Book(String idISBN, String bookName, String bookAuthor, String content) {
        setBookName(bookName);
        setBookAuthor(bookAuthor);
        setContent(content);
        setIdISBN(idISBN);
    }



    private void setBookName(String bookName) {
        this.bookName = bookName;
    }

    private void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setIdISBN(String idISBN) {
        this.idISBN = idISBN;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getContent() {
        return content;
    }

    public String getIdISBN() {
        return idISBN;
    }

    @Override
    public String toString() {
        return String.format("%s by %s. Books ISBN: %s",getBookName(),getBookAuthor(),getIdISBN());
    }
}
