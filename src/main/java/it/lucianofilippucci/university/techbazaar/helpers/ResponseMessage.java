package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.Getter;

@Getter
public class ResponseMessage<T> {
    private final T message;
    private boolean isError;

    public ResponseMessage(T text) {
        this.message = text;
    }

    public ResponseMessage<T> setIsError(boolean isError) {
        this.isError = isError;
        return this;
    }

}
