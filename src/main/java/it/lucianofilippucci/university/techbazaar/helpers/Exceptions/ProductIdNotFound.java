package it.lucianofilippucci.university.techbazaar.helpers.Exceptions;

public class ProductIdNotFound extends Exception {
    private String message;
    private int http_error;

    public ProductIdNotFound() {
    }
}