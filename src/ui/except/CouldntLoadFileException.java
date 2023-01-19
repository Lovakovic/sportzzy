package ui.except;

public class CouldntLoadFileException extends RuntimeException {
    public CouldntLoadFileException(String message) {
        super(message);
    }
}
