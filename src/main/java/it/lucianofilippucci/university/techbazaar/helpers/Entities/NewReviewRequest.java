package it.lucianofilippucci.university.techbazaar.helpers.Entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class NewReviewRequest {
    private int user_id;
    private int star_count;
    private String title;
    private String body;

}
