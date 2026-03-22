package bt.edu.gcit.usermicroservice.exception;

public class FileSizeException extends RuntimeException {
    public FileSizeException(String message) {
        super(message);
    }
}