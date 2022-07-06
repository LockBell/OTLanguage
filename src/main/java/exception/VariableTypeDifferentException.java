package exception;

public class VariableTypeDifferentException extends RuntimeException {

    public VariableTypeDifferentException() {
        super();
    }

    public VariableTypeDifferentException(String message) {
        super(message);
    }

    public VariableTypeDifferentException(String message, Throwable cause) {
        super(message, cause);
    }

    public VariableTypeDifferentException(Throwable cause) {
        super(cause);
    }
}
