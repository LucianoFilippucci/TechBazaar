package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.Getter;

@Getter
public class ResponseMessage<String> {
    private final String message;
    private boolean isError;

    public ResponseMessage(String text) {
        this.message = text;
    }

    public ResponseMessage<String> setIsError(boolean isError) {
        this.isError = isError;
        return this;
    }

}
