package tdd.exception;

public class IllegalArgValueException extends RuntimeException {
    public IllegalArgValueException(String message) {
        super(message);
    }
}
