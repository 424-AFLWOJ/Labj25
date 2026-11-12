package edu.course.lab08;


public final class ValidationError {
    private final int lineNumber;
    private final String field;
    private final String message;
    public ValidationError(int lineNumber, String field, String message) {
        this.lineNumber = lineNumber;
        this.field = field;
        this.message = message;
    }
    public int getLineNumber() { return lineNumber; }
    public String getField() { return field; }
    public String getMessage() { return message; }
}
