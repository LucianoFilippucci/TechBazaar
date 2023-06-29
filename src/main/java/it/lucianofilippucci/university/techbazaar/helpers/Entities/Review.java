package it.lucianofilippucci.university.techbazaar.helpers.Entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Review {
    private int review_id;
    private int user_id;
    private int star_count;
    private String title;
    private String body;
    private int likes;
}
