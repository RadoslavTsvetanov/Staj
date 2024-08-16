package uk.gov.hmcts.reform.demo.exceptions;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
