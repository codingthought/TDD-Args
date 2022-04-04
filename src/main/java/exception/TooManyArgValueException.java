package exception;

public class TooManyArgValueException extends RuntimeException {
    public TooManyArgValueException(String message) {
        super(message);
    }
}
