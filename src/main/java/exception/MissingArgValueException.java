package exception;

public class MissingArgValueException extends RuntimeException {
    public MissingArgValueException(String message) {
        super(message);
    }
}
