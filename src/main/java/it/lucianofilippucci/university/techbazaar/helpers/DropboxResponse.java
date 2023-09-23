package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.Getter;

import java.util.List;

public record DropboxResponse(List<String> message, boolean isError) {

}
