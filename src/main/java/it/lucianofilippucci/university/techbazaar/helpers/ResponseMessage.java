package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.Getter;

@Getter
public class ResponseMessage<T> {
    private String message;
    private boolean isError;
    private T object;

    public ResponseMessage(String text) {
        this.message = text;
    }

    public ResponseMessage (T object) {
        this.object = object;
    }

    public ResponseMessage<T> setIsError(boolean isError) {
        this.isError = isError;
        return this;
    }

}
