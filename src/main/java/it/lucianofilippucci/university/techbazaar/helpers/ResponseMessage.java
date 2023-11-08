package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResponseMessage<T> {
    private T message;
    private boolean isError;

    public ResponseMessage(T text) {
        this.message = text;
    }

    public ResponseMessage<T> setIsError(boolean isError) {
        this.isError = isError;
        return this;
    }

    public ResponseMessage<T> setMessage(T message) {
        this.message = message;
        return this;
    }

}
